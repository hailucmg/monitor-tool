package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.PMF;

public class MailMonitorDaoImpl implements MailMonitorDAO {
	private static final Logger logger = Logger.getLogger(MailMonitorDaoImpl.class
			.getCanonicalName());

	@Override
	public MailConfigMonitor getMailConfig(String maild) {
		MailConfigMonitor mailConfig = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<MailConfigMonitor> listData = null;
		Query query = pm.newQuery(MailConfigMonitor.class);
		query.setFilter("mailId == strMailId");
		query.declareParameters("String strMailId");
		query.setRange(0, 1);
		try {
			pm.currentTransaction().begin();
			listData = (List<MailConfigMonitor>) query.execute(maild);
			if (listData.size() > 0) {
				mailConfig = listData.get(0);
			}
			pm.currentTransaction().commit();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, " -> ERROR get mail config. Message: " + ex.getMessage());
		} finally {
			query.closeAll();
			pm.close();
		}
		if (mailConfig == null) {
			mailConfig = new MailConfigMonitor();
			mailConfig.setMailId(maild);
		}
		return mailConfig;
	}

	@Override
	public void putMailMonitor(MailMonitor mail) {
		MonitorMemcache.put(Key.create(Key.MAIL_STORE, mail.getSender()), mail);
	}

	@Override
	public MailMonitor getMailMonitor(String sender) {
		MailMonitor mail = null;
		Object obj = MonitorMemcache.get(Key.create(Key.MAIL_STORE, sender));
		if (obj != null) {
			if (obj instanceof MailMonitor) {
				mail = (MailMonitor) obj;
			}
		}
		return mail;
	}

}
