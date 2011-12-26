package cmg.org.monitor.app.schedule;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AlertMailHandlerServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7599950554394002744L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException {
		//TODO Receive alert mail configuration ...
	}
}
