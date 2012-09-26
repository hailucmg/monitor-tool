package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.formatters.BarFormat;
import com.google.gwt.visualization.client.formatters.BarFormat.Color;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.Table.Options;

public class GroupManagement extends AncestorEntryPoint{
	FlexTable flexTable;
	
	protected void init() {
		if (currentPage == HTMLControl.PAGE_GROUP_MANAGEMENT) {
			monitorGwtSv.getDefaultContent(new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if (result != null) {
						addWidget(HTMLControl.ID_BODY_CONTENT, new HTML(result));
						setVisibleLoadingImage(false);
						setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
						
					} else {
						showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
								"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
						setVisibleLoadingImage(false);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					showMessage("Oops! Error.", HTMLControl.HTML_DASHBOARD_NAME,
							"Goto Dashboard. ", HTMLControl.RED_MESSAGE, true);
					setVisibleLoadingImage(false);
				}
			});

		}
	}

	
	
	
	
	
	
	/*
	 * Create options of table list system
	 */
	static Options createOptionsTableListSystem() {
		Options ops = Options.create();
		ops.setAllowHtml(true);
		ops.setShowRowNumber(true);
		return ops;
	}
	
	/*
	 * Create data table list system without value
	 */
	static AbstractDataTable createDataListSystem() {
		// create object data table
		DataTable dataListSystem = DataTable.create();
	/*	// add all columns
		dataListSystem.addColumn(ColumnType.STRING, "SID");
		dataListSystem.addColumn(ColumnType.STRING, "Group Name");
		dataListSystem.addColumn(ColumnType.STRING, "Description");
		dataListSystem.addColumn(ColumnType.STRING, "Delete");
		
		dataListSystem.addRows(result.length);
		for (int i = 0; i < result.length; i++) {
			dataListSystem.setValue(i, 1, result[i].getName());
			dataListSystem.setValue(i, 2, "<a href=\"" + result[i].getUrl()
					+ "\"" + " target=\"_blank\">" + result[i].getUrl()
					+ "</a>");
			dataListSystem.setValue(i,3,
							"<a onClick=\"javascript:showConfirmDialogBox('"+ result[i].getCode()
									+ "','"+ result[i].getId()
									+ "');\" title=\"Delete\" class=\"icon-2 info-tooltip\"></a>");
		}

		com.google.gwt.visualization.client.formatters.BarFormat.Options ops = com.google.gwt.visualization.client.formatters.BarFormat.Options
				.create();
		ops.setColorPositive(Color.RED);
		ops.setColorNegative(Color.RED);
		ops.setMax(100);
		ops.setMin(0);
		ops.setWidth(200);

		BarFormat bf = BarFormat.create(ops);
		bf.format(dataListSystem, 4);
		bf.format(dataListSystem, 5);*/
		return dataListSystem;

	}
	
}
