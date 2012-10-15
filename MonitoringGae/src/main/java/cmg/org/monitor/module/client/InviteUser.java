package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.DynAnyPackage.Invalid;

import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;

public class InviteUser extends AncestorEntryPoint{
	AbsolutePanel panelValidateEmail3rd;
    AbsolutePanel panelButtonInvite;
    AbsolutePanel panelAssign;
	TextBox txt_email;
	Label txt_label_email;
	List<SystemGroup> listGroup;
	List<InvitedUser> listUser3rds;
	Label label_addGroup;
	ListBox listTemp;
	ListBox listAll;
	Button btt_invite;
	Button btt_MappingGroup;
	Button btt_UnMappingGroup;
	Button btt_reset;
	String DefaulValueOfListTemp = "None Group Mapping";
	static private String defaultFilter = "ALL";
	static private String filter_inActive = "";
	static private String filter_pending = "";
	static private String filter_Active = "";
	FlexTable tableManagement;
	public static Table table_list_3rdParty;
	Button invited;
	ListBox filter_box;
	DialogBox dialogFunction;
	DialogBox dialogInvite;
 	@Override
	protected void init() {
 		if (currentPage == HTMLControl.PAGE_INVITE) {
 			initData(defaultFilter);
 		}
		
	}
 	
 	
 	void initData(String filter){
 		monitorGwtSv.getAllForInvite(new AsyncCallback<MonitorContainer>() {
			@Override
			public void onSuccess(MonitorContainer result) {
				if(result!=null){
					if(result.getListSystemGroup()!=null){
						SystemGroup[] sg = result.getListSystemGroup();
						listGroup = new ArrayList<SystemGroup>();
						if(sg.length > 0 ){
							 for(SystemGroup g : sg){
								 listGroup.add(g);
							 }
							InvitedUser [] users = result.getListInvitedUsers();
							listUser3rds = new ArrayList<InvitedUser>();
							if(users!=null){
								for(InvitedUser u : users){
									listUser3rds.add(u);
								}
							}
							
						}else{
							setVisibleLoadingImage(false);
							setOnload(false);
							showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
						}
					}else{
						setVisibleLoadingImage(false);
						setOnload(false);
						showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
					}
				}else{
					setVisibleLoadingImage(false);
					setOnload(false);
					showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				setVisibleLoadingImage(false);
				setOnload(false);
				showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
			}
		});
 	}
 	
 	void initUI(){
 		table_list_3rdParty = new Table();
 		
 	}
 	
 	static AbstractDataTable createDataListSystem(List<InvitedUser> result, String filter) {
 		DataTable dataListUser = DataTable.create();
 		dataListUser.addColumn(ColumnType.STRING, "USERNAME");
 		dataListUser.addColumn(ColumnType.STRING, "STATUS");
 		dataListUser.addColumn(ColumnType.STRING, "ACTION");
 		
 		List<InvitedUser> listTemp = new ArrayList<InvitedUser>();
 		
 		if(filter.equalsIgnoreCase(defaultFilter)){
 			for(InvitedUser u : result){
 				listTemp.add(u);
 			}
 		}else{
 			for(InvitedUser u : result){
 	 			if(u.getStatus().equalsIgnoreCase(filter)){
 	 				listTemp.add(u);
 	 			}
 	 		}
 		}
 		
 		if(listTemp.size() > 0){
 			dataListUser.addRows(listTemp.size());
 			for(int i = 0 ; i < listTemp.size() ; i++){
 				InvitedUser u = listTemp.get(i);
 				dataListUser.setValue(i, 0, u.getEmail());
 				dataListUser.setValue(i, 1, u.getStatus());
 				if(u.getStatus().equalsIgnoreCase(filter_Active)){
 					String html = "<div>" +HTMLControl.getButtonForActiveUser(u)+ "<div>";
 				}
 				if(u.getStatus().equalsIgnoreCase(filter_inActive)){
 					String html = "<div>" +HTMLControl.getButtonForInActiveUser(u) + "<div>";
 					dataListUser.setValue(i, 2, html);
 				}
 				if(u.getStatus().equalsIgnoreCase(filter_pending)){
 					String html = "<div>" +HTMLControl.getButtonForPendingUser(u)+ "<div>";
 					dataListUser.setValue(i, 2, html);
 				}
 			}
 		}
 		return dataListUser;
 	}
 	static boolean drawTable(List<InvitedUser> users, String filter){
 		if(users.size() > 0 ){
 			table_list_3rdParty.draw(createDataListSystem(users, filter));
 			return true;
 		}else{
 			return false;
 		}
 	}
 	
 	class MappingGroup implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			//get value of select then delete it in list
			int index = listAll.getSelectedIndex();
			String groupMapping = listAll.getValue(index);
			listAll.removeItem(index);
			
			//add value of group mapping then delete the default value
			listTemp.addItem(groupMapping);
			for(int i = 0 ; i < listTemp.getItemCount();i++){
				if(listTemp.getValue(i).toString().equalsIgnoreCase(DefaulValueOfListTemp)){
					listTemp.removeItem(i);
				}
			}
		}
 		
 	}
 	
 	class UnMappingGroup implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			List<Integer> listSelected = new ArrayList<Integer>();
			for(int i = 0 ; i < listTemp.getItemCount();i++){
				if(listTemp.isItemSelected(i) && listTemp.getValue(i).equalsIgnoreCase(DefaulValueOfListTemp)){
					listSelected.add(i);
				}
			}
			if(listSelected.size() > 0 ){
				showMessage("Oops! Error.", "", "Can not un mapping group", HTMLControl.RED_MESSAGE, false);
				List<String> listValue = new ArrayList<String>();
				
				for(int index : listSelected){
					listValue.add(listTemp.getValue(index));
					listTemp.removeItem(index);
				}
				
				if(listValue.size() > 0 ){
					//add group to list all again
					for(String value : listValue){
						listAll.addItem(value);
					}
					//add default value again when list temp is null
					if(listTemp.getItemCount() == 0){
						listTemp.addItem(DefaulValueOfListTemp);
					}
				}else{
					showMessage("Oops! Error.", "", "Can not un mapping group", HTMLControl.RED_MESSAGE, true);
				}
			}else{
				showMessage("Oops! Error.", "", "Can not un mapping group", HTMLControl.RED_MESSAGE, true);
			}
			
		}
 		
 	}
 	
 	
 	class inviteUser implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			panelValidateEmail3rd.setVisible(false);
			String email = txt_email.getText();
			String validate = validateEmail(email);
			if(validate!= ""){
				panelValidateEmail3rd.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"+ validate + "</div>"));
				panelValidateEmail3rd.setVisible(true);
				return;
			}
			SystemUser userInvite = new SystemUser();
			userInvite.setEmail(email);
			userInvite.setUsername(email.split("@")[0]);
			for(int i = 0 ; i < listTemp.getItemCount();i++){
				if(listTemp.isItemSelected(i) && listTemp.getValue(i).equalsIgnoreCase(DefaulValueOfListTemp)){
					for(SystemGroup s : listGroup){
						if(s.getName().toString().equals(listTemp.getValue(i))){
							userInvite.addGroup(s.getId());
						}
					}
				}
			}
			
			sendData(userInvite);
			
		}
 		
 	}
 	
 	
 	
 	
 	private void sendData(SystemUser u) {
 		monitorGwtSv.inviteUser3rd(u, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
			
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
				
			}
		});
 	}
 	
 	private String validateEmail(String email) {
		String msg = "";
		boolean check = false;
		if (email == null || email == "") {
			msg = "This field is required";
			check = true;
		}
		String pattern = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		RegExp regExp = RegExp.compile(pattern);
		boolean matchFound = regExp.test(email);
		if (matchFound == false) {
			msg = "Email is not validate";
			check = true;
		}
		if (listUser3rds!=null) {
				for (InvitedUser e : listUser3rds) {
					if(e.getEmail().toString().equals(email)){
						msg = "Email is existed";
						break;
					}

				}
			}

		return msg;
	}
}
