package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResetButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class AddnewSystem implements EntryPoint {
	AddnewSystemServiceAsync addSystemSA = GWT
			.create(AddnewSystemService.class);
	ListBox listGroup;

	@Override
	public void onModuleLoad() {
		addSystemSA.getUserLogin(new AsyncCallback<UserLoginDto>() {
			@Override
			public void onFailure(Throwable caught) {
				setVisibleLoadingImage(false);
				initMessage("Server error. ", HTMLControl.HTML_ADD_NEW_SYSTEM_NAME,
						"Try again. ", HTMLControl.RED_MESSAGE);
				setVisibleMessage(true, HTMLControl.RED_MESSAGE);
			}
			@Override
			public void onSuccess(UserLoginDto result) {
				setVisibleLoadingImage(false);
				if (result != null) {
					if (result.isLogin()) {
						RootPanel.get("menuContent").add(
								HTMLControl.getMenuHTML(
										HTMLControl.SYSTEM_MANAGEMENT_PAGE,
										result.getRole()));
						RootPanel.get("nav-right")
								.add(HTMLControl.getLogoutHTML(result
										.getLogoutUrl()));
						if (result.getRole() == MonitorConstant.ROLE_GUEST) {
							initMessage(
									"Hello "
											+ result.getNickName()
											+ ". You might not have permission to use Monitor System. ",
									result.getLogoutUrl(),
									"Login with another account.",
									HTMLControl.YELLOW_MESSAGE);
							setVisibleMessage(true, HTMLControl.YELLOW_MESSAGE);
						} else {
							initMessage(
									"Wellcome to Monitor System, "
											+ result.getNickName()
											+ ". If have any question. ",
									HTMLControl.HTML_ABOUT_NAME, "Contact Us.",
									HTMLControl.GREEN_MESSAGE);
							setVisibleMessage(true, HTMLControl.GREEN_MESSAGE);
							init();
						}
					} else {
						initMessage("Must login to use Monitor System. ", result.getLoginUrl(),
								"Login. ", HTMLControl.RED_MESSAGE);
						setVisibleMessage(true, HTMLControl.RED_MESSAGE);
					}
				} else {
					initMessage("Server error. ", HTMLControl.HTML_ADD_NEW_SYSTEM_NAME,
							"Try again. ", HTMLControl.RED_MESSAGE);
					setVisibleMessage(true, HTMLControl.RED_MESSAGE);
				}
			}
		});

	}
	
	void init() {
		addSystemSA.groups(new AsyncCallback<String[]>() {

			@Override
			public void onSuccess(String[] result) {
				// TODO Auto-generated method stub
				listGroup = new ListBox();
				listGroup.setWidth("198px");
				listGroup.setHeight("28px");
				for (int i = 0; i < result.length; i++) {
					listGroup.addItem(result[i]);
				}
				listGroup.setSelectedIndex(0);
				RootPanel.get("group").add(listGroup);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				Window.alert(caught.toString());

			}
		});
		// TODO Auto-generated method stub
		final TextBox txtName = new TextBox();
		txtName.addStyleName("inp-form");
		RootPanel.get("name").add(txtName);

		final TextBox txtURL = new TextBox();
		txtURL.addStyleName("inp-form");
		RootPanel.get("url").add(txtURL);

		final TextBox txtIP = new TextBox();
		txtIP.addStyleName("inp-form");
		RootPanel.get("myIP").add(txtIP);

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
		listProtocol.addItem("HTTP(s)");
		listProtocol.addItem("SMTP");
		RootPanel.get("protocol").add(listProtocol);

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

		class MyHandler implements ClickHandler, KeyUpHandler {

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
				String validateRemoteURL = validateRemoteURL(txtRemote
						.getText());
				if (validateName != "") {
					lbhide.setText(validateName);
					return;
				} else if (validateURL != "") {
					lbhide.setText(validateURL);
					return;
				} else if (validateIp != "") {
					lbhide.setText(validateIp);
					return;
				} else if (validateRemoteURL != "") {
					lbhide.setText(validateRemoteURL);
					return;
				} else {

					lbhide.setText("running");
					// clear(DOM.getElementById("URL"));
					SystemMonitor system = new SystemMonitor();
					system.setName(txtName.getText().toString());
					system.setUrl(txtURL.getText().toString());
					system.setActive(isActive(listActive.getValue(listActive
							.getSelectedIndex())));
					system.setProtocol(listProtocol.getValue(listProtocol
							.getSelectedIndex()));
					system.setGroupEmail(listGroup.getItemText(listGroup
							.getSelectedIndex()));
					system.setIp(txtIP.getText());
					system.setRemoteUrl(txtRemote.getText());
					sendData(system, txtURL.getText());
				}

			}

			private void sendData(SystemMonitor system, String url) {
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

		class myReset implements ClickHandler, KeyUpHandler {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				txtName.setText("");
				txtIP.setText("");
				txtRemote.setText("");
				txtURL.setText("");
			}

		}
		class myBack implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Window.Location.assign("SystemManagement.html");
			}

		}
		MyHandler handler = new MyHandler();
		bttCreate.addClickHandler(handler);
		myReset resetHandler = new myReset();
		bttReset.addClickHandler(resetHandler);
		myBack backHandler = new myBack();
		bttBack.addClickHandler(backHandler);
	}

	void setVisibleLoadingImage(boolean b) {
		RootPanel.get("img-loading").setVisible(b);
	}

	void setVisibleMessage(boolean b, int type) {
		RootPanel.get("message-" + HTMLControl.getColor(type)).setVisible(b);
	}

	/*
	 * Show message with content
	 */
	void initMessage(String message, String url, String titleUrl, int type) {
		RootPanel.get("content-" + HTMLControl.getColor(type)).clear();
		RootPanel.get("content-" + HTMLControl.getColor(type)).add(
				new HTML(message
						+ ((url.trim().length() == 0) ? "" : ("  <a href=\""
								+ url + "\">" + titleUrl + "</a>")), true));
	}
	// get boolean isActive by String
	private boolean isActive(String active) {
		boolean isActive = false;
		if (active.equals("Yes")) {
			isActive = true;
		}
		return isActive;
	}

	// validate Name
	private String validateName(String name) {
		String msg = "";
		if (name == null || name.trim().length() == 0) {
			msg = "System Name can not be blank ";
		} else if (name.contains("$") || name.contains("%")
				|| name.contains("*")) {
			msg = "name is not validate";
		}

		return msg;

	}

	// validate URL
	private String validateURL(String url) {
		String msg = "";
		if (url == null || url.trim().length() == 0) {
			msg = "URL can not be blank ";
		} else if (url.length() < 3) {
			msg += "URL is not validate";
		}
		/*
		 * String patternStr =
		 * "(?i)\b((?:https?://|www\\d"+"{0,3}[.]|[a-z0-9.\\-]"
		 * +"+[.][a-z]{2,4}/)(?:[^\\s"
		 * +"()<>]+|\\("+"([^\\s"+"()<>]+|(\\("+"[^\\s"
		 * +"()<>]+\\))"+")*\\))"+"+(?:\\("
		 * +"([^\\s()<>]"+"+|(\\([^\\s"+"()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:'\\"
		 * +".,<>?«»“”‘’]))"; RegExp regExp = RegExp.compile(patternStr);
		 * boolean matchFound = regExp.test(url); if(matchFound == false){ msg =
		 * "url is not validate"; }
		 */
		return msg;
	}

	// validate RemoteURL
	private String validateRemoteURL(String remoteUrl) {
		String msg = "";
		if (remoteUrl == null || remoteUrl.trim().length() == 0) {
			msg += "Remote url can not be blank ";
		} else if (remoteUrl.length() < 3) {
			msg += "Remote url is not validate";
		}
		return msg;
	}

	private String validateIP(String ip) {
		String msg = "";
		String patternStr = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
		RegExp regExp = RegExp.compile(patternStr);
		boolean matchFound = regExp.test(ip);
		if (matchFound == false) {
			msg = "ip is not validate";
		}
		return msg;
	}

	public static void clear(Element parent) {
		Element firstChild;
		while ((firstChild = DOM.getFirstChild(parent)) != null) {
			DOM.removeChild(parent, firstChild);
		}

	}
}
