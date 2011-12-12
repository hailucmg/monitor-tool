package cmg.org.monitor.common;

/**
 * Short Description.
 *
 * <p>Long description</p>
 *
 * @author   $author$
 * @version  $Revision: 1.5 $, $Date: 2005/02/16 17:44:47 $
 */
public interface Constant {

    //~ Static fields/initializers ---------------------------------------------
	
	public static String DATE_EMAIL_FORMAT = "E, dd MMM yyyy HH:mm:ss Z";
	
	public static String PERCENTAGE_SYMBOL = "%";
	
	/** User input protocol . */
	public static String USER_PROTOCOL = "SMTP";
	
	/** Error description. */
	public static String ERROR = "ERROR";
	
    /** Encoding iso 8859_1. */
    public static String ENCODING_ISO_8859_1 = "ISO-8859-1";

    /** The null format used in date time. */
    public static String SYS_DATE_NULL_FORMAT = "0000-00-00 00:00:00.0";

    /** A string that descibe the none error. */
    public static String COMPONENT_NONE_STRING = "None";

    /** the color that indicates the component error. */
    public static String COLOR_ON_ERROR = "#FF0000";

    /** the color that indicates the component running. */
    public static String COLOR_ON_SUCESS = "#FFFFFF";

    /** The service name of node used in RMI. */
    public static String SERVICE_NODE_NAME = "nodeService";

    /** The service name of project used in RMI. */
    public static String SERVICE_PROJECT_NAME = "projectService";

    /** The service name of component used in RMI. */
    public static String SERVICE_COMPONENT_NAME = "componentService";

    /** The service name of action used in RMI. */
    public static String SERVICE_ACTION_NAME = "actionService";

    /** The service name of user used in RMI. */
    public static String SERVICE_USER_NAME = "userManager";

    /** The service name of Alert level used in RMI. */
    public static String SERVICE_ALER_LEVEL_NAME = "alertLevelService";

    /** The service name of mail used in RMI. */
    public static String SERVICE_MAIL_NAME = "mailService";

    /** The service name of group used in RMI. */
    public static String SERVICE_GROUP_NAME = "groupService";

    /** The service name of history used in RMI. */
    public static String SERVICE_HISTORY_NAME = "historyService";

    /** The unique service name of RMI. */
    public static String SERVICE_MONITOR_NAME = "monitorService";

    /** The alert level name that describe the running status of a component. */
    public static String ALERT_LEVEL_RUNNING = "RUNNING";

    /** The alert level name that describe the warning status of a component. */
    public static String ALERT_LEVEL_WARNING = "WARNING";

    /**
     * The alert level name that describe the critical status of a component.
     */
    public static String ALERT_LEVEL_CRITICAL = "CRITICAL";
    
    public static String BLANK = "";
    
    // Values for Pattern
    /** The string describe the pattern of ping time. */
    public static String PATTERN_PING_TIME = "[0-9]*\\d";

    /** The string describe the pattern of a component. */
    public static String PATTERN_COMPONENT =
        "(<td(|class='value')+>)([^<>]*)(</td>)";

    /** The string describe the pattern of a component. */
    public static String PATTERN_HREF = "([\\s]*href=[\"']#([^<>]*)[\"'])";
    
    public static String PATTERN_HREF_A_NAME =
            "(<a[\\s]*name=[\"']([^<>]*)[\"'][\\s]*/>)";
    
    public static String PATTERN_HTML =
            "<html>";
    
    /** The pattern for CPU usage value. */
    public static String PATTERN_CPU_USAGE = "(CPU usage:)(\\s\\d*[.][0-9][%])";

    /** The pattern for CPU Vendor value. */
    public static String PATTERN_CPU_VENDOR = "(CPU Vendor:)(\\s.*[^,\\s])";

    /** The pattern for CPU Model value. */
    public static String PATTERN_CPU_MODEL = "(CPU Model:)(\\s.*[^,\\s])";

    /** The pattern for Total CPUs value. */
    public static String PATTERN_CPU_TOTAL = "(Total CPUs:)(\\s\\d)";

    /** The pattern for Total CPUs value. */
    public static String PATTERN_FILESYSTEM = "(<td id=\"fs\">)([^<>]*)(</td>)";

    /** The pattern for System memory value. */
    public static String PATTERN_SYSTEM_MEMORY =
        "(<td id=\"mem\">)([^<>]*)(</td>)";

    /** The value level will be updated into database for history of CPU. */
    public static int CPU_LEVEL_HISTORY_UPDATE = 90;

    /** The value level will be updated into database for jvm . */
    public static int LEVEL_JVM_PERCENTAGE = 90;
    
    /** The value level will be updated into database for history of DF. */
    public static double DF_LEVEL_HISTORY_UPDATE = 95;
    
    /** The value level will be updated into database for history of DF. */
    public static double PING_LEVEL_RESPONSE = 500;

    /** The value level will be updated into database for history of RAM. */
    public static int RAM_LEVEL_HISTORY_UPDATE = 5;

    /** The time in second to check the CPUUsage table and delete record. */
    public static int TIME_IN_SECOND_CLEAR_CPU_HISTORY = 2592000;

    /** The string describe the pattern of pair name/value of memory. */
    public static String PATTERN_MEMORY_FULL =
        "((<p>)((.)*(m|M)emory(|Used))([' ']|&nbsp;)+([0-9]*)(</p>))";

    /** The string describe the pattern of name of memory values. */
    public static String PATTERN_MEMORY_KEY_VALUE =
        "((freeMemory|totalMemory|maxMemory|memoryUsed)((&nbsp;|\\s)*)([0-9]+))";

    /** The string describe the pattern of name of JVM memory values. */
    public static String PATTERN_MEMORY_JVM_KEY_VALUE =
    		"((Free Memory|Total Memory|Max Memory|Memory Used)((&nbsp;|\\s)*)([0-9]+))";
    
    /** The string describe the pattern of values of memory values. */
    public static String PATTERN_MEMORY_VALUE = "([0-9]+)";

    /** The number that decides how many numbers following the dot. */
    public static int NUMBER_ROUND_PLACE = 4;

    /** The string describe the pattern of pair name/value of memory. */
    public static String PATTERN_MEMORY_TOTAL =
        "(([0-9]*)(of)+([0-9]*)(MB used))";
    
    /**
     * The number of days that decides how many days the history's records be
     * remained.
     */
    public static int NUMBER_DAY_TO_DELETE = 30;
}
