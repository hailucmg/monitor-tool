package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
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

	private FileSystemMonitor[] fileSystems;

	private boolean isShowService = true;
	private boolean isShowFileSystem = true;
	private boolean isShowCpuMemory = true;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL
				|| currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			sysID = HTMLControl.getSystemId(History.getToken());
			try {
				monitorGwtSv.validSystemId(sysID,
						new AsyncCallback<SystemMonitor>() {
							@Override
							public void onSuccess(SystemMonitor result) {
								if (result != null) {
									flexTableContent = new FlexTable();
									addWidget(HTMLControl.ID_BODY_CONTENT,
											flexTableContent);
									addWidget(HTMLControl.ID_PAGE_HEADING,
											HTMLControl.getPageHeading(result));
									initContent(result);
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
										"Goto Dashboard. ",
										HTMLControl.RED_MESSAGE, true);
							}
						});
			} catch (Exception e) {
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
			}
		}

	}

	void initContent(SystemMonitor result) {
		initFlexTableContent();
		setVisibleLoadingImage(false);
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
		setOnload(false);
		if (currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			initSystemStatistic(result);
		} else if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL) {
			initSystemDetails(result);
		}
	}

	void initSystemStatistic(final SystemMonitor result) {
		timerReload = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listAlertStore(result,
						new AsyncCallback<AlertStoreMonitor[]>() {

							@Override
							public void onSuccess(AlertStoreMonitor[] result) {
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
								caught.printStackTrace();
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

	void drawSystemStatistic(AlertStoreMonitor[] list) {
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
			if (list[i].getTimeStamp() != null) {
				data.setValue(i, 0, list[i].getTimeStamp());
				data.setValue(i, 1, list[i].getMemUsage());
				data.setValue(i, 4, list[i].getCpuUsage());
				ArrayList<AlertMonitor> alerts = list[i].getAlerts();
				if (alerts != null && alerts.size() > 0) {
					if (list[i].getName() != null) {
						data.setValue(i, 2, list[i].getName());
					}
					if (list[i].getErrors() != null) {
						data.setValue(i, 3, list[i].getErrors());
					}
				}
			}
		}
		atlStatistic.draw(data, createAnnotatedTimeLineOptions());
	}

	void initSystemDetails(SystemMonitor result) {
		drawSystemDetails(result);
		drawCpuInfo(result);
		drawMemory(result);
		drawServiceInfo(result);
		drawFileSystemInfo(result);
		drawJvmInfo(result);
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
				if (fileSystems != null && fileSystems.length > 0) {
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
	}

	void drawJvmInfo(final SystemMonitor sys) {
		timerJvm = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listJvms(sys, new AsyncCallback<JvmMonitor[]>() {
					@Override
					public void onSuccess(JvmMonitor[] result) {
						if (result != null && result.length > 0) {
							JvmMonitor jvm = result[result.length - 1];
							if (jvm != null) {
								pieJvm.draw(
										createDataTableJvm(jvm),
										createPieChartOptions("Java Virtual Machine ("
												+ HTMLControl
														.convertMemoryToString(jvm
																.getTotalMemory())
												+ " of "
												+ HTMLControl
														.convertMemoryToString(jvm
																.getMaxMemory())
												+ ")"));
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
			}
		};
		timerJvm.scheduleRepeating(MonitorConstant.REFRESH_RATE);
		timerJvm.run();
	}

	void drawMemory(final SystemMonitor sys) {
		timerMem = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listMems(sys,
						new AsyncCallback<MonitorContainer>() {
							@Override
							public void onSuccess(MonitorContainer result) {
								if (result != null) {
									MemoryMonitor mem = null;
									MemoryMonitor[] rams = result.getRams();
									MemoryMonitor[] swaps = result.getSwaps();
									if (rams != null && rams.length > 0) {
										mem = rams[rams.length - 1];
										ggMemory.draw(
												createGaugeDataTable("Memory ",
														mem.getPercentUsage()),
												createGaugeOptions());
										achMemory.draw(
												createAreaChartDataTableMemory(
														"Memory Usage (RAM)",
														rams),
												createAreaChartOptions(100, 0));
									}
								}
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
			}
		};
		timerMem.scheduleRepeating(MonitorConstant.CPU_MEMORY_REFRESH_RATE);
		timerMem.run();
	}

	void drawCpuInfo(final SystemMonitor sys) {
		timerCpu = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listCpus(sys, new AsyncCallback<CpuMonitor[]>() {

					@Override
					public void onSuccess(CpuMonitor[] result) {
						if (result != null && result.length > 0) {
							CpuMonitor cpu = result[result.length - 1];
							ggCpu.draw(
									createGaugeDataTable("CPU",
											cpu.getCpuUsage()),
									createGaugeOptions());
							achCpu.draw(
									createAreaChartDataTableCpu("CPU Usage",
											result),
									createAreaChartOptions(100, 0));
						}
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
			}
		};
		timerCpu.scheduleRepeating(MonitorConstant.CPU_MEMORY_REFRESH_RATE);
		timerCpu.run();
	}

	void drawServiceInfo(final SystemMonitor sys) {
		timerService = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listServices(sys,
						new AsyncCallback<ServiceMonitor[]>() {
							@Override
							public void onSuccess(ServiceMonitor[] result) {
								if (result != null && result.length > 0) {
									tblService.draw(
											createDataTableListService(result),
											createTableOptions());
								}
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
			}
		};
		timerService.scheduleRepeating(MonitorConstant.REFRESH_RATE);
		timerService.run();
	}

	void drawFileSystemInfo(final SystemMonitor sys) {
		timerFilesystem = new Timer() {
			@Override
			public void run() {
				monitorGwtSv.listFileSystems(sys,
						new AsyncCallback<FileSystemMonitor[]>() {

							@Override
							public void onSuccess(FileSystemMonitor[] result) {
								if (result != null && result.length > 0) {
									fileSystems = result;
									tblFileSystem
											.draw(createDataTableListFileSystem(result),
													createTableOptions());
									tblFileSystem
											.addSelectHandler(createSelectHandler(tblFileSystem));
									drawPieFileSystem(0);
								}
							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});
			}
		};
		timerFilesystem.scheduleRepeating(MonitorConstant.REFRESH_RATE);
		timerFilesystem.run();
	}

	void drawPieFileSystem(int index) {
		if (fileSystems != null && fileSystems.length > 0) {
			pieFileSystem.draw(
					createDataTableFileSystem(fileSystems[index]),
					createPieChartOptions("Local Disk "
							+ fileSystems[index].getName() + " ("
							+ fileSystems[index].getType() + ")"));
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
			data.setValue(i, 0, list[i].getName());
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
			// do nothing
		}
		return data;
	}

	AbstractDataTable createAreaChartDataTableCpu(String title,
			CpuMonitor[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.NUMBER, title);
		int[] temp = null;
		if (list.length < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
			temp = new int[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH];
			for (int i = 0; i < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH
					- list.length; i++) {
				temp[i] = 0;
			}
			for (int i = 0; i < list.length; i++) {
				temp[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length
						+ i] = list[i].getCpuUsage();
			}
		} else {
			temp = new int[list.length];
			for (int i = 0; i < list.length; i++) {
				temp[i] = list[i].getCpuUsage();
			}
		}
		data.addRows(temp.length);
		for (int i = 0; i < temp.length; i++) {
			data.setValue(i, 0, temp[i]);
		}
		return data;
	}

	AbstractDataTable createAreaChartDataTableMemory(String title,
			MemoryMonitor[] list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.NUMBER, title);
		int[] temp = null;
		if (list.length < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
			temp = new int[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH];
			for (int i = 0; i < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH
					- list.length; i++) {
				temp[i] = 0;
			}
			for (int i = 0; i < list.length; i++) {
				temp[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length
						+ i] = list[i].getPercentUsage();
			}
		} else {
			temp = new int[list.length];
			for (int i = 0; i < list.length; i++) {
				temp[i] = list[i].getPercentUsage();
			}
		}
		data.addRows(temp.length);
		for (int i = 0; i < temp.length; i++) {
			data.setValue(i, 0, temp[i]);
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
		ops.setAllowHtml(true);
		ops.setDisplayAnnotations(true);
		return ops;
	}
}
