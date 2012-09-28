package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class GoogleManagement extends AncestorEntryPoint {
    FlexTable flexTable;
    Table myTable;
    FlexTable totalTable;

    Label lbDomain;
    Label lbUsername;
    Label lbPassword;
    TextBox txtDomain;
    TextBox txtUsername;
    PasswordTextBox txtPassword;
    Button btnAddAccount;
    Button btnSaveAccount;
    Button btnClearAccount;
    static TextArea txtLog;

    DisclosurePanel advancedDisclosure;
    static GoogleAccount adminAcc;
    
    protected void init() {
	if (currentPage == HTMLControl.PAGE_GOOGLE_MANAGEMENT) {
	    GoogleManagement.exportStaticMethod();
	    flexTable = new FlexTable();
	    
	    totalTable = new FlexTable();
	    txtLog = new TextArea();
	    txtLog.setWidth("1185px");
	    txtLog.setHeight("300px");
	    txtLog.setEnabled(false);
	    advancedDisclosure = new DisclosurePanel("View Log");
	    advancedDisclosure.setAnimationEnabled(true);
	    advancedDisclosure.setContent(txtLog);
	    myTable = new Table();
	    myTable.setWidth("1185px");
	    initInterface();
	    totalTable.setWidget(0, 0, flexTable);
	    totalTable.setWidget(2, 0, myTable);
	    totalTable.setWidget(3, 0, advancedDisclosure);
	    addWidget(HTMLControl.ID_BODY_CONTENT, totalTable);
	    initContent();
	}
    }

    public static native void exportStaticMethod() /*-{
	$wnd.syncAccount =
	$entry(@cmg.org.monitor.module.client.GoogleManagement::syncAccount(Ljava/lang/String;Ljava/lang/String;))
	}-*/;

    static void syncAccount(String domain, String username){
	System.out.println(domain + "|" +username);
	adminAcc = new GoogleAccount();
	adminAcc.setDomain(MonitorConstant.DOMAIN);
	adminAcc.setUsername(MonitorConstant.ADMIN_EMAIL_ID);
	adminAcc.setPassword("ABC");
	try {
	    	monitorGwtSv.syncAccount(adminAcc, new AsyncCallback<String>() {

		    @Override
		    public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub 
			System.out.println("bull shit man");
		    }

		    @Override
		    public void onSuccess(String result) {
			// TODO Auto-generated method stub 
			System.out.println("yow man");
			txtLog.setText(result);
		    }
	    	    
		});
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

    }

    void initInterface() {
	lbDomain = new Label("Domain name:");
	lbUsername = new Label("User name:");
	lbPassword = new Label("Password:");

	btnAddAccount = new Button("Add account");
	btnSaveAccount = new Button("Update account");
	// btnClearAccount = new Button("Clear account");

	txtDomain = new TextBox();
	txtUsername = new TextBox();
	txtPassword = new PasswordTextBox();

	flexTable.setWidget(0, 0, lbDomain);
	flexTable.setWidget(0, 1, txtDomain);
	flexTable.setWidget(1, 0, lbUsername);
	flexTable.setWidget(1, 1, txtUsername);
	flexTable.setWidget(2, 0, lbPassword);
	flexTable.setWidget(2, 1, txtPassword);
	flexTable.setWidget(3, 0, btnAddAccount);
	flexTable.setWidget(3, 1, btnSaveAccount);
	// flexTable.setWidget(3, 2, btnClearAccount);
	flexTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
	flexTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_MIDDLE);
	flexTable.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_MIDDLE);
	
	btnAddAccount.addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		GoogleAccount acc = new GoogleAccount();
		acc.setDomain(txtDomain.getText());
		acc.setUsername(txtUsername.getText());
		acc.setPassword(txtPassword.getText());
		monitorGwtSv.addGoogleAccount(acc, new AsyncCallback<Boolean>() {

		    @Override
		    public void onFailure(Throwable caught) {
			
			// TODO Auto-generated method stub 
			showMessage("Added Failed.", "", "", HTMLControl.RED_MESSAGE, true);
		    }

		    @Override
		    public void onSuccess(Boolean result) {
			
			// TODO Auto-generated method stub 
			showMessage("Added Successfully", "", "", HTMLControl.BLUE_MESSAGE, true);
			initContent();
		    }
		});
	    }
	    
	    
	});
    }

    private void initContent(){
	monitorGwtSv.listAllGoogleAcc(new AsyncCallback<GoogleAccount[]>() {

	    @Override
	    public void onFailure(Throwable caught) {
		caught.printStackTrace();
		showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME, "Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
	    }

	    @Override
	    public void onSuccess(GoogleAccount[] result) {
		// TODO Auto-generated method stub 
		if (result != null) {
		    setVisibleLoadingImage(false);
		    setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
		    drawTable(result);
		} else {
		    showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME, "Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
		}
	    }
	    
	});
	
    }

    private AbstractDataTable createData(GoogleAccount[] listUser) {
	DataTable data = DataTable.create();
	data.addColumn(ColumnType.STRING, "Domain name");
	data.addColumn(ColumnType.STRING, "Username");
	data.addColumn(ColumnType.STRING, "");
	data.addRows(listUser.length);

	GoogleAccount[] sortUser = sortByname(listUser);
	for (int j = 0; j < sortUser.length; j++) {

	    data.setValue(j, 0, sortUser[j].getDomain());
	    data.setValue(j, 1, sortUser[j].getUsername());
	    data.setValue(j, 2, "<input type='button' class='SyncAcc' id='btnSync' value='Sync account' domain_name='c-mg.com.vn' user_name='"
		    + sortUser[j].getId()+ "' pwd='"+sortUser[j].getPassword()+"' onClick=\"javascript:syncAccount('abc','abc');\">"  
		    + "<input type='button' class='ClearAcc' id='btnClear' value='Clear account' domain_name='c-mg.com.vn' user_name='"
		    + sortUser[j].getId() + "'>");
	}
	return data;
    }

    public GoogleAccount[] sortByname(GoogleAccount[] users) {
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

    private Options option() {
	Options option = Options.create();
	option.setAllowHtml(true);
	option.setShowRowNumber(true);
	return option;

    }

    private void drawTable(GoogleAccount[] listUser) {
	myTable.draw(createData(listUser), option());
    }
}
