/*
 * Copyright (c) CMG Ltd All rights reserved.

 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import cmg.org.monitor.entity.shared.LinkDefaultMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

/**
 * DOCME *
 * 
 * @author ISTC *
 * @version .Revision: # .Date:Sep 25, 2012*
 */
public class LinkService {
    private static final Logger logger = Logger.getLogger(LinkService.class.getCanonicalName());

    PersistenceManager pm;

    void initPersistence() {
	if (pm == null || pm.isClosed()) {
	    pm = PMF.get().getPersistenceManager();
	}
    }

    public boolean updateLink(LinkDefaultMonitor link) throws Exception {
	initPersistence();
	boolean check = false;
	try {
	    pm.currentTransaction().begin();
	    pm.makePersistent(link);
	    pm.currentTransaction().commit();
	    check = true;
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, " ERROR when update system JDO. Message: " + ex.getMessage());
	    pm.currentTransaction().rollback();
	    throw ex;
	} finally {
	    pm.close();
	}
	return check;
    }

    public LinkDefaultMonitor getLink() throws Exception {
	initPersistence();
	List<LinkDefaultMonitor> link = null;
	Query query = pm.newQuery(LinkDefaultMonitor.class);
	try {
	    pm.currentTransaction().begin();
	    link = (List<LinkDefaultMonitor>) query.execute();
	    pm.currentTransaction().commit();
	    if (!link.isEmpty()) {
		return link.get(0);
	    } else {
		return null;
	    }
	} catch (Exception ex) {
	    logger.log(Level.SEVERE, " ERROR when list systems JDO. Message: " + ex.getMessage());
	    pm.currentTransaction().rollback();
	    throw ex;
	} finally {
	    query.closeAll();
	    pm.close();
	}
    }
}
