package cmg.org.monitor.module.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



import cmg.org.monitor.ext.model.shared.UserDto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class UserManagement implements EntryPoint {
	UserManagementServiceAsync userService = GWT.create(UserManagementService.class);
	static private Table myTable;
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		Runnable onLoadCallback = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				myTable = new Table();
				RootPanel.get("dataTableUser").add(myTable);
				init();
			}};
			VisualizationUtils.loadVisualizationApi(onLoadCallback, Table.PACKAGE);
	}
	
	private void init(){
		userService.listUser(new AsyncCallback<Map<String, UserDto>>() {
			
			@Override
			public void onSuccess(Map<String, UserDto> result) {
				// TODO Auto-generated method stub
				drawTable(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.toString());
			}
		});
	}
	@SuppressWarnings({ "rawtypes" })
	private AbstractDataTable createData(Map<String, UserDto> listUser){
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING,"Username");
		data.addColumn(ColumnType.STRING,"Email");
		data.addColumn(ColumnType.STRING,"Group");
		data.addColumn(ColumnType.STRING,"Permission");
		data.addRows(listUser.size());
		Set set = listUser.entrySet();
		Iterator iter = set.iterator();
		int i = 0;
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			UserDto u = (UserDto) entry.getValue();
			String permission = "N/A";
			if(u.getGroup().contains("admin")){
				permission = "Admin";
			}else if(u.getGroup().startsWith("monitor")){
				permission = "Normal user";
			}
			data.setValue(i, 0, u.getUsername());
			data.setValue(i, 1, u.getEmail());
			data.setValue(i, 2, u.getGroup());
			data.setValue(i, 3, permission);
			i++;
		}
		return data;
	}
	private Options option(){
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(true);
		return option;
		
	}
	
	@SuppressWarnings("unused")
	private void drawTable(Map<String, UserDto> listUser){
		myTable.draw(createData(listUser), option());
		
	}
}
