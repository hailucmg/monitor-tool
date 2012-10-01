package cmg.org.monitor.module.client;

import java.util.List;

import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.ext.model.shared.MonitorContainer;

import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;



import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class GroupManagement extends AncestorEntryPoint{
	public static List<SystemGroup> listGroup;
	static private Table tableListGroup;
    static private FlexTable rootLinkDefault;
    static private FlexTable tableLinkDefault;
    static DialogBox dialogBox;
    private static HTML popupContent;
    private static FlexTable flexHTML;
    
    
    
    
	protected void init() {
		if (currentPage == HTMLControl.PAGE_GROUP_MANAGEMENT) {
			GroupManagement.exportStaticMethod();
			tableListGroup = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT,tableListGroup);
			initUI();	
		}
	}

	public static void initUI(){
		monitorGwtSv.getAllGroup(new AsyncCallback<MonitorContainer>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
				setVisibleLoadingImage(false);
			}

			@Override
			public void onSuccess(MonitorContainer result) {
				if (result.getListSystemGroup()!=null && result!= null) {
					SystemGroup[] listTempGroup = result.getListSystemGroup();
					for(SystemGroup s : listTempGroup){
						listGroup.add(s);
					}
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(listGroup);
				} else {
					  showMessage("No group found. ", HTMLControl.HTML_ADD_NEW_GROUP_NAME, "Add new group.", HTMLControl.BLUE_MESSAGE, true);
					  setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
					  setVisibleLoadingImage(false);
				}
			}
		});
	}
	
	 public static native void exportStaticMethod() /*-{
		$wnd.showConfirmDialogBox =
		$entry(@cmg.org.monitor.module.client.GroupManagement::showConfirmDialogBox(Ljava/lang/String;Ljava/lang/String;))
		}-*/;

	    static void showConfirmDialogBox(final String name, final String id) {
		dialogBox = new DialogBox();
		dialogBox.setAnimationEnabled(true);
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
			dialogBox.hide();
		    }
		});
		VerticalPanel dialogVPanel = new VerticalPanel();
		popupContent = new HTML();
		popupContent.setHTML("<h4>Do you want to delete Group : " + name + "?</h4>");
		flexHTML = new FlexTable();
		flexHTML.setWidget(0, 0, popupContent);
		flexHTML.setStyleName("table-popup");
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		FlexTable table = new FlexTable();
		table.setCellPadding(10);
		table.setCellSpacing(10);
		table.setWidget(0, 0, okButton);
		table.setWidget(0, 1, closeButton);
		dialogVPanel.add(exitButton);
		dialogVPanel.add(flexHTML);
		dialogVPanel.add(table);
		dialogVPanel.setStyleName("dialogVPanel");
		okButton.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
		    	setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
				setVisibleLoadingImage(true);
				deleteGroup(name, id);
		    }
		});
		closeButton.addClickHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
			dialogBox.hide();
		    }
		});
		dialogBox.setWidget(dialogVPanel);
		dialogBox.center();
	    }
	
	   static void drawTable(List<SystemGroup> result) {
		tableListGroup.draw(createDataListSystem(result), createOptionsTableListSystem());
				
			
	   }
	
	/*
	 * Create options of table list system
	 */
	static Options createOptionsTableListSystem() {
		Options ops = Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(false);
		return ops;
	}
	
	/*
	 * Create data table list system without value
	 */
	static AbstractDataTable createDataListSystem(List<SystemGroup> listGroup) {
		// create object data table
		DataTable dataListSystem = DataTable.create();
		// add all columns
		dataListSystem.addColumn(ColumnType.STRING, "Group Name");
		dataListSystem.addColumn(ColumnType.STRING, "Description");
		dataListSystem.addColumn(ColumnType.STRING, "Delete");
		dataListSystem.addRows(listGroup.size());
		for (int i = 0; i < listGroup.size(); i++) {
			dataListSystem.setValue(i, 0,HTMLControl.getLinkEditGroup(listGroup.get(i).getId(), listGroup.get(i).getName()));
			dataListSystem.setValue(i, 1, listGroup.get(i).getDescription());
			dataListSystem.setValue(i,2,
							"<a onClick=\"javascript:showConfirmDialogBox('"+ listGroup.get(i).getName()
									+ "','"+ listGroup.get(i).getId()
									+ "');\" title=\"Delete\" class=\"icon-2 info-tooltip\"></a>");
		}

		com.google.gwt.visualization.client.formatters.BarFormat.Options ops = com.google.gwt.visualization.client.formatters.BarFormat.Options
				.create();
		ops.setColorPositive(Color.RED);
		ops.setColorNegative(Color.RED);
		ops.setMax(100);
		ops.setMin(0);
		ops.setWidth(100);

		BarFormat bf = BarFormat.create(ops);
		bf.format(dataListSystem, 1);
		bf.format(dataListSystem, 2);
		return dataListSystem;

	}
	
	
	public static void deleteGroup(String groupname, String id){
		setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
		final String tempName = groupname;
		monitorGwtSv.deleteGroup(groupname, id, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				showMessage("Group deleted sucessfully.", "", "", HTMLControl.BLUE_MESSAGE, true);
				dialogBox.hide();
				if(listGroup.size() > 1 ){
					for(int i = 0 ; i < listGroup.size();i++){
						if(listGroup.get(i).getName().equalsIgnoreCase(tempName)){
							listGroup.remove(i);
							break;
						}	
					}
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
					drawTable(listGroup);
				}else{
					if(listGroup.size() == 1){
						  listGroup.remove(0);	
						  showMessage("No group found. ", HTMLControl.HTML_ADD_NEW_GROUP_NAME, "Add new group.", HTMLControl.BLUE_MESSAGE, true);
						  setVisibleWidget(HTMLControl.ID_BODY_CONTENT, false);
						  setVisibleLoadingImage(false);
					}	
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showMessage("Cannot delete this group.", "", "", HTMLControl.RED_MESSAGE, true);
				dialogBox.hide();
			}
		});
	}
	
}
