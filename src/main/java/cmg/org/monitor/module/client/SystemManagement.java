package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class SystemManagement extends AncestorEntryPoint {
	static SystemMonitor[] listSystem;
	static private Table tableListSystem;
	static DialogBox dialogBox;
	
	protected void init() {
		SystemManagement.exportStaticMethod();
		if (currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT) {		
			tableListSystem = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, tableListSystem);
			initContent();
		}
	}
	
	public static native void exportStaticMethod() /*-{
	$wnd.showConfirmDialogBox =
	$entry(@cmg.org.monitor.module.client.SystemManagement::showConfirmDialogBox(Ljava/lang/String;Ljava/lang/String;))
	}-*/;
	
	static void showConfirmDialogBox(final String code,final String id) {	
		dialogBox = new DialogBox();
		dialogBox.setText("Confirm delete");
		dialogBox.setAnimationEnabled(true);
		dialogBox.setStyleName("color: #333");
		final Button closeButton = new Button("Cancel");
		closeButton.addStyleName("form-back");
		final Button okButton = new Button("Ok");
		okButton.setStyleName("form-submit");
		closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("loginbox");
		dialogVPanel.add(new HTML("<h3>Do you want to delete System ID "+code+"</h3>"));
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		FlexTable table = new FlexTable();
		table.setCellPadding(10);
		table.setCellSpacing(10);
		table.setWidget(0, 0, okButton);
		table.setWidget(0, 1, closeButton);
		dialogVPanel.add(table);
		okButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {				
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
				setVisibleLoadingImage(true);
				deleteSystem(id);
			}
		});
		closeButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();				
			}
		});
		dialogBox.setWidget(dialogVPanel);
		dialogBox.center();
	}

	private static void initContent() {			
		monitorGwtSv.listSystem(false, new AsyncCallback<SystemMonitor[]>() {
			@Override
			public void onSuccess(SystemMonitor[] result) {
				listSystem = result;
				if (result != null) {
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(result);
				} else {
					showMessage("No system found. ",
							HTMLControl.HTML_ADD_NEW_SYSTEM_NAME,
							"Add new system.", HTMLControl.RED_MESSAGE, true);
					setVisibleLoadingImage(false);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				showMessage("Server error. ",
						HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Try again.",
						HTMLControl.RED_MESSAGE, true);
			}
		});

	}

	public static void deleteSystem(String sysID) {
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
		monitorGwtSv.deleteSystem(sysID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				showMessage("System deleted sucessfully.", "", "",
						HTMLControl.BLUE_MESSAGE, true);
				dialogBox.hide();
				initContent();				
			}

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Cannot delete this system.", "", "",
						HTMLControl.RED_MESSAGE, true);
				dialogBox.hide();
			}
		});

	}

	

	static void drawTable(SystemMonitor[] result) {
		if (result != null) {
			tableListSystem.draw(createDataListSystem(result),
					createOptionsTableListSystem());

		} else {
			showMessage("No system found. ",
					HTMLControl.HTML_ADD_NEW_SYSTEM_NAME, "Add new system.",
					HTMLControl.RED_MESSAGE, true);
			showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
		}
	}

	/*
	 * Create options of table list system
	 */
	static Options createOptionsTableListSystem() {
		Options ops = Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(true);
		return ops;
	}

	/*
	 * Create data table list system without value
	 */
	static AbstractDataTable createDataListSystem(SystemMonitor[] result) {
		// create object data table
		DataTable dataListSystem = DataTable.create();
		// add all columns
		dataListSystem.addColumn(ColumnType.STRING, "SID");
		dataListSystem.addColumn(ColumnType.STRING, "System");
		dataListSystem.addColumn(ColumnType.STRING, "URL");
		dataListSystem.addColumn(ColumnType.STRING, "IP");
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
			dataListSystem
					.setValue(i, 4, HTMLControl.getHTMLStatusImage(result[i]
							.getHealthStatus()));
			dataListSystem.setValue(i, 5,
					HTMLControl.getHTMLActiveImage(result[i].isActive()));
			dataListSystem
					.setValue(
							i,
							6,
							"<a onClick=\"javascript:showConfirmDialogBox('"+result[i].getCode()+"','"
									+ result[i].getId()
									+ "');\" title=\"Delete\" class=\"icon-2 info-tooltip\"></a>");
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
