/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.model;

import java.util.Map;
import java.util.Set;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class MailContent {
	public static final String INVITE_USER = "invite-monitor-user";
	public static final String REQUEST_PERMISSION = "request-permission";
	
	private String subject;
	private String body;
	
	private Map<String, String> map;
	
	public MailContent() {
		
	}
	
	public MailContent(String subject, String body) {
		this.subject = subject;
		this.body = body;
	}
	
	public MailContent(String subject, String body, Map<String, String> map) {
		this.subject = subject;
		this.body = body;
		this.map = map;
	}
	
	public void init() {
		if (subject != null && subject.length() > 0
				&& body != null && body.length() > 0
				&& map != null && !map.isEmpty()) {
			Set<String> keys = map.keySet();
			if (keys.size() > 0) {
				for (String key : keys) {
					if (subject.contains("%(" + key + ")%")) {
						subject = subject.replaceAll("%(" + key + ")%", map.get(key));
					}
					if (body.contains("%(" + key + ")%")) {
						body = body.replace("%(" + key + ")%", map.get(key));
					}
				}
			}
		}
	}
	
	/** 
	 * @return the subject 
	 */
	public String getSubject() {
		return subject;
	}
	/** 
	 * @param subject the subject to set 
	 */
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/** 
	 * @return the body 
	 */
	public String getBody() {
		return body;
	}
	/** 
	 * @param body the body to set 
	 */
	
	public void setBody(String body) {
		this.body = body;
	}

	/** 
	 * @return the map 
	 */
	public Map<String, String> getMap() {
		return map;
	}

	/** 
	 * @param map the map to set 
	 */
	
	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	
}
