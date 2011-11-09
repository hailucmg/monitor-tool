package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;


public class SystemManagement implements EntryPoint {
	SystemManagementServiceAsync systemMSA = GWT.create(SystemManagementService.class);
	SystemMonitor[] listSystem;
	static private Table myTable;
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
			Runnable onLoadCallback = new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					myTable = new Table();
					RootPanel.get("dataTableSystem").add(myTable);
					init();
					History.addValueChangeHandler(new ValueChangeHandler<String>() {
						
						@Override
						public void onValueChange(ValueChangeEvent<String> event) {
							// TODO Auto-generated method stub
							boolean check = Window.confirm("do you want to delete?");
							if(check==false){
								return;
							}
							String id = event.getValue().toString();
							
							systemMSA.deleteSystem(id, new AsyncCallback<Boolean>() {
								
								@Override
								public void onSuccess(Boolean result) {
									// TODO Auto-generated method stub
									if(result){
										try {
											init();
										} catch (Exception e) {
											// TODO: handle exception
											e.printStackTrace();
										}
										
									}
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
								DOM.getElementById("page-heading").setInnerHTML("can not connect to server" + caught.getMessage());	
								}
							});
						}
					});
				}
			};
			VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);

	}
	
	private void init(){
		systemMSA.listSystem(false, new AsyncCallback<SystemMonitor[]>() {
			
			@Override
			public void onSuccess(SystemMonitor[] result) {
				// TODO Auto-generated method stub
				if(result!=null){
					setVisibleLoadingImage(false);
					drawTable(result);
					
				}else{
					DOM.getElementById("dataTableSystem").setInnerHTML("<h1>no system</h1>");
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert("cannot connect to server,please try again");
				caught.printStackTrace();
			}
		});
		
	}
	
	private Options createOption(){
		Options myOption = Options.create();
		myOption.setAllowHtml(true);
		myOption.setShowRowNumber(true);
		return myOption;
	}
	
	private AbstractDataTable createData(SystemMonitor[] listSystem){
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"SID");
		data.addColumn(ColumnType.STRING,"Name");
		data.addColumn(ColumnType.STRING,"URL");
		data.addColumn(ColumnType.STRING,"IP");
		data.addColumn(ColumnType.STRING,"Monitor Status");
		data.addColumn(ColumnType.STRING,"System Health Status");
		data.addColumn(ColumnType.STRING,"Action");
		data.addRows(listSystem.length);
		for(int i = 0;i < listSystem.length ; i++){
			data.setValue(i, 0,(listSystem[i].getCode()==null)?"N/A" :listSystem[i].getCode().toString());
			data.setValue(i, 1, listSystem[i].getName().toString());
			data.setValue(i, 2, listSystem[i].getUrl().toString());
			data.setValue(i, 3, (listSystem[i].getIp()==null)?"N/A" : listSystem[i].getIp());
			data.setValue(i, 4,"<img src=\"images/icon/"+ Boolean.toString(listSystem[i].isActive()) + "_icon.png\" />" );
			data.setValue(i, 5,"<img src=\"images/icon/"+ Boolean.toString(listSystem[i].getStatus()) + "_icon.png\" />" );
			data.setValue(i, 6,"<a href=\"EditSystem.html?id="+listSystem[i].getId()+" \" title=\"Edit\" class=\"icon-1 info-tooltip\"></a>"
								+ "<a href=\"SystemManagement.html#"+listSystem[i].getId()+" \" title=\"Delete\" class=\"icon-2 info-tooltip\"></a>");
		}
		return data;
	}
	private void drawTable(SystemMonitor[] list){
		myTable.draw(createData(list), createOption());
	}
	
	void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
	}
}
