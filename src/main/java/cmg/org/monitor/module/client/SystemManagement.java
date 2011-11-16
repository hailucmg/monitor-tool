package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class SystemManagement extends AncestorEntryPoint {
	SystemMonitor[] listSystem;
	static private Table tableListSystem;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT) {
			tableListSystem = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, tableListSystem);
			initContent();

		}
	}

	private void initContent() {
		monitorGwtSv.listSystem(false, new AsyncCallback<SystemMonitor[]>() {
			@Override
			public void onSuccess(SystemMonitor[] result) {
				listSystem = result;
				if (result != null) {
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					isReadyDelete = true;
					drawTable(result);
				} else {
					initMessage("No system found. ",
							HTMLControl.HTML_ADD_NEW_SYSTEM_NAME,
							"Add new system.", HTMLControl.RED_MESSAGE);
					setVisibleMessage(true, HTMLControl.RED_MESSAGE);
					setVisibleLoadingImage(false);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				initMessage("Server error. ", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
						"Try again.", HTMLControl.RED_MESSAGE);
				setVisibleMessage(true, HTMLControl.RED_MESSAGE);	
			}
		});

	}

	void drawTable(SystemMonitor[] result) {
		if (result != null) {
			tableListSystem.draw(createDataListSystem(result),
					createOptionsTableListSystem());

		} else {
			initMessage("No system found. ",
					HTMLControl.HTML_ADD_NEW_SYSTEM_NAME, "Add new system.",
					HTMLControl.RED_MESSAGE);
			setVisibleMessage(true, HTMLControl.RED_MESSAGE);
			showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
		}
	}

	/*
	 * Create options of table list system
	 */
	Options createOptionsTableListSystem() {
		Options ops = Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(true);
		return ops;
	}

	/*
	 * Create data table list system without value
	 */
	AbstractDataTable createDataListSystem(SystemMonitor[] result) {
		// create object data table
		DataTable dataListSystem = DataTable.create();
		// add all columns
		dataListSystem.addColumn(ColumnType.STRING, "SID");
		dataListSystem.addColumn(ColumnType.STRING, "System");
		dataListSystem.addColumn(ColumnType.STRING, "URL");
		dataListSystem.addColumn(ColumnType.STRING, "IP");
		dataListSystem.addColumn(ColumnType.NUMBER, "CPU Usage (%)");
		dataListSystem.addColumn(ColumnType.NUMBER, "Memory Usage (%)");
		dataListSystem.addColumn(ColumnType.STRING, "Health Status");
		dataListSystem.addColumn(ColumnType.STRING, "Monitor Status");
		dataListSystem.addColumn(ColumnType.STRING, "Delete");
		dataListSystem.addRows(result.length);
		for (int i = 0; i < result.length; i++) {
			dataListSystem.setValue(
					i,
					0,
					HTMLControl.getLinkEditSystem(result[i].getId(),
							result[i].getCode()));
			dataListSystem.setValue(i, 1, (result[i].getName() == null) ? "N/A"
					: result[i].getName());
			dataListSystem.setValue(i, 2, (result[i].getUrl() == null) ? "N/A"
					: result[i].getUrl());
			dataListSystem.setValue(i, 3, (result[i].getIp() == null) ? "N/A"
					: result[i].getIp());
			dataListSystem.setValue(
					i,
					4,
					(result[i].getLastCpuMemory() == null) ? 0 : (result[i]
							.isActive() && result[i].getStatus() ? result[i]
							.getLastCpuMemory().getCpuUsage() : 0));
			dataListSystem.setValue(
					i,
					5,
					(result[i].getLastCpuMemory() == null) ? 0 : (result[i]
							.isActive() && result[i].getStatus() ? result[i]
							.getLastCpuMemory().getPercentMemoryUsage() : 0));
			dataListSystem
					.setValue(i, 6, HTMLControl.getHTMLStatusImage(result[i]
							.getHealthStatus()));
			dataListSystem.setValue(i, 7,
					HTMLControl.getHTMLActiveImage(result[i].isActive()));
			dataListSystem
					.setValue(
							i,
							8,
							"<a onClick=\"javascript:deleteSystem('"+result[i].getId()+"','"+result[i].getCode()+"');\" title=\"Delete\" class=\"icon-2 info-tooltip\"></a>");
		}

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
		return dataListSystem;

	}

}
