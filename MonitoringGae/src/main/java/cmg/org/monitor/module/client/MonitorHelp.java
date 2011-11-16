package cmg.org.monitor.module.client;

import cmg.org.monitor.util.shared.HTMLControl;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

public class MonitorHelp extends AncestorEntryPoint {
	FlexTable flexTable;

	protected void init() {
		if (currentPage == HTMLControl.PAGE_HELP) {
			flexTable = new FlexTable();
			addWidget(HTMLControl.ID_BODY_CONTENT, flexTable);
			flexTable.setWidget(0, 0, new HTML(
					"<h3>Help-page is in progress</h3>"));
			setVisibleLoadingImage(false);
			setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
		}
	}

}
