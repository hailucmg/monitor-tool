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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class InviteUser extends AncestorEntryPoint{
    AbsolutePanel panelButtonInvite;
    static AbsolutePanel panelValidateGroups;
    AbsolutePanel panelAssign;
	static TextArea txt_email;
	static TextArea txt_log;
	static DisclosurePanel panelLog;
	static AbsolutePanel panelValidateEmail;
	static List<SystemGroup> listGroup;
	static List<InvitedUser> listUser3rds;
	static ListBox listTemp;
	static ListBox listAll;
	static ListBox listGroupInvi;
	static Button btt_invite;
	static Button btt_MappingGroup;
	static Button btt_UnMappingGroup;
	static Button btt_reset;
	static String DefaulValueOfListTemp = "None Group Mapping";
	static private String defaultFilter = "ALL";
	static private String filter_requesting = InvitedUser.STATUS_REQUESTING;
	static private String filter_pending = InvitedUser.STATUS_PENDING;
	static private String filter_Active = InvitedUser.STATUS_ACTIVE;
	static FlexTable tableManagement;
	static FlexTable tableInterface;
	static Table table_list_3rdParty;
	static Button invited;
	static ListBox filter_box;
	static DialogBox dialogFunction;
	static DialogBox dialogInvite;
	static SimplePanel panelAuto;
	static String filterStatic;
	static String ActionStatic;
 	@Override
	protected void init() {
 		if (currentPage == HTMLControl.PAGE_INVITE) {
 			InviteUser.exportViewDialogFunction();
 			InviteUser.exportViewDialogInvite();
 			table_list_3rdParty = new Table();
 	 		tableInterface = new FlexTable();
 	 		tableManagement = new FlexTable();
 	 		filter_box = new ListBox();
 	 		filter_box.setTitle("FILTER");
 	 		filter_box.setStyleName("filter_box");
 	 		filter_box.addItem(defaultFilter);
 	 		filter_box.addItem(filter_Active);
 	 		filter_box.addItem(filter_requesting);
 	 		filter_box.addItem(filter_pending);
 	 		tableManagement.setWidget(0, 0, new HTML("<a class=\"btnInviteUser\" title=\"INVITE USER\" onClick=\"javascript:showDialogInvited()\" />"));
 	 		tableManagement.setWidget(0, 1, filter_box);
 	 		/*tableManagement.setWidget(1, 0, panelLog);*/
 	 		tableManagement.setCellPadding(5);
 	 		tableManagement.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_CENTER);
 	 		/*tableManagement.setCellSpacing(20);*/
 			initData(defaultFilter);
 		}
		
	}
 	
 	public static native void exportViewDialogFunction() /*-{
	$wnd.showDialogBox =
	$entry(@cmg.org.monitor.module.client.InviteUser::showDialogBox(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;))
	}-*/;
 	
 	public static native void exportViewDialogInvite() /*-{
	$wnd.showDialogInvited =
	$entry(@cmg.org.monitor.module.client.InviteUser::showDialogInvited())
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
							initUI(listUser3rds, filter);
						}else{
							setVisibleLoadingImage(false);
							setOnload(false);
							showMessage("Oops!There are no group in system.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
						}
					}else{
						setVisibleLoadingImage(false);
						setOnload(false);
						showMessage("Oops!There are no group in system.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
					}
				}else{
					setVisibleLoadingImage(false);
					setOnload(false);
					showMessage("Oops!Server Error please try again!.", HTMLControl.HTML_USER_INVITE, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
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
 		//create table list users
 			panelAuto = new SimplePanel();
			panelAuto.setStyleName("userGroupPanel");
			panelAuto.add(new HTML("there is no user"));
 		if(users.size() > 0){
 	 		int index = 0;
 	 		for(int i = 0 ; i < filter_box.getItemCount();i++){
 	 			if(filter_box.getValue(i).toString().equalsIgnoreCase(filter)){
 	 				index = i ;
 	 			}
 	 		}
 	 		filter_box.setSelectedIndex(index);
 	 		table_list_3rdParty.draw(createDataListSystem(users, filter),createOptionsTableListUser());
 	 		//add table conteiner 2 Widget in table interface
 	 		tableInterface.setWidget(0, 0, tableManagement);
 	 		tableInterface.setWidget(1, 0, table_list_3rdParty);
 	 		setVisibleLoadingImage(false);
 			setOnload(false);
 	 		addWidget(HTMLControl.ID_BODY_CONTENT, tableInterface);
 	 		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
 	 		filter_box.addClickHandler(new ClickHandler() {
 				
 				@Override
 				public void onClick(ClickEvent event) {
 					String filter_box_value = filter_box.getValue(filter_box.getSelectedIndex());
 					boolean check = checkingFilter(listUser3rds, filter_box_value);
 					if(check){
 			 			table_list_3rdParty.draw(createDataListSystem(listUser3rds, filter_box_value),createOptionsTableListUser());
 			 			tableInterface.setWidget(1, 0, table_list_3rdParty);
 					}else{
 						panelAuto.setWidget(new HTML("There is no users"));
 						tableInterface.setWidget(1, 0, panelAuto);
 					}
 				}
 			});
 		}else{
 	 		int index = 0;
 	 		for(int i = 0 ; i < filter_box.getItemCount();i++){
 	 			if(filter_box.getValue(i).toString().equalsIgnoreCase(filter)){
 	 				index = i ;
 	 			}
 	 		}
 	 		filter_box.setSelectedIndex(index);
 			tableInterface.setWidget(0, 0, tableManagement);
 			tableInterface.setWidget(1, 0, panelAuto);
 			setVisibleLoadingImage(false);
 			setOnload(false);
 	 		addWidget(HTMLControl.ID_BODY_CONTENT, tableInterface);
 	 		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
 	 		filter_box.addClickHandler(new ClickHandler() {
 				
 				@Override
 				public void onClick(ClickEvent event) {
 					String filter_box_value = filter_box.getValue(filter_box.getSelectedIndex());
 					boolean check = checkingFilter(listUser3rds, filter_box_value);
 					if(check){
 			 			table_list_3rdParty.draw(createDataListSystem(listUser3rds, filter_box_value),createOptionsTableListUser());
 			 			tableInterface.setWidget(1, 0, table_list_3rdParty);
 					}else{
 						panelAuto.setWidget(new HTML("There is no users"));
 						tableInterface.setWidget(1, 0, panelAuto);
 					}
 				}
 			});
 		}
 	
 	}
 	

 	
 	public static AbstractDataTable createDataListSystem(List<InvitedUser> result, String filter) {
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
 				dataListUser.setValue(i, 0,"<div style=\"min-width:450px\">"+ u.getEmail()+ "</div>");
 				if(u.getStatus().equalsIgnoreCase(filter_Active)){
 					String status = "<a style=\"margin-left:auto;margin-right:auto;\" class=\"stt_userActive\" title=\""+filter_Active+"\"/>";
 					dataListUser.setValue(i, 1, "<div style=\"min-width:450px\">"+status+"</div>");
 					String html = HTMLControl.getButtonForActiveUser(u);
 					dataListUser.setValue(i, 2,"<div style=\"min-width:220px\">" +html  + "</div>");
 				}
 				if(u.getStatus().equalsIgnoreCase(filter_requesting)){
 					String status = "<a style=\"margin-left:auto;margin-right:auto;\" class=\"stt_inactive\" title=\""+filter_requesting+"\"/>";
 					dataListUser.setValue(i, 1, "<div style=\"min-width:450px\">"+status+"</div>");
 					String html = HTMLControl.getButtonForRequestingUser(u);
 					dataListUser.setValue(i, 2,"<div style=\"min-width:220px\">" + html + "</div>");
 				}
 				if(u.getStatus().equalsIgnoreCase(filter_pending)){
 					String status = "<a style=\"margin-left:auto;margin-right:auto;\" class=\"stt_pending\" title=\""+filter_pending+"\"/>";
 					dataListUser.setValue(i, 1, "<div style=\"min-width:450px\">"+status+"</div>");
 					String html = HTMLControl.getButtonForPendingUser(u);
 					dataListUser.setValue(i, 2,"<div style=\"min-width:220px\">" + html + "</div>");
 				}
 			}
 		}
 		
 		return dataListUser;
 	}
 	static Options createOptionsTableListUser() {
		Options ops = Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(false);
		return ops;
	}
 	static boolean checkingFilter(List<InvitedUser> users, String filter){
 		if(users.size() > 0 ){
 			if(filter.equalsIgnoreCase(defaultFilter))
 			{
 				return true;
 			}
 			for(InvitedUser u : users){
 				if(u.getStatus().toString().equals(filter)){
 					return true;
 				}
 			}
 			return false;
 		}else{
 			return false;
 		}
 	}
 	
 	
 	
 	
 	
 	
 	// this is the method that invited user
 	private static void sendData(String[] data,String groupID ) {
 		monitorGwtSv.inviteUser3rd(data,groupID, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				initData(filterStatic);
				dialogInvite.hide();
				showMessage("Invite sucessfully.", "", "",
						HTMLControl.BLUE_MESSAGE, true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				initData(filterStatic);
				dialogInvite.hide();
				showMessage("Server error.", "", "",
						HTMLControl.RED_MESSAGE, true);
			}
		});
 	}
 	
 	//this is the method that action every popup show
 	private static void popupAction(String filter, String action_type, String userID){
 		InvitedUser u = new InvitedUser();
 		for(InvitedUser us : listUser3rds){
 			if(us.getId().toString().equals(userID)){
 				u.setEmail(us.getEmail());
 				u.setId(userID);
 				u.setStatus(us.getStatus());
 			}
 		}
 		
 		//if action type active user we must set group for this user
 		if(action_type.equalsIgnoreCase("active")){
 			
 			for(int i = 0 ; i < listTemp.getItemCount();i++){
				if(listTemp.isItemSelected(i) && !listTemp.getValue(i).equalsIgnoreCase(DefaulValueOfListTemp)){
					for(SystemGroup s : listGroup){
						if(s.getName().toString().equals(listTemp.getValue(i))){
							 u.addGroup(s.getId());
						}
					}
				}
			}
 		}
 		filterStatic = filter;
 		ActionStatic = action_type;
 		monitorGwtSv.action3rd(action_type, u, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					initData(filterStatic);
					showMessage(ActionStatic + " sucessfully!", "", "",
							HTMLControl.BLUE_MESSAGE, true);
					dialogFunction.hide();
					
					
				}else{
					showMessage("Cannot "+ ActionStatic + ".", "", "",
							HTMLControl.RED_MESSAGE, true);
					dialogFunction.hide();
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					setVisibleLoadingImage(false);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showMessage("Cannot "+ ActionStatic + ".", "", "",
						HTMLControl.RED_MESSAGE, true);
				dialogFunction.hide();
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
				setVisibleLoadingImage(false);
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
 	
 	static void showDialogBox(final String idUser,  String actionType,  String filter){
 		filterStatic = filter;
 		ActionStatic = actionType;
 		if(filter.equalsIgnoreCase(filter_Active)){
 			dialogFunction = new DialogBox();
 	 		dialogFunction.setAnimationEnabled(true);
 	 		VerticalPanel dialogVPanel = new VerticalPanel();
 			//if filter is active so the idUser will be inactive or delete.we will do this in this
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
 			popupContent.setWidth("400px");
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.getCellFormatter().setWidth(0, 0, "400px");
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
 				    	setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
 						setVisibleLoadingImage(true);
 				    	popupAction(filterStatic,ActionStatic, idUser);
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
 		if(filter.equalsIgnoreCase(filter_requesting)){
 			//if filter requesting,so they will action to active this user
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
 			popupContent.setHTML("<h4>Assgin this" +" user : " + tempName + " to group</h4>");
 			popupContent.setWidth("400px");
 			listTemp = new ListBox();
 			listTemp.setMultipleSelect(true);
 			listTemp.setWidth("300px");
 			listTemp.setHeight("80px");
 			listTemp.addItem(DefaulValueOfListTemp);
 			listAll = new ListBox();
 			listAll.setWidth("150px");
 			for(SystemGroup s : listGroup){
 				listAll.addItem(s.getName());
 			}
 			btt_MappingGroup = new Button("Mapping");
 			btt_MappingGroup.addClickHandler(new MappingGroup());
 			btt_UnMappingGroup = new Button("UnMapping");
 			btt_UnMappingGroup.addClickHandler(new UnMappingGroup());
 			panelValidateGroups = new AbsolutePanel();
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.getCellFormatter().setWidth(1, 0, "100px");
 			flexHTML.getCellFormatter().setWidth(1, 1, "100px");
 			flexHTML.getCellFormatter().setWidth(1, 2, "100px");
 			flexHTML.getCellFormatter().setWidth(1, 3, "200px");
 			flexHTML.getCellFormatter().setWidth(1, 4, "400px");
 			flexHTML.getFlexCellFormatter().setColSpan(0, 0, 5);
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.setWidget(1, 0, listAll);
 			flexHTML.setWidget(1, 1, btt_MappingGroup);
 			flexHTML.setWidget(1, 2, btt_UnMappingGroup);
 			flexHTML.setWidget(1, 3, listTemp);
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
 				    	if(listTemp.getValue(0).equalsIgnoreCase(DefaulValueOfListTemp)){
 				    		listTemp.setFocus(true);
 				    		return;
 				    	}else{
 				    		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
 	 						setVisibleLoadingImage(true);
 	 				    	popupAction(filterStatic,ActionStatic, idUser);
 				    	}
 				    	
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
 			//if filter is pending so the idUser will be delete.we will do this in this
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
 			popupContent.setWidth("400px");
 			FlexTable flexHTML = new FlexTable();
 			flexHTML.setWidget(0, 0, popupContent);
 			flexHTML.getCellFormatter().setWidth(0, 0, "400px");
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
 				    	setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
 						setVisibleLoadingImage(true);
 				    	popupAction(filterStatic,ActionStatic, idUser);
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
			panelValidateGroups.setVisible(false);
			List<Integer> listSelected = new ArrayList<Integer>();
			int index1 = 0;
			String value = DefaulValueOfListTemp;
			try {
				 index1 = listTemp.getSelectedIndex();
				 value = listTemp.getValue(index1);
			} catch (Exception e) {
				return;
			}
			if(!value.equalsIgnoreCase(DefaulValueOfListTemp)){
				listSelected.add(index1);
			}
			if(listSelected.size() > 0 ){
				List<String> listValue = new ArrayList<String>();
				
				for(int index : listSelected){
					listValue.add(listTemp.getValue(index));
					listTemp.removeItem(index);
				}
				
				if(listValue.size() > 0 ){
					//add group to list all again
					for(String value1 : listValue){
						listAll.addItem(value1);
					}
					//add default value again when list temp is null
					if(listTemp.getItemCount() == 0){
						listTemp.addItem(DefaulValueOfListTemp);
					}
				}else{
					panelValidateGroups.clear();
					panelValidateGroups.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"
							+ "Can not unmapping"
							+ "</div>"));
					panelValidateGroups.setVisible(true);
					return;
				}
			}else{
				panelValidateGroups.clear();
				panelValidateGroups.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"
											+ "Can not unmapping"
											+ "</div>"));
				panelValidateGroups.setVisible(true);
				return;
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
		btt_invite.addClickHandler(new inviteUserHandler());
		btt_reset = new Button("Reset");
		btt_reset.setStyleName("margin:6px;");
		btt_reset.addStyleName("form-button");
		btt_reset.addClickHandler(new ResetInviteHandler());
		txt_email = new TextArea();
		txt_email.setTitle("Invite user");
		txt_email.setWidth("400px");
		txt_email.setHeight("100px");
		FlexTable panelButton = new FlexTable();
		panelButton.setWidget(0, 0, btt_invite);
		panelButton.setWidget(0, 1, btt_reset);
		panelButton.setCellPadding(5);
		panelButton.setCellSpacing(5);
		panelValidateEmail = new AbsolutePanel();
		panelValidateEmail.setVisible(false);
		listGroupInvi = new ListBox();
		listGroupInvi.setTitle("List Group");
		listGroupInvi.setWidth("200px");
		for(SystemGroup s : listGroup){
			listGroupInvi.addItem(s.getName());
		}
		FlexTable table = new FlexTable();
		table.setCellPadding(5);
		table.setCellSpacing(5);
		table.setWidget(0, 0, txt_email);
		table.setWidget(0, 1, panelValidateEmail);
		table.setWidget(0, 2, listGroupInvi);
		/*table.setWidget(2, 0, panelLog);*/
		table.getCellFormatter().setHorizontalAlignment(0, 0, VerticalPanel.ALIGN_LEFT);
		table.getCellFormatter().setHorizontalAlignment(0, 1, VerticalPanel.ALIGN_RIGHT);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(exitButton);
		dialogVPanel.setCellHorizontalAlignment(exitButton, VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(table);
		dialogVPanel.add(panelButton);
		dialogVPanel.setCellHorizontalAlignment(panelButton, VerticalPanel.ALIGN_RIGHT);
		dialogInvite.setWidget(dialogVPanel);
		dialogInvite.getCaption().asWidget().setStyleName("myCaption");
		dialogInvite.center();
 	}
	static class ResetInviteHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			panelValidateEmail.clear();
			panelValidateEmail.setVisible(false);
			txt_email.setText("");
			listGroupInvi.setSelectedIndex(0);
		}
		
	}
 	static class inviteUserHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			panelValidateEmail.setVisible(false);
			String emails = txt_email.getText();
			String validate = validateEmail(emails);
			if(validate!= ""){
				panelValidateEmail.clear();
				panelValidateEmail.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"+ validate + "</div>"));
				panelValidateEmail.setVisible(true);
				return;
			}
			String[] data = emails.split(",");
			int index = listGroupInvi.getSelectedIndex();
			String group = listGroupInvi.getValue(index);
			String groupID = "";
			for(SystemGroup s : listGroup){
				if(s.getName().equalsIgnoreCase(group)){
					groupID = s.getId();
				}
			}
			if(groupID.trim().length() > 0){
				filterStatic = filter_pending;
				sendData(data, groupID);
			}else{
				return;
			}
			
		}
 		
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
