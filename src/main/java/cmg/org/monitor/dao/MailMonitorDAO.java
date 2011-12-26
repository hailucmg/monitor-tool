package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;

public interface MailMonitorDAO {
	public MailConfigMonitor getMailConfig(String maild);
	
	public void putMailMonitor(MailMonitor mail);
	
	public MailMonitor getMailMonitor(String sender);
}
