package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;

public class InviteUser extends AncestorEntryPoint{
	AbsolutePanel panelValidateEmail3rd;
    AbsolutePanel panelButtonInvite;
    AbsolutePanel panelAssign;
	TextBox txt_email;
	Label txt_label_email;
	List<SystemGroup> listGroup;
	List<SystemUser> listUser3rds;
	Label label_addGroup;
	ListBox listTemp;
	ListBox listAll;
	Button btt_invite;
	Button btt_MappingGroup;
	Button btt_UnMappingGroup;
	Button btt_reset;
	FlexTable tableInvite;
	String DefaulValueOfListTemp = "None Group assigned";
 	@Override
	protected void init() {
 		if (currentPage == HTMLControl.PAGE_INVITE) {
 			initData();
 		}
		
	}
 	
 	
 	void initData(){
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
						}
						SystemUser [] users = result.getListSystemUsers();
						listUser3rds = new ArrayList<SystemUser>();
						if(users!=null){
							for(SystemUser u : users){
								listUser3rds.add(u);
							}
						}
						initUI(listGroup);
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
 	
 	void initUI(List<SystemGroup> list){
 		tableInvite = new FlexTable();
 		
 		txt_label_email = new Label();
 		txt_label_email.setText("Email");
 		
 		txt_email = new TextBox();
 		txt_email.setWidth("196px");
 		txt_email.setHeight("30px");
 		
 		panelValidateEmail3rd = new AbsolutePanel();
 		
 		label_addGroup = new Label();
 		label_addGroup.setText("Mapping Group");
 		
 		listAll = new ListBox();
 		if(list.size() > 0){
 			for(SystemGroup s : list){
 				listAll.addItem(s.getName());
 			}
 		}
 		
 		listTemp = new ListBox();
 		listTemp.addItem(DefaulValueOfListTemp);
 		listTemp.setMultipleSelect(true);
 		
 		btt_MappingGroup = new Button();
 		btt_MappingGroup.setTitle("Assign Group");
 		btt_MappingGroup.addClickHandler(new MappingGroup());
 		
 		btt_UnMappingGroup = new Button();
 		btt_UnMappingGroup.setTitle("Remove This Group");
 		btt_UnMappingGroup.addClickHandler(new UnMappingGroup());
 		
 		panelAssign = new AbsolutePanel();
 		panelAssign.add(btt_MappingGroup);
 		panelAssign.add(btt_UnMappingGroup);
 		
 		btt_invite = new Button();
 		btt_invite.setText("Submit");
 		
 		btt_reset = new Button();
 		btt_reset.setText("Reset");
 		panelButtonInvite = new AbsolutePanel();
 		panelButtonInvite.add(btt_invite);
 		panelButtonInvite.add(btt_reset);
 		
 		tableInvite.setWidget(0, 0, txt_label_email);
 		tableInvite.setWidget(0, 2, txt_email);
 		tableInvite.setWidget(0, 3, panelValidateEmail3rd);
 		tableInvite.setWidget(1, 0, label_addGroup);
 		tableInvite.setWidget(1, 1, listAll);
 		tableInvite.setWidget(1, 2, panelAssign);
 		tableInvite.setWidget(1, 4, listTemp);
 		tableInvite.getFlexCellFormatter().setColSpan(2, 0, 3);
 		tableInvite.setWidget(2, 0, panelButtonInvite);
 		
 		tableInvite.getFlexCellFormatter().setWidth(0, 0, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(0, 1, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(0, 2, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(1, 0, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(1, 1, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(1, 2, "80px");
 		tableInvite.getFlexCellFormatter().setWidth(1, 3, "100px");
 		tableInvite.getFlexCellFormatter().setWidth(2, 0, "100px");
 		addWidget(HTMLControl.ID_BODY_CONTENT, tableInvite);
		setVisibleLoadingImage(false);
		setOnload(false);
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
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
		if(!email.endsWith("gmail.com")){
			msg = "Email is not validate,only email in domain Google can access to Monitor";
			check = true;
		}
		if (!check) {
			if (listUser3rds!=null) {
				for (SystemUser e : listUser3rds) {
					if(e.getEmail().toString().equals(email)){
						msg = "Email is existed";
						break;
					}

				}
			}

		}
		
		return msg;
	}
}
