package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.NotifyMonitor;
import cmg.org.monitor.entity.shared.SystemGroup;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorContainer;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.summatech.gwt.client.HourMinutePicker;
import com.summatech.gwt.client.HourMinutePicker.PickerFormat;

public class EditSystem extends AncestorEntryPoint {
	int index = 0;
	SystemMonitor system;
	MonitorContainer container;
	ListBox listGroup;
	ListBox listActive;
	ListBox listProtocol;
	TextBox txtName;
	TextBox txtURL;
	TextBox txtIP;
	TextBox txtRemote;
	TextBox txtEmail;
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
	Label labelEmail;
	Label lblNotifyCpu;
	Label lblNotifyMemory;
	Label lblNotifyServices;
	Label lblNotifyServicesConnection;
	Label lblNotifyJVM;
	Label lblNotifyConnectionPool;
	CheckBox cbNotifyJVM;
	CheckBox cbNotifyCpu;
	CheckBox cbNotifyMemory;
	CheckBox cbNotifyServices;
	CheckBox cbNotifyServicesConnection;
	CheckBox cbNotifyConnectionPool;
	AbsolutePanel panelAdding;
	AbsolutePanel panelValidateName;
	AbsolutePanel panelValidateURL;
	AbsolutePanel panelValidateIP;
	AbsolutePanel panelValidateRemoteURL;
	AbsolutePanel panelLabelEmail;
	AbsolutePanel paneltxtEmail;
	AbsolutePanel panelButton;
	AbsolutePanel panelValidateEmail;
	AbsolutePanel panelValidateGroups;
	AbsolutePanel panelValidateExcTime;

	private static FlexTable tableForm;
	private static Grid tableNotify;
	private DisclosurePanel advancedDisclosure;
	private NotifyMonitor notify;
	StringBuffer description;

	HourMinutePicker startTime;
	HourMinutePicker endTime;
	Label lblExclusionTime;
	Label lblStartTime;
	Label lblEndTime;
	Grid tableExTime;

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
							showMessage("Invalid System ID.", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						showMessage("Oops! Error.", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
					}
				});
			} catch (Exception e) {
				showMessage("Oops! Error.", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
			}
		}
	}

	void initFlexTable(String sysID) {
		monitorGwtSv.getSystemMonitorContainer(sysID, new AsyncCallback<MonitorContainer>() {
			@Override
			public void onSuccess(MonitorContainer result) {
				if (result != null) {
					try {
						system = result.getSys();
						notify = result.getNotify();
						container = result;
						listGroup = new ListBox();
						listGroup.setWidth("198px");
						listGroup.setHeight("28px");
						SystemGroup[] groups = container.getListSystemGroup();
						List<SystemGroup> list = new ArrayList<SystemGroup>();
						boolean checkExistGroup = false;
						if (groups != null && groups.length > 0) {
							for (int i = 0; i < groups.length; i++) {
								System.out.println(groups[i].getName());
								if (system.getGroupEmail().equalsIgnoreCase(groups[i].getName())) {
									System.out.println(system.getGroupEmail());
									checkExistGroup = true;
								}
								list.add(groups[i]);
							}
							if (checkExistGroup) {
								list = sortBynameSystemGroup(list);
								for (int i = 0; i < list.size(); i++) {
									System.out.println(list.get(i).getName());
									if (list.get(i).getName().equalsIgnoreCase(system.getGroupEmail())) {
										index = i;
									}
									listGroup.addItem(list.get(i).getName());
								}
								System.out.println(index);
								listGroup.setSelectedIndex(index);
							} else {
								listGroup.addItem("");
								list = sortBynameSystemGroup(list);
								for (SystemGroup g : list) {
									listGroup.addItem(g.getName());
								}
								listGroup.setSelectedIndex(0);
							}
							lblStartTime = new Label("Start");
							lblEndTime = new Label("End");
							tableExTime = new Grid(1, 4);
							tableExTime.setCellPadding(2);

							lblExclusionTime = new Label(MonitorConstant.EXCLUSION_TIME);

							startTime = new HourMinutePicker(PickerFormat._24_HOUR);

							startTime.setTitle("Start");
							startTime.setSize("90", "280");
							startTime.setTime("24h", system.getStartHour(), system.getStartMinute());

							endTime = new HourMinutePicker(PickerFormat._24_HOUR);
							endTime.setTitle("End");
							endTime.setSize("4em", "2em");
							endTime.setTime("24h", system.getEndHour(), system.getEndMinute());
							tableExTime.setWidget(0, 0, lblStartTime);
							tableExTime.setWidget(0, 1, startTime);
							tableExTime.setWidget(0, 2, lblEndTime);
							tableExTime.setWidget(0, 3, endTime);

							tableNotify = new Grid(6, 2);
							tableNotify.setCellSpacing(3);
							cbNotifyCpu = new CheckBox();
							cbNotifyCpu.setValue(notify.isNotifyCpu());
							cbNotifyCpu.setStyleName("");
							cbNotifyCpu.setStyleName("checkbox-size");

							cbNotifyMemory = new CheckBox();
							cbNotifyMemory.setValue(notify.isNotifyMemory());
							cbNotifyMemory.setStyleName("");
							cbNotifyMemory.setStyleName("checkbox-size");

							cbNotifyServices = new CheckBox();
							cbNotifyServices.setValue(notify.isNotifyServices());
							cbNotifyServices.setStyleName("");
							cbNotifyServices.setStyleName("checkbox-size");

							cbNotifyJVM = new CheckBox();
							cbNotifyJVM.setValue(notify.isJVM());
							cbNotifyJVM.setStyleName("");
							cbNotifyJVM.setStyleName("checkbox-size");

							cbNotifyConnectionPool = new CheckBox();
							cbNotifyConnectionPool.setValue(notify.isNotifyConnectionPool());
							cbNotifyConnectionPool.setStyleName("");
							cbNotifyConnectionPool.setStyleName("checkbox-size");

							cbNotifyServicesConnection = new CheckBox();
							cbNotifyServicesConnection.setValue(notify.isNotifyServicesConnection());
							cbNotifyServicesConnection.setStyleName("");
							cbNotifyServicesConnection.setStyleName("checkbox-size");

							lblNotifyJVM = new Label(MonitorConstant.Notify_JVM);
							lblNotifyCpu = new Label(MonitorConstant.Notify_Cpu);
							lblNotifyConnectionPool = new Label(MonitorConstant.Notify_Connection_Pool);
							lblNotifyMemory = new Label(MonitorConstant.Notify_Memory);
							lblNotifyServices = new Label(MonitorConstant.Notify_Service);
							lblNotifyServicesConnection = new Label(MonitorConstant.Notify_ServiceConnection);
							tableNotify.setWidget(0, 0, lblNotifyCpu);
							tableNotify.setWidget(0, 1, cbNotifyCpu);
							tableNotify.setWidget(1, 0, lblNotifyMemory);
							tableNotify.setWidget(1, 1, cbNotifyMemory);
							tableNotify.setWidget(2, 0, lblNotifyServices);
							tableNotify.setWidget(2, 1, cbNotifyServices);
							tableNotify.setWidget(3, 0, lblNotifyServicesConnection);
							tableNotify.setWidget(3, 1, cbNotifyServicesConnection);
							tableNotify.setWidget(4, 0, lblNotifyJVM);
							tableNotify.setWidget(4, 1, cbNotifyJVM);
							tableNotify.setWidget(5, 0, lblNotifyConnectionPool);
							tableNotify.setWidget(5, 1, cbNotifyConnectionPool);
							advancedDisclosure = new DisclosurePanel(HTMLControl.NOTIFY_OPTION);
							advancedDisclosure.setAnimationEnabled(true);
							advancedDisclosure.setContent(tableNotify);
							advancedDisclosure.setOpen(true);

							labelEmail = new Label();
							labelEmail.setText("Email");
							txtEmail = new TextBox();
							txtEmail.setWidth("196px");
							txtEmail.setHeight("30px");
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
							tableForm.getFlexCellFormatter().setWidth(9, 0, "298px");
							tableForm.getFlexCellFormatter().setWidth(10, 0, "100px");
							tableForm.getFlexCellFormatter().setWidth(11, 0, "100px");

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
							labelmailgroup.setText("Group ");

							txtName = new TextBox();
							txtName.setWidth("196px");
							txtName.setHeight("30px");
							txtName.setText(system.getName());

							txtURL = new TextBox();
							txtURL.setWidth("196px");
							txtURL.setHeight("30px");
							txtURL.setText(system.getUrl());

							txtIP = new TextBox();
							txtIP.setWidth("196px");
							txtIP.setHeight("30px");
							txtIP.setText(system.getIp());

							txtRemote = new TextBox();
							txtRemote.setWidth("196px");
							txtRemote.setHeight("30px");
							txtRemote.setText(system.getRemoteUrl());

							txtEmail.setText(system.getEmailRevice());

							panelLabelEmail = new AbsolutePanel();
							panelLabelEmail.add(labelEmail);
							panelLabelEmail.setVisible(false);

							paneltxtEmail = new AbsolutePanel();
							paneltxtEmail.add(txtEmail);

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
							listProtocol.addItem(MonitorConstant.HTTP_PROTOCOL);
							listProtocol.addItem(MonitorConstant.SMTP_PROTOCOL);
							if (system.getProtocol().equals("HTTP(s)")) {
								listProtocol.setSelectedIndex(0);
								paneltxtEmail.setVisible(false);
								panelLabelEmail.setVisible(false);
								txtRemote.setEnabled(true);
							} else {
								listProtocol.setSelectedIndex(1);
								paneltxtEmail.setVisible(true);
								panelLabelEmail.setVisible(true);
								txtRemote.setEnabled(false);
							}

							btnEdit = new Button();
							btnEdit.setText("Update");
							btnEdit.setStyleName("margin:6px;");
							btnEdit.addStyleName("form-button");

							bttReset = new Button();
							bttReset.setText("Reset");
							bttReset.setStyleName("margin:6px;");
							bttReset.addStyleName("form-button");

							bttBack = new Button();
							bttBack.setText("Back");
							bttBack.setStyleName("margin:6px;");
							bttBack.addStyleName("form-button");

							panelButton = new AbsolutePanel();
							panelButton.add(btnEdit);
							panelButton.add(bttReset);
							panelButton.add(bttBack);

							panelAdding = new AbsolutePanel();
							panelAdding.add(new HTML("<div id=\"img-adding\"><img src=\"images/icon/loading11.gif\"/></div>"));
							panelAdding.setVisible(false);

							panelValidateName = new AbsolutePanel();

							panelValidateEmail = new AbsolutePanel();

							panelValidateIP = new AbsolutePanel();

							panelValidateURL = new AbsolutePanel();

							panelValidateRemoteURL = new AbsolutePanel();

							panelValidateGroups = new AbsolutePanel();

							panelValidateExcTime = new AbsolutePanel();

							tableForm.setWidget(0, 0, labelName);
							tableForm.setWidget(0, 1, txtName);
							tableForm.setWidget(0, 2, panelValidateName);
							tableForm.setWidget(1, 0, labelurl);
							tableForm.setWidget(1, 1, txtURL);
							tableForm.setWidget(1, 2, panelValidateURL);
							tableForm.setWidget(2, 0, labelip);
							tableForm.setWidget(2, 1, txtIP);
							tableForm.setWidget(2, 2, panelValidateIP);
							tableForm.setWidget(3, 0, lblExclusionTime);
							tableForm.setWidget(3, 1, tableExTime);
							tableForm.setWidget(3, 2, panelValidateExcTime);
							tableForm.setWidget(4, 0, labelactive);
							tableForm.setWidget(4, 1, listActive);
							tableForm.setWidget(5, 0, labelprotocol);
							tableForm.setWidget(5, 1, listProtocol);
							tableForm.setWidget(6, 0, panelLabelEmail);
							tableForm.setWidget(6, 1, paneltxtEmail);
							tableForm.setWidget(6, 2, panelValidateEmail);
							tableForm.setWidget(7, 0, labelmailgroup);
							tableForm.setWidget(7, 1, listGroup);
							tableForm.setWidget(7, 2, panelValidateGroups);
							tableForm.setWidget(8, 0, labelremoteurl);
							tableForm.setWidget(8, 1, txtRemote);
							tableForm.setWidget(8, 2, panelValidateRemoteURL);
							tableForm.getFlexCellFormatter().setColSpan(9, 0, 2);
							tableForm.setWidget(9, 0, advancedDisclosure);
							tableForm.getFlexCellFormatter().setColSpan(10, 0, 2);
							tableForm.setWidget(10, 0, panelAdding);
							tableForm.getFlexCellFormatter().setColSpan(11, 0, 3);
							tableForm.setWidget(11, 0, panelButton);
							initHandler();
							setVisibleLoadingImage(false);
							setOnload(false);
							setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						} else {
							setVisibleLoadingImage(false);
							setOnload(false);
							showMessage("No Group Found.", HTMLControl.HTML_GROUP_MANAGEMENT_NAME, "Goto Group Management. ", HTMLControl.RED_MESSAGE, true);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				showMessage("Oops! Error.", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
			}

		});

	}

	void initHandler() {
		listProtocol.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (listProtocol.getSelectedIndex() == 0) {
					txtRemote.setEnabled(true);
					txtRemote.setText(system.getRemoteUrl());
					panelLabelEmail.setVisible(false);
					paneltxtEmail.setVisible(false);
					txtEmail.setText("");
					panelValidateEmail.setVisible(false);
				} else if (listProtocol.getSelectedIndex() == 1) {
					txtRemote.setText("");
					txtRemote.setEnabled(false);
					panelLabelEmail.setVisible(true);
					paneltxtEmail.setVisible(true);
					txtEmail.setText(system.getEmail());
					panelValidateRemoteURL.setVisible(false);
				}
			}
		});
		bttReset.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				panelValidateEmail.setVisible(false);
				panelValidateIP.setVisible(false);
				panelValidateName.setVisible(false);
				panelValidateIP.setVisible(false);
				panelValidateURL.setVisible(false);
				txtName.setText(system.getName());
				txtURL.setText(system.getUrl());
				txtIP.setText(system.getIp());
				txtRemote.setText(system.getRemoteUrl());
				if (system.isActive()) {
					listActive.setSelectedIndex(0);
				} else {
					listActive.setSelectedIndex(1);
				}
				listGroup.setSelectedIndex(index);
				if (system.getProtocol().equals("HTTP(s)")) {
					listProtocol.setSelectedIndex(0);
					txtRemote.setEnabled(true);
					txtRemote.setText(system.getRemoteUrl());
					panelLabelEmail.setVisible(false);
					paneltxtEmail.setVisible(false);
				} else {
					listProtocol.setSelectedIndex(1);
					panelLabelEmail.setVisible(true);
					paneltxtEmail.setVisible(true);
					txtRemote.setVisible(false);
					txtEmail.setText(system.getEmail());

				}

				cbNotifyCpu.setValue(notify.isNotifyCpu());

				cbNotifyMemory.setValue(notify.isNotifyMemory());

				cbNotifyServices.setValue(notify.isNotifyServices());

				cbNotifyServicesConnection.setValue(notify.isNotifyServicesConnection());
				cbNotifyJVM.setValue(notify.isJVM());
				cbNotifyConnectionPool.setValue(notify.isNotifyConnectionPool());
			}
		});
		bttBack.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Window.Location.replace(HTMLControl.trimHashPart(Window.Location.getHref()) + HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME);
			}
		});
		btnEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (listProtocol.getItemText(listProtocol.getSelectedIndex()).equals(MonitorConstant.HTTP_PROTOCOL)) {
					String validateGroup = listGroup.getItemText(listGroup.getSelectedIndex());
					String validateName = validateName(txtName.getText());
					String validateURL = validateURL(txtURL.getText());
					String validateIp = validateIP(txtIP.getText());
					String validateRemoteURL = validateRemoteURL(txtRemote.getText());
					String validateExctime = validateExcTime();

					panelValidateEmail.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateName.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(false);
					panelValidateGroups.setVisible(false);
					panelValidateExcTime.setVisible(false);
					if (validateName != "") {
						panelValidateName.clear();
						panelValidateName.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateName + "</div>"));
						panelValidateName.setVisible(true);
						panelValidateURL.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateURL != "") {
						panelValidateURL.clear();
						panelValidateURL.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateURL + "</div>"));
						panelValidateURL.setVisible(true);
						panelValidateName.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateIp != "") {
						panelValidateIP.clear();
						panelValidateIP.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateIp + "</div>"));
						panelValidateIP.setVisible(true);
						panelValidateName.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateExctime != "") {
						panelValidateExcTime.clear();
						panelValidateExcTime.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateExctime + "</div>"));
						panelValidateExcTime.setVisible(true);
						panelValidateIP.setVisible(false);
						panelValidateName.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						return;
					} else if (validateRemoteURL != "") {
						if (!listProtocol.getItemText(listProtocol.getSelectedIndex()).equals("SMTP")) {
							panelValidateRemoteURL.clear();
							panelValidateRemoteURL.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateRemoteURL + "</div>"));
							panelValidateRemoteURL.setVisible(true);
							panelValidateIP.setVisible(false);
							panelValidateName.setVisible(false);
							panelValidateEmail.setVisible(false);
							panelValidateURL.setVisible(false);
							panelValidateGroups.setVisible(false);
							panelValidateExcTime.setVisible(false);
							return;
						}
					} else if (validateGroup == "") {
						panelValidateGroups.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">Please select a Group</div>"));
						panelValidateGroups.setVisible(true);
						panelValidateEmail.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateName.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					}
					panelValidateGroups.setVisible(false);
					panelValidateEmail.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateName.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(false);
					panelAdding.setVisible(true);
					panelValidateExcTime.setVisible(false);

					NotifyMonitor nm = new NotifyMonitor();
					nm.setNotifyCpu(cbNotifyCpu.getValue());
					nm.setNotifyMemory(cbNotifyMemory.getValue());
					nm.setNotifyServices(cbNotifyServices.getValue());
					nm.setNotifyServicesConnection(cbNotifyServicesConnection.getValue());
					nm.setJVM(cbNotifyJVM.getValue());
					nm.setNotifyConnectionPool(cbNotifyConnectionPool.getValue());
					SystemMonitor sysNew = new SystemMonitor();
					sysNew.setId(system.getId());
					sysNew.setName(txtName.getText());
					sysNew.setUrl(txtURL.getText());
					sysNew.setProtocol(listProtocol.getItemText(listProtocol.getSelectedIndex()));
					sysNew.setGroupEmail(listGroup.getItemText(listGroup.getSelectedIndex()));
					sysNew.setIp(txtIP.getText());
					sysNew.setRemoteUrl(txtRemote.getText());
					sysNew.setEmailRevice(txtEmail.getText());
					sysNew.setActive(isActive(listActive.getItemText(listActive.getSelectedIndex())));
					sysNew.setStartHour(startTime.getHour());
					sysNew.setEndHour(endTime.getHour());
					sysNew.setStartMinute(startTime.getMinute());
					sysNew.setEndMinute(endTime.getMinute());
					sysNew.setNotify(nm);
					sysNew.setStartMinutes(startTime.getMinutes());
					sysNew.setEndMinutes(endTime.getMinutes());
					editSystem(sysNew);
				} else if (listProtocol.getItemText(listProtocol.getSelectedIndex()).equals(MonitorConstant.SMTP_PROTOCOL)) {
					String validateGroup = listGroup.getItemText(listGroup.getSelectedIndex());
					String validateName = validateName(txtName.getText());
					String validateExctime = validateExcTime();
					String validateURL = validateURL(txtURL.getText());
					String validateIp = validateIP(txtIP.getText());
					String validateEmail = validateEmail(txtEmail.getText());
					panelValidateEmail.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateName.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(false);
					panelValidateGroups.setVisible(false);
					panelValidateExcTime.setVisible(false);
					if (validateName != "") {
						panelValidateName.clear();
						panelValidateName.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateName + "</div>"));
						panelValidateName.setVisible(true);
						panelValidateURL.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateURL != "") {
						panelValidateURL.clear();
						panelValidateURL.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateURL + "</div>"));
						panelValidateURL.setVisible(true);
						panelValidateName.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateIp != "") {
						panelValidateIP.clear();
						panelValidateIP.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateIp + "</div>"));
						panelValidateIP.setVisible(true);
						panelValidateName.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					} else if (validateExctime != "") {
						panelValidateExcTime.clear();
						panelValidateExcTime.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateExctime + "</div>"));
						panelValidateExcTime.setVisible(true);
						panelValidateIP.setVisible(false);
						panelValidateName.setVisible(false);
						panelValidateRemoteURL.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateEmail.setVisible(false);
						panelValidateGroups.setVisible(false);
						return;
					} else if (validateEmail != "") {
						panelValidateEmail.clear();
						panelValidateEmail.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">" + validateEmail + "</div>"));
						panelValidateEmail.setVisible(true);
						panelValidateIP.setVisible(true);
						panelValidateName.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateGroups.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;

					} else if (validateGroup == "") {
						panelValidateGroups.add(new HTML("<div class=\"error-left\"></div><div class=\"error-inner\">Please select a Group</div>"));
						panelValidateGroups.setVisible(true);
						panelValidateEmail.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateName.setVisible(false);
						panelValidateIP.setVisible(false);
						panelValidateURL.setVisible(false);
						panelValidateExcTime.setVisible(false);
						return;
					}
					panelValidateGroups.setVisible(false);
					panelValidateEmail.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateName.setVisible(false);
					panelValidateIP.setVisible(false);
					panelValidateURL.setVisible(false);
					panelValidateExcTime.setVisible(false);
					panelAdding.setVisible(true);

					NotifyMonitor nm = new NotifyMonitor();
					nm.setNotifyCpu(cbNotifyCpu.getValue());
					nm.setNotifyMemory(cbNotifyMemory.getValue());
					nm.setNotifyServices(cbNotifyServices.getValue());
					nm.setNotifyServicesConnection(cbNotifyServicesConnection.getValue());
					nm.setJVM(cbNotifyJVM.getValue());
					nm.setNotifyConnectionPool(cbNotifyConnectionPool.getValue());
					SystemMonitor sysNew = new SystemMonitor();
					sysNew.setId(system.getId());
					sysNew.setName(txtName.getText());
					sysNew.setUrl(txtURL.getText());
					sysNew.setProtocol(listProtocol.getItemText(listProtocol.getSelectedIndex()));
					sysNew.setGroupEmail(listGroup.getItemText(listGroup.getSelectedIndex()));
					sysNew.setIp(txtIP.getText());
					sysNew.setEmailRevice(txtEmail.getText());
					sysNew.setRemoteUrl(txtRemote.getText());
					sysNew.setActive(isActive(listActive.getItemText(listActive.getSelectedIndex())));
					sysNew.setNotify(nm);
					sysNew.setStartHour(startTime.getHour());
					sysNew.setEndHour(endTime.getHour());
					sysNew.setStartMinute(startTime.getMinute());
					sysNew.setEndMinute(endTime.getMinute());
					sysNew.setStartMinutes(startTime.getMinutes());
					sysNew.setEndMinutes(endTime.getMinutes());
					editSystem(sysNew);
				}
			}
		});
	}

	private void editSystem(SystemMonitor sys) {
		panelAdding.setVisible(false);
		monitorGwtSv.editSystem(sys, new AsyncCallback<Boolean>() {
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					showMessage("System edited sucessfully. ", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "View system list. ", HTMLControl.BLUE_MESSAGE, true);

				} else {
					showMessage("Server error! ", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Server error! ", HTMLControl.HTML_SYSTEM_MANAGEMENT_NAME, "Goto System Management. ", HTMLControl.RED_MESSAGE, true);
			}
		});

	}

	public static List<SystemGroup> sortBynameSystemGroup(List<SystemGroup> groups) {
		SystemGroup temp = null;
		for (int i = 1; i < groups.size(); i++) {
			int j;
			SystemGroup val = groups.get(i);
			for (j = i - 1; j > -1; j--) {
				temp = groups.get(j);
				if (temp.compareByName(val) <= 0) {
					break;
				}
				groups.set(j + 1, temp);
			}
			groups.set(j + 1, val);
		}
		return groups;
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

		} /*
		 * else if (name.contains("$") || name.contains("%") ||
		 * name.contains("*")) { msg = "name is not validate";
		 * 
		 * }
		 */
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

	/**
	 * @param remoteUrl
	 * @return
	 */
	private String validateRemoteURL(String remoteUrl) {
		String msg = "";
		boolean check = false;
		if (remoteUrl == null || remoteUrl.trim().length() == 0) {
			msg += "This field is required ";
			check = true;
		} else if (remoteUrl.length() < 3) {
			msg = "Remote url is not validate";
			check = true;
		}
		if (!check) {
			if (!remoteUrl.equals(system.getRemoteUrl())) {
				String[] remoteURLs = container.getRemoteUrls();
				if (remoteURLs != null) {
					for (int i = 0; i < remoteURLs.length; i++) {
						if (remoteURLs[i] != null) {
							if (remoteUrl.equals(remoteURLs[i])) {
								msg = "Remote URL is exitsting";
								break;
							}
						}
					}

				}

			}
		}

		return msg;
	}

	/**
	 * @param email
	 * @return
	 */

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
			msg = "email is not validate";
			check = true;
		}
		if (!check) {
			if (!email.equals(system.getEmail())) {
				String[] emails = container.getEmails();
				if (emails != null) {
					for (int i = 0; i < emails.length; i++) {
						if (emails[i] != null) {
							if (emails[i].equals(email)) {
								msg = "Email is exitsting";
								break;
							}
						}
					}

				}
			}
		}
		return msg;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see cmg.org.monitor.module.client.AncestorEntryPoint#initDialog()
	 */
	@Override
	protected void initDialog() {
		// TODO Auto-generated method stub

	}

	private String validateExcTime() {
		if (startTime.getMinutes() == null) {
			return "Please set value of start time";
		}
		if (endTime.getMinutes() == null) {
			return "Please set value of end time";
		}

		if (startTime.getMinutes() > endTime.getMinutes()) {
			return "Please set end time greater than start time";
		}
		return "";
	}
}
