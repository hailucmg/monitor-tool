package cmg.org.monitor.module.server;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.dao.SystemAccountDAO;
import cmg.org.monitor.dao.impl.SystemAccountDaoImpl;
import cmg.org.monitor.entity.shared.SystemUser;
import cmg.org.monitor.util.shared.HTMLControl;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(LoginServlet.class
			.getCanonicalName());

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			validLogin(req, resp);
		} catch (IOException ex) {
			throw ex;
		}
	    
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		try {
			validLogin(req, resp);
		} catch (IOException ex) {
			throw ex;
		}
	}
	
	private void validLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		try {
			if (userService.isUserLoggedIn()) {
				if (userService.isUserAdmin()) {
					SystemAccountDAO accDao = new SystemAccountDaoImpl();
					List<SystemUser> list = null;
					try {
						list = accDao.listAllSystemUser(true);
					} catch (Exception e) {
						//
					}
					if (list != null && list.size() > 0) {
						resp.sendRedirect(resp.encodeRedirectURL(HTMLControl.HTML_INDEX_NAME));
					} else {
						resp.sendRedirect(resp.encodeRedirectURL(HTMLControl.HTML_GOOGLE_MANAGEMENT_NAME));
					}
				} else {
					resp.sendRedirect(resp.encodeRedirectURL(HTMLControl.HTML_INDEX_NAME));
				}
			} else {
			    resp.sendRedirect(resp.encodeRedirectURL(
			    		userService.createLoginURL(HTMLControl.HTML_INDEX_NAME, MonitorConstant.DOMAIN)));
			}
		} catch (IOException ex) {
			throw ex;
		}
		
	}
	
}
