package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
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

	private HTML popupContent;

	private HTML buttonDetails;

	private HTML buttonStatistic;

	private SystemMonitor[] systemList;

	private FlexTable flexTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_DASHBOARD) {
			tableListSystem = new Table();
			createOptionsTableListSystem();
			addWidget(HTMLControl.ID_BODY_CONTENT, tableListSystem);
			initDialogBox();
			tableListSystem.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {

					// May be multiple selections.
					JsArray<Selection> selections = tableListSystem
							.getSelections();
					for (int i = 0; i < selections.length(); i++) {
						Selection selection = selections.get(i);
						if (selection.isRow()) {
							int row = selection.getRow();
							if (systemList != null) {
								showDialogBox(systemList[row]);
							}
						}
					}
				}
			});
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

	void showDialogBox(SystemMonitor sys) {
		try {
			popupContent.setText(sys.getHealthStatus());
			buttonDetails.setHTML(HTMLControl.getButtonHtml(sys.getId(), true));
			buttonStatistic.setHTML(HTMLControl.getButtonHtml(sys.getId(),
					false));
			dialogBox.center();
		} catch (Exception ex) {
			// do nothing
		}
	}

	void initDialogBox() {
		dialogBox.setAnimationEnabled(true);
		dialogBox.setAutoHideEnabled(true);
		popupContent = new HTML();
		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, popupContent);
		flexTable.setStyleName("table-popup");
		FlexTable flexButton = new FlexTable();
		buttonDetails = new HTML();
		buttonStatistic = new HTML();
		flexButton.setCellPadding(5);
		flexButton.setCellSpacing(5);
		flexButton.setWidget(0, 0, buttonDetails);
		flexButton.setWidget(0, 1, buttonStatistic);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(flexTable);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(flexButton);
		dialogBox.setWidget(dialogVPanel);
	}

	/*
	 * Create callback to server via RPC
	 */
	void callBack() {
		monitorGwtSv.listSystems(new AsyncCallback<SystemMonitor[]>() {
			@Override
			public void onFailure(Throwable caught) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				showMessage("Server error. ", HTMLControl.HTML_DASHBOARD_NAME,
						"Try again.", HTMLControl.RED_MESSAGE, true);
			}

			@Override
			public void onSuccess(SystemMonitor[] result) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				setVisibleLoadingImage(false);
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
				drawTable(result);
				systemList = result;
			}
		});
	}

	/*
	 * Draw table ui with result callback from server via RPC
	 */
	void drawTable(SystemMonitor[] result) {
		if (result != null) {
			createDataListSystem();
			dataListSystem.addRows(result.length);
			for (int i = 0; i < result.length; i++) {
				dataListSystem.setValue(i, 0, HTMLControl.getLinkSystemDetail(
						result[i].getId(), result[i].getCode()));
				dataListSystem.setValue(
						i,
						1,
						(result[i].getName() == null) ? "N/A" : result[i]
								.getName());
				dataListSystem.setValue(
						i,
						2,
						(result[i].getUrl() == null) ? "N/A" : result[i]
								.getUrl());
				dataListSystem
						.setValue(i, 3, (result[i].getIp() == null) ? "N/A"
								: result[i].getIp());
				dataListSystem.setValue(
						i,
						4,
						(result[i].getLastCpuMemory() == null) ? 0
								: (result[i].isActive()
										&& result[i].getStatus() ? result[i]
										.getLastCpuMemory().getCpuUsage() : 0));
				dataListSystem.setValue(
						i,
						5,
						(result[i].getLastCpuMemory() == null) ? 0
								: (result[i].isActive()
										&& result[i].getStatus() ? result[i]
										.getLastCpuMemory()
										.getPercentMemoryUsage() : 0));
				dataListSystem.setValue(i, 6, HTMLControl
						.getHTMLStatusImage(result[i].getHealthStatus()));
				dataListSystem.setValue(i, 7,
						HTMLControl.getHTMLActiveImage(result[i].isActive()));
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
