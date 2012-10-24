/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import cmg.org.monitor.memcache.Key;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.util.shared.MonitorConstant;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class DateTimeUtils {	
	
	public static String currentZone;
	
	public static Date convertDateWithZone(Date input) {
		if (currentZone == null) {
			 Object obj = MonitorMemcache.get(Key.create(Key.CURRENT_ZONE));
			 if (obj != null && obj instanceof String) {
				 currentZone = (String) obj; 
			 } else {
				 currentZone = MonitorConstant.DEFAULT_SYSTEM_TIME_ZONE;
			 }
		}
		DateTime temp = new DateTime(input);
		temp = temp.withZoneRetainFields(DateTimeZone.forID(currentZone));
		return temp.toDate();		
	}

}
