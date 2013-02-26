package cmg.org.monitor.util.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SecurityUtil {
	private static final Logger logger = Logger.getLogger(SecurityUtil.class
			.getCanonicalName());

	private static final String SECR_KEY = "cmg-monitor-tool-secr-key";

	public static String encrypt(String input) {
		try {
			DesEncrypter de = new DesEncrypter(SECR_KEY);
			return de.encrypt(input);
		} catch (Exception ex) {
			logger.log(Level.WARNING,
					"Error when ecrypt. Message: " + ex.getMessage());
		}
		return "";

	}

	public static String decrypt(String input) {
		try {
			DesEncrypter de = new DesEncrypter(SECR_KEY);
			return de.decrypt(input);
		} catch (Exception ex) {
			logger.log(Level.WARNING,
					"Error when decrypt. Message: " + ex.getMessage());
		}
		return "";
	}
	
	public static void main(String[] args) {
		String password = "test password";
		String enPass = SecurityUtil.encrypt(password);
		System.out.println(enPass);
		String dePass = SecurityUtil.decrypt(enPass);
		System.out.println(dePass);
	}
}
