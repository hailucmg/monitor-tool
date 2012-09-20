package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gson.Gson;

import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.services.PMF;

public class MailMonitorDaoImpl implements MailMonitorDAO {
	private static final Logger logger = Logger
			.getLogger(MailMonitorDaoImpl.class.getCanonicalName());

	@Override
	public MailConfigMonitor getMailConfig(String mailId) {
	
		MailConfigMonitor mailConfig = null;
		// get from memcache
		Object obj = MonitorMemcache.get(Key.create(Key.MAIL_CONFIG_STORE,
				mailId));
		if (obj != null && obj instanceof MailConfigMonitor) {
			Gson gson = new Gson();
			try {
				mailConfig = gson.fromJson(String.valueOf(obj), MailConfigMonitor.class); 
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
		//get from JDO
		if (obj == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			List<MailConfigMonitor> listData = null;
			Query query = pm.newQuery(MailConfigMonitor.class);
			query.setFilter("mailId == strMailId");
			query.declareParameters("String strMailId");
			query.setRange(0, 1);
			try {
				pm.currentTransaction().begin();
				listData = (List<MailConfigMonitor>) query.execute(mailId);
				if (listData.size() > 0) {
					mailConfig = listData.get(0);
				}
				pm.currentTransaction().commit();
			} catch (Exception ex) {
				logger.log(Level.SEVERE, " -> ERROR get mail config. Message: "
						+ ex.getMessage());
				pm.currentTransaction().rollback();
			} finally {
				query.closeAll();
				pm.close();
			}
		}
		
		if (mailConfig == null) {
			mailConfig = new MailConfigMonitor();
			mailConfig.setMailId(mailId);
		}
	
		return mailConfig;
	}

	@Override
	public void putMailMonitor(MailMonitor mail) {
		if (mail != null) {
		
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(Key.create(Key.MAIL_STORE, mail.getSender()),
					gson.toJson(mail));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}
		}
	}

	@Override
	public MailMonitor getMailMonitor(String sender) {
		MailMonitor mail = null;
		Object obj = MonitorMemcache.get(Key.create(Key.MAIL_STORE, sender));
		if (obj != null && obj instanceof String) {
			Gson gson = new Gson();
			try {
				mail = gson.fromJson(String.valueOf(obj), MailMonitor.class);
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error: " + ex.getMessage());
			}
		}
		return mail;
	}

	@Override
	public void putMailConfig(MailConfigMonitor mailConfig) {
		if (mailConfig != null) {
		
			// put to memcache
			Gson gson = new Gson();
			try {
			MonitorMemcache.put(
					Key.create(Key.MAIL_CONFIG_STORE,
							mailConfig.getMailId(true)), gson.toJson(mailConfig));
			} catch (Exception ex) {
				logger.log(Level.WARNING, "Error:" + ex.getMessage());
			}

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				pm.currentTransaction().begin();
				pm.makePersistent(mailConfig);
				pm.currentTransaction().commit();
			} catch (Exception ex) {
				logger.log(Level.SEVERE, " -> ERROR add mail config. Message: "
						+ ex.getMessage());
				pm.currentTransaction().rollback();
			} finally {
				pm.close();
			}
		}
	}

	public void clearMailStore(String sender) {
	
		MonitorMemcache.delete(Key.create(Key.MAIL_STORE, sender));
		try {
		MonitorMemcache.put(Key.create(Key.MAIL_STORE, sender), null);
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error:" + ex.getMessage());
		}
	}

}
