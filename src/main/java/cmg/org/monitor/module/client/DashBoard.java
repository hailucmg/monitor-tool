package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
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

	private static HTML popupContent;

	private static HTML buttonDetails;

	private static HTML buttonStatistic;

	private SystemMonitor[] systems;

	private FlexTable flexTable;

	protected void init() {
		DashBoard.exportStaticMethod();
		if (currentPage == HTMLControl.PAGE_DASHBOARD) {
			tableListSystem = new Table();
			createOptionsTableListSystem();
			addWidget(HTMLControl.ID_BODY_CONTENT, tableListSystem);
			initDialogBox();
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

	public static native void exportStaticMethod() /*-{
	$wnd.showStatusDialogBox =
	$entry(@cmg.org.monitor.module.client.DashBoard::showStatusDialogBox(Ljava/lang/String;Ljava/lang/String;))
	}-*/;

	public static void showStatusDialogBox(String sysId, String healthStatus) {
		String mes = "";
		if (healthStatus.equals("dead")) {
			mes = "<h4>The system is dead.</h4> Cannot gather data from this system.";
		} else if (healthStatus.equals("bored")) {
			mes = "<h4>Not good health.</h4>";
			mes += "<p>CPU or Memory is running at 90%";
			mes += "<br>Ping time is too long, or one of a service is Dead such as Database server.</p>";
		} else if (healthStatus.equals("smile")) {
			mes += "<h4>Good health</h4>";
		}
		try {
			popupContent.setHTML(mes);
			buttonDetails.setHTML(HTMLControl.getButtonHtml(sysId, true));
			buttonStatistic.setHTML(HTMLControl.getButtonHtml(sysId, false));
			dialogBox.center();
		} catch (Exception ex) {
			// do nothing
		}
	}

	void initDialogBox() {
		dialogBox.setAnimationEnabled(true);
		Button close = new Button();
		close.setStyleName("");
		close.getElement().setId("closeButton");
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
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

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(close);
		dialogVPanel.add(flexTable);
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
				setVisibleLoadingImage(false);
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
				setOnload(false);
			}

			@Override
			public void onSuccess(SystemMonitor[] result) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				setVisibleLoadingImage(false);
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
				setOnload(false);
				systems = result;
				drawTable(systems);				
			}
		});
	}

	/*
	 * Draw table ui with result callback from server via RPC
	 */
	void drawTable(SystemMonitor[] result) {
		if (result != null && result.length > 0) {
			createDataListSystem();
			dataListSystem.addRows(result.length);
			for (int i = 0; i < result.length; i++) {
				dataListSystem.setValue(i, 0, HTMLControl.getLinkSystemDetail(
						result[i].getId(), result[i].getCode()));
				dataListSystem.setValue(
						i,
						1,
						result[i].getName());
				dataListSystem.setValue(
						i,
						2,
						result[i].getUrl());
				dataListSystem
						.setValue(i, 3, result[i].getIp());
				dataListSystem
						.setValue(
								i,
								4,
								result[i].getLastestCpuUsage());
				dataListSystem
						.setValue(
								i,
								5,
								result[i].getLastestMemoryUsage());
				dataListSystem.setValue(i, 6, HTMLControl.getHTMLStatusImage(
						result[i].getId(), result[i].getHealthStatus()));
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
