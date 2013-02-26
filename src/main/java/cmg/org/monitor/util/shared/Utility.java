/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.util.shared;


/**
 * The Class Utility.
 */
public class Utility {

	/** Represent standard date format. */
	public static final String STANDARD_FORMAT_DATE = "yyyy-mm-dd";

	/** Represent common date format. */
	public static final String COMMON_FORMAT_DATE = "dd/mm/yyyy";

	/** symbol value. */
	public static final char UPPER_LINE = '-';

	/** Forward slash. */
	public static final char SLASH = '/';

	
	
	/**
	 * Parses the file system value.
	 *
	 * @param value the value
	 * @return the long
	 */
	public static long parseFileSystemValue(long value) {
		return value * 1024 * 1024 * 1024;
	}

	/**
	 * Parses the memory value.
	 *
	 * @param value the value
	 * @return the long
	 */
	public static long parseMemoryValue(long value) {
		return value * 1024;
	}
	
	/**
	 * Gets the integer value.
	 *
	 * @param input the input
	 * @param def the default value if input is null or empty
	 * @return the integer value
	 */
	public static int getIntegerValue(Integer input, int def) {		
		return (input == null) ? def : input;
	}
	
	

}
