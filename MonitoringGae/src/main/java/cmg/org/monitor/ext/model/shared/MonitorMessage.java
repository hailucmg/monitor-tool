/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.model.shared;

import java.io.Serializable;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class MonitorMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6924779795316671355L;
	private String id;
	private String name;
	private String message;
	
	public MonitorMessage() {
		
	}
	
	public MonitorMessage(String message) {
		this.message = message;
	}
	/** 
	 * @return the id 
	 */
	public String getId() {
		return id;
	}
	/** 
	 * @param id the id to set 
	 */
	
	public void setId(String id) {
		this.id = id;
	}
	/** 
	 * @return the name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * @param name the name to set 
	 */
	
	public void setName(String name) {
		this.name = name;
	}
	/** 
	 * @return the message 
	 */
	public String getMessage() {
		return message;
	}
	/** 
	 * @param message the message to set 
	 */
	
	public void setMessage(String message) {
		this.message = message;
	}
	
}
