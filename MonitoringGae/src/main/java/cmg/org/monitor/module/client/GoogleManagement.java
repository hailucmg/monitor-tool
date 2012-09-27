package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
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
    TextBox txtPassword;
    Button btnAddAccount;
    Button btnSaveAccount;
    Button btnClearAccount;
    TextArea txtLog;

    DisclosurePanel advancedDisclosure;

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
	    totalTable.setWidget(1, 0, myTable);
	    totalTable.setWidget(2, 0, advancedDisclosure);
	    addWidget(HTMLControl.ID_BODY_CONTENT, totalTable);
	    initContent();
	}
    }

    public static native void exportStaticMethod() /*-{
	$wnd.updateUserRole =
	$entry(@cmg.org.monitor.module.client.GoogleManagement::syncAccount(Ljava/lang/String;Ljava/lang/String;))
	}-*/;

    static void syncAccount(String domain, String username) {
	System.out.println(domain);
	System.out.println(username);
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
	txtPassword = new TextBox();

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
    }

    private void initContent() {
	monitorGwtSv.listAllUsers(new AsyncCallback<UserMonitor[]>() {

	    @Override
	    public void onSuccess(UserMonitor[] result) {
		if (result != null) {
		    setVisibleLoadingImage(false);
		    setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
		    drawTable(result);
		} else {
		    showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME, "Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
		}
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME, "Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
	    }
	});
    }

    private AbstractDataTable createData(UserMonitor[] listUser) {
	DataTable data = DataTable.create();
	data.addColumn(ColumnType.STRING, "Domain name");
	data.addColumn(ColumnType.STRING, "Username");
	data.addColumn(ColumnType.STRING, "Password");
	data.addRows(listUser.length);

	UserMonitor[] sortUser = sortByname(listUser);
	for (int j = 0; j < sortUser.length; j++) {

	    data.setValue(j, 0, "c-mg.com.vn");
	    data.setValue(j, 1, sortUser[j].getId());
	    data.setValue(j, 2, "<input type='button' class='SyncAcc' id='btnSync' value='Sync account' domain_name='c-mg.com.vn' user_name='"
		    + sortUser[j].getId()
		    + "'>    <input type='button' class='ClearAcc' id='btnClear' value='Clear account' domain_name='c-mg.com.vn' user_name='"
		    + sortUser[j].getId() + "'>");
	}
	return data;
    }

    public UserMonitor[] sortByname(UserMonitor[] users) {
	UserMonitor temp = null;
	for (int i = 1; i < users.length; i++) {
	    int j;
	    UserMonitor val = users[i];
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

    private void drawTable(UserMonitor[] listUser) {
	myTable.draw(createData(listUser), option());
    }
}
