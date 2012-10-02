package cmg.org.monitor.module.client;


import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class AddNewGroup extends AncestorEntryPoint {
	private static FlexTable tableForm;
	TextBox txtGroupName;
	TextBox txtGroupDescription;
	Label lblGroupName;
	Label lblGroupDescription;
	AbsolutePanel panelValidateGroupName;
	AbsolutePanel panelValidateGroupDescription;
	List<SystemGroup> groupNames = new ArrayList<SystemGroup>();
	AbsolutePanel panelButton;
	AbsolutePanel panelAdding;
	Button bttCreate;
	Button bttBack;
	Button bttReset;
	
	private String validateGroupName(String name){
		String msg = "";
		if (name == null || name.trim().length() == 0) {
			msg = "This field is required ";
		} else if (name.contains("$") || name.contains("%")
				|| name.contains("*")) {
			msg = "Name is not validate";
		}else{
			if(groupNames!=null){
				for(SystemGroup gName : groupNames){
					if(name.equalsIgnoreCase(gName.getName())){
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

	protected void init() {
		if (currentPage == HTMLControl.PAGE_ADD_GROUP) { 
			monitorGwtSv.getAllGroup(new AsyncCallback<MonitorContainer>(){
				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					showMessage("Oops! Error.",
							HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
							"Goto Group Management. ",
							HTMLControl.RED_MESSAGE, true);
				}
				@Override
				public void onSuccess(MonitorContainer result) {
						if(result.getListSystemGroup()!=null){
							SystemGroup[] listTempGroup = result.getListSystemGroup();
							groupNames = new ArrayList<SystemGroup>();
							for(SystemGroup s : listTempGroup){
								groupNames.add(s);
							}
						}
						initUI();
						addWidget(HTMLControl.ID_BODY_CONTENT, tableForm);
						setVisibleLoadingImage(false);
						setOnload(false);
						setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						
				}
				
			});
				

		}
	}
	void initUI(){
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
		
		txtGroupDescription = new TextBox();
		txtGroupDescription.setWidth("196px");
		txtGroupDescription.setHeight("30px");
		
		panelValidateGroupName = new AbsolutePanel();
		panelValidateGroupName.setVisible(false);
		panelValidateGroupDescription = new AbsolutePanel();
		panelValidateGroupDescription.setVisible(false);
		
		bttCreate = new Button();
		bttCreate.setText("Create");
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
		bttCreate.addClickHandler(new ClickHandler() {
		    
		    @Override
		    public void onClick(ClickEvent event) {
			String name = validateGroupName(txtGroupName.getText());
			String groupdes = validateGroupDescription(txtGroupDescription.getText());
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
			sendData(txtGroupName.getText(), txtGroupDescription.getText());			
		    }
		});
		
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
			String groupdes = validateGroupDescription(txtGroupDescription.getText());
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
			sendData(txtGroupName.getText(), txtGroupDescription.getText());
		}
		
	}
	
	
	class myReset implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			txtGroupName.setText("");
			txtGroupDescription.setText("");
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

	
	private void sendData(String name, String groupDescription){
		panelAdding.setVisible(false);
		SystemGroup  g = new SystemGroup();
		g.setDescription(groupDescription);
		g.setName(name);
	
		monitorGwtSv.addGroupByObj(g, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					showMessage("Group added sucessfully. ",
							HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
							"View Group list. ", HTMLControl.BLUE_MESSAGE,
							true);
					init();
				}else{
					showMessage("Server error! ",
							HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
							"Go to Group Management. ",
							HTMLControl.RED_MESSAGE, true);
				}
			
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showMessage("Server error! ",
						HTMLControl.HTML_GROUP_MANAGEMENT_NAME,
						"Go to Group Management. ",
						HTMLControl.RED_MESSAGE, true);
				
			}
		});
		
	}
	
	
	
}
