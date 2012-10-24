package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.Constant;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
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
	static DialogBox dialogBox;
	static DialogBox dialogRequest;
	static TextBox txtFname;
	static TextBox txtLname;
	static TextArea txtDesciption;

	static DialogBox dialogConfirm;
	static TextBox txtFname_confirm;
	static TextBox txtLname_confirm;
	
	
	protected void init() {
		DashBoard.exportStaticMethod();
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

	public static void showRequestForm() {
		dialogRequest.center();
	}

	static class requestHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			String fname = txtFname.getValue();
			if (fname == null || fname.trim().length() == 0) {
				txtFname.setFocus(true);
				return;
			}
			String lname = txtLname.getValue();
			if (lname == null || lname.trim().length() == 0) {
				txtLname.setFocus(true);
				return;
			}
			String descript = txtDesciption.getValue();
			if (descript == null || descript.trim().length() == 0) {
				txtDesciption.setFocus(true);
				return;
			}
			monitorGwtSv.sendRequestPermission(fname, lname, descript,
					new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							showMessage("Server error! ",
									HTMLControl.HTML_ABOUT_NAME,
									"Contact us. ", HTMLControl.RED_MESSAGE,
									true);
						}

						public void onSuccess(Boolean result) {
							if (result) {
								showMessage("Send request sucessfully. ", "",
										"", HTMLControl.BLUE_MESSAGE, true);
								dialogRequest.hide();
							} else {
								showMessage(
										"Cannot send the request. Your request is exists or you account has been suspended.",
										HTMLControl.HTML_ABOUT_NAME,
										"Contact us. ",
										HTMLControl.RED_MESSAGE, true);
								dialogRequest.hide();
							}
						}
					});
		}
	}

	static class resetRequestHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			txtFname.setText("");
			txtLname.setText("");
			txtDesciption.setText("");

		}

	}

	public static native void exportRequest() /*-{
		$wnd.showRequestForm = $entry(@cmg.org.monitor.module.client.DashBoard::showRequestForm())
	}-*/;
	
	public static native void exporConfirmt() /*-{
		$wnd.showConfirm = $entry(@cmg.org.monitor.module.client.DashBoard::showConfirm())
	}-*/;
	
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
	public static void showConfirm() {
		dialogConfirm.center();
	}
	static class resetConfirmHanlder implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			txtFname_confirm.setText("");
			txtLname_confirm.setText("");
		}
		
	}
	static class ConfirmHandler implements ClickHandler{
		@Override
		public void onClick(ClickEvent event) {
			String fname = txtFname_confirm.getText();
			if(fname.trim().length() == 0 ){
				txtFname_confirm.setFocus(true);
				return;
			}
			String lname = txtLname_confirm.getText();
			if(fname.trim().length() == 0 ){
				txtLname_confirm.setFocus(true);
				return;
			}
			
			//do function call server
			
		}
		
	}
	@Override
	protected void initDialog() {
		dialogConfirm = new DialogBox();
		dialogConfirm.setAnimationEnabled(true);
		Button closeConfirm = new Button();
		closeConfirm.setStyleName("");
		closeConfirm.getElement().setId("closeButton");
		closeConfirm.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogConfirm.hide();
			}
		});
		FlexTable tableContainerConfirm = new FlexTable();
		tableContainerConfirm.setCellPadding(3);
		tableContainerConfirm.setCellSpacing(3);
		tableContainerConfirm.getFlexCellFormatter().setWidth(0, 0, "100px");
		tableContainerConfirm.getFlexCellFormatter().setWidth(1, 0, "100px");
		tableContainerConfirm.getFlexCellFormatter().setWidth(2, 0, "100px");
		tableContainerConfirm.getFlexCellFormatter().setWidth(3, 0, "100px");
		tableContainerConfirm.getFlexCellFormatter().setWidth(4, 0, "100px");
		Label lblFnameConfirm = new Label("First Name :");
		Label lblLnameConfirm = new Label("Last Name :");
		txtFname_confirm = new TextBox();
		txtLname_confirm = new TextBox();
		tableContainerConfirm.setWidget(0, 0, lblFnameConfirm);
		tableContainerConfirm.setWidget(0, 1, txtFname_confirm);
		tableContainerConfirm.setWidget(1, 0, lblLnameConfirm);
		tableContainerConfirm.setWidget(1, 1, txtLname_confirm);
		FlexTable flexButtonConfirm = new FlexTable();
		final Button resetButtonConfirm = new Button("Reset");
		resetButtonConfirm.setStyleName("margin:6px;");
		resetButtonConfirm.addStyleName("form-button");
		resetButtonConfirm.addClickHandler(new resetConfirmHanlder());
		final Button okButtonConfirm = new Button("OK");
		okButtonConfirm.setStyleName("margin:6px;");
		okButtonConfirm.addStyleName("form-button");
		okButtonConfirm.addClickHandler(new ConfirmHandler());
		flexButtonConfirm.setCellPadding(5);
		flexButtonConfirm.setCellSpacing(5);
		flexButtonConfirm.setWidget(0, 0, okButtonConfirm);
		flexButtonConfirm.setWidget(0, 1, resetButtonConfirm);
		flexButtonConfirm.getCellFormatter().setHorizontalAlignment(0, 0,
				VerticalPanel.ALIGN_RIGHT);
		flexButtonConfirm.getCellFormatter().setHorizontalAlignment(0, 1,
				VerticalPanel.ALIGN_RIGHT);
		VerticalPanel dialogVPanelConfirm = new VerticalPanel();
		dialogVPanelConfirm.add(closeConfirm);
		dialogVPanelConfirm.add(tableContainerConfirm);
		dialogVPanelConfirm.add(flexButtonConfirm);
		dialogVPanelConfirm.setCellHorizontalAlignment(closeConfirm,
				VerticalPanel.ALIGN_RIGHT);
		dialogVPanelConfirm.setCellHorizontalAlignment(flexButtonConfirm,
				VerticalPanel.ALIGN_RIGHT);
		dialogConfirm.setWidget(dialogVPanelConfirm);
		dialogConfirm.getCaption().asWidget().setStyleName("myCaption");
		
		//
		dialogRequest = new DialogBox();
		dialogRequest.setAnimationEnabled(true);
		Button close = new Button();
		close.setStyleName("");
		close.getElement().setId("closeButton");
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				dialogRequest.hide();
			}
		});
		FlexTable tableContainer = new FlexTable();
		tableContainer.setCellPadding(3);
		tableContainer.setCellSpacing(3);
		tableContainer.getFlexCellFormatter().setWidth(0, 0, "100px");
		tableContainer.getFlexCellFormatter().setWidth(1, 0, "100px");
		tableContainer.getFlexCellFormatter().setWidth(2, 0, "100px");
		tableContainer.getFlexCellFormatter().setWidth(3, 0, "100px");
		tableContainer.getFlexCellFormatter().setWidth(4, 0, "100px");
		Label lblFname = new Label("First Name :");
		Label lblLname = new Label("Last Name :");
		Label lblDesc = new Label("Description");
		txtFname = new TextBox();
		txtLname = new TextBox();
		txtDesciption = new TextArea();
		tableContainer.setWidget(0, 0, lblFname);
		tableContainer.setWidget(0, 1, txtFname);
		tableContainer.setWidget(1, 0, lblLname);
		tableContainer.setWidget(1, 1, txtLname);
		tableContainer.setWidget(2, 0, lblDesc);
		tableContainer.setWidget(2, 1, txtDesciption);

		FlexTable flexButton = new FlexTable();
		final Button resetButton = new Button("Reset");
		resetButton.setStyleName("margin:6px;");
		resetButton.addStyleName("form-button");
		resetButton.addClickHandler(new resetRequestHandler());
		final Button okButton = new Button("OK");
		okButton.setStyleName("margin:6px;");
		okButton.addStyleName("form-button");
		okButton.addClickHandler(new requestHandler());
		flexButton.setCellPadding(5);
		flexButton.setCellSpacing(5);
		flexButton.setWidget(0, 0, okButton);
		flexButton.setWidget(0, 1, resetButton);
		flexButton.getCellFormatter().setHorizontalAlignment(0, 0,
				VerticalPanel.ALIGN_RIGHT);
		flexButton.getCellFormatter().setHorizontalAlignment(0, 1,
				VerticalPanel.ALIGN_RIGHT);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(close);
		dialogVPanel.add(tableContainer);
		dialogVPanel.add(flexButton);
		dialogVPanel.setCellHorizontalAlignment(close,
				VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.setCellHorizontalAlignment(flexButton,
				VerticalPanel.ALIGN_RIGHT);
		dialogRequest.setWidget(dialogVPanel);
		dialogRequest.getCaption().asWidget().setStyleName("myCaption");

		//
		dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);
		Button close1 = new Button();
		close1.setStyleName("");
		close1.getElement().setId("closeButton");
		close1.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		popupContent = new HTML();
		flexTable = new FlexTable();
		flexTable.setWidget(0, 0, popupContent);
		flexTable.setStyleName("table-popup");
		FlexTable flexButton1 = new FlexTable();
		buttonDetails = new HTML();
		buttonStatistic = new HTML();
		flexButton1.setCellPadding(5);
		flexButton1.setCellSpacing(5);
		flexButton1.setWidget(0, 0, buttonDetails);
		flexButton1.setWidget(0, 1, buttonStatistic);
		flexButton1.getCellFormatter().setHorizontalAlignment(0, 0,
				VerticalPanel.ALIGN_RIGHT);
		flexButton1.getCellFormatter().setHorizontalAlignment(0, 1,
				VerticalPanel.ALIGN_RIGHT);
		VerticalPanel dialogVPanel1 = new VerticalPanel();
		dialogVPanel1.add(close1);
		dialogVPanel1.add(flexTable);
		dialogVPanel1.add(flexButton1);
		dialogVPanel1.setCellHorizontalAlignment(close1,
				VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.setCellHorizontalAlignment(flexTable,
				VerticalPanel.ALIGN_LEFT);
		dialogVPanel.setCellHorizontalAlignment(flexButton1,
				VerticalPanel.ALIGN_RIGHT);
		dialogBox.setWidget(dialogVPanel1);
		dialogBox.getCaption().asWidget().setStyleName("myCaption");
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
				dataListSystem.setValue(i, 1, result[i].getName());
				dataListSystem.setValue(i, 2, "<a href=\"" + result[i].getUrl()
						+ "\"" + " target=\"_blank\">" + result[i].getUrl()
						+ "</a>");
				dataListSystem.setValue(i, 3, result[i].getIp());
				dataListSystem.setValue(i, 4, HTMLControl.getPercentBar(
						result[i].getLastestCpuUsage(),
						Constant.CPU_LEVEL_HISTORY_UPDATE));
				dataListSystem.setValue(i, 5, HTMLControl.getPercentBar(
						result[i].getLastestMemoryUsage(),
						Constant.MEMORY_LEVEL_HISTORY_UPDATE));
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
		dataListSystem.addColumn(ColumnType.STRING, "CPU Usage");
		dataListSystem.addColumn(ColumnType.STRING, "Memory Usage");
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
		/*
		 * BarFormat bf = BarFormat.create(ops); bf.format(dataListSystem, 4);
		 * bf.format(dataListSystem, 5);
		 */
	}

}
