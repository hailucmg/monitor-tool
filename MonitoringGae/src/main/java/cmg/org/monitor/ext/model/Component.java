/**
 * 
 */
package cmg.org.monitor.ext.model;

import cmg.org.monitor.common.Constant;

import java.io.Serializable;

/**
 * Please enter a short description for this class.
 *
 * <p>Optionally, enter a longer description.</p>
 *
 * @author   Binh Nguyen
 * @version  1.0.3 August 10, 2009
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
    private String projectId;


    /**
     * documents description.
     *
     * @return  the return value
     */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("ComponentId = " + componentId);
        sb.append(", Name = " + name);
        sb.append(", Error = " + error);
        sb.append(", Sysdate = " + sysDate);
        sb.append(", Description = " + description);
        sb.append(", Reference = " + reference);
        sb.append(", ProjectId = " + projectId);
        sb.append("]");

        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  discription  DOCUMENT ME!
     */
    public void setDiscription(String discription) {
        this.description = discription;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getReference() {
        return reference;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  reference  DOCUMENT ME!
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
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
     * @param  error  status DOCUMENT ME!
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getError() {
        return error;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  projectId  DOCUMENT ME!
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getComponentId() {
        return componentId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  componentId  DOCUMENT ME!
     */
    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSysDate() {
        return sysDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sysDate  DOCUMENT ME!
     */
    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }
}
