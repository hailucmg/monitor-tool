package cmg.org.monitor.module.client;

import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class UserRole extends AncestorEntryPoint {
    static private Table myTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_USER_ROLE) {
		    	UserRole.exportStaticMethod();
			myTable = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, myTable);
			initContent();
		}
	}

	public static native void exportStaticMethod() /*-{
	$wnd.updateUserRole =
	$entry(@cmg.org.monitor.module.client.UserRole::updateUserRole(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;))
	}-*/;
	
	static void updateUserRole(String username, String role, String b){
	    System.out.println(username);
	    System.out.println(role);
	    boolean isChecked = Boolean.parseBoolean(b);
	    System.out.println(isChecked);
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
					showMessage("Oops! Error.",
							HTMLControl.HTML_DASHBOARD_NAME,
							"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
						"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
			}
		});
	}

	private AbstractDataTable createData(UserMonitor[] listUser) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.STRING, "Administrator");
		data.addColumn(ColumnType.STRING, "User");
		data.addRows(listUser.length);

		UserMonitor[] sortUser = sortByname(listUser);
		for (int j = 0; j < sortUser.length; j++) {

			data.setValue(j, 0, sortUser[j].getId());
			data.setValue(j, 1, "<input class='ckUserRole' type='checkbox' name='user_role' role='Admin' username='"+sortUser[j].getId()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;'>");
			data.setValue(j, 2, "<input class='ckUserRole' type='checkbox' name='user_role' role='User' username='"+sortUser[j].getId()+"' style='display:block;margin-left:auto;margin-right:auto;border-color:green;'>");
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
