package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorEditDto;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ResetButton;
import com.google.gwt.user.client.ui.TextBox;

public class EditSystem extends AncestorEntryPoint {
	MonitorEditDto system;
	ListBox listGroup;
	TextBox txtName;
	TextBox txtURL;
	TextBox txtIP;
	TextBox txtRemote;
	ListBox listActive;
	ListBox listProtocol;
	Button btnEdit;
	Button bttBack;
	Button bttReset;
	Label labelName;
	Label labelurl;
	Label labelip;
	Label labelremoteurl;
	Label labelactive;
	Label labelprotocol;
	Label labelmailgroup;
	Label labeladdnew;
	AbsolutePanel panelAdding;
	AbsolutePanel panelValidateName;
	AbsolutePanel panelValidateURL;
	AbsolutePanel panelValidateIP;
	AbsolutePanel panelValidateRemoteURL;
	AbsolutePanel panelButton;
	AbsolutePanel panelValidateRemoteURLServer;
	private static FlexTable tableForm;
	Boolean check = false;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_EDIT_SYSTEM) {
			final String sysID = HTMLControl.getSystemId(History.getToken());
			try {
				monitorGwtSv.validSystemId(sysID, new AsyncCallback<SystemMonitor>() {
					@Override
					public void onSuccess(SystemMonitor result) {
						if (result != null) {
							tableForm = new FlexTable();
							addWidget(HTMLControl.ID_BODY_CONTENT, tableForm);
							initFlexTable(sysID);
						} else {
							showMessage("Invalid System ID.",
									HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
									"Goto System Management. ",
									HTMLControl.RED_MESSAGE, true);

						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showMessage("Oops! Error.",
								HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
								"Goto System Management. ",
								HTMLControl.RED_MESSAGE, true);
					}
				});
			} catch (Exception e) {
				showMessage("Oops! Error.",
						HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
						"Goto System Management. ",
						HTMLControl.RED_MESSAGE, true);
			}
		}
	}

	void initFlexTable(String sysID) {
		monitorGwtSv.getSystembyID(sysID, new AsyncCallback<MonitorEditDto>() {
			@Override
			public void onSuccess(MonitorEditDto result) {
				if (result != null) {
					system = result;
					tableForm.setCellPadding(3);
					tableForm.setCellSpacing(3);
					tableForm.getFlexCellFormatter().setWidth(0, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(1, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(2, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(3, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(4, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(5, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(6, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(7, 0, "100px");
					tableForm.getFlexCellFormatter().setWidth(8, 0, "100px");

					labelName = new Label();
					labelName.setText("Name");

					labelurl = new Label();
					labelurl.setText("URL");

					labelip = new Label();
					labelip.setText("IP");

					labelremoteurl = new Label();
					labelremoteurl.setText("Remote-URL");

					labelactive = new Label();
					labelactive.setText("Active");

					labelprotocol = new Label();
					labelprotocol.setText("Protocol");

					labelmailgroup = new Label();
					labelmailgroup.setText("Notification mail group");

					txtName = new TextBox();
					txtName.setWidth("190px");
					txtName.setHeight("28px");
					txtName.setText(system.getName());

					txtURL = new TextBox();
					txtURL.setWidth("190px");
					txtURL.setHeight("28px");
					txtURL.setText(system.getUrl());

					txtIP = new TextBox();
					txtIP.setWidth("190px");
					txtIP.setHeight("28px");
					txtIP.setText(system.getIp());

					txtRemote = new TextBox();
					txtRemote.setWidth("190px");
					txtRemote.setHeight("28px");
					txtRemote.setText(system.getRemoteURl());

					listActive = new ListBox();
					listActive.setWidth("198px");
					listActive.setHeight("28px");
					listActive.addItem("Yes");
					listActive.addItem("No");
					if (system.isActive()) {
						listActive.setSelectedIndex(0);
					} else {
						listActive.setSelectedIndex(1);
					}

					listProtocol = new ListBox();
					listProtocol.setWidth("198px");
					listProtocol.setHeight("28px");
					listProtocol.addItem("HTTP(s)");
					listProtocol.addItem("SMTP");
					if (system.getProtocol().equals("HTTP(s)")) {
						listProtocol.setSelectedIndex(0);
					} else {
						listProtocol.setSelectedIndex(1);
					}
					listGroup = new ListBox();
					listGroup.setWidth("198px");
					listGroup.setHeight("28px");
					for (int i = 0; i < system.getGroups().length; i++) {
						listGroup.addItem(system.getGroups()[i]);
					}
					listGroup.setSelectedIndex(system.getSelect());

					btnEdit = new Button();
					btnEdit.setText("Edit");
					btnEdit.setStylePrimaryName("margin:4px;");
					btnEdit.addStyleName("form-button");
					
					bttReset = new Button();
					bttReset.setText("Reset");
					bttReset.setStylePrimaryName("margin:4px;");
					bttReset.addStyleName("form-button");

					bttBack = new Button();
					bttBack.setText("Back");
					bttBack.setStylePrimaryName("margin:4px;");
					bttBack.addStyleName("form-button");

					panelButton = new AbsolutePanel();
					panelButton.add(btnEdit);
					panelButton.add(bttReset);
					panelButton.add(bttBack);

					panelAdding = new AbsolutePanel();
					panelAdding
							.add(new HTML(
									"<div id=\"img-adding\"><img src=\"images/icon/loading11.gif\"/></div>"));
					panelAdding.setVisible(false);

					panelValidateName = new AbsolutePanel();
					panelValidateName
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">Name is not validate</div>"));
					panelValidateName.setVisible(false);

					panelValidateIP = new AbsolutePanel();
					panelValidateIP
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">IP is not validate</div>"));
					panelValidateIP.setVisible(false);

					panelValidateURL = new AbsolutePanel();
					panelValidateURL
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">URL is not validate</div>"));
					panelValidateURL.setVisible(false);

					panelValidateRemoteURL = new AbsolutePanel();
					panelValidateRemoteURL
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">Remote-url is not validate or it is existing</div>"));
					panelValidateRemoteURL.setVisible(false);
					panelValidateRemoteURLServer = new AbsolutePanel();
					panelValidateRemoteURLServer
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">it is existing</div>"));
					panelValidateRemoteURLServer.setVisible(false);
					tableForm.setWidget(0, 0, labelName);
					tableForm.setWidget(0, 1, txtName);
					tableForm.setWidget(0, 2, panelValidateName);
					tableForm.setWidget(1, 0, labelurl);
					tableForm.setWidget(1, 1, txtURL);
					tableForm.setWidget(1, 2, panelValidateURL);
					tableForm.setWidget(2, 0, labelip);
					tableForm.setWidget(2, 1, txtIP);
					tableForm.setWidget(2, 2, panelValidateIP);
					tableForm.setWidget(3, 0, labelactive);
					tableForm.setWidget(3, 1, listActive);
					tableForm.setWidget(4, 0, labelprotocol);
					tableForm.setWidget(4, 1, listProtocol);
					tableForm.setWidget(5, 0, labelmailgroup);
					tableForm.setWidget(5, 1, listGroup);
					tableForm.setWidget(6, 0, labelremoteurl);
					tableForm.setWidget(6, 1, txtRemote);
					tableForm.setWidget(6, 2, panelValidateRemoteURL);
					tableForm.setWidget(6, 3, panelValidateRemoteURLServer);
					tableForm.getFlexCellFormatter().setColSpan(7, 0, 2);
					tableForm.setWidget(7, 0, panelAdding);
					tableForm.getFlexCellFormatter().setColSpan(8, 0, 3);
					tableForm.setWidget(8, 0, panelButton);
					initHandler();
					setVisibleLoadingImage(false);
					setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);

				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Oops! Error.",
						HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
						"Goto System Management. ",
						HTMLControl.RED_MESSAGE, true);
			}

		});

	}

	void initHandler() {
		bttReset.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				txtName.setText(system.getName());
				txtURL.setText(system.getUrl());
				txtIP.setText(system.getIp());
				txtRemote.setText(system.getRemoteURl());
				if (system.isActive()) {
					listActive.setSelectedIndex(0);
				} else {
					listActive.setSelectedIndex(1);
				}
				listGroup.setSelectedIndex(system.getSelect());
				if (system.getProtocol().equals("HTTP(s)")) {
					listProtocol.setSelectedIndex(0);
				} else {
					listProtocol.setSelectedIndex(1);
				}

			}
		});
		bttBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(HTMLControl
						.trimHashPart(Window.Location.getHref())
						+ HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME);
			}
		});
		btnEdit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String validateName = validateName(txtName.getText());
				String validateURL = validateURL(txtURL.getText());
				String validateIp = validateIP(txtIP.getText());
				String validateRemoteURL = validateRemoteURL(txtRemote
						.getText());
				panelValidateName.setVisible(false);
				panelValidateRemoteURL.setVisible(false);
				panelValidateIP.setVisible(false);
				panelValidateRemoteURL.setVisible(false);
				panelValidateRemoteURLServer.setVisible(false);
				if (validateName != "") {
					panelValidateName.setVisible(true);
					panelValidateURL.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateRemoteURL.setVisible(false);
					return;
				} else if (validateURL != "") {
					panelValidateName.setVisible(false);
					panelValidateRemoteURL.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(true);
					return;
				} else if (validateIp != "") {
					panelValidateName.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(false);
					panelValidateIP.setVisible(true);
					return;
				} else if (validateRemoteURL != "") {
					panelValidateIP.setVisible(false);
					panelValidateName.setVisible(false);
					panelValidateURL.setVisible(false);
					panelValidateRemoteURL.setVisible(true);
					return;
				}
				panelAdding.setVisible(true);
				monitorGwtSv.editSystembyID(system, txtName.getText(), txtURL
						.getText(), listProtocol.getItemText(listProtocol
						.getSelectedIndex()), listGroup.getItemText(listGroup
						.getSelectedIndex()), txtIP.getText(), txtRemote
						.getText(), isActive(listActive.getItemText(listActive
						.getSelectedIndex())), new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						panelAdding.setVisible(false);
						if (result != null) {
							if (result.equals("Remote URL is exitsting")) {
								panelValidateIP.setVisible(false);
								panelValidateName.setVisible(false);
								panelValidateRemoteURL.setVisible(false);
								panelValidateURL.setVisible(false);
								panelValidateRemoteURLServer.setVisible(true);
							} else if (result.equals("config database error")) {
								showMessage("Server error! ",
										HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
										"Goto System Management. ",
										HTMLControl.RED_MESSAGE, true);
							} else if (result.equals("done")) {
								showMessage("System edited sucessfully. ",
										HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
										"View system list. ",
										HTMLControl.BLUE_MESSAGE, true);
							}
						}  else {
							showMessage("Server error! ",
									HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
									"Goto System Management. ",
									HTMLControl.RED_MESSAGE, true);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showMessage("Server error! ",
								HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME,
								"Goto System Management. ",
								HTMLControl.RED_MESSAGE, true);
					}
				});

			}
		});
	}

	private boolean isActive(String active) {
		boolean isActive = false;
		if (active.equals("Yes")) {
			isActive = true;
		}
		return isActive;
	}

	private String validateName(String name) {
		String msg = "";
		if (name == null || name.trim().length() == 0) {
			msg = "This field is required ";
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
			msg = "This field is required ";
		} else if (url.length() < 3) {
			msg = "URL is not validate";
		}
		return msg;
	}

	// validate RemoteURL
	private String validateRemoteURL(String remoteUrl) {
		String msg = "";
		if (remoteUrl == null || remoteUrl.trim().length() == 0) {
			msg += "This field is required ";
		} else if (remoteUrl.length() < 3) {
			msg = "Remote url is not validate";
		}
		return msg;
	}

	private String validateIP(String ip) {
		String msg = "";
		if (ip == "" || ip == null) {
			msg = "This field is required";
		}
		String patternStr = "^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$";
		RegExp regExp = RegExp.compile(patternStr);
		boolean matchFound = regExp.test(ip);
		if (matchFound == false) {
			msg = "ip is not validate";
		}
		return msg;
	}

}
