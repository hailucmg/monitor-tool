/**
 * 
 */
package cmg.org.monitor.ext.model;

import java.io.Serializable;

import cmg.org.monitor.common.Constant;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Binh Nguyen
 * @version 1.0.3 August 10, 2009
 */
public class Component implements Serializable {

	/** Default UUID value */
	static final long serialVersionUID = 1436363632L;

	/** Default UUID value */
	private String componentId;

	/** Default UUID value */
	private String name;

	/** Default UUID value */
	private String error;

	/** Default UUID value */
	private String sysDate;

	/** Default UUID value */
	private String description;

	/** Default UUID value */
	private String reference;

	/** Default UUID value */
	private String systemId;

	/** Default UUID value */
	private String ping;
	
	/** Default UUID value */
	private String valueComponent;

	/**
	 * documents description.
	 * 
	 * @return the return value
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("ComponentId = " + componentId);
		sb.append(", Name = " + name);
		sb.append(", Error = " + error);
		sb.append(", Sysdate = " + sysDate);
		sb.append(", Description = " + description);
		sb.append(", Reference = " + reference);
		sb.append(", ProjectId = " + systemId);
		sb.append("]");

		return sb.toString();
	}

	
	
	
	public String getValueComponent() {
		return valueComponent;
	}




	public void setValueComponent(String valueComponent) {
		this.valueComponent = valueComponent;
	}

	/**
	 * @return
	 */
	public String getPing() {
		return ping;
	}

	/**
	 * @param ping
	 */
	public void setPing(String ping) {
		this.ping = ping;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param discription
	 *            DOCUMENT ME!
	 */
	public void setDiscription(String discription) {
		this.description = discription;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param reference
	 *            DOCUMENT ME!
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean hasError() {
		if ((sysDate == null)
				|| (sysDate.equals(Constant.SYS_DATE_NULL_FORMAT))) {
			return true;
		}

		return false;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param error
	 *            status DOCUMENT ME!
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getError() {
		return error;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getSystemId() {
		return systemId;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param systemId
	 *            DOCUMENT ME!
	 */
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getComponentId() {
		return componentId;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param componentId
	 *            DOCUMENT ME!
	 */
	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getSysDate() {
		return sysDate;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param sysDate
	 *            DOCUMENT ME!
	 */
	public void setSysDate(String sysDate) {
		this.sysDate = sysDate;
	}
}
