package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.ChangeLogMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class SystemChangeLog extends AncestorEntryPoint {
	static SystemMonitor[] systems;
	static SystemMonitor selectedSystem;
	static private Table tableListSystem;
	static private Table tableChangeLog;
	static private FlexTable tableContent;
	static DialogBox dialogBox;
	static ToggleButton togBtnAll;
	static ToggleButton togBtnSys;
	static boolean isAll;
	static boolean isSys;

	static private Button btnFirst;
	static private Button btnPrev;
	static private Button btnNext;
	static private Button btnLast;
	static private Button pageInfo;

	static private int currentLogPage = 1;
	static private int totalPage = 1;
	static private int totalRows = 1;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_CHANGE_LOG) {
			isAll = true;
			isSys = false;
			HorizontalPanel hPanel = new HorizontalPanel();
			togBtnAll = new ToggleButton("All systems", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					togBtnSys.setVisible(false);
					if (!togBtnSys.isVisible()) {
						togBtnAll.setDown(true);
					}
					currentLogPage = 1;
					viewChangeLog(null);
				}
			});
			togBtnAll.setDown(true);
			togBtnSys = new ToggleButton("S003", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					togBtnAll.setDown(!togBtnSys.isDown());
				}
			});
			togBtnSys.setVisible(false);
			hPanel.add(togBtnAll);
			hPanel.add(togBtnSys);

			tableContent = new FlexTable();
			tableContent.setCellPadding(10);
			tableContent.setCellSpacing(10);
			
			tableChangeLog = new Table();
			tableListSystem = new Table();
			tableListSystem.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					if (systems != null && systems.length > 0) {
						JsArray<Selection> selections = tableListSystem
								.getSelections();
						if (selections.length() == 1) {
							Selection selection = selections.get(0);
							if (selection.isRow()) {
								selectedSystem = systems[selection.getRow()];
								togBtnAll.setDown(false);
								togBtnSys.setDown(true);
								togBtnSys.setVisible(true);
								togBtnSys.setText(selectedSystem.getCode());
								currentLogPage = 1;
								viewChangeLog(selectedSystem);
							}
						}
					}
				}
			});
			
			// START paging zone
			HorizontalPanel hPanelPage = new HorizontalPanel();
			pageInfo = new Button("Page 1/1");
			pageInfo.setEnabled(false);
			// new HTML("<h4>Page 1/1</h4>");
			btnFirst = new Button();
			btnFirst.setStyleName("");
			btnFirst.setStyleName("page-far-left");
			btnFirst.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (totalPage != 1) {
						currentLogPage = 1;
						viewChangeLog(togBtnSys.isVisible() ? selectedSystem
								: null);
					}
				}
			});
			hPanelPage.add(btnFirst);
			btnPrev = new Button();
			btnPrev.setStyleName("");
			btnPrev.setStyleName("page-left");
			btnPrev.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (currentLogPage > 1 && totalPage > 1) {
						currentLogPage--;
						viewChangeLog(togBtnSys.isVisible() ? selectedSystem
								: null);
					}
				}
			});
			hPanelPage.add(btnPrev);
			hPanelPage.add(pageInfo);
			btnNext = new Button();
			btnNext.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (currentLogPage < totalPage) {
						currentLogPage++;
						viewChangeLog(togBtnSys.isVisible() ? selectedSystem
								: null);
					}
				}
			});
			btnNext.setStyleName("");
			btnNext.setStyleName("page-right");
			hPanelPage.add(btnNext);
			btnLast = new Button();
			btnLast.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (totalPage > 1) {
						currentLogPage = totalPage;
						viewChangeLog(togBtnSys.isVisible() ? selectedSystem
								: null);
					}
				}
			});
			btnLast.setStyleName("");
			btnLast.setStyleName("page-far-right");
			hPanelPage.add(btnLast);
			// END paging zone
			 
			tableContent.getFlexCellFormatter().setRowSpan(0, 0, 3);
			tableContent.setWidget(0, 0, tableListSystem);
			tableContent.setWidget(0, 1, hPanel);
			tableContent.setWidget(1, 0, tableChangeLog);
			tableContent.setWidget(2, 0, hPanelPage);

			addWidget(HTMLControl.ID_BODY_CONTENT, tableContent);
			initContent();
			currentLogPage = 1;
			viewChangeLog(null);
		}
	}

	static void viewChangeLog(SystemMonitor sys) {
		int start = (currentLogPage - 1) * MonitorConstant.MAX_ROW_COUNT_CHANGELOG;
		int end = (currentLogPage) * MonitorConstant.MAX_ROW_COUNT_CHANGELOG;
		monitorGwtSv.listChangeLog(sys, start, end,
				new AsyncCallback<MonitorContainer>() {

					@Override
					public void onSuccess(MonitorContainer result) {
						drawTableChangeLog(result.getChangelogs());
						if (result != null) {
							totalRows = result.getChangelogCount();
							if (totalRows % MonitorConstant.MAX_ROW_COUNT_CHANGELOG == 0) {
								totalPage = totalRows
										/ MonitorConstant.MAX_ROW_COUNT_CHANGELOG;
							} else {
								totalPage = Math.round(totalRows
										/ MonitorConstant.MAX_ROW_COUNT_CHANGELOG) + 1;
							}
							
							pageInfo.setText("Page " + currentLogPage + "/"
									+ totalPage);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						drawTableChangeLog(null);
					}
				});
	}

	static void drawTableChangeLog(ChangeLogMonitor[] list) {
		tableChangeLog.draw(createDataListChangeLog(list),
				createOptionsTableListSystem());
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
							"",
							"", HTMLControl.RED_MESSAGE, true);
					setVisibleLoadingImage(false);
				}
				setOnload(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
				showMessage("Server error. ",
						HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Go to System management.",
						HTMLControl.RED_MESSAGE, true);
				setOnload(false);
			}
		});

	}


	static void drawTable(SystemMonitor[] result) {
		if (result != null && result.length > 0) {
			tableListSystem.draw(createDataListSystem(result),
					createOptionsTableListSystem());

		} else {
			showMessage("No system found. ",
					"", "",
					HTMLControl.RED_MESSAGE, true);			
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

	static AbstractDataTable createDataListChangeLog(ChangeLogMonitor[] result) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "System Name");
		data.addColumn(ColumnType.DATETIME, "Date Change");
		data.addColumn(ColumnType.STRING, "Changed By");
		data.addColumn(ColumnType.STRING, "Description");
		if (result != null) {
			data.addRows(result.length);
			for (int i = 0; i < result.length; i++) {
				data.setValue(i, 0, result[i].getSystemName());
				data.setValue(i, 1, result[i].getDatetime());
				data.setValue(i, 2, result[i].getUsername());
				data.setValue(i, 3, result[i].getDescription());
			}
		} else {
			data.addRows(1);
			data.setValue(0, 1, "N/A");
			data.setValue(0, 2, "No changelog found");
		}
		return data;

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
		dataListSystem.addRows(result.length);
		for (int i = 0; i < result.length; i++) {
			dataListSystem.setValue(
					i,
					0,
					result[i].getCode());
			dataListSystem.setValue(i, 1, result[i].getName());
			dataListSystem.setValue(i, 2, result[i].getUrl());
			dataListSystem.setValue(i, 3, result[i].getIp());
			dataListSystem.setValue(
					i,
					4,
					HTMLControl.getHTMLStatusImage(result[i].getId(),
							result[i].getHealthStatus()));
			dataListSystem.setValue(i, 5,
					HTMLControl.getHTMLActiveImage(result[i].isActive()));			
		}		
		return dataListSystem;
	}

}
