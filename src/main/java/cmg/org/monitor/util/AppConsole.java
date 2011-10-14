package cmg.org.monitor.util;

import cmg.org.monitor.entity.SystemMonitor;
import cmg.org.monitor.services.SystemService;

public class AppConsole {
	public static void main (String args[]) {
		SystemService systemService = new SystemService();
		SystemMonitor system = new SystemMonitor();
		systemService.addSystemMonitor(system);
	}
}
