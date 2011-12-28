package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.MailConfigMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.GroupMonitor;
import cmg.org.monitor.ext.model.shared.UserMonitor;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.email.MailService;

public class MailServiceScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1934785013663672044L;

	private static final Logger logger = Logger
			.getLogger(MailServiceScheduler.class.getCanonicalName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doSchedule();
	}

	public  void doSchedule() {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
				+ " -> START: Scheduled send alert mail ...");
		// BEGIN LOG

		SystemDAO sysDAO = new SystemDaoImpl();
		AlertDao alertDAO = new AlertDaoImpl();
		ArrayList<SystemMonitor> systems = sysDAO
				.listSystemsFromMemcache(false);
		MailService mailService = new MailService();
		MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
		UtilityDAO utilDAO = new UtilityDaoImpl();
		if (systems != null && systems.size() > 0) {
			ArrayList<UserMonitor> listUsers = utilDAO.listAllUsers();
			for (int i = 0; i < listUsers.size(); i++) {
				UserMonitor user = listUsers.get(i);
				ArrayList<GroupMonitor> groups = user.getGroups();
				for (int j = 0; j < groups.size(); j++) {
					for (int k = 0; k < systems.size(); k++) {
						SystemMonitor sys = systems.get(k);
						String groupName = sys.getGroupEmail();
						if (groupName.equals(groups.get(j).getName())) {
							user.addSystem(sys);
						}
					}
				}
			}
			
			for (int i = 0; i < listUsers.size(); i++) {
				UserMonitor user = listUsers.get(i);
				ArrayList<SystemMonitor> allSystem = listUsers.get(i).getSystems();
				if(allSystem!=null){
					for(int j = 0; j<allSystem.size();j++){
						AlertStoreMonitor alertstore = alertDAO.getLastestAlertStore(allSystem.get(j));
						user.addAlertStore(alertstore);
					}
				}
			}
			
			if(listUsers!=null && listUsers.size() > 0){
				for(int i= 0;i< listUsers.size();i++){
					UserMonitor user = listUsers.get(i);
					if(user.getStores()!=null && user.getStores().size() > 0){
						MailConfigMonitor config = mailDAO.getMailConfig(user.getId());
						String content;
						try {
							content = mailService.createMailContent(user.getStores());
							mailService.sendMail("myMail coming", content, config);
							
						} catch (Exception e) {
							e.printStackTrace();
							logger.log(Level.INFO, "Can not send mail" + e.getMessage().toString());
						}
						
					}
					
				}
				
			}

		} else {
			logger.log(Level.INFO, "NO SYSTEM FOUND");
		}

		/*
		 * mailDAO.getMailConfig(maiId); mailService.sendMail(subject, content,
		 * mailConfig);
		 */

		// END LOG
		long end = System.currentTimeMillis();
		long time = end - start;
		logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
				+ " -> END: Scheduled send alert mail. Time executed: " + time
				+ " ms");
		// END LOG

	}
}
