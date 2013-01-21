/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.entity.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class ConnectionPool  implements IsSerializable {
	
	private String name;
	/**
	 *  the current number of objects that are active
	 */
	private int currentActive = -1;
	/**
	 * the maximum number of objects that can be borrowed from the pool
	 */
	private int maxActive = -1;
	
	private int minActive = -1;
	/**
	 * the minimum number of objects that will kept connected
	 */
	private int minIdle = -1;
	/**
	 *  the maximum number of objects that can sit idled in the pool
	 */
	private int maxIdle = -1;
	/**
	 *  the current number of objects that are idle
	 */
	private int currentIdle = -1;
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
	 * @return the currentActive 
	 */
	public int getCurrentActive() {
		return currentActive;
	}
	/** 
	 * @param currentActive the currentActive to set 
	 */
	
	public void setCurrentActive(int currentActive) {
		this.currentActive = currentActive;
	}
	/** 
	 * @return the maxActive 
	 */
	public int getMaxActive() {
		return maxActive;
	}
	/** 
	 * @param maxActive the maxActive to set 
	 */
	
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
	/** 
	 * @return the minIdle 
	 */
	public int getMinIdle() {
		return minIdle;
	}
	/** 
	 * @param minIdle the minIdle to set 
	 */
	
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
	/** 
	 * @return the maxIdle 
	 */
	public int getMaxIdle() {
		return maxIdle;
	}
	/** 
	 * @param maxIdle the maxIdle to set 
	 */
	
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
	/** 
	 * @return the currentIdle 
	 */
	public int getCurrentIdle() {
		return currentIdle;
	}
	/** 
	 * @param currentIdle the currentIdle to set 
	 */
	
	public void setCurrentIdle(int currentIdle) {
		this.currentIdle = currentIdle;
	}
	/** 
	 * @return the minActive 
	 */
	public int getMinActive() {
		return minActive;
	}
	/** 
	 * @param minActive the minActive to set 
	 */
	
	public void setMinActive(int minActive) {
		this.minActive = minActive;
	}
}
