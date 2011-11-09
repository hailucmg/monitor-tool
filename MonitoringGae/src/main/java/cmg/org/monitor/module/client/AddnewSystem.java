package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.Ultility;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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


public class AddnewSystem implements EntryPoint {
	AddnewSystemServiceAsync addSystemSA = GWT.create(AddnewSystemService.class);
	@Override
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		final TextBox txtName = new TextBox();
		txtName.addStyleName("inp-form");
		RootPanel.get("name").add(txtName);
		
		final TextBox txtURL = new TextBox();
		txtURL.addStyleName("inp-form");
		RootPanel.get("url").add(txtURL);
		
		final TextBox txtRemote = new TextBox();
		txtRemote.addStyleName("inp-form");
		RootPanel.get("remoteURL").add(txtRemote);
		
		final ListBox listActive = new ListBox();
		listActive.setWidth("198px");
		listActive.setHeight("28px");
		listActive.setSelectedIndex(0);
		listActive.addItem("Yes");
		listActive.addItem("No");
		RootPanel.get("active").add(listActive);
		
		final ListBox listProtocol = new ListBox();
		listProtocol.setWidth("198px");
		listProtocol.setHeight("28px");
		listProtocol.setSelectedIndex(0);
		listProtocol.addItem("HTTP");
		listProtocol.addItem("SMTP");
		RootPanel.get("protocol").add(listProtocol);
		
		ListBox listGroup = new ListBox();
		listGroup.setWidth("198px");
		listGroup.setHeight("28px");
		listGroup.setSelectedIndex(0);
		listGroup.addItem("hanoi@c-mg.com");
		RootPanel.get("group").add(listGroup);
		
		final Label lbhide = new Label();
		lbhide.setText("");
		RootPanel.get("labelhide").add(lbhide);
		
		Button bttCreate = new Button();
		bttCreate.addStyleName("form-create");
		RootPanel.get("button").add(bttCreate);
		
		ResetButton bttReset = new ResetButton();
		bttReset.addStyleName("form-reset");
		RootPanel.get("button").add(bttReset);
		
		Button bttBack = new Button();
		bttBack.addStyleName("form-back");
		RootPanel.get("button").add(bttBack);
	
		class MyHandler implements ClickHandler, KeyUpHandler{

			@Override
			public void onKeyUp(KeyUpEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				String msg = validateName(txtName.getText().toString()) + validateURL(txtURL.getText().toString()) 
							+ validateRemoteURL(txtRemote.getText().toString());
				if(!msg.trim().equals("")){
					lbhide.setText(msg);
					return;
				}else{
					
						lbhide.setText("running");
					
					//clear(DOM.getElementById("URL"));
					SystemMonitor system = new SystemMonitor();
					system.setName(txtName.getText().toString());
					system.setUrl(txtURL.getText().toString());
					system.setActive(isActive(listActive.getValue(listActive.getSelectedIndex())));
					system.setProtocol(listProtocol.getValue(listProtocol.getSelectedIndex()));
					system.setRemoteUrl(txtRemote.getText());
					sendData(system,txtURL.getText());
					
					
				}
								
			}
			private void sendData(SystemMonitor system, String url){
				addSystemSA.addSystem(system, url, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						lbhide.setText(result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						lbhide.setText("can not connect to server");
						caught.printStackTrace();
					}
				});
			}
		}
		
		MyHandler handler = new MyHandler();
		bttCreate.addClickHandler(handler);
		
	}
		//get boolean isActive by String
		private boolean isActive(String active){
			boolean isActive = false;
			if(active.equals("Yes")){
				isActive = true;
			}
			return isActive;
		}
		
		//validate Name
		private String validateName(String name){
			String msg = "";
			if(name==null || name.trim().length() == 0){
				msg+="System Name can not be blank ";
			}
			return msg;
		}
		// validate URL
		private String validateURL(String url){
			String msg = "";
			if(url==null || url.trim().length()==0){
				msg+="URL can not be blank ";
			
				return msg;
			}
			
			return msg;
		}
		
		//validate RemoteURL
		private String validateRemoteURL(String remoteUrl){
			String msg = "";
			if(remoteUrl == null || remoteUrl.trim().length() == 0){
				msg += "Remote url can not be blank ";
				return msg;
			}
			return msg;
		}
		
		public static void clear(Element parent) {
			Element firstChild;
			while((firstChild = DOM.getFirstChild(parent)) != null) {
				DOM.removeChild(parent, firstChild);	
			}
			
		}
}