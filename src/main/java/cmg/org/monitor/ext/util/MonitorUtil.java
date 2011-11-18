package cmg.org.monitor.ext.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cmg.org.monitor.common.Constant;

/**
 * Please enter a short description for this class.
 *
 * <p>Optionally, enter a longer description.</p>
 *
 * @author Lamphan
 * @version 1.0
 */
public class MonitorUtil {
	
	private static final Logger logger = Logger.getLogger(MonitorUtil.class.getCanonicalName());
	
	
	/**
     * Returns a map that contains a pair feeMemory, totalMemory, ... and their
     * values
     *
     * @param   inputStr  the string that contains the pair key/value
     *
     * @return  a map that contains the pair key/value
     */
    public static Map<String, Long> getMemoryJVM(String inputStr) {
        Map<String, Long> map = new HashMap<String, Long>(10);
        if (inputStr != null) {
            Pattern pattern = Pattern.compile(
                    Constant.PATTERN_MEMORY_JVM_KEY_VALUE);
            Matcher matcher = pattern.matcher(inputStr);
            while (matcher.find()) {
                String key = matcher.group(2);
                String value = matcher.group(5);
                if ((key != null) && (value != null)) {
                    long lValue = -1;
                    try {
                        lValue = Long.parseLong(value);
                    } catch (Exception e) {
                        lValue = -1;
                    } // try-catch
                    map.put(key, new Long(lValue));
                }
            } // if
        } // if

        return map;
    }
	
	/**
     * Get interval value of ping command
     *
     * @param   inputStr  value pattern DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static long getPingTime(String inputStr) {
        long temp = -1;
        if ((inputStr == null) || (inputStr.equals(""))) {
            temp = -1;
        }
        Pattern pattern = Pattern.compile(Constant.PATTERN_PING_TIME);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.find()) {
            String tempStr = matcher.group();
            if (tempStr != null) {
                try {
                    temp = Long.parseLong(tempStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    temp = -1;
                }
            }
        } // if

        return temp;
    }

    /**
     * Returns a map that contains a pair feeMemory, totalMemory, ... and their
     * values
     *
     * @param   inputStr  the string that contains the pair key/value
     *
     * @return  a map that contains the pair key/value
     */
    public static Map<String, Long> getMemoryMap(String inputStr) {
        Map<String, Long> map = new HashMap<String, Long>(10);
        if (inputStr != null) {
            Pattern pattern = Pattern.compile(
                    Constant.PATTERN_MEMORY_KEY_VALUE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.find()) {
                String key = matcher.group(2);
                
                String value = matcher.group(5);
                if ((key != null) && (value != null)) {
                    long lValue = -1;
                    try {
                        lValue = Long.parseLong(value);
                    } catch (Exception e) {
                        lValue = -1;
                    } // try-catch
                    map.put(key, new Long(lValue));
                }
            } // if
        } // if

        return map;
    }
    
	/**
     * Gets property.
     *
     * @return  the return value
     */
    public static String getErrorContent() {
        String s = "<html>"
            + "<head>"
            + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
            + "<title>CMG Email Monitor</title>"
            + "</head><body>ERROR</body></html>";

        return s;
    }
    
    
    public static String getIpFromUrl(String url) {
    	try {
	    	InetAddress addr = InetAddress.getByName ( "microsoft.com" );
	        return addr.getHostAddress();
    	} catch(UnknownHostException uhe) {
    		uhe.printStackTrace();
    		logger.info("unknow host exception occurrence");
    		return null;
    	}
        
    }
    
}
