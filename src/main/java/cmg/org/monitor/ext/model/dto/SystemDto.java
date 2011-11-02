/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cmg.org.monitor.ext.model.Component;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author
 * @version
 */
public class SystemDto implements Serializable {

	/** Default UUID value */
	static final long serialVersionUID = 18547457474L;

	private long systemId;
	private String name;
	private String description;
	private String reference;
	private boolean status;
	private String url;
	private String ip;
	private boolean active;
	private long nodeId;
	private List<Component> components = new ArrayList<Component>();

	// ~ Methods
	// ----------------------------------------------------------------

	/**
	 * documents description.
	 * 
	 * @return the return value
	 */
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(" ");
		ret.append("systemId=" + systemId);
		ret.append(", projectName=" + name);
		ret.append(", discription=" + description);
		ret.append(", ip=" + ip);
		ret.append(", reference=" + reference);
		ret.append(", status=" + status);
		ret.append(", url=" + url);
		ret.append(", active=" + active);
		ret.append(", nodeId=" + nodeId);

		return ret.toString();
	}

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public List<Component> getComponents() {
		return components;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param components
	 *            DOCUMENT ME!
	 */
	public void setComponents(List<Component> components) {
		this.components = components;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param comp
	 *            DOCUMENT ME!
	 */
	public void addComp(Component comp) {
		if (components != null) {
			components.add(comp);
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getErrors() {
		int errors = 0;
		for (int i = 0; i < components.size(); i++) {
			Component comp = components.get(i);
			if (comp != null) {
				if (comp.hasError()) {
					// Means has error
					errors++;
				} // if
			} // if
		} // for

		return errors;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param comp
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean contains(Component comp) {
		boolean contains = false;
		for (Component c : this.components) {
			if (c.getComponentId() == comp.getComponentId()) {
				contains = true;

				break;
			}
		}

		return contains;
	}
}