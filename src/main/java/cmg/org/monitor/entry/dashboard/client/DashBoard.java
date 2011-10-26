package cmg.org.monitor.entry.dashboard.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;
import cmg.org.monitor.entity.shared.*;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

public class DashBoard implements EntryPoint {
	// Table of list system with google chart API
	static private Table tableListSystem;
	// Store data of list system
	static private DataTable dataListSystem;
	// Options of table list system
	static private Options opsTableListSystem;
	//
	private final DashBoardServiceAsync dashboardSv = GWT.create(DashBoardService.class);
	
	private static boolean isDone = false;
	
	private static int count = 0;
	
	Timer timerMess;
	Timer timerReload;
	@Override
	public void onModuleLoad() {
		// Create a callback to be called when the visualization API
	    // has been loaded.		
		Runnable onLoadCallback = new Runnable() {
			@Override
			public void run() {		
				// Create table
				tableListSystem = new Table();
				// Create options and datatable of table list system
				createOptionsTableListSystem();
				// Add table to div id 'tableListSystem'
				RootPanel.get("tableListSystem").add(tableListSystem);
				// Create Timer object which auto refresh data table list system
				timerReload = new Timer() {
					@Override
					public void run() {											
						callBack();
					}					
				};
				timerReload.run();
				timerReload.scheduleRepeating(MonitorConstant.REFRESH_RATE);
			}
			
		};
		// Load the visualization api, passing the onLoadCallback to be called
	    // when loading is done.
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);		
	}
	
	public static void clear(Element parent) { 
		Element firstChild; 
	    while((firstChild = DOM.getFirstChild(parent)) != null) { 
	    	DOM.removeChild(parent, firstChild); 
	    } 
	} 
	/*
	 *  Create callback to server via RPC
	 */
	void callBack() {		
		dashboardSv.listSystems(new AsyncCallback<SystemMonitor[]>() {
			@Override
			public void onFailure(Throwable caught) {	
				initMessage("Server error. ","DashBoard.html", "Try again.", HTMLControl.RED_MESSAGE);
				setVisibleMessage(true, HTMLControl.RED_MESSAGE);
				setVisibleLoadingImage(true);
				showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
			}
			@Override
			public void onSuccess(SystemMonitor[] result) {						
				drawTable(result);
			}					
		});		
	}	
	/*
	 *  Draw table ui with result callback from server via RPC
	 */
	void drawTable(SystemMonitor[] result) {
		if (result != null) {
			createDataListSystem();
			dataListSystem.addRows(result.length);
			for(int i = 0; i < result.length; i++) {
				dataListSystem.setValue(i, 0,
						HTMLControl.getLinkSystemDetail(result[i].getId(), result[i].getCode()));
				dataListSystem.setValue(i, 1,
						(result[i].getName() == null) ? "N/A" : result[i].getName());
				dataListSystem.setValue(i, 2, 
						(result[i].getUrl() == null) ? "N/A" : result[i].getUrl());
				dataListSystem.setValue(i, 3, 
						(result[i].getIp() == null) ? "N/A" : result[i].getIp());
				dataListSystem.setValue(i, 4, 
						(result[i].getLastCpuMemory() == null) 
								? 0 
								: (result[i].isActive() && result[i].getStatus()
										? result[i].getLastCpuMemory().getCpuUsage()
										: 0));
				dataListSystem.setValue(i, 5, 
						(result[i].getLastCpuMemory() == null) 
								? 0 
								: (result[i].isActive() && result[i].getStatus()
										? result[i].getLastCpuMemory().getPercentMemoryUsage()
										: 0));
				dataListSystem.setValue(i, 6, 
						HTMLControl.getHTMLStatusImage(result[i].getStatus() && result[i].isActive()));
				dataListSystem.setValue(i, 7, 
						(result[i].isActive() 
								? HTMLControl.HTML_ACTIVE_IMAGE
								: ""));
			}		
			createFormatDataTableListSystem();
			setVisibleMessage(false, HTMLControl.RED_MESSAGE);			
			setVisibleLoadingImage(false);
			// draw table
			tableListSystem.draw(dataListSystem, opsTableListSystem);
		} else {
			initMessage("No system found. ","AddNewSystem.html", "Add new system.", HTMLControl.RED_MESSAGE);
			setVisibleMessage(true,  HTMLControl.RED_MESSAGE);
			showReloadCountMessage(HTMLControl.YELLOW_MESSAGE);
			setVisibleLoadingImage(true);
		}
	}
	
	void showReloadCountMessage(final int typeMessage) {
		count = MonitorConstant.REFRESH_RATE / 1000;
		if (timerMess != null) {
			setVisibleMessage(false, typeMessage);
			timerMess.cancel();
		}
		timerMess = new Timer() {			
			@Override
			public void run() {
				initMessage("Reload table in " + HTMLControl.getStringTime(--count),
						"DashBoard.html", 
						"Reload now.", 
						typeMessage);
				setVisibleMessage(true, typeMessage);
				if (count <= 0) {
					setVisibleMessage(false, typeMessage);
					this.cancel();
				}
			}
		};				
		timerMess.run();
		timerMess.scheduleRepeating(1000);
	}
	
	void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
	}
	
	/*
	 *  Set visible message box
	 */
	void setVisibleMessage(boolean b, int type) {
		RootPanel.get("message-" +  HTMLControl.getColor(type)).setVisible(b);
	}
	/*
	 *  Show message with content
	 */
	void initMessage(String message, String url, String titleUrl, int type) {		
		RootPanel.get("content-" + HTMLControl.getColor(type)).clear();
		RootPanel.get("content-" + HTMLControl.getColor(type))
			.add(new HTML(message + " <a href=\""+ url + "\">" + titleUrl + "</a>", true));
	}
	
	/*
	 *  Create options of table list system
	 */
	void createOptionsTableListSystem() {
		opsTableListSystem = Options.create();
		opsTableListSystem.setAllowHtml(true);
		opsTableListSystem.setShowRowNumber(true);
	}
	
	/*
	 *  Create data table list system without value
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
		dataListSystem.addColumn(ColumnType.STRING, "Status");
		dataListSystem.addColumn(ColumnType.STRING, "Active");		
		
	}
	
	void createFormatDataTableListSystem() {
		// create options of bar format (format columns 'CPU Usage' and 'Memory Usage')
		com.google.gwt.visualization.client.formatters.BarFormat.Options ops
			= com.google.gwt.visualization.client.formatters.BarFormat.Options.create();
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
