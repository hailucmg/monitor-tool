package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class DashBoard extends AncestorEntryPoint {

	// Table of list system with google chart API
	private Table tableListSystem;
	// Store data of list system
	private DataTable dataListSystem;
	// Options of table list system
	private Options opsTableListSystem;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_DASHBOARD) {
			tableListSystem = new Table();
			createOptionsTableListSystem();
			addWidget(HTMLControl.ID_BODY_CONTENT, tableListSystem);
			timerReload = new Timer() {
				@Override
				public void run() {
					callBack();
				}
			};
			timerReload.run();
			timerReload.scheduleRepeating(MonitorConstant.REFRESH_RATE);
		}

	}

	/*
	 * Create callback to server via RPC
	 */
	void callBack() {
		monitorGwtSv
				.listSystems(new AsyncCallback<ArrayList<SystemMonitorDto>>() {
					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
						showMessage("Server error. ",
								HTMLControl.HTML_DASHBOARD_NAME, "Try again.",
								HTMLControl.RED_MESSAGE, true);
					}

					@Override
					public void onSuccess(ArrayList<SystemMonitorDto> result) {
						showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
						setVisibleLoadingImage(false);
						setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						drawTable(result);
					}
				});
	}

	/*
	 * Draw table ui with result callback from server via RPC
	 */
	void drawTable(ArrayList<SystemMonitorDto> result) {
		if (result != null) {
			createDataListSystem();
			dataListSystem.addRows(result.size());
			SystemMonitorDto sys = null;
			for (int i = 0; i < result.size(); i++) {
				sys = result.get(i);
				dataListSystem.setValue(
						i,
						0,
						HTMLControl.getLinkSystemDetail(sys.getId(),
								sys.getCode()));
				dataListSystem.setValue(i, 1, (sys.getName() == null) ? "N/A"
						: sys.getName());
				dataListSystem.setValue(i, 2, (sys.getUrl() == null) ? "N/A"
						: sys.getUrl());
				dataListSystem.setValue(i, 3, (sys.getIp() == null) ? "N/A"
						: sys.getIp());
				dataListSystem.setValue(i, 4, (sys.getLastestCpu() == null) ? 0
						: (sys.isActive() && sys.isStatus() ? sys
								.getLastestCpu().getCpuUsage() : 0));
				dataListSystem.setValue(i, 5,
						(sys.getLastestMemory() == null) ? 0 : (sys.isActive()
								&& sys.isStatus() ? sys.getLastestMemory()
								.getPercentUsage() : 0));
				dataListSystem.setValue(i, 6,
						HTMLControl.getHTMLStatusImage(sys.getHealthStatus()));
				dataListSystem.setValue(i, 7,
						HTMLControl.getHTMLActiveImage(sys.isActive()));
			}
			createFormatDataTableListSystem();
			setVisibleMessage(false, HTMLControl.RED_MESSAGE);
			setVisibleLoadingImage(false);
			// draw table
			tableListSystem.draw(dataListSystem, opsTableListSystem);
		} else {
			showMessage(
					"No system found. ",
					(role == MonitorConstant.ROLE_ADMIN) ? HTMLControl.HTML_ADD_NEW_SYSTEM_NAME
							: "",
					(role == MonitorConstant.ROLE_ADMIN) ? "Add new system."
							: "", HTMLControl.RED_MESSAGE, true);
			showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
			setVisibleLoadingImage(false);
		}
	}

	/*
	 * Create options of table list system
	 */
	void createOptionsTableListSystem() {
		opsTableListSystem = Options.create();
		opsTableListSystem.setAllowHtml(true);
		opsTableListSystem.setShowRowNumber(true);
	}

	/*
	 * Create data table list system without value
	 */
	void createDataListSystem() {
		// create object data table
		dataListSystem = DataTable.create();
		// add all columns
		dataListSystem.addColumn(ColumnType.STRING, "SID");
		dataListSystem.addColumn(ColumnType.STRING, "System");
		dataListSystem.addColumn(ColumnType.STRING, "URL");
		dataListSystem.addColumn(ColumnType.STRING, "IP");
		dataListSystem.addColumn(ColumnType.NUMBER, "CPU Usage (%)");
		dataListSystem.addColumn(ColumnType.NUMBER, "Memory Usage (%)");
		dataListSystem.addColumn(ColumnType.STRING, "Health Status");
		dataListSystem.addColumn(ColumnType.STRING, "Monitor Status");

	}

	void createFormatDataTableListSystem() {
		// create options of bar format (format columns 'CPU Usage' and 'Memory
		// Usage')
		com.google.gwt.visualization.client.formatters.BarFormat.Options ops = com.google.gwt.visualization.client.formatters.BarFormat.Options
				.create();
		ops.setColorPositive(Color.RED);
		ops.setColorNegative(Color.RED);
		ops.setMax(100);
		ops.setMin(0);
		ops.setWidth(200);
		BarFormat bf = BarFormat.create(ops);
		bf.format(dataListSystem, 4);
		bf.format(dataListSystem, 5);
	}
}
