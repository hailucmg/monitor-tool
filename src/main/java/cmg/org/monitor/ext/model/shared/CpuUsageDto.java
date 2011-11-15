package cmg.org.monitor.ext.model.shared;


import java.io.Serializable;

/**
 * Please enter a short description for this class.
 *
 * <p>Optionally, enter a longer description.</p>
 *
 * @author
 * @version
 */
public class CpuUsageDto implements Serializable {

    //~ Instance fields --------------------------------------------------------
	
	/** Default UUID value */
	private static final long serialVersionUID = 1L;

    /** This attribute maps to the column cpu_usage_id in the cpuusage table. */
    protected long cpuUsageId;

    /**
     * This attribute represents whether the primitive attribute cpuUsageId is
     * null.
     */
    protected boolean cpuUsageIdNull = true;

    /** This attribute maps to the column time_stamp in the cpuusage table. */
    protected String timeStamp;

    /** This attribute maps to the column cpu_usage in the cpuusage table. */
    protected double cpuUsage;

    /** This attribute maps to the column cpu_usage in the project_id table. */
    protected long project_id;


    /**
     * Method 'CpuUsageDto'
     */
    public CpuUsageDto() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Gets property.
     *
     * @return  the return value
     */
    public long getProject_id() {
        return project_id;
    }

    /**
     * Sets property.
     *
     * @param  project_id  the parameter
     */
    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    /**
     * Method 'getCpuUsageId'
     *
     * @return  long
     */
    public long getCpuUsageId() {
        return cpuUsageId;
    }

    /**
     * Method 'setCpuUsageId'
     *
     * @param  cpuUsageId
     */
    public void setCpuUsageId(long cpuUsageId) {
        this.cpuUsageId = cpuUsageId;
        this.cpuUsageIdNull = false;
    }

    /**
     * Method 'setCpuUsageIdNull'
     *
     * @param  value
     */
    public void setCpuUsageIdNull(boolean value) {
        this.cpuUsageIdNull = value;
    }

    /**
     * Method 'isCpuUsageIdNull'
     *
     * @return  boolean
     */
    public boolean isCpuUsageIdNull() {
        return cpuUsageIdNull;
    }

    /**
     * Method 'getTimeStamp'
     *
     * @return  String
     */
    public String getTimeStamp() {
        return timeStamp;
    }

    /**
     * Method 'setTimeStamp'
     *
     * @param  timeStamp
     */
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Method 'getCpuUsage'
     *
     * @return  double
     */
    public double getCpuUsage() {
        return cpuUsage;
    }

    /**
     * Method 'setCpuUsage'
     *
     * @param  cpuUsage
     */
    public void setCpuUsage(double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    /**
     * Method 'equals'
     *
     * @param   _other
     *
     * @return  boolean
     */
    public boolean equals(Object _other) {
        if (_other == null) {
            return false;
        }
        if (_other == this) {
            return true;
        }
        if (!(_other instanceof CpuUsageDto)) {
            return false;
        }
        final CpuUsageDto _cast = (CpuUsageDto) _other;
        if (cpuUsageId != _cast.cpuUsageId) {
            return false;
        }
        if (cpuUsageIdNull != _cast.cpuUsageIdNull) {
            return false;
        }
        if ((timeStamp == null) ? (_cast.timeStamp != timeStamp)
                                : (!timeStamp.equals(_cast.timeStamp))) {
            return false;
        }
        if (cpuUsage != _cast.cpuUsage) {
            return false;
        }

        return true;
    }

    /**
     * Method 'hashCode'
     *
     * @return  int
     */
    public int hashCode() {
        int _hashCode = 0;
        _hashCode = (29 * _hashCode) + (int) (cpuUsageId ^ (cpuUsageId >>> 32));
        _hashCode = (29 * _hashCode) + (cpuUsageIdNull ? 1 : 0);
        if (timeStamp != null) {
            _hashCode = (29 * _hashCode) + timeStamp.hashCode();
        }
        long temp_cpuUsage = Double.doubleToLongBits(cpuUsage);
        _hashCode = (29 * _hashCode)
            + (int) (temp_cpuUsage ^ (temp_cpuUsage >>> 32));

        return _hashCode;
    }

    /**
     * Method 'toString'
     *
     * @return  String
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("org.cmg.monitor.dto.CpuUsageDto: ");
        ret.append("cpuUsageId=" + cpuUsageId);
        ret.append(", timeStamp=" + timeStamp);
        ret.append(", cpuUsage=" + cpuUsage);

        return ret.toString();
    }
}
