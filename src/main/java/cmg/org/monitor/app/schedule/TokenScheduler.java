package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.GoogleAccount;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.services.GoogleAccountService;

public class TokenScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -441314521680111158L;

	private static final Logger logger = Logger.getLogger(TokenScheduler.class
			.getCanonicalName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		doSchedule();
	}

	void doSchedule() {
		try {
			long start = System.currentTimeMillis();
			logger.log(Level.INFO, MonitorUtil.parseTime(start, true)
					+ " -> START: Scheduled token begin ...");
			// BEGIN LOG
			
			UtilityDAO uDAO = new UtilityDaoImpl();
			uDAO.putTokenSite();
			
			SystemAccountDAO accountDao = new SystemAccountDaoImpl(); 
			List<GoogleAccount> listAcc = accountDao.listAllGoogleAccount();
			if (listAcc != null && listAcc.size() > 0) {
				for (GoogleAccount acc : listAcc) {
					acc.setToken("");
					GoogleAccountService service = new GoogleAccountService(acc);
					service.sync();
				}
			}
			// END LOG
			long end = System.currentTimeMillis();
			long time = end - start;
			logger.log(Level.INFO, MonitorUtil.parseTime(end, true)
					+ " -> END: Scheduled token. Time executed: " + time
					+ " ms");
			// END LOG
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.log(
					Level.SEVERE,
					" ->ERROR: When Scheduled token. Message: "
							+ ex.getMessage());
		}
	}

}
