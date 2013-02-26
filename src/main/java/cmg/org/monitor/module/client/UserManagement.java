package cmg.org.monitor.module.client;





import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;


import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.ui.SimplePanel;

import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;

import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;


public class UserManagement extends AncestorEntryPoint {
	static private Table myTable;
	static private List<SystemGroup> listGroup = null;
	static private List<SystemUser> listUser = null;
	SimplePanel  panelAuto;
	protected void init() {
		if (currentPage == HTMLControl.PAGE_USER_MANAGEMENT) {
			panelAuto = new SimplePanel();
			panelAuto.addStyleName("userGroupPanel");
			myTable = new Table();
			panelAuto.add(myTable);
			addWidget(HTMLControl.ID_BODY_CONTENT, panelAuto);
			initUI();
		}
	}

	private void initUI(){
		UserManagement.exportStaticMethod();
		monitorGwtSv.getAllUserAndGroup(new AsyncCallback<MonitorContainer>() {
			@Override
			public void onSuccess(MonitorContainer result) {
				if(result!=null){
					boolean checkUser = false;
					boolean checkGroup = false;
					SystemGroup[] listTempGroup = result.getListSystemGroup();
					if(listTempGroup !=null){
						listGroup = new ArrayList<SystemGroup>();
						for(SystemGroup s : listTempGroup){
							listGroup.add(s);
						}
						checkGroup = true;
					}
					SystemUser[] listTempUsers =result.getListSystemUsers();
					if(listTempUsers != null){
						listUser = new ArrayList<SystemUser>();
						for(SystemUser se : listTempUsers){
							listUser.add(se);
						}
						checkUser = true;
					}
					
					if(!checkGroup && !checkUser){
						setVisibleLoadingImage(false);
						showMessage("No user found. ",HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME,"Goto Google Management", HTMLControl.RED_MESSAGE, true);
					}else if(!checkGroup && checkUser){
						setVisibleLoadingImage(false);
						showMessage("No group found. ",HTMLControl.HTML_GROUP_MANAGEMENT_NAME,"Goto Group Management",HTMLControl.RED_MESSAGE, true);
					}else if(!checkUser && checkGroup){
						setVisibleLoadingImage(false);
						showMessage("No user found. ",HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME,"Goto Google Management", HTMLControl.RED_MESSAGE, true);
					}else if(checkGroup && checkUser){
						setVisibleLoadingImage(false);
						setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						/*Element div =  Document.get().getElementById("body-content");
						div.setInnerHTML("<div class=\"userGroupPanel\">" + drawTableHTML(listUser, listGroup) + "</div>");*/
						drawTable(listUser, listGroup);
					}
				}else{
					
					setVisibleLoadingImage(false);
					showMessage("No user found.",HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME, "Goto Google Management",HTMLControl.RED_MESSAGE, true);
					
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
		
		Element cb = Document.get().getElementById(idCheckbox);
		String check = cb.getAttribute("ischeck");
		boolean sendServer;
		if(check.equalsIgnoreCase("true")){
			sendServer = false;
			updateUserMapping(idCheckbox,username, groupID, sendServer);
		}else if(check.equalsIgnoreCase("false")){
			sendServer = true;
			updateUserMapping(idCheckbox,username, groupID, sendServer);
		}
	}
	
	/*private String drawTableHTML(List<SystemUser> listUser ,List<SystemGroup> listGroup){
		listGroup = sortBynameSystemGroup(listGroup);
		int indexGroup = listGroup.size();
		String html = "<table id=\"tableMapping\" border=\"1\">";
		html = html + "<tr><th>User\\Group</th>";
		for(int i = 0; i < listGroup.size();i++){
			html= html + "<th style=\"width : 200px;\">"+listGroup.get(i).getName() +"</th>";
		}
		html = html+ "</tr>";
		for(int j = 0 ; j < listUser.size() ; j++){
			html = html + "<tr>";
			html = html + "<td >" + listUser.get(j).getEmail()+" ("+listUser.get(j).getFirstName()  +" " +listUser.get(j).getLastName()+")" + "</td>";
			for(int k = 0; k < indexGroup ; k++){
				List<String> listGroupUSer = listUser.get(j).getGroupIDs();
				boolean checkInGroup = false;
				for(String g : listGroupUSer){
					if(g.equalsIgnoreCase(listGroup.get(k).getId())){
						checkInGroup = true;
					}
				}
				
				if(checkInGroup){
					String id =listUser.get(j).getId()+ "-" + listGroup.get(k).getId();
					html = html + "<td>" + "<input type=\"checkbox\" title=\""+listUser.get(j).getEmail()+"\"  onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\"      id=\""+id+"\"  class=\"checkbox-size\" ischeck=\"true\"  checked=\"checked\"> " + "</td>";
				}else{
					String id =listUser.get(j).getId()+"-"+listGroup.get(k).getId();
					html = html + "<td>" + "<input type=\"checkbox\" title=\""+listUser.get(j).getEmail()+"\"  onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\" id=\""+id+"\"  class=\"checkbox-size\" ischeck =\"false\">" +"</td>";
				}
				if(k == indexGroup-1){
					html = html +"</tr>";
				}
			}
		}
		html = html + "</table>";
		return html;
	}*/
	private AbstractDataTable createData(List<SystemUser> listUser ,List<SystemGroup> listGroup) {
		listGroup = sortBynameSystemGroup(listGroup);
		listUser = sortBynameSystemUser(listUser);
		DataTable data = DataTable.create();
		int indexGroup = listGroup.size();
		data.addColumn(ColumnType.STRING, "Domain");
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.STRING, "Fullname");
		for(int i = 0; i < listGroup.size();i++){
			data.addColumn(ColumnType.STRING, listGroup.get(i).getName());
		}
		data.addRows(listUser.size());
		for(int j = 0 ; j < listUser.size() ; j++){
		    	data.setValue(j, 0,  "<div style=\"min-width:80px\">" + listUser.get(j).getDomain() + "<div>");
			data.setValue(j, 1,  "<div style=\"min-width:80px\">" + listUser.get(j).getUsername() + "<div>");
			data.setValue(j, 2,  "<div style=\"min-width:80px\">" + listUser.get(j).getFullName() + "<div>");
			for(int k = 0; k < indexGroup ; k++){
				List<String> listGroupUSer = listUser.get(j).getGroupIDs();
				boolean checkInGroup = false;
				for(String g : listGroupUSer){
					if(g.equalsIgnoreCase(listGroup.get(k).getId())){
						checkInGroup = true;
					}
				}
				
				if(checkInGroup){
					String id =listUser.get(j).getId()+ "-" + listGroup.get(k).getId();	
					data.setValue(j, k+3, "<input type=\"checkbox\" title=\""+listUser.get(j).getEmail()+'-' + listGroup.get(k).getName() + "\"  onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\"      id=\""+id+"\"  class=\"checkbox-size\" style='display:block;margin-left:auto;margin-right:auto;border-color:green;box-sizing:content-box;' ischeck=\"true\"  checked=\"checked\"> ");
				}else{
					String id =listUser.get(j).getId()+"-"+listGroup.get(k).getId();	
					data.setValue(j, k+3, "<input type=\"checkbox\" title=\""+listUser.get(j).getEmail()+ '-' + listGroup.get(k).getName() +  "\"  onClick=\"javascript:RecipeData('"+listUser.get(j).getEmail() +"','" + listGroup.get(k).getId() + "','" + id +"');\" id=\""+id+"\"  class=\"checkbox-size\" style='display:block;margin-left:auto;margin-right:auto;border-color:green;box-sizing:content-box;' ischeck =\"false\">");
					
				}
				
			}
		}
		return data;
	}
	
	public static List<SystemUser> sortBynameSystemUser(List<SystemUser> users) {
		SystemUser temp = null;
		for (int i = 1; i < users.size(); i++) {
			int j;
			SystemUser val = users.get(i);
			for (j = i - 1; j > -1; j--) {
				temp = users.get(j);
				if (temp.compareByName(val) <= 0) {
					break;
				}
				users.set(j+1, temp);
			}
			users.set(j+1, val);
		}
		return users;
	}
	public static List<SystemGroup> sortBynameSystemGroup(List<SystemGroup> groups) {
		SystemGroup temp = null;
		for (int i = 1; i < groups.size(); i++) {
			int j;
			SystemGroup val = groups.get(i);
			for (j = i - 1; j > -1; j--) {
				temp = groups.get(j);
				if (temp.compareByName(val) <= 0) {
					break;
				}
				groups.set(j+1, temp);
			}
			groups.set(j+1, val);
		}
		return groups;
	}

	private Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(false);
		return option;

	}
	
	private void drawTable(List<SystemUser> listUser , List<SystemGroup> listGroup ) {
		if(listUser.size() > 0 && listGroup.size() > 0){
			myTable.draw(createData(listUser,listGroup), option());
			
		}else{
			showMessage("No user found.",HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME, "Goto Google Management",HTMLControl.RED_MESSAGE, true);
		}
		
	}
	public static void updateUserMapping(String idCheckBox,String email, String idGroup, boolean mapping){
		 final String idCb = idCheckBox;
		 final boolean map = mapping;
		monitorGwtSv.updateUserMapping(email, idGroup, mapping, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				Element cb = Document.get().getElementById(idCb);
				if(result){
					if(map){
						cb.setAttribute("checked", "checked");
						cb.setAttribute("ischeck", "true");
					}else{
						cb.removeAttribute("checked");
						cb.setAttribute("ischeck", "false");
					}
					showMessage("","","",HTMLControl.RED_MESSAGE, false);
				}else{
					 if(map){
						 cb.removeAttribute("checked");
						 cb.setAttribute("ischeck", "false");
					 }else{
						 cb.setAttribute("checked", "checked");
						 cb.setAttribute("ischeck", "true");
					 }
					 showMessage("Server error. ","","", HTMLControl.RED_MESSAGE, true);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Element cb = Document.get().getElementById(idCb);
				 if(map){
					 cb.removeAttribute("checked");
					 cb.setAttribute("ischeck", "false");
				 }else{
					 cb.setAttribute("checked", "checked");
					 cb.setAttribute("ischeck", "true");
				 }
				 showMessage("Server error. ","","", HTMLControl.RED_MESSAGE, true);
			}
		});
	}

	/**
	 * (non-Javadoc)
	 * @see cmg.org.monitor.module.client.AncestorEntryPoint#initDialog() 
	 */
	@Override
	protected void initDialog() {
		// TODO Auto-generated method stub
		
	}
	
}
