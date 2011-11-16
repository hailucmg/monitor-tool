package cmg.org.monitor.module.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import cmg.org.monitor.util.shared.HTMLControl;


public class MonitorAbout extends AncestorEntryPoint {

	FlexTable flexTable;
	protected void init() {
		
		if (currentPage == HTMLControl.PAGE_ABOUT) {	
			flexTable = new FlexTable();
			
			addWidget(HTMLControl.ID_BODY_CONTENT, flexTable);
			flexTable.setWidget(0, 0, new HTML(
					"<h3>About-page is in progress</h3>"));
			setVisibleLoadingImage(false);
			setVisibleWidget(HTMLControl.ID_BODY_CONTENT, true);
		}
	}
}
