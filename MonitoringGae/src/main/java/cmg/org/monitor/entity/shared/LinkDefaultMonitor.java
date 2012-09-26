
/*
 * Copyright (c) CMG Ltd All rights reserved.

 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.entity.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * DOCME *
 * 
 * @author ISTC *
 * @version .Revision: # .Date:Sep 25, 2012*
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class LinkDefaultMonitor implements Serializable{

    /** * */
    private static final long serialVersionUID = 1L;
    
    public static final String DEFAULT_LABEL = "Monitor Alert";

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String id;

	@Persistent
	private String linkContent;
	
	public LinkDefaultMonitor(){
	    
	}
	
	public String getId() {
	    return id;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public String getLinkContent() {
	    return linkContent;
	}

	public void setLinkContent(String linkContent) {
	    this.linkContent = linkContent;
	}

}
