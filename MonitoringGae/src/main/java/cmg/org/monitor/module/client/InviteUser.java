package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.List;



import cmg.org.monitor.entity.shared.InvitedUser;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;

public class InviteUser extends AncestorEntryPoint{
    AbsolutePanel panelButtonInvite;
    AbsolutePanel panelAssign;
	static TextArea txt_email;
	static TextArea txt_log;
	static DisclosurePanel panelLog;
	static AbsolutePanel panelValidateEmail;
	static List<SystemGroup> listGroup;
	static List<InvitedUser> listUser3rds;
	Label label_addGroup;
	static ListBox listTemp;
	static ListBox listAll;
	static Button btt_invite;
	static Button btt_MappingGroup;
	static Button btt_UnMappingGroup;
	static Button btt_reset;
	static String DefaulValueOfListTemp = "None Group Mapping";
	static private String defaultFilter = "ALL";
	static private String filter_inActive = "";
	static private String filter_pending = "";
	static private String filter_Active = "";
	static FlexTable tableManagement;
	static FlexTable tableInterface;
	public static Table table_list_3rdParty;
	static Button invited;
	static ListBox filter_box;
	static DialogBox dialogFunction;
	static DialogBox dialogInvite;
	static SimplePanel panelAuto;
 	@Override
	protected void init() {
 		if (currentPage == HTMLControl.PAGE_INVITE) {
 			initData(defaultFilter);
 		}
		
	}
 	
 	public static native void exportViewDialogFunction() /*-{
	$wnd.viewLastestLog =
	$entry(@cmg.org.monitor.module.client.InviteUser::showDialogBox(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;))
	}-*/;
 	
 	static void initData(final String filter){
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
							//init UI
							exportViewDialogFunction();
							initUI(listUser3rds, filter);
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
 	
 	static void initUI(List<InvitedUser> users,String filter){
 		tableInterface = new FlexTable();
 		
 		tableManagement = new FlexTable();
 		invited = new Button();
 		invited.setText("Invited");
 		invited.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showDialogInvited();
			}
		});
 		filter_box = new ListBox();
 		filter_box.addItem(defaultFilter);
 		filter_box.addItem(filter_Active);
 		filter_box.addItem(filter_inActive);
 		filter_box.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String filter_box_value = filter_box.getValue(filter_box.getSelectedIndex());
				boolean check = drawTable(listUser3rds, filter_box_value);
				if(check){
					panelAuto.add(table_list_3rdParty);
				}else{
					panelAuto.add(new HTML("there is no user"));
				}
			}
		});
 		int index = 0;
 		for(int i = 0 ; i < filter_box.getItemCount();i++){
 			if(filter_box.getValue(i).toString().equalsIgnoreCase(filter)){
 				index = i ;
 			}
 		}
 		filter_box.setSelectedIndex(index);
 		tableManagement.setWidget(0, 1, invited);
 		tableManagement.setWidget(0, 2, filter_box);
 		
 		//add table conteiner 2 Widget in table interface
 		tableInterface.setWidget(0, 0, tableManagement);
 		
 		//create table list users
 		table_list_3rdParty = new Table();
 		boolean check = drawTable(users, filter);
 		panelAuto = new SimplePanel();
		panelAuto.setStyleName("userGroupPanel");
 		if(check){
 			panelAuto.add(table_list_3rdParty);
 			tableInterface.setWidget(1, 0, panelAuto);
 		}else{
 			panelAuto.add(new HTML("there is no user"));
 			tableInterface.setWidget(1, 0, panelAuto);
 		}
 		setVisibleLoadingImage(false);
		setOnload(false);
 		addWidget(HTMLControl.ID_BODY_CONTENT, tableInterface);
 		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
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
 					dataListUser.setValue(i, 2, html);
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
 	
 	
 	
 	
 	
 	
 	
 	private static void sendData(String[] data , final String filter) {
 		monitorGwtSv.inviteUser3rd(data, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				initData(filter);
				dialogInvite.hide();
				showMessage("Invite sucessfully.", "", "",
						HTMLControl.BLUE_MESSAGE, true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				initData(filter);
				dialogInvite.hide();
				showMessage("Server error.", "", "",
						HTMLControl.RED_MESSAGE, true);
			}
		});
 	}
 	
 	private static String validateEmail(String email) {
		String msg = "";
		if (email == null || email == "") {
			msg = "This field is required";
		}
		String pattern = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		RegExp regExp = RegExp.compile(pattern);
		String[] data = email.split(",");
		for(String em : data){
			boolean matchFound = regExp.test(em);
			if (matchFound == false) {
				msg = "Email "+ em + " is not validate";
				return msg;
			}
			if (listUser3rds!=null) {
					for (InvitedUser e : listUser3rds) {
						if(e.getEmail().toString().equals(em)){
							msg = "Email : "+ em + "is existed";
							return msg;
						}

					}
				}

		}
		return msg;
	}
 	
 	static void showDialogBox(String idUser, final String actionType, final String filter){
 		if(filter.equalsIgnoreCase(filter_Active)){
 			dialogFunction = new DialogBox();
 	 		dialogFunction.setAnimationEnabled(true);
 	 		VerticalPanel dialogVPanel = new VerticalPanel();
 			//if filter is active so the idUser will be inactive.we will do this in this
 			String tempName = null;
 			for(InvitedUser u : listUser3rds){
 				if(u.getId().toString().equals(idUser)){
 					tempName = u.getEmail();
 				}
 			}
 		
 			final Button closeButton = new Button("Cancel");
 			closeButton.setStyleName("margin:6px;");
 			closeButton.addStyleName("form-button");
 			final Button okButton = new Button("OK");
 			okButton.setStyleName("margin:6px;");
 			okButton.addStyleName("form-button");
 			final Button exitButton = new Button();
 			exitButton.setStyleName("");
 			exitButton.getElement().setId("closeButton");
 			exitButton.addClickHandler(new ClickHandler() {
 				   @Override
 				   public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				   }
 			});
 			HTML popupContent = new HTML();
 			popupContent.setHTML("<h4>Do you want to "+ actionType +" user : " + tempName + "?</h4>");
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.setStyleName("table-popup");
 			FlexTable table = new FlexTable();
 			table.setCellPadding(5);
 			table.setCellSpacing(5);
 			table.setWidget(0, 0, okButton);
 			table.setWidget(0, 1, closeButton);
 			table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
 			table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.add(exitButton);
 			dialogVPanel.add(flexHTML);
 			dialogVPanel.add(table);
 			dialogVPanel.setCellHorizontalAlignment(exitButton, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.setCellHorizontalAlignment(flexHTML, VerticalPanel.ALIGN_LEFT);
 			dialogVPanel.setCellHorizontalAlignment(table, VerticalPanel.ALIGN_RIGHT);
 			okButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	
 				    }
 			});
 			closeButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				    }
 			});
 			dialogFunction.setWidget(dialogVPanel);
 			dialogFunction.getCaption().asWidget().setStyleName("myCaption");
 			dialogFunction.center();
 			
 		}
 		if(filter.equalsIgnoreCase(filter_inActive)){
 			//if filter inActive,so they will action to active this user
 			dialogFunction = new DialogBox();
 	 		dialogFunction.setAnimationEnabled(true);
 	 		VerticalPanel dialogVPanel = new VerticalPanel();
 			//if filter is active so the idUser will be inactive.we will do this in this
 			String tempName = null;
 			for(InvitedUser u : listUser3rds){
 				if(u.getId().toString().equals(idUser)){
 					tempName = u.getEmail();
 				}
 			}
 			final Button closeButton = new Button("Cancel");
 			closeButton.setStyleName("margin:6px;");
 			closeButton.addStyleName("form-button");
 			final Button okButton = new Button("OK");
 			okButton.setStyleName("margin:6px;");
 			okButton.addStyleName("form-button");
 			final Button exitButton = new Button();
 			exitButton.setStyleName("");
 			exitButton.getElement().setId("closeButton");
 			exitButton.addClickHandler(new ClickHandler() {
 				   @Override
 				   public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				   }
 			});
 			HTML popupContent = new HTML();
 			popupContent.setHTML("<h4>Assgin this " +" user : " + tempName + " to group</h4>");
 			listTemp = new ListBox();
 			listTemp.setMultipleSelect(true);
 			listTemp.addItem(DefaulValueOfListTemp);
 			listAll = new ListBox();
 			for(SystemGroup s : listGroup){
 				listAll.addItem(s.getName());
 			}
 			btt_MappingGroup = new Button("Mapping");
 			btt_MappingGroup.addClickHandler(new MappingGroup());
 			btt_UnMappingGroup = new Button("UnMapping");
 			btt_UnMappingGroup.addClickHandler(new UnMappingGroup());
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.setWidget(1, 0, listAll);
 			flexHTML.setWidget(1, 2, btt_MappingGroup);
 			flexHTML.setWidget(1, 3, btt_UnMappingGroup);
 			flexHTML.setWidget(1, 4, listTemp);
 			flexHTML.setStyleName("table-popup");
 			FlexTable table = new FlexTable();
 			table.setCellPadding(5);
 			table.setCellSpacing(5);
 			table.setWidget(0, 0, okButton);
 			table.setWidget(0, 1, closeButton);
 			table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
 			table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.add(exitButton);
 			dialogVPanel.add(flexHTML);
 			dialogVPanel.add(table);
 			dialogVPanel.setCellHorizontalAlignment(exitButton, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.setCellHorizontalAlignment(flexHTML, VerticalPanel.ALIGN_LEFT);
 			dialogVPanel.setCellHorizontalAlignment(table, VerticalPanel.ALIGN_RIGHT);
 			okButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	//send to server
 				    }
 			});
 			closeButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				    }
 			});
 			dialogFunction.setWidget(dialogVPanel);
 			dialogFunction.getCaption().asWidget().setStyleName("myCaption");
 			dialogFunction.center();
 		}
 		if(filter.equalsIgnoreCase(filter_pending)){
 			dialogFunction = new DialogBox();
 	 		dialogFunction.setAnimationEnabled(true);
 	 		VerticalPanel dialogVPanel = new VerticalPanel();
 			//if filter is active so the idUser will be inactive.we will do this in this
 			String tempName = null;
 			for(InvitedUser u : listUser3rds){
 				if(u.getId().toString().equals(idUser)){
 					tempName = u.getEmail();
 				}
 			}
 		
 			final Button closeButton = new Button("Cancel");
 			closeButton.setStyleName("margin:6px;");
 			closeButton.addStyleName("form-button");
 			final Button okButton = new Button("OK");
 			okButton.setStyleName("margin:6px;");
 			okButton.addStyleName("form-button");
 			final Button exitButton = new Button();
 			exitButton.setStyleName("");
 			exitButton.getElement().setId("closeButton");
 			exitButton.addClickHandler(new ClickHandler() {
 				   @Override
 				   public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				   }
 			});
 			HTML popupContent = new HTML();
 			popupContent.setHTML("<h4>Do you want to "+ actionType +" user : " + tempName + "?</h4>");
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.setStyleName("table-popup");
 			FlexTable table = new FlexTable();
 			table.setCellPadding(5);
 			table.setCellSpacing(5);
 			table.setWidget(0, 0, okButton);
 			table.setWidget(0, 1, closeButton);
 			table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
 			table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.add(exitButton);
 			dialogVPanel.add(flexHTML);
 			dialogVPanel.add(table);
 			dialogVPanel.setCellHorizontalAlignment(exitButton, VerticalPanel.ALIGN_RIGHT);
 			dialogVPanel.setCellHorizontalAlignment(flexHTML, VerticalPanel.ALIGN_LEFT);
 			dialogVPanel.setCellHorizontalAlignment(table, VerticalPanel.ALIGN_RIGHT);
 			okButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	
 				    }
 			});
 			closeButton.addClickHandler(new ClickHandler() {
 				    @Override
 				    public void onClick(ClickEvent event) {
 				    	dialogFunction.hide();
 				    }
 			});
 			dialogFunction.setWidget(dialogVPanel);
 			dialogFunction.getCaption().asWidget().setStyleName("myCaption");
 			dialogFunction.center();
 		}
 	}
 	
 	
 	static class MappingGroup implements ClickHandler{

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
 	
 	static class UnMappingGroup implements ClickHandler{

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
 	
	static void showDialogInvited(){
 		dialogInvite = new DialogBox();
 		dialogInvite.setAnimationEnabled(true);
 		final Button exitButton = new Button();
		exitButton.setStyleName("");
		exitButton.getElement().setId("closeButton");
		exitButton.addStyleName("align=right");
		exitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogInvite.hide();
			}
		});
		btt_invite = new Button("Invite");
		btt_invite.setStyleName("margin:6px;");
		btt_invite.addStyleName("form-button");
		btt_invite.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panelValidateEmail.setVisible(false);
				String emails = txt_email.getText();
				String validate = validateEmail(emails);
				if(validate!= ""){
					panelValidateEmail.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"+ validate + "</div>"));
					panelValidateEmail.setVisible(true);
					return;
				}
				String[] data = emails.split(",");
				setOnload(true);
				sendData(data , filter_pending);
				
			/*	SystemUser userInvite = new SystemUser();
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
				}*/
				
			}
		});
		
		
		btt_reset = new Button("Reset");
		btt_reset.setStyleName("margin:6px;");
		btt_reset.addStyleName("form-button");
		
		
		
		txt_email = new TextArea();
	/*	txt_log = new TextArea();
		txt_log.setWidth("1185px");
		txt_log.setHeight("300px");
		txt_log.setReadOnly(true);
		panelLog = new DisclosurePanel();
		panelLog.setTitle("View Log");
		panelLog.setContent(txt_log);*/
		
		panelValidateEmail = new AbsolutePanel();
		panelValidateEmail.setVisible(false);
		
		FlexTable table = new FlexTable();
		table.setCellPadding(5);
		table.setCellSpacing(5);
		table.setWidget(0, 0, txt_email);
		table.setWidget(0, 1, panelValidateEmail);
		table.setWidget(1, 0, btt_invite);
		table.setWidget(1, 1, btt_reset);
		/*table.setWidget(2, 0, panelLog);*/
		table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_RIGHT);
		table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(exitButton);
		dialogVPanel.add(table);
		dialogBox.setWidget(dialogVPanel);
		dialogBox.getCaption().asWidget().setStyleName("myCaption");
		dialogBox.center();
 	}
 	
}
