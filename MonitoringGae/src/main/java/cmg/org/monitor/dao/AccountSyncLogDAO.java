/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.dao;

import java.util.List;

import cmg.org.monitor.entity.shared.AccountSyncLog;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface AccountSyncLogDAO {
	public boolean createLog(AccountSyncLog log) throws Exception;
	
	public AccountSyncLog getLastestLog(String adminAccount) throws Exception;
	
	public List<AccountSyncLog> listLog(String adminAccount, int len) throws Exception;
	
	public void initAccountSyncLogStore();
}
