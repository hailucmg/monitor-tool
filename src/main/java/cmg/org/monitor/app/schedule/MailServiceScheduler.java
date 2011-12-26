package cmg.org.monitor.app.schedule;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MailServiceScheduler extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1934785013663672044L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//TODO send mail ....
	}
}
