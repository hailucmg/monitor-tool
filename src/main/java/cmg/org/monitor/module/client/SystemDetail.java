package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.LegendPosition;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.formatters.ColorFormat;
import com.google.gwt.visualization.client.visualizations.AnnotatedTimeLine;
import com.google.gwt.visualization.client.visualizations.AreaChart;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.PieChart;
import com.google.gwt.visualization.client.visualizations.Table;

public class SystemDetail extends AncestorEntryPoint {
	//
	private FlexTable flexTableContent;

	private FlexTable flexTableFileSystem;

	private Gauge ggCpu;

	private Gauge ggMemory;

	private Table tblService;

	private Table tblFileSystem;

	private PieChart pieFileSystem;
	
	private PieChart pieJvm;

	private AbsolutePanel panelSystemInfo;

	private AreaChart achCpu;

	private AreaChart achMemory;

	private AnnotatedTimeLine atlStatistic;

	private String sysID;

	private FileSystemMonitor[] listFileSystem;

	private ServiceMonitor[] listService;

	private MemoryMonitor[] listCpuMemory;

	private boolean isShowService = true;
	private boolean isShowFileSystem = true;
	private boolean isShowCpuMemory = true;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL
				|| currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			sysID = HTMLControl.getSystemId(History.getToken());
			try {
				monitorGwtSv.validSystemId(sysID, new AsyncCallback<SystemMonitor>() {
					@Override
					public void onSuccess(SystemMonitor result) {
						if (result != null) {
							flexTableContent = new FlexTable();
							addWidget(HTMLControl.ID_BODY_CONTENT,
									flexTableContent);
							addWidget(HTMLControl.ID_PAGE_HEADING,
									HTMLControl.getPageHeading(result));
							initContent();
						} else {
							showMessage("Invalid System ID. ",
									HTMLControl.HTML_DASHBOARD_NAME,
									"Goto Dashboard. ",
									HTMLControl.RED_MESSAGE, true);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showMessage("Oops! Error.",
								HTMLControl.HTML_DASHBOARD_NAME,
								"Goto Dashboard. ", HTMLControl.RED_MESSAGE,
								true);
					}
				});
			} catch (Exception e) {
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
			}
		}

	}

	void initContent() {
		initFlexTableContent();

		if (currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			initSystemStatistic();
		} else if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL) {
			initSystemDetails();
		}
	}

	void initSystemStatistic() {
		timerReload = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listCpuMemoryHistory(sysID,
						new AsyncCallback<MemoryMonitor[]>() {

							@Override
							public void onSuccess(MemoryMonitor[] result) {
								setVisibleLoadingImage(false);
								setVisibleWidget(HTMLControl.ID_BODY_CONTENT,
										true);
								if (result != null) {
									drawSystemStatistic(result);
								} else {
									showMessage("No statistic found. ", "", "",
											HTMLControl.YELLOW_MESSAGE, true);
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								showMessage("Server error! ",
										HTMLControl.HTML_DASHBOARD_NAME,
										"Goto Dashboard. ",
										HTMLControl.RED_MESSAGE, true);
							}
						});
			}
		};
		timerReload.run();
		timerReload.scheduleRepeating(MonitorConstant.REFRESH_RATE);
	}

	void drawSystemStatistic(MemoryMonitor[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.DATETIME, "Date");
		data.addColumn(ColumnType.NUMBER, "Memory Usage");
		data.addColumn(ColumnType.STRING, "title1");
		data.addColumn(ColumnType.STRING, "text1");
		data.addColumn(ColumnType.NUMBER, "CPU Usage");
		data.addColumn(ColumnType.STRING, "title2");
		data.addColumn(ColumnType.STRING, "text2");
		data.addRows(list.length);
		for (int i = 0; i < list.length; i++) {
			data.setValue(i, 0, list[i].getTimeStamp());
			data.setValue(i, 1, list[i].getPercentMemoryUsage());
			data.setValue(i, 4, list[i].getCpuUsage());
		}
		atlStatistic.draw(data, createAnnotatedTimeLineOptions());
	}

	void initSystemDetails() {
		timerReload = new Timer() {

			@Override
			public void run() {
				monitorGwtSv.getLastestDataMonitor(sysID,
						new AsyncCallback<SystemMonitor>() {

							@Override
							public void onSuccess(SystemMonitor result) {
								if (result != null) {
									setVisibleLoadingImage(false);
									setVisibleWidget(
											HTMLControl.ID_BODY_CONTENT, true);
									drawSystemDetails(result);
								} else {

								}
							}

							@Override
							public void onFailure(Throwable caught) {
								showMessage("Server error! ",
										HTMLControl.HTML_DASHBOARD_NAME,
										"Goto Dashboard. ",
										HTMLControl.RED_MESSAGE, true);
							}
						});
			}
		};
		timerReload.run();
		timerReload.scheduleRepeating(MonitorConstant.REFRESH_RATE);
	}

	void initFlexTableContent() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			atlStatistic = new AnnotatedTimeLine("1000px", "600px");
			flexTableContent.setWidget(0, 0, atlStatistic);
		} else if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL) {
			tblService = new Table();
			ggCpu = new Gauge();
			ggMemory = new Gauge();
			achCpu = new AreaChart();
			achMemory = new AreaChart();
			pieFileSystem = new PieChart();
			pieJvm = new PieChart();
			tblFileSystem = new Table();
			panelSystemInfo = new AbsolutePanel();

			flexTableContent.setCellPadding(3);
			flexTableContent.setCellSpacing(0);
			flexTableContent.getFlexCellFormatter().setColSpan(0, 0, 2);
			flexTableContent.getFlexCellFormatter().setColSpan(2, 0, 2);
			flexTableContent.getFlexCellFormatter().setColSpan(4, 0, 2);
			flexTableContent.getFlexCellFormatter().setWidth(0, 0, "1000px");
			flexTableContent.getFlexCellFormatter().setWidth(1, 0, "300px");
			flexTableContent.getFlexCellFormatter().setWidth(2, 0, "1000px");
			flexTableContent.getFlexCellFormatter().setWidth(3, 0, "300px");
			flexTableContent.getFlexCellFormatter().setWidth(4, 0, "1000px");
			flexTableContent.getFlexCellFormatter().setWidth(5, 0, "300px");
			flexTableContent.getFlexCellFormatter().setWidth(6, 0, "300px");
			flexTableContent.getFlexCellFormatter().setWidth(1, 1, "700px");
			flexTableContent.getFlexCellFormatter().setWidth(3, 1, "700px");
			flexTableContent.getFlexCellFormatter().setWidth(5, 1, "700px");
			flexTableContent.getFlexCellFormatter().setWidth(6, 1, "700px");
			flexTableContent.setWidget(0, 0,
					HTMLControl.getColorTitle("Service Information", true));
			flexTableContent.setWidget(2, 0,
					HTMLControl.getColorTitle("File System Information", true));
			flexTableContent
					.setWidget(4, 0, HTMLControl.getColorTitle(
							"CPU & Memory Information", true));

			flexTableContent.setWidget(1, 0, pieJvm);
			flexTableContent.setWidget(1, 1, tblService);
			flexTableContent.setWidget(3, 0, pieFileSystem);
			flexTableContent.setWidget(3, 1, tblFileSystem);
			flexTableContent.setWidget(5, 0, ggCpu);
			flexTableContent.setWidget(5, 1, achCpu);
			flexTableContent.setWidget(6, 0, ggMemory);
			flexTableContent.setWidget(6, 1, achMemory);

			flexTableContent.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					try {
						Cell cell = flexTableContent.getCellForEvent(event);
						if (cell != null) {
							int cellIndex = cell.getRowIndex();
							if (cellIndex == 0) {
								isShowService = !isShowService;
								tblService.setVisible(isShowService);
								panelSystemInfo.setVisible(isShowService);
								pieJvm.setVisible(isShowService);
								flexTableContent.setWidget(0, 0, HTMLControl
										.getColorTitle("Service Information",
												isShowService));
							} else if (cellIndex == 2) {
								isShowFileSystem = !isShowFileSystem;
								pieFileSystem.setVisible(isShowFileSystem);
								tblFileSystem.setVisible(isShowFileSystem);
								flexTableContent.setWidget(2, 0, HTMLControl
										.getColorTitle(
												"File System Information",
												isShowFileSystem));
							} else if (cellIndex == 4) {
								isShowCpuMemory = !isShowCpuMemory;
								ggCpu.setVisible(isShowCpuMemory);
								ggMemory.setVisible(isShowCpuMemory);
								achCpu.setVisible(isShowCpuMemory);
								achMemory.setVisible(isShowCpuMemory);
								flexTableContent.setWidget(4, 0, HTMLControl
										.getColorTitle(
												"CPU & Memory Information",
												isShowCpuMemory));
							}
						}
					} catch (Exception ex) {
					}
				}
			});

		}
	}

	private SelectHandler createSelectHandler(final Table tbl) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (listFileSystem != null) {
					JsArray<Selection> selections = tbl.getSelections();

					if (selections.length() == 1) {
						Selection selection = selections.get(0);
						if (selection.isRow()) {
							drawPieFileSystem(selection.getRow());
						}
					}
				}
			}
		};
	}

	void drawSystemDetails(SystemMonitor sys) {
		
		panelSystemInfo.clear();
		panelSystemInfo.add(HTMLControl.getSystemInfo(sys));

		drawCpuMemoryInfo(sys);

		drawServiceInfo(sys);

		drawFileSystemInfo(sys);
		
		drawJvmInfo(sys);
	}
	
	void drawJvmInfo(SystemMonitor sys) {
		JvmMonitor jvm = sys.getLastestJvm();
		if (jvm != null) {
			pieJvm.draw(createDataTableJvm(jvm),
					createPieChartOptions("Java Virtual Machine (" + HTMLControl.convertMemoryToString(jvm.getTotalMemory())
												+ " of " + HTMLControl.convertMemoryToString(jvm.getMaxMemory()) + ")"));
		}
	}

	void drawCpuMemoryInfo(SystemMonitor sys) {
		MemoryMonitor cm = sys.getLastCpuMemory() == null ? null : sys
				.getLastCpuMemory();
		// Gauge CPU Usage
		if (cm != null) {
			ggCpu.draw(createGaugeDataTable("CPU", cm.getCpuUsage()),
					createGaugeOptions());
			ggMemory.draw(
					createGaugeDataTable("Memory", cm.getPercentMemoryUsage()),
					createGaugeOptions());
		}
		// Gauge Memory Usage
		;
		// Area Chart CPU Usage & Memory Usage
		listCpuMemory = sys.getListHistoryCpuMemory();
		if (listCpuMemory != null) {
			double[] listCpuUsage = new double[20];
			double[] listMemoryUsage = new double[20];
			if (listCpuMemory.length < 20) {
				for (int i = 0; i < 20 - listCpuMemory.length; i++) {
					listCpuUsage[i] = 0;
					listMemoryUsage[i] = 0;
				}
				for (int i = 0; i < listCpuMemory.length; i++) {
					listCpuUsage[19 - i] = listCpuMemory[i].getCpuUsage();
					listMemoryUsage[19 - i] = listCpuMemory[i].getUsedMemory();
				}
			} else {
				for (int i = 0; i < listCpuMemory.length; i++) {
					listCpuUsage[listCpuMemory.length - i - 1] = listCpuMemory[i]
							.getCpuUsage();
					listMemoryUsage[listCpuMemory.length - i - 1] = listCpuMemory[i]
							.getUsedMemory();
				}
			}
			achCpu.draw(createAreaChartDataTable("CPU Usage", listCpuUsage),
					createAreaChartOptions(100, 0));
			achMemory
					.draw(createAreaChartDataTable("Memory Usage",
							listMemoryUsage),
							createAreaChartOptions(
									listCpuMemory[0].getTotalMemory(), 0));
		}
	}

	void drawServiceInfo(SystemMonitor sys) {
		listService = sys.getLastestServiceMonitors();
		if (listService != null) {
			tblService.draw(createDataTableListService(listService),
					createTableOptions());
		}
	}

	void drawFileSystemInfo(SystemMonitor sys) {
		listFileSystem = sys.getLastestFileSystems();
		if (listFileSystem != null) {
			tblFileSystem.draw(createDataTableListFileSystem(listFileSystem),
					createTableOptions());
			tblFileSystem.addSelectHandler(createSelectHandler(tblFileSystem));
			drawPieFileSystem(0);
		}
	}

	void drawPieFileSystem(int index) {
		if (listFileSystem != null) {
			pieFileSystem.draw(
					createDataTableFileSystem(listFileSystem[index]),
					createPieChartOptions("Local Disk "
							+ listFileSystem[index].getName() + " ("
							+ listFileSystem[index].getType() + ")"));
		}
	}

	PieChart.Options createPieChartOptions(String title) {
		PieChart.Options ops = PieChart.Options.create();
		ops.setTitle(title);
		ops.setHeight(300);
		ops.setWidth(300);
		ops.setEnableTooltip(true);
		ops.set3D(true);
		ops.setLegend(LegendPosition.BOTTOM);

		return ops;
	}
	
	AbstractDataTable createDataTableJvm(JvmMonitor jvm) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Memory");
		data.addRows(2);
		data.setValue(0, 0, "Free Space");
		data.setValue(0, 1, jvm.getFreeMemory());
		data.setValue(1, 0, "Used Space");
		data.setValue(1, 1, jvm.getUsedMemory());
		return data;
	}

	AbstractDataTable createDataTableListFileSystem(FileSystemMonitor[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Local Disk");
		data.addColumn(ColumnType.STRING, "Type");
		data.addColumn(ColumnType.STRING, "Used space");
		data.addColumn(ColumnType.STRING, "Total space");
		data.addRows(list.length);
		for (int i = 0; i < list.length; i++) {
			data.setValue(i, 0, list[i].getName());
			data.setValue(i, 1, list[i].getType());
			data.setValue(i, 2,
					HTMLControl.convertMemoryToString(list[i].getUsed()));
			data.setValue(i, 3,
					HTMLControl.convertMemoryToString(list[i].getSize()));
		}
		return data;
	}

	AbstractDataTable createDataTableFileSystem(FileSystemMonitor fs) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Memory");
		data.addRows(2);
		data.setValue(0, 0, "Free Space");
		data.setValue(0, 1, fs.getSize() - fs.getUsed());
		data.setValue(1, 0, "Used Space");
		data.setValue(1, 1, fs.getUsed());

		return data;
	}

	AbstractDataTable createDataTableListService(ServiceMonitor[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Name");
		data.addColumn(ColumnType.DATETIME, "System date");
		data.addColumn(ColumnType.NUMBER, "Ping time (ms)");
		data.addColumn(ColumnType.STRING, "Status");
		data.addRows(list.length);
		for (int i = 0; i < list.length; i++) {
			data.setValue(i, 0,
					(list[i].getName() == null) ? "N/A" : list[i].getName());
			data.setValue(i, 1, list[i].getSystemDate());
			data.setValue(i, 2, list[i].getPing());
			data.setValue(i, 3,
					HTMLControl.getHTMLStatusImage(list[i].getStatus()));
		}
		try {
			ColorFormat fm = ColorFormat.create();
			fm.addRange(0, 200, "blue", "");
			fm.addRange(200, 500, "orange", "");
			fm.addRange(500, 1000000, "red", "");
			fm.format(data, 2);
		} catch (Exception ex) {
			//
			ex.printStackTrace();
		}
		return data;
	}

	AbstractDataTable createAreaChartDataTable(String title, double[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.NUMBER, title);
		data.addRows(list.length);
		for (int i = 0; i < list.length; i++) {
			data.setValue(i, 0, list[i]);
		}
		return data;
	}

	AbstractDataTable createGaugeDataTable(String title, int value) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Label");
		data.addColumn(ColumnType.NUMBER, "Value");
		data.addRows(1);
		data.setValue(0, 0, title);
		data.setValue(0, 1, value);
		return data;
	}

	Gauge.Options createGaugeOptions() {
		Gauge.Options ops = Gauge.Options.create();
		ops.setGaugeRange(0, 100);
		ops.setMinorTicks(5);
		ops.setYellowRange(75, 90);
		ops.setRedRange(90, 100);
		ops.setWidth(200);
		ops.setHeight(200);
		return ops;
	}

	AreaChart.Options createAreaChartOptions(double max, double min) {
		AreaChart.Options ops = AreaChart.Options.create();
		ops.setWidth(700);
		ops.setHeight(200);
		ops.setMax(max);
		ops.setMin(min);
		ops.setPointSize(0);
		ops.setShowCategories(false);
		ops.setStacked(false);
		return ops;
	}

	Table.Options createTableOptions() {
		Table.Options ops = Table.Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(true);
		ops.setWidth("700px");
		return ops;
	}

	AnnotatedTimeLine.Options createAnnotatedTimeLineOptions() {
		AnnotatedTimeLine.Options ops = AnnotatedTimeLine.Options.create();
		ops.setMax(100);
		ops.setMin(0);
		ops.setDisplayAnnotations(true);
		return ops;
	}
}
