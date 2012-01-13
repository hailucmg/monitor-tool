package cmg.org.monitor.util.shared;


public class Utility {

	/** Represent standard date format */
	public static final String STANDARD_FORMAT_DATE = "yyyy-mm-dd";

	/** Represent common date format */
	public static final String COMMON_FORMAT_DATE = "dd/mm/yyyy";

	/** symbol value */
	public static final char UPPER_LINE = '-';

	/** Forward slash */
	public static final char SLASH = '/';

	
	
	public static long parseFileSystemValue(long value) {
		return value * 1024 * 1024 * 1024;
	}

	public static long parseMemoryValue(long value) {
		return value * 1024;
	}

}
