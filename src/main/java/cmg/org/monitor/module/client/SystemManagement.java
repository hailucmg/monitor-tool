package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class SystemManagement extends AncestorEntryPoint {
	static SystemMonitor[] systems;
	static private Table tableListSystem;
	static private FlexTable rootLinkDefault;
	static private FlexTable tableLinkDefault;
	static DialogBox dialogBox;
	private static HTML popupContent;
	private static FlexTable flexHTML;
	static TextBox linkTxt;
	static Button updateBtn;
	static Label linkLb;
	static Label redirectLb;

	protected void init() {
		SystemManagement.exportStaticMethod();
		if (currentPage == HTMLControl.PAGE_SYSTEM_MANAGEMENT) {
			tableListSystem = new Table();
			tableListSystem.setWidth("1185px");
			tableLinkDefault = new FlexTable();
			linkTxt = new TextBox();
			updateBtn = new Button("Update");
			linkLb = new Label();
			linkLb.setText("Proxy link ");
			redirectLb = new Label();
			redirectLb.setText("This will be used to get data from remote system when the monitor tool can't get data directly because unsigned certificate failure");
			tableLinkDefault = new FlexTable();
			rootLinkDefault = new FlexTable();
			tableLinkDefault.getCellFormatter().setVerticalAlignment(0, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			tableLinkDefault.getCellFormatter().setVerticalAlignment(0, 1,
					HasVerticalAlignment.ALIGN_MIDDLE);
			tableLinkDefault.getCellFormatter().setVerticalAlignment(0, 2,
					HasVerticalAlignment.ALIGN_MIDDLE);
			tableLinkDefault.getCellFormatter().setVerticalAlignment(0, 3,
				HasVerticalAlignment.ALIGN_MIDDLE);
			tableLinkDefault.setWidget(0, 0, linkLb);
			tableLinkDefault.setWidget(0, 1, linkTxt);
			tableLinkDefault.setWidget(0, 2, updateBtn);
			tableLinkDefault.setWidget(0, 3, redirectLb);
			rootLinkDefault.setWidget(0, 0, tableLinkDefault);
			rootLinkDefault.setWidget(1, 0, tableListSystem);
			updateBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					updateLink(linkTxt.getText());
				}
			});
			addWidget(HTMLControl.ID_BODY_CONTENT, rootLinkDefault);
			initContent();
		}
	}

	public static native void exportStaticMethod() /*-{
	$wnd.showConfirmDialogBox =
	$entry(@cmg.org.monitor.module.client.SystemManagement::showConfirmDialogBox(Ljava/lang/String;Ljava/lang/String;))
	}-*/;

	static void showConfirmDialogBox(final String code, final String id) {
		dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);
		String name = "";
		if(systems!=null){
			for(SystemMonitor s : systems){
				if(s.getId().toString().equals(id)){
					name = s.getName();
				}
			}
		}
		final Button closeButton = new Button("Cancel");
		closeButton.setStyleName("margin:6px;");
		closeButton.addStyleName("form-button");
		final Button okButton = new Button("OK");
		okButton.setStyleName("margin:6px;");
		okButton.addStyleName("form-button");
		final Button exitButton = new Button();
		exitButton.setStyleName("");
		exitButton.getElement().setId("closeButton");
		exitButton.addStyleName("align=right");
		exitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		VerticalPanel dialogVPanel = new VerticalPanel();
		popupContent = new HTML();
		popupContent.setHTML("<h4>Do you want to delete System "+ name+ "?</h4>");
		flexHTML = new FlexTable();
		flexHTML.setWidget(0, 0, popupContent);
		flexHTML.setStyleName("table-popup");
		flexHTML.addStyleName("align=left");
		FlexTable table = new FlexTable();
		table.setCellPadding(5);
		table.setCellSpacing(5);
		table.setWidget(0, 0, okButton);
		table.setWidget(0, 1, closeButton);
		table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
		table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(exitButton);
		dialogVPanel.add(flexHTML);
		dialogVPanel.add(table);
		dialogVPanel.setCellHorizontalAlignment(exitButton, VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.setCellHorizontalAlignment(flexHTML, VerticalPanel.ALIGN_LEFT);
		dialogVPanel.setCellHorizontalAlignment(table, VerticalPanel.ALIGN_RIGHT);
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
		monitorGwtSv.listSystems(new AsyncCallback<SystemMonitor[]>() {
			@Override
			public void onSuccess(SystemMonitor[] result) {
				systems = result;
				if (result != null) {
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(systems);
				} else {
					showMessage("No system found. ",
							HTMLControl.HTML_ADD_NEW_SYSTEM_NAME,
							"Add new system.", HTMLControl.RED_MESSAGE, true);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
					setVisibleLoadingImage(false);
				}
				setOnload(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				showMessage("Server error. ",
						HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Try again.",
						HTMLControl.RED_MESSAGE, true);
				setOnload(false);
			}
		});
		monitorGwtSv.getLink(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showMessage("Can not load default link.", "", "",
						HTMLControl.RED_MESSAGE, true);
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				createLinkDefaultTable(result);
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
		if (result != null && result.length > 0) {
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
			dataListSystem.setValue(i, 1, result[i].getName());
			dataListSystem.setValue(i, 2, "<a href=\"" + result[i].getUrl()
					+ "\"" + " target=\"_blank\">" + result[i].getUrl()
					+ "</a>");
			dataListSystem.setValue(i, 3, result[i].getIp());
			dataListSystem.setValue(
					i,
					4,
					HTMLControl.getHTMLStatusImage(result[i].getId(),
							result[i].getHealthStatus()));
			dataListSystem.setValue(i, 5,
					HTMLControl.getHTMLActiveImage(result[i].isActive()));
			dataListSystem
					.setValue(
							i,
							6,
							"<a onClick=\"javascript:showConfirmDialogBox('"
									+ result[i].getCode()
									+ "','"
									+ result[i].getId()
									+ "');\" title=\"Delete\" style=\"margin-left: auto;margin-right: auto;margin-top: 5px;\" class=\"icon-delete\"></a>");
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

	static void createLinkDefaultTable(String link) {
		linkTxt.setText(link);
	}

	public static void updateLink(String link) {
		monitorGwtSv.editLink(link, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				showMessage("Updated Failed.", "", "", HTMLControl.RED_MESSAGE,
						true);
			}

			@Override
			public void onSuccess(Void result) {
				// TODO Auto-generated method stub
				showMessage("Updated sucessfully.", "", "",
						HTMLControl.BLUE_MESSAGE, true);
			}
		});
	}
}
