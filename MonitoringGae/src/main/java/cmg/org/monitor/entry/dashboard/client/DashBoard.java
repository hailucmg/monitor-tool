package cmg.org.monitor.entry.dashboard.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
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
				callBack();				
				Timer t = new Timer() {
					@Override
					public void run() {											
						callBack();
					}					
				};
				t.scheduleRepeating(MonitorConstant.REFRESH_RATE);
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
				showMessage("Server error. ","DashBoard.html", "Try again.", HTMLControl.RED_MESSAGE);
				setVisibleLoadingImage(true);
			}

			@Override
			public void onSuccess(SystemMonitor[] result) {						
				drawTable(result);
			}					
		});
		
		
	}	
	
	void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
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
			showMessage("No system found. ","AddNewSystem.html", "Add new system.", HTMLControl.RED_MESSAGE);	
			setVisibleLoadingImage(true);
		}
	}
	/*
	 *  Set visible message box
	 */
	void setVisibleMessage(boolean b, int type) {
		String color = "";
		switch (type) {		
		case HTMLControl.BLUE_MESSAGE:
			color = "blue";
			break;
		case HTMLControl.RED_MESSAGE:
			color = "red";
		case HTMLControl.YELLOW_MESSAGE :
			color = "yellow";
			break;
		case HTMLControl.GREEN_MESSAGE:			
		default:
			color = "green";
			break;
		}	
		RootPanel.get("message-" + color).setVisible(b);
	}
	/*
	 *  Show message with content
	 */
	void showMessage(String message, String url, String titleUrl, int type) {
		String color = "";
		switch (type) {		
		case HTMLControl.BLUE_MESSAGE:
			color = "blue";
			break;
		case HTMLControl.RED_MESSAGE:
			color = "red";
		case HTMLControl.YELLOW_MESSAGE :
			color = "yellow";
			break;
		case HTMLControl.GREEN_MESSAGE:			
		default:
			color = "green";
			break;
		}		
		
		RootPanel.get("content-" + color).clear();
		RootPanel.get("content-" + color)
			.add(new HTML(message + " <a href=\""+ url + "\">" + titleUrl + "</a>", true));
		setVisibleMessage(true, type);
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
		/*
		// create pattern format SID column
		PatternFormat pfSid = PatternFormat.create("<a href=\"demo_system_details.html?sid={0}\">{0}</a>");
		JsArrayInteger jsaSid = (JsArrayInteger) JsArrayInteger.createArray();
		jsaSid.set(0, 0);
		pfSid.format(dataListSystem, jsaSid, 0);
				
		//create pattern format Status column	
		PatternFormat pfStatus = PatternFormat.create("<img src=\"images/icon/{0}_icon.png\" width=\"24\" height=\"24\" alt=\"\" />");
		JsArrayInteger jsaStatus = (JsArrayInteger) JsArrayInteger.createArray();
		jsaStatus.set(0, 6);
		pfStatus.format(dataListSystem, jsaStatus, 6);
		
		//create pattern format Active column	
		PatternFormat pfActive = PatternFormat.create();
		JsArrayInteger jsaActive = (JsArrayInteger) JsArrayInteger.createArray();
		jsaActive.set(0, 7);
		pfActive.format(dataListSystem, jsaActive, 7);		
		*/
	}
}
