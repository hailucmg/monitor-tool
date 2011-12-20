package cmg.org.monitor.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.dao.MailStoreDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.entity.shared.MailStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MailStoreDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.util.shared.PMF;

public class MailStoreDaoJDO  implements MailStoreDAO {
	private static final Logger logger = Logger.getLogger(MailStoreDaoJDO.class
			.getName());
	private static SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();
	
	public MailStoreMonitor addMail(MailStoreDto mailDTO, SystemDto sysDto) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		MailStoreMonitor mailEntity = null;
		SystemMonitor existSystemEntity = null;
		
		try {

			// Begin a jdo transation
			pm.currentTransaction().begin();
			existSystemEntity = systemDao.getSystembyID(sysDto.getId());
			
			// Check a system existence
			if (existSystemEntity != null) {
				mailEntity = new MailStoreMonitor(mailDTO);
				existSystemEntity.addMail(mailEntity);
				existSystemEntity.setStatus(sysDto.getSystemStatus());
				pm.makePersistent(existSystemEntity);
			}
			// Do commit a transaction
			pm.currentTransaction().commit();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.fillInStackTrace().getMessage());
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);

		} finally {
			pm.close();
		}
		return mailEntity;
	}
	
	@Override
	public MailStoreMonitor listLastestMailStore(SystemMonitor system)
			throws Exception {
		MailStoreMonitor mail = new MailStoreMonitor();
		List<MailStoreMonitor> list = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(MailStoreMonitor.class);
		query.setOrdering("timeStamp desc");
		try {
			list = (List<MailStoreMonitor>)query.execute(system);
			if (list.size() > 0)
			mail = list.get(0);
		} catch (Exception ex) {
			throw ex;
		} finally {
			query.closeAll();
			pm.close();
		}
		return mail;
	}
}
