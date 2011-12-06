package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.memcache.shared.CpuDTO;
import cmg.org.monitor.memcache.shared.FileSystemCacheDto;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.memcache.shared.MemoryDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;
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

	private ArrayList<FileSystemCacheDto> fileSystemList;

	private ArrayList<ServiceMonitorDto> services;

	private ArrayList<CpuDTO> cpuHistory;

	private ArrayList<ArrayList<MemoryDto>> memHistory;

	private boolean isShowService = true;
	private boolean isShowFileSystem = true;
	private boolean isShowCpuMemory = true;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_SYSTEM_DETAIL
				|| currentPage == HTMLControl.PAGE_SYSTEM_STATISTIC) {
			sysID = HTMLControl.getSystemId(History.getToken());
			try {
				monitorGwtSv.validSystemId(sysID, new AsyncCallback<Boolean>() {
					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							flexTableContent = new FlexTable();
							addWidget(HTMLControl.ID_BODY_CONTENT,
									flexTableContent);
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
						caught.printStackTrace();
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
				/*
				 * monitorGwtSv.listCpuMemoryHistory(sysID, new
				 * AsyncCallback<CpuMemory[]>() {
				 * 
				 * @Override public void onSuccess(CpuMemory[] result) {
				 * setVisibleLoadingImage(false);
				 * setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true); if
				 * (result != null) { drawSystemStatistic(result); } else {
				 * showMessage("No statistic found. ", "", "",
				 * HTMLControl.YELLOW_MESSAGE, true); } }
				 * 
				 * @Override public void onFailure(Throwable caught) {
				 * showMessage("Server error! ",
				 * HTMLControl.HTML_DASHBOARD_NAME, "Goto Dashboard. ",
				 * HTMLControl.RED_MESSAGE, true); } });
				 */
			}
		};
		timerReload.run();
		timerReload.scheduleRepeating(MonitorConstant.REFRESH_RATE);
	}

	void drawSystemStatistic(CpuMemory[] list) {
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
						new AsyncCallback<SystemMonitorDto>() {
							@Override
							public void onSuccess(SystemMonitorDto result) {
								if (result != null) {
									setVisibleLoadingImage(false);
									setVisibleWidget(
											HTMLControl.ID_BODY_CONTENT, true);
									drawSystemDetails(result);
								} else {
									showMessage("Oops! Error.",
											HTMLControl.HTML_DASHBOARD_NAME,
											"Goto Dashboard. ",
											HTMLControl.RED_MESSAGE, true);
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
				if (fileSystemList != null) {
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

	void drawSystemDetails(SystemMonitorDto sys) {

		panelSystemInfo.clear();
		panelSystemInfo.add(HTMLControl.getSystemInfo(sys));

		drawCpuMemoryInfo(sys);

		drawServiceInfo(sys);

		drawFileSystemInfo(sys);

		drawJvmInfo(sys);
	}

	void drawJvmInfo(SystemMonitorDto sys) {
		JvmDto jvm = sys.getLastestJvm();
		if (jvm != null) {
			pieJvm.draw(
					createDataTableJvm(jvm),
					createPieChartOptions("Java Visual Memory ("
							+ HTMLControl.convertMemoryToString(jvm
									.getUsedMemory())
							+ " of "
							+ HTMLControl.convertMemoryToString(jvm
									.getMaxMemory()) + ")"));
		}
	}

	void drawCpuMemoryInfo(SystemMonitorDto sys) {
		cpuHistory = sys.getCpuHistory();
		memHistory = sys.getMemHistory();

		ArrayList<MemoryDto> memList = null;

		CpuDTO cpu = null;
		MemoryDto mem = null;
		if (cpuHistory != null) {
			cpu = cpuHistory.get(0);
			if (cpuHistory.size() < MonitorConstant.HISTORY_CPU_MEMORY_LENGTH) {
				CpuDTO cpuTemp = null;
				int cpuListSize = cpuHistory.size();
				for (int i = 0; i < MonitorConstant.HISTORY_CPU_MEMORY_LENGTH
						- cpuListSize; i++) {
					cpuTemp = new CpuDTO();
					cpuTemp.setCpuUsage(0);
					cpuHistory.add(cpuTemp);
				}
			}
		}
		if (memHistory != null) {
			memList = new ArrayList<MemoryDto>();
			for (ArrayList<MemoryDto> ramList : memHistory) {
				memList.add(ramList.get(0));
			}
			if (memHistory.size() < MonitorConstant.HISTORY_CPU_MEMORY_LENGTH) {
				MemoryDto memTemp = null;
				for (int i = 0; i < MonitorConstant.HISTORY_CPU_MEMORY_LENGTH
						- memHistory.size(); i++) {
					memTemp = new MemoryDto();
					memTemp.setUsedMemory(0);
					memList.add(memTemp);
				}
			}
			mem = memList.get(0);
		}
		// Gauge CPU Usage
		if (cpu != null) {
			ggCpu.draw(createGaugeDataTable("CPU", cpu.getCpuUsage()),
					createGaugeOptions());

		}
		// Gauge Memory Usage
		if (mem != null) {
			ggMemory.draw(
					createGaugeDataTable("Memory", mem.getPercentUsage()),
					createGaugeOptions());
		}

		// Area Chart CPU Usage & Memory Usage
		if (cpuHistory != null) {
			double[] listCpuUsage = new double[cpuHistory.size()];

			for (int i = 0; i < cpuHistory.size(); i++) {
				listCpuUsage[cpuHistory.size() - i - 1] = cpuHistory.get(i)
						.getCpuUsage();
			}

			achCpu.draw(createAreaChartDataTable("CPU Usage", listCpuUsage),
					createAreaChartOptions(100, 0));

		}
		if (memList != null) {
			double[] listMemoryUsage = new double[memList.size()];
			for (int i = 0; i < memList.size(); i++) {
				listMemoryUsage[memList.size() - i - 1] = memList.get(i)
						.getUsedMemory() / 1024;
			}
			achMemory.draw(
					createAreaChartDataTable("Memory Usage", listMemoryUsage),
					createAreaChartOptions(memList.get(0).getTotalMemory() / 1024, 0));
		}

	}

	void drawServiceInfo(SystemMonitorDto sys) {
		services = sys.getServices();
		if (services != null) {
			tblService.draw(createDataTableListService(services),
					createTableOptions());
		}
	}

	void drawFileSystemInfo(SystemMonitorDto sys) {
		fileSystemList = sys.getFileSystems();
		if (fileSystemList != null) {
			tblFileSystem.draw(createDataTableListFileSystem(fileSystemList),
					createTableOptions());
			tblFileSystem.addSelectHandler(createSelectHandler(tblFileSystem));
			drawPieFileSystem(0);
		}
	}

	void drawPieFileSystem(int index) {
		if (fileSystemList != null) {
			FileSystemCacheDto fs = fileSystemList.get(index);
			pieFileSystem.draw(createDataTableFileSystem(fs),
					createPieChartOptions("Local Disk " + fs.getName() + " ("
							+ fs.getType() + ")"));
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

	AbstractDataTable createDataTableJvm(JvmDto jvm) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Task");
		data.addColumn(ColumnType.NUMBER, "Memory");
		data.addRows(2);
		data.setValue(0, 0, "Free Space");
		data.setValue(0, 1, jvm.getFreeMemory());
		data.setValue(1, 0, "Used Space");
		data.setValue(1, 1, jvm.getTotalMemory() - jvm.getFreeMemory());
		return data;
	}

	AbstractDataTable createDataTableListFileSystem(
			ArrayList<FileSystemCacheDto> list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Local Disk");
		data.addColumn(ColumnType.STRING, "Type");
		data.addColumn(ColumnType.STRING, "Used space");
		data.addColumn(ColumnType.STRING, "Total space");
		data.addRows(list.size());
		FileSystemCacheDto fs = null;
		for (int i = 0; i < list.size(); i++) {
			fs = list.get(i);
			data.setValue(i, 0, fs.getName());
			data.setValue(i, 1, fs.getType());
			data.setValue(i, 2, HTMLControl.convertMemoryToString(fs.getUsed()));
			data.setValue(i, 3, HTMLControl.convertMemoryToString(fs.getSize()));
		}
		return data;
	}

	AbstractDataTable createDataTableFileSystem(FileSystemCacheDto fs) {
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

	AbstractDataTable createDataTableListService(
			ArrayList<ServiceMonitorDto> list) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Name");
		data.addColumn(ColumnType.DATETIME, "System date");
		data.addColumn(ColumnType.NUMBER, "Ping time (ms)");
		data.addColumn(ColumnType.STRING, "Status");
		data.addRows(list.size());
		ServiceMonitorDto service = null;
		for (int i = 0; i < list.size(); i++) {
			service = list.get(i);
			data.setValue(i, 0,
					(service.getName() == null) ? "N/A" : service.getName());
			data.setValue(i, 1, service.getSystemDate());
			data.setValue(i, 2, service.getPing());
			data.setValue(i, 3,
					HTMLControl.getHTMLStatusImage(service.isStatus()));
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
