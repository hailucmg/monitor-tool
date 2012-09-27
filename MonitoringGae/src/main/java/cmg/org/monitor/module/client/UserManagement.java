package cmg.org.monitor.module.client;




import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;


public class UserManagement extends AncestorEntryPoint {
	static private Table myTable;
	static private List<SystemGroup> listGroup = null;
	static private List<SystemUser> listUser = null;
	protected void init() {
		if (currentPage == HTMLControl.PAGE_USER_MANAGEMENT) {
			myTable = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, myTable);
			initUI();
		}
	}

	private void initUI(){
		UserManagement.exportStaticMethod();
		monitorGwtSv.getAllGroup(new AsyncCallback<MonitorContainer>() {
			@Override
			public void onSuccess(MonitorContainer result) {
				if(result!=null){
					listGroup = result.getListSystemGroup();
					listUser = result.getListSystemUsers();
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(listUser, listGroup);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showMessage("Oops! Error.",
						HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
				
			}
		});
	}
	
	public static native void exportStaticMethod() /*-{
	$wnd.RecipeData =
	$entry(@cmg.org.monitor.module.client.UserManagement::RecipeData(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;))
	}-*/;
	
	private static void RecipeData(String username, String groupID, String idCheckbox){
		System.out.println("goto recipeData");
		Element cb = Document.get().getElementById(idCheckbox);
		String check = cb.getAttribute("ischeck");
		boolean sendServer;
		if(check.equalsIgnoreCase("true")){
			sendServer = false;
			updateUserMapping(username, groupID, sendServer);
			cb.setAttribute("ischeck", "false");
		}else if(check.equalsIgnoreCase("false")){
			sendServer = true;
			updateUserMapping(username, groupID, sendServer);
			cb.setAttribute("ischeck", "true");
		}
	}
	
	
	private AbstractDataTable createData(List<SystemUser> listUser ,List<SystemGroup> listGroup) {
		DataTable data = DataTable.create();
		int indexGroup = listGroup.size();
		data.addColumn(ColumnType.STRING, "");
		for(int i = 0; i < listGroup.size();i++){
			data.addColumn(ColumnType.STRING, listGroup.get(i).getName());
		}
		data.addRows(listUser.size());
		for(int j = 0 ; j < listUser.size() ; j++){
			data.setValue(j, 0, listUser.get(j).getUsername());
			
			for(int k = 0; k < indexGroup ; k++){
				List<SystemGroup> listGroupUSer = listUser.get(j).getGroups();
				boolean checkInGroup = false;
				for(SystemGroup g : listGroupUSer){
					if(g.getName().equalsIgnoreCase(listGroup.get(k).getName())){
						checkInGroup = true;
					}
				}
				
				if(checkInGroup){
					String id =listUser.get(j).getId()+ "-" + listGroup.get(k).getId();	
					data.setValue(j, k+1, "<input type=\"checkbox\"  onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\"      id=\""+id+"\"  style='display:block;margin-left:auto;margin-right:auto;border-color:green;' ischeck=\"true\"  checked> ");
					
				}else{
					String id =listUser.get(j).getId()+"-"+listGroup.get(k).getId();	
					data.setValue(j, k+1, "<input type=\"checkbox\" onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\" id=\""+id+"\" style='display:block;margin-left:auto;margin-right:auto;border-color:green;' ischeck =\"false\">");
				}
			}
		}
		return data;
	}

	public UserMonitor[] sortByname(UserMonitor[] users) {
		UserMonitor temp = null;
		for (int i = 1; i < users.length; i++) {
			int j;
			UserMonitor val = users[i];
			for (j = i - 1; j > -1; j--) {
				temp = users[j];
				if (temp.compareByName(val) <= 0) {
					break;
				}
				users[j + 1] = temp;
			}
			users[j + 1] = val;
		}
		return users;
	}

	private Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(false);
		return option;

	}
	
	private void drawTable(List<SystemUser> listUser , List<SystemGroup> listGroup ) {
		myTable.draw(createData(listUser,listGroup), option());
		/*myTable.draw(createData(listUser), option());*/

	}
	public static void updateUserMapping(String email, String idGroup, boolean mapping){
		monitorGwtSv.updateUserMapping(email, idGroup, mapping, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				System.out.println("hey yow");
			}
			
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("hey fail");
			}
		});
	}
	
}
