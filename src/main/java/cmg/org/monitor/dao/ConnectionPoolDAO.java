/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.ConnectionPool;
import cmg.org.monitor.entity.shared.SystemMonitor;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface ConnectionPoolDAO {
	public void storePools(SystemMonitor sys, ArrayList connectionPools) throws Exception;
	
	public ArrayList<ConnectionPool> getPools(SystemMonitor sys) throws Exception;
}
