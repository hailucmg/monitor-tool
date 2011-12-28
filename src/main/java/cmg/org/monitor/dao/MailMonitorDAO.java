package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;

public interface MailMonitorDAO {
	public void putMailConfig(MailConfigMonitor mailConfig);
	
	public MailConfigMonitor getMailConfig(String maiId);
	
	public void putMailMonitor(MailMonitor mail);
	
	public MailMonitor getMailMonitor(String sender);
	
	public void clearMailStore(String sender);
}
