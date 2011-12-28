package cmg.org.monitor.module.client;

import java.util.ArrayList;

import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class UserManagement extends AncestorEntryPoint {
	static private Table myTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_USER_MANAGEMENT) {
			myTable = new Table();
			addWidget(HTMLControl.ID_BODY_CONTENT, myTable);
			initContent();
		}
	}

	private void initContent() {
		monitorGwtSv.listAllUsers(new AsyncCallback<ArrayList<UserMonitor>>() {

			@Override
			public void onSuccess(ArrayList<UserMonitor> result) {
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

	private AbstractDataTable createData(ArrayList<UserMonitor> listUser) {
		DataTable data = DataTable.create();
		data.addColumn(ColumnType.STRING, "Username");
		data.addColumn(ColumnType.STRING, "Group");
		data.addColumn(ColumnType.STRING, "Permission");
		data.addRows(listUser.size());

		ArrayList<UserMonitor> sortUser = sortByname(listUser);
		for (int j = 0; j < sortUser.size(); j++) {

			data.setValue(j, 0, sortUser.get(j).getId());
			data.setValue(j, 1, sortUser.get(j).getGroupsName());
			data.setValue(j, 2, sortUser.get(j).getRoleName());
		}
		return data;
	}

	public ArrayList<UserMonitor> sortByname(ArrayList<UserMonitor> users) {
		UserMonitor temp = null;
		for (int i = 1; i < users.size(); i++) {
			int j;
			UserMonitor val = users.get(i);
			for (j = i - 1; j > -1; j--) {
				temp = users.get(j);
				if (temp.compareByName(val) <= 0) {
					break;
				}
				users.set(j + 1, temp);
			}
			users.set(j + 1, val);
		}
		return users;
	}

	private Options option() {
		Options option = Options.create();
		option.setAllowHtml(true);
		option.setShowRowNumber(true);
		return option;

	}

	private void drawTable(ArrayList<UserMonitor> listUser) {
		myTable.draw(createData(listUser), option());

	}
}
