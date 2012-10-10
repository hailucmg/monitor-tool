package cmg.org.monitor.module.client;

import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class GoogleManagement extends AncestorEntryPoint {
	FlexTable flexTable;
	static Table myTable;
	FlexTable totalTable;

	static DialogBox dialogBox;
	private static HTML popupContent;
	private static FlexTable flexHTML;

	Label lbDomain;
	Label lbUsername;
	Label lbPassword;
	Label lbConfirmPwd;
	TextBox txtDomain;
	TextBox txtUsername;
	PasswordTextBox txtPassword;
	PasswordTextBox txtConfirmPwd;
	Button btnAddAccount;
	Button btnSaveAccount;
	Button btnClearAccount;
	Button btnResetAccount;
	static TextArea txtLog;

	static DisclosurePanel advancedDisclosure;
	static GoogleAccount adminAcc;
	static GoogleAccount selected;
	static GoogleAccount[] listGoogleAcc;
	AbsolutePanel panelValidateDomain;
	AbsolutePanel panelValidateUsername;
	AbsolutePanel panelValidatePassword;
	AbsolutePanel panelValidateConfirmPassword;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_GOOGLE_MANAGEMENT) {
			if (currentUser.isAdmin() && currentUser.isNeedAddAccount()) {
				showMessage(
						"There are no user in system. Please create a google account to sync. ",
						"", "",
						HTMLControl.RED_MESSAGE, true);	
			}
			GoogleManagement.exportStaticMethod();
			GoogleManagement.clearGoogleAccount();
			GoogleManagement.exportConfirmDialog();
			GoogleManagement.exportViewLastestLog();
			flexTable = new FlexTable();

			totalTable = new FlexTable();
			txtLog = new TextArea();
			txtLog.setWidth("1185px");
			txtLog.setHeight("300px");
			txtLog.setReadOnly(true);
			advancedDisclosure = new DisclosurePanel("View Log");
			advancedDisclosure.setAnimationEnabled(true);
			advancedDisclosure.setContent(txtLog);
			myTable = new Table();
			myTable.setWidth("1185px");
			myTable.addSelectHandler(createSelectHandler(myTable));
			initInterface();
			totalTable.setWidget(0, 0, flexTable);
			totalTable.setWidget(2, 0, myTable);
			totalTable.setWidget(3, 0, advancedDisclosure);
			addWidget(HTMLControl.ID_BODY_CONTENT, totalTable);
			initContent();
		}
	}

	public static native void exportViewLastestLog() /*-{
	$wnd.viewLastestLog =
	$entry(@cmg.org.monitor.module.client.GoogleManagement::viewLastestLog(Ljava/lang/String;Ljava/lang/String;))
	}-*/;
	
	public static native void exportStaticMethod() /*-{
													$wnd.syncAccount =
													$entry(@cmg.org.monitor.module.client.GoogleManagement::syncAccount(Ljava/lang/String;Ljava/lang/String;))
													}-*/;

	public static native void clearGoogleAccount() /*-{
													$wnd.clearAccount =
													$entry(@cmg.org.monitor.module.client.GoogleManagement::clearAccount(Ljava/lang/String;))
													}-*/;
	
	public static String getUserAgent(){
	    return Navigator.getUserAgent();
	}

	static void viewLastestLog(String adminAccount, String id) {
		monitorGwtSv.viewLastestLog(adminAccount, new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				txtLog.setText("Error. Message: " + caught.getMessage());
				advancedDisclosure.setOpen(true);
			}

			public void onSuccess(String result) {
				if (result == null || result.length() == 0) {
					result = "No log found";
				}
				txtLog.setText(result);
				advancedDisclosure.setOpen(true);
			}
		});
	}
	
	static void clearAccount(String id) {
		final String userid = id;
		monitorGwtSv.deleteGoogleAccount(id, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Deleted Failed.", "", "", HTMLControl.RED_MESSAGE,
						true);
			}

			@Override
			public void onSuccess(Boolean result) {

				showMessage("Deleted Successfully", "", "",
						HTMLControl.BLUE_MESSAGE, true);
				List<GoogleAccount> listBan = new ArrayList<GoogleAccount>();
				for (int i = 0; i < listGoogleAcc.length; i++) {
					if (!listGoogleAcc[i].getId().equalsIgnoreCase(userid)) {
						listBan.add(listGoogleAcc[i]);
					}
				}
				if (!listBan.isEmpty()) {
					listGoogleAcc = new GoogleAccount[listBan.size()];
					listBan.toArray(listGoogleAcc);
					drawTable(listGoogleAcc);
				} else {
					drawTable(null);
				}
			}

		});
	}

	static void syncAccount(String domain, String username) {
		setOnload(true);
		setVisibleLoadingImage(true);
		adminAcc = new GoogleAccount();
		adminAcc.setDomain(domain);
		adminAcc.setUsername(username);
		try {
			monitorGwtSv.syncAccount(adminAcc, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					setOnload(false);
					setVisibleLoadingImage(false);
					setVisibleMessage(false, HTMLControl.RED_MESSAGE);
					setVisibleMessage(false, HTMLControl.BLUE_MESSAGE);
					showMessage("Synchronized failed.", "", "",
							HTMLControl.RED_MESSAGE, true);
				}

				@Override
				public void onSuccess(String result) {
					setOnload(false);
					setVisibleLoadingImage(false);
					if (result.toLowerCase().startsWith("fail:")) {
						result = result.substring(6, result.length());
						setVisibleMessage(false, HTMLControl.RED_MESSAGE);
						setVisibleMessage(false, HTMLControl.BLUE_MESSAGE);
						showMessage(result, "", "", HTMLControl.RED_MESSAGE,
								true);
					} else {
						setVisibleMessage(false, HTMLControl.RED_MESSAGE);
						setVisibleMessage(false, HTMLControl.BLUE_MESSAGE);
						showMessage("Synchronized successfully", "", "",
								HTMLControl.BLUE_MESSAGE, true);
						txtLog.setText(result);
						advancedDisclosure.setOpen(true);
						monitorGwtSv.listAllGoogleAcc(new AsyncCallback<GoogleAccount[]>() {
							@Override
							public void onFailure(Throwable caught) {								
							}

							@Override
							public void onSuccess(GoogleAccount[] result) {										
								listGoogleAcc = result;
								drawTable(result);
							}

						});
					}
					
				}

			});
		} catch (Exception e1) {
			//
		}

	}

	private String checkDomain(String str) {
		String msg = "";
		if (str == null || str.trim().length() == 0) {
			msg = "This field is required ";
		}
		return msg;
	}

	private String checkUsername(String domain, String username) {
		String msg = "";
		if (username == null || username.trim().length() == 0) {
			msg = "This field is required ";
		}

		// if (listGoogleAcc != null && listGoogleAcc.length > 0) {
		// for (int i = 0; i < listGoogleAcc.length; i++) {
		// if (listGoogleAcc[i].getUsername().equalsIgnoreCase(username) &&
		// listGoogleAcc[i].getDomain().equalsIgnoreCase(domain)) {
		// msg = "User existed on this domain";
		// }
		// }
		// }
		return msg;
	}

	private String checkPassword(String str) {
		String msg = "";
		if (str == null || str.trim().length() == 0) {
			msg = "This field is required ";
		}
		return msg;
	}

	private String checkConfirmPwd(String str, String pass) {
		String msg = "";
		if (str == null || str.trim().length() == 0) {
			msg = "This field is required ";
		}

		if (!str.equals(pass)) {
			msg = "Confirm password is not the same with password";
		}
		return msg;
	}

	void initInterface() {
		lbDomain = new Label("Domain name:");
		lbUsername = new Label("User name:");
		lbPassword = new Label("Password:");
		lbConfirmPwd = new Label("Confirm Password:");

		btnAddAccount = new Button("Add/Edit account");
		btnResetAccount = new Button("Reset Account");
		// btnSaveAccount = new Button("Update account");
		// btnClearAccount = new Button("Clear account");

		txtDomain = new TextBox();
		txtDomain.getElement().setAttribute("style", "margin-left:2px;");
		txtDomain.setWidth("167px");
		txtUsername = new TextBox();
		txtUsername.getElement().setAttribute("style", "margin-left:2px;");
		txtUsername.setWidth("167px");
		txtPassword = new PasswordTextBox();
		txtConfirmPwd = new PasswordTextBox();
		txtPassword.getElement().setAttribute("style", "margin-left:2px;");
		txtPassword.getElement().setAttribute("id", "txtPassword");
		txtPassword.setWidth("159px");
		if(getUserAgent().contains("Firefox")){
		    txtPassword.setWidth("179px");
		}
		txtConfirmPwd.getElement().setAttribute("style", "margin-left:2px;");
		txtConfirmPwd.getElement().setAttribute("id", "txtConfirmPwd");
		txtConfirmPwd.setWidth("159px");
		if(getUserAgent().contains("Firefox")){
		    txtConfirmPwd.setWidth("179px");
		}

		panelValidateDomain = new AbsolutePanel();
		panelValidateDomain.setVisible(false);
		panelValidateUsername = new AbsolutePanel();
		panelValidateUsername.setVisible(false);
		panelValidatePassword = new AbsolutePanel();
		panelValidatePassword.setVisible(false);
		panelValidateConfirmPassword = new AbsolutePanel();
		panelValidateConfirmPassword.setVisible(false);

		flexTable.setWidget(0, 0, lbDomain);
		flexTable.setWidget(0, 1, txtDomain);
		flexTable.setWidget(0, 2, panelValidateDomain);
		flexTable.setWidget(1, 0, lbUsername);
		flexTable.setWidget(1, 1, txtUsername);
		flexTable.setWidget(1, 2, panelValidateUsername);
		flexTable.setWidget(2, 0, lbPassword);
		flexTable.setWidget(2, 1, txtPassword);
		flexTable.setWidget(2, 2, panelValidatePassword);
		flexTable.setWidget(3, 0, lbConfirmPwd);
		flexTable.setWidget(3, 1, txtConfirmPwd);
		flexTable.setWidget(3, 2, panelValidateConfirmPassword);
		flexTable.setWidget(4, 0, btnAddAccount);
		flexTable.setWidget(4, 1, btnResetAccount);
		// flexTable.setWidget(3, 1, btnSaveAccount);
		// flexTable.setWidget(3, 2, btnClearAccount);
		flexTable.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(1, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(2, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(3, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(4, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		panelValidateConfirmPassword = new AbsolutePanel();
		panelValidateConfirmPassword.setVisible(false);
		panelValidateConfirmPassword = new AbsolutePanel();
		panelValidateConfirmPassword.setVisible(false);

		btnResetAccount.addClickHandler(new ClickHandler(){
		    @Override
		    public void onClick(ClickEvent event) {
			txtDomain.setText("");
			txtUsername.setText("");
			txtConfirmPwd.setText("");
			txtPassword.setText("");
		    }
		});
		
		btnAddAccount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panelValidateDomain.setVisible(false);
				panelValidatePassword.setVisible(false);
				panelValidateUsername.setVisible(false);
				panelValidateConfirmPassword.setVisible(false);
				if (checkDomain(txtDomain.getText()) != "") {
					panelValidateDomain.clear();
					panelValidateDomain
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">"
											+ "Please fill in domain field."
											+ "</div>"));
					panelValidatePassword.setVisible(false);
					panelValidateUsername.setVisible(false);
					panelValidateDomain.setVisible(true);
					panelValidateConfirmPassword.setVisible(false);
					return;
				} else if (checkUsername(txtDomain.getText(),
						txtUsername.getText()) != "") {
					panelValidateUsername.clear();
					panelValidateUsername
							.add(new HTML(
									"<div class=\"error-left\"></div><div class=\"error-inner\">"
											+ "Please fill in username field or username existed."
											+ "</div>"));
					panelValidatePassword.setVisible(false);
					panelValidateDomain.setVisible(false);
					panelValidateUsername.setVisible(true);
					panelValidateConfirmPassword.setVisible(false);
					return;
				} else if (checkPassword(txtPassword.getText()) != "") {
					panelValidatePassword.clear();
					panelValidatePassword.add(new HTML(
							"<div class=\"error-left\"></div><div class=\"error-inner\">"
									+ "Please fill in password field."
									+ "</div>"));
					panelValidateUsername.setVisible(false);
					panelValidateDomain.setVisible(false);
					panelValidateConfirmPassword.setVisible(false);
					panelValidatePassword.setVisible(true);
					return;
				} else if (checkConfirmPwd(txtConfirmPwd.getText(),
						txtPassword.getText()) != "") {
					panelValidateConfirmPassword.clear();
					panelValidateConfirmPassword.add(new HTML(
							"<div class=\"error-left\"></div><div class=\"error-inner\">"
									+ checkConfirmPwd(txtConfirmPwd.getText(),
											txtPassword.getText()) + "</div>"));
					panelValidateUsername.setVisible(false);
					panelValidateDomain.setVisible(false);
					panelValidatePassword.setVisible(false);
					panelValidateConfirmPassword.setVisible(true);
					return;
				}
				setVisibleLoadingImage(true);
				GoogleAccount acc = new GoogleAccount();
				acc.setDomain(txtDomain.getText());
				acc.setUsername(txtUsername.getText());
				acc.setPassword(txtPassword.getText());
				monitorGwtSv.addGoogleAccount(acc,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
							    	setVisibleLoadingImage(false);
								showMessage("Added Failed.", "", "",
										HTMLControl.RED_MESSAGE, true);
							}

							@Override
							public void onSuccess(Boolean result) {
							    	setVisibleLoadingImage(false);
								showMessage("Successfully", "", "",
										HTMLControl.BLUE_MESSAGE, true);
								monitorGwtSv.listAllGoogleAcc(new AsyncCallback<GoogleAccount[]>() {

									@Override
									public void onFailure(Throwable caught) {
										
									}

									@Override
									public void onSuccess(GoogleAccount[] result) {		
										currentUser.setNeedAddAccount(false);
										listGoogleAcc = result;
										drawTable(result);
									}

								});
							}
						});
			}

		});
	}

	static void initContent() {
		monitorGwtSv.listAllGoogleAcc(new AsyncCallback<GoogleAccount[]>() {

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
			}

			@Override
			public void onSuccess(GoogleAccount[] result) {
				setVisibleLoadingImage(false);
				setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
				if (result == null || result.length == 0) {
					showMessage(
							"There are no user in system. Please create a google account to sync. ",
							"", "",
							HTMLControl.RED_MESSAGE, true);
				}
				listGoogleAcc = result;
				drawTable(result);
			}

		});

	}

	static AbstractDataTable createData(GoogleAccount[] listUser) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Domain name");
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.DATETIME, "Last Synchronized");
		data.addColumn(ColumnType.STRING, "");
		if (listUser != null) {
			data.addRows(listUser.length);

			GoogleAccount[] sortUser = sortByname(listUser);
			for (int j = 0; j < sortUser.length; j++) {
				if (sortUser[j].getId() != null) {
					data.setValue(j, 0, sortUser[j].getDomain());
					data.setValue(j, 1, sortUser[j].getUsername());
					data.setValue(j, 2, sortUser[j].getLastSync());
					data.setValue(
							j,
							3,
							"<div class='btnSync' title='Synchronized' domain_name='"
									+ sortUser[j].getDomain()
									+ "' user_name='"
									+ sortUser[j].getId()
									+ "' pwd='"
									+ sortUser[j].getPassword()
									+ "' onClick=\"javascript:syncAccount('"
									+ sortUser[j].getDomain()
									+ "','"
									+ sortUser[j].getUsername()
									+ "');\"></div>"
									+ "<div user_name='"
									+ sortUser[j].getId()
									+ "' onClick=\"javascript:viewLastestLog('"
									+ sortUser[j].getUsername()
									+ "@"
									+ sortUser[j].getDomain()
									+ "','"
									+ sortUser[j].getId()
									+ "');\" title=\"View lastest log\" class=\"btnViewLog\"></div>"
									+ "<div user_name='"
									+ sortUser[j].getId()
									+ "' onClick=\"javascript:downloadFullLog('"
									+ sortUser[j].getUsername()
									+ "@"
									+ sortUser[j].getDomain()
									+ "','"
									+ sortUser[j].getId()
									+ "');\" title=\"Download full log\" class=\"btnDownloadLog\"></div>"
									+ "<div class='btnClear' domain_name='c-mg.com.vn' user_name='"
									+ sortUser[j].getId()
									+ "' onClick=\"javascript:showConfirmDialog('"
									+ sortUser[j].getUsername()
									+ "@"
									+ sortUser[j].getDomain()
									+ "','"
									+ sortUser[j].getId()
									+ "');\" title=\"Delete\"></div>");
				}
			}
		}
		return data;
	}

	public static GoogleAccount[] sortByname(GoogleAccount[] users) {
		GoogleAccount temp = null;
		for (int i = 1; i < users.length; i++) {
			int j;
			GoogleAccount val = users[i];
			for (j = i - 1; j > -1; j--) {
				temp = users[j];
				if (temp.compareByName(val) <= 0) {
					break;
				}
				users[j + 1] = temp;
			}
			users[j + 1] = val;
		}
		return users;
	}

	static Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(true);
		return option;

	}

	static void drawTable(GoogleAccount[] listUser) {
		myTable.draw(createData(listUser), option());
	}

	private SelectHandler createSelectHandler(final Table tbl) {
		return new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (listGoogleAcc != null && listGoogleAcc.length > 0) {
					JsArray<Selection> selections = tbl.getSelections();
					if (selections.length() == 1) {
						Selection selection = selections.get(0);
						if (selection.isRow()) {
							selected = listGoogleAcc[selection.getRow()];
							txtDomain.setText(selected.getDomain());
							txtUsername.setText(selected.getUsername());
							txtPassword.setText(selected.getPassword());
							txtConfirmPwd.setText(selected.getPassword());
						}
					}
				}
			}
		};
	}

	public static native void exportConfirmDialog() /*-{
													$wnd.showConfirmDialog =
													$entry(@cmg.org.monitor.module.client.GoogleManagement::showConfirmDialog(Ljava/lang/String;Ljava/lang/String;))
													}-*/;

	static void showConfirmDialog(final String code, final String id) {
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
		popupContent.setHTML("<h4>Do you want to delete Account " + code
				+ "?</h4>");
		flexHTML = new FlexTable();
		flexHTML.setWidget(0, 0, popupContent);
		flexHTML.setStyleName("table-popup");
		FlexTable table = new FlexTable();
		table.setCellPadding(10);
		table.setCellSpacing(10);
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
				clearAccount(id);
				dialogBox.hide();
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
}
