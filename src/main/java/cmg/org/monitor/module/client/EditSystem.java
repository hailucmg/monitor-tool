package cmg.org.monitor.module.client;




import cmg.org.monitor.ext.model.shared.MonitorEditDto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResetButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class EditSystem implements EntryPoint {
	EditSystemServiceAsync editService = GWT.create(EditSystemService.class);
	MonitorEditDto system;
	TextBox txtIP;
	TextBox txtName;
	TextBox txtURL;
	TextBox txtRemoteURL;
	ListBox listprotocol;
	ListBox listgroup;
	ListBox active;
	Button bttEdit;
	ResetButton bttReset;
	Button bttBack;
	Label lblhide;
	Boolean check = false;
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
	
			editService.getSystembyID(Window.Location.getParameter("id").toString(), new AsyncCallback<MonitorEditDto>() {
				
				@Override
				public void onSuccess(MonitorEditDto result) {
					// TODO Auto-generated method stub
					if(result!=null){
						system = result;
						txtName = new TextBox();
						txtName.setText(system.getName());
						txtName.addStyleName("inp-form");
						RootPanel.get("name").add(txtName);
							
						txtRemoteURL = new TextBox();
						txtRemoteURL.setText(system.getRemoteURl());
						txtRemoteURL.addStyleName("inp-form");
						RootPanel.get("remoteurl").add(txtRemoteURL);
						
						txtURL = new TextBox();
						txtURL.setText(system.getUrl());
						txtURL.addStyleName("inp-form");
						RootPanel.get("url").add(txtURL);
						
						
						txtIP = new TextBox();
						txtIP.setText(system.getIp());
						txtIP.addStyleName("inp-form");
						RootPanel.get("ip").add(txtIP);
					
						active = new ListBox();
						active.setWidth("198px");
						active.setHeight("28px");
						active.addItem("Yes");
						active.addItem("No");
						if(system.isActive()==true){
							active.setSelectedIndex(0);
						}else{
							active.setSelectedIndex(1);
						}
						RootPanel.get("active").add(active);
						
						listgroup = new ListBox();
						listgroup.setWidth("198px");
						listgroup.setHeight("28px");
						for(int i = 0;i <system.getGroups().length;i++){
							listgroup.addItem(system.getGroups()[i]);
						}
						listgroup.setSelectedIndex(system.getSelect());
						RootPanel.get("group").add(listgroup);
						
						listprotocol = new ListBox();
						listprotocol.setWidth("198px");
						listprotocol.setHeight("28px");
						listprotocol.addItem("HTTP");
						listprotocol.addItem("SMTP");
						if(system.getProtocol()=="HTTP"){
							listprotocol.setSelectedIndex(0);
						}else{
							listprotocol.setSelectedIndex(1);
						}
						RootPanel.get("protocol").add(listprotocol);
						
						lblhide = new Label();
						RootPanel.get("label").add(lblhide);
						
						bttEdit = new Button();
						bttEdit.addStyleName("form-edit");
						RootPanel.get("button").add(bttEdit);
						
						bttReset = new ResetButton();
						bttReset.addStyleName("form-reset");
						RootPanel.get("button").add(bttReset);
						
						bttBack = new Button();
						bttBack.addStyleName("form-back");
						RootPanel.get("button").add(bttBack);
						MyHandlerEdit edithandler = new MyHandlerEdit();
						bttEdit.addClickHandler(edithandler);
					}else{
						clear(DOM.getElementById("content-table"));
						DOM.getElementById("page-heading").setInnerHTML("<h1>this system is not alive</h1>");
					}	
				}
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					clear(DOM.getElementById("content-table"));
					DOM.getElementById("page-heading").setInnerHTML("<h1>can not connect to server</h1>");
				}
				
			});
	}
			class MyHandlerEdit implements ClickHandler, KeyUpHandler{

				@Override
				public void onKeyUp(KeyUpEvent event) {
					// TODO Auto-generated method stub
			
				}

				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					String validateName = validateName(txtName.getText());
					String validateURL = validateURL(txtURL.getText());
					String validateIp = validateIP(txtIP.getText());
					String validateRemoteURL = validateRemoteURL(txtRemoteURL.getText());
					if(validateName!=""){
						lblhide.setText(validateName);
						return;
					}else if(validateURL!=""){
						lblhide.setText(validateURL);
						return;
					}else if(validateIp!=""){
						lblhide.setText(validateIp);
						return;
					}else if(validateRemoteURL!=""){
						lblhide.setText(validateRemoteURL);
						return;
					}
					else{
					boolean isactive = false;
					if(active.getItemText(active.getSelectedIndex()) == "Yes"){
						isactive = true;
					}else if(active.getItemText(active.getSelectedIndex()) == "No"){
						isactive = false;
					}
					lblhide.setText("running");
					editService.editSystembyID(system.getId(), txtName.getText(), txtURL.getText(), listgroup.getItemText(listgroup.getSelectedIndex())
					, listprotocol.getItemText(listprotocol.getSelectedIndex())
					, txtIP.getText(),txtRemoteURL.getText(),isactive, new AsyncCallback<Boolean>() {
				
						@Override
						public void onSuccess(Boolean result) {
							// TODO Auto-generated method stub
							if(result==true)
							{
								lblhide.setText("done");
							}
							else{
								lblhide.setText("database connect error");
							}
						}
				
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							lblhide.setText("<h1> can not connect to server,please try again");
						}
					});
				}
			
				}
			
	}
	
	//delete all html by id
	public static void clear(Element parent) {
		Element firstChild;
		while((firstChild = DOM.getFirstChild(parent)) != null) {
			DOM.removeChild(parent, firstChild);	
		}
	}
	
	private String validateName(String name){
		String msg = "";
		if(name==null || name.trim().length() == 0){
			msg="System Name can not be blank ";
		}else if(name.contains("$") || name.contains("%")||name.contains("*")){
			msg="name is not validate";
		}
		
		return msg;
	}
	// validate URL
	private String validateURL(String url){
		String msg = "";
		if(url==null || url.trim().length()==0){
			msg="URL can not be blank ";
		}else if(url.length()<3){
			msg+="URL is not validate";
		}
		
		return msg;
	}
	
	//validate RemoteURL
	private String validateRemoteURL(String remoteUrl){
		String msg = "";
		if(remoteUrl == null || remoteUrl.trim().length() == 0){
			msg += "Remote url can not be blank ";
		}else if(remoteUrl.length() < 3){
			msg+="Remote url is not validate";
		}
		return msg;
	}
	
	
	private String validateIP(String ip){
		String msg ="";
		String patternStr = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
		RegExp regExp = RegExp.compile(patternStr);
		boolean matchFound = regExp.test(ip);
		if(matchFound == false){
			msg = "ip is not validate";
		}
		return msg;
	}
	
	
	
	
}
