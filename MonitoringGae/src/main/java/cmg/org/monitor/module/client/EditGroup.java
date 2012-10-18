package cmg.org.monitor.module.client;


import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class EditGroup extends AncestorEntryPoint {
	private static FlexTable tableForm;
	TextBox txtGroupName;
	TextBox txtGroupDescription;
	Label lblGroupName;
	Label lblGroupDescription;
	AbsolutePanel panelValidateGroupName;
	AbsolutePanel panelValidateGroupDescription;
	List<SystemGroup> groupNames;
	AbsolutePanel panelButton;
	AbsolutePanel panelAdding;
	Button bttCreate;
	Button bttBack;
	Button bttReset;
	String oldGroupName;
	String oldGroupDescription;
	String id;
	protected void init() {
		if (currentPage == HTMLControl.PAGE_EDIT_GROUP) {
			final String sysID = HTMLControl.getSystemId(History.getToken());
			try {
				monitorGwtSv.getGroupById(sysID,new AsyncCallback<SystemGroup>() {
					@Override
					public void onSuccess(SystemGroup result) {
						if (result != null) {
							initSystemGroups();
							oldGroupName = result.getName();
							oldGroupDescription = result.getDescription();
							id = result.getId();
							initUI(result.getName(),result.getDescription());
							addWidget(HTMLControl.ID_BODY_CONTENT, tableForm);
							setVisibleLoadingImage(false);
							setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
							
						} else {
							showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
									"Go to Group Management. ", HTMLControl.RED_MESSAGE, true);
							setVisibleLoadingImage(false);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
								"Go to Group Management. ", HTMLControl.RED_MESSAGE, true);
						setVisibleLoadingImage(false);
					}
				});
			} catch (Exception e) {
				showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
						"Go to Group Management. ", HTMLControl.RED_MESSAGE, true);
				setVisibleLoadingImage(false);
			}
		}
	}
	
	
	
	private void initSystemGroups(){
		monitorGwtSv.getAllGroup(new AsyncCallback<SystemGroup[]>() {
			@Override
			public void onSuccess(SystemGroup[]  result) {
				if(result!=null){
					SystemGroup[] listTempGroup = result;
					groupNames = new ArrayList<SystemGroup>();
					for(SystemGroup s : listTempGroup){
						groupNames.add(s);
					}
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showMessage("Oops! Error.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
						"Go to Group Management. ", HTMLControl.RED_MESSAGE, true);
				setVisibleLoadingImage(false);	
			}
		});
	}
	
	
	
	
	private String validateGroupName(String name){
		String msg = "";
		if (name == null || name.trim().length() == 0) {
			msg = "This field is required ";
		}else{
			if(groupNames!=null){
				for(SystemGroup gName : groupNames){
					if(name.equalsIgnoreCase(gName.getName()) && !name.equalsIgnoreCase(oldGroupName)){
						msg = "This name is existed	";
					}
				}
			}
			
		}
		return msg;
	}
	
	private String validateGroupDescription(String description){
		String msg = "";
		if (description == null || description.trim().length() == 0) {
			msg = "This field is required ";
		}
		return msg;
	}
	
	void initUI(String groupName, String groupDescription){
		tableForm = new FlexTable();
		tableForm.setCellPadding(3);
		tableForm.setCellSpacing(3);
		tableForm.getFlexCellFormatter().setWidth(0, 0, "100px");
		tableForm.getFlexCellFormatter().setWidth(1, 0, "100px");
		tableForm.getFlexCellFormatter().setWidth(2, 0, "100px");
		tableForm.getFlexCellFormatter().setWidth(3, 0, "100px");
		lblGroupName = new Label();
		lblGroupName.setText("Group Name :");
		lblGroupDescription = new Label();
		lblGroupDescription.setText("Description :");
		
		txtGroupName = new TextBox();
		txtGroupName.setWidth("196px");
		txtGroupName.setHeight("30px");
		txtGroupName.setText(groupName);
		
		txtGroupDescription = new TextBox();
		txtGroupDescription.setWidth("196px");
		txtGroupDescription.setHeight("30px");
		txtGroupDescription.setText(groupDescription);
		
		panelValidateGroupName = new AbsolutePanel();
		panelValidateGroupName.setVisible(false);
		panelValidateGroupDescription = new AbsolutePanel();
		panelValidateGroupDescription.setVisible(false);
		
		bttCreate = new Button();
		bttCreate.setText("Update");
		bttCreate.setStyleName("margin:6px;");
		bttCreate.addStyleName("form-button");

		bttReset = new Button();
		bttReset.setText("Reset");
		bttReset.setStyleName("margin:6px;");
		bttReset.addStyleName("form-button");

		bttBack = new Button();
		bttBack.setText("Back");
		bttBack.setStyleName("margin:6px;");
		bttBack.addStyleName("form-button");
		
		CreateHandler createHandler =  new CreateHandler();
		bttCreate.addClickHandler(createHandler);
		
		myReset resetHandler = new myReset();
		bttReset.addClickHandler(resetHandler);
		
		myBack backHandler = new myBack();
		bttBack.addClickHandler(backHandler);
		
		panelAdding = new AbsolutePanel();
		panelAdding.add(new HTML("<div id=\"img-adding\"><img src=\"images/icon/loading11.gif\"/></div>"));
		panelAdding.setVisible(false);
		
		panelButton = new AbsolutePanel();
		panelButton.add(bttCreate);
		panelButton.add(bttReset);
		panelButton.add(bttBack);
		
		

		tableForm.setWidget(0, 0, lblGroupName);
		tableForm.setWidget(0, 1, txtGroupName);
		tableForm.setWidget(0, 2, panelValidateGroupName);
		tableForm.setWidget(1, 0, lblGroupDescription);
		tableForm.setWidget(1, 1, txtGroupDescription);
		tableForm.setWidget(1, 2, panelValidateGroupDescription);
		tableForm.getFlexCellFormatter().setColSpan(2, 0, 2);
		tableForm.setWidget(2, 0, panelAdding);
		tableForm.getFlexCellFormatter().setColSpan(3, 0, 3);
		tableForm.setWidget(3, 0, panelButton);
	}
	
	
	
	class CreateHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			String name = validateGroupName(txtGroupName.getText());
			String groupdes =validateGroupDescription(txtGroupDescription.getText());
			panelValidateGroupName.setVisible(false);
			panelValidateGroupDescription.setVisible(false);
			if(name != ""){
				panelValidateGroupName.clear();
				panelValidateGroupName.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"
									+ name + "</div>"));
				panelValidateGroupDescription.setVisible(false);
				panelValidateGroupName.setVisible(true);
				return;
			}else if(groupdes !=""){
				panelValidateGroupDescription.clear();
				panelValidateGroupDescription.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">"
									+ groupdes + "</div>"));
				panelValidateGroupName.setVisible(false);
				panelValidateGroupDescription.setVisible(true);
				return;
			}
			
			panelValidateGroupName.setVisible(false);
			panelValidateGroupDescription.setVisible(false);
			panelAdding.setVisible(true);
			String nameEdit = txtGroupName.getText();
			String groupEdit = txtGroupDescription.getText();
			sendData(nameEdit, groupEdit, id);
		}
		
	}
	
	
	class myReset implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			txtGroupName.setText(oldGroupName);
			txtGroupDescription.setText(oldGroupDescription);
			panelAdding.setVisible(false);
			panelValidateGroupDescription.setVisible(false);
			panelValidateGroupName.setVisible(false);

		}
	}
	
	class myBack implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Window.Location.replace(HTMLControl.trimHashPart(Window.Location
					.getHref()) + HTMLControl.HTML_GROUP_MANAGEMENT_NAME);
		}
	}

	
	private void sendData(String name, String groupDescription, String id){
		panelAdding.setVisible(false);
		monitorGwtSv.updateGroup(name, groupDescription, id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					initSystemGroups();
					showMessage("Group edited sucessfully. ",
							HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
							"View group list. ", HTMLControl.BLUE_MESSAGE,
							true);
				}
				else{
					showMessage("Server error! ",
							HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
							"Goto Group Management. ", HTMLControl.RED_MESSAGE,
							true);
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showMessage("Server error! ",
						HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
						"Goto Group Management. ", HTMLControl.RED_MESSAGE,
						true);
				
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
