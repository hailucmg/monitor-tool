/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cmg.org.monitor.entity.shared.SystemMonitor;
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

		return temp.withZone(DateTimeZone.forID(currentZone)).toDate();
	}

	public static String[] listAllTimeZone() {
		Object obj = MonitorMemcache.get(Key
				.create(Key.LIST_AVAILABLE_TIME_ZONE));
		List<String> list = null;
		Gson gson = new Gson();
		if (obj != null && obj instanceof String) {			
			Type type = new TypeToken<Collection<String>>() {
			}.getType();
			list = gson.fromJson(String.valueOf(obj), type);
		} else {
			Set<String> listTemp = DateTimeZone.getAvailableIDs();			
			if (listTemp != null && listTemp.size() > 0) {
				list = new ArrayList<String>();
				for (String t : listTemp) {
					list.add(t);
				}
			}
			MonitorMemcache.put(Key
					.create(Key.LIST_AVAILABLE_TIME_ZONE), gson.toJson(list));
		}
		if (list != null && !list.isEmpty()) {
			String[] temp = new String[list.size()];
			list.toArray(temp);
			return temp;
		}
		return null;
	}

}
