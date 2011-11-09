package cmg.org.monitor.app.schedule;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.services.MonitorService;

/**
 * @author Lamphan
 * @version 1.0
 */
public class GlobalScheduler extends HttpServlet {

	/** Default UUID value */
	private static final long serialVersionUID = -5005043235328590690L;

	/** Count value */
	public static long boCounter = 0;

	/** The log of application */
	private static final Logger logger = Logger.getLogger(GlobalScheduler.class
			.getName());

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {

		try {
			doSchedule();
		} catch (MonitorException me) {
			logger.info("System cannot perform alert jobs successfully ");
		}
	}

	/**
	 * Main schedule method.<br>
	 * The method is executed by cron job task.
	 */
	public void doSchedule() throws MonitorException {
		MonitorService monitorService = null;
		try {

			long start = System.currentTimeMillis();
			if (logger.isLoggable(Level.CONFIG)) {
				logger.log(Level.ALL, "Start scheduled monitoring ...");
			} // if
			monitorService = new MonitorService();

			// Initial monitor scheduled
			monitorService.monitor();
			if (logger.isLoggable(Level.CONFIG)) {
				logger.log(Level.ALL, "Scheduled monitoring completed!");
			} // if
			long end = System.currentTimeMillis();
			long time = end - start;
			boCounter++;
			logger.info("Time executed: " + time + " ms" + ", email task: " // EmailTimely.emailCounter
					+ " times, business object: " + boCounter + " times");
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getLocalizedMessage());
		}

	}

}
