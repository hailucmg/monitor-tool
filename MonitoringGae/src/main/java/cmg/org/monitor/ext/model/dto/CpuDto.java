package cmg.org.monitor.ext.model.dto;


import java.io.Serializable;
import java.util.Date;

public class CpuDto implements Serializable
{
	/** Default UUID value */
	private static final long serialVersionUID = 1L;

	/** 
	 * This attribute maps to the column cpu_name in the cpu table.
	 */
	protected String cpuName;

	/** 
	 * This attribute represents whether the attribute cpuName has been modified since being read from the database.
	 */
	protected boolean cpuNameModified = false;

	/** 
	 * This attribute maps to the column cpu_usage in the cpu table.
	 */
	protected String cpuUsage;

	/** 
	 * This attribute represents whether the attribute cpuUsage has been modified since being read from the database.
	 */
	protected boolean cpuUsageModified = false;

	/** 
	 * This attribute maps to the column vendor in the cpu table.
	 */
	protected String vendor;

	/** 
	 * This attribute represents whether the attribute vendor has been modified since being read from the database.
	 */
	protected boolean vendorModified = false;

	/** 
	 * This attribute maps to the column model in the cpu table.
	 */
	protected String model;

	/** 
	 * This attribute represents whether the attribute model has been modified since being read from the database.
	 */
	protected boolean modelModified = false;

	/** 
	 * This attribute maps to the column total_cpu in the cpu table.
	 */
	protected String totalCpu;

	/** 
	 * This attribute represents whether the attribute totalCpu has been modified since being read from the database.
	 */
	protected boolean totalCpuModified = false;

	/** 
	 * This attribute maps to the column project_id in the cpu table.
	 */
	protected long projectId;

	/** 
	 * This attribute represents whether the attribute projectId has been modified since being read from the database.
	 */
	protected boolean projectIdModified = false;

	/** 
	 * This attribute maps to the column time_stamp in the cpu table.
	 */
	protected Date timeStamp;

	/** 
	 * This attribute represents whether the attribute timeStamp has been modified since being read from the database.
	 */
	protected boolean timeStampModified = false;

	/**
	 * Method 'Cpu'
	 * 
	 */
	public CpuDto()
	{
	}

	/**
	 * Method 'getCpuName'
	 * 
	 * @return String
	 */
	public String getCpuName()
	{
		return cpuName;
	}

	/**
	 * Method 'setCpuName'
	 * 
	 * @param cpuName
	 */
	public void setCpuName(String cpuName)
	{
		this.cpuName = cpuName;
		this.cpuNameModified = true;
	}

	/** 
	 * Sets the value of cpuNameModified
	 */
	public void setCpuNameModified(boolean cpuNameModified)
	{
		this.cpuNameModified = cpuNameModified;
	}

	/** 
	 * Gets the value of cpuNameModified
	 */
	public boolean isCpuNameModified()
	{
		return cpuNameModified;
	}

	/**
	 * Method 'getCpuUsage'
	 * 
	 * @return String
	 */
	public String getCpuUsage()
	{
		return cpuUsage;
	}

	/**
	 * Method 'setCpuUsage'
	 * 
	 * @param cpuUsage
	 */
	public void setCpuUsage(String cpuUsage)
	{
		this.cpuUsage = cpuUsage;
		this.cpuUsageModified = true;
	}

	/** 
	 * Sets the value of cpuUsageModified
	 */
	public void setCpuUsageModified(boolean cpuUsageModified)
	{
		this.cpuUsageModified = cpuUsageModified;
	}

	/** 
	 * Gets the value of cpuUsageModified
	 */
	public boolean isCpuUsageModified()
	{
		return cpuUsageModified;
	}

	/**
	 * Method 'getVendor'
	 * 
	 * @return String
	 */
	public String getVendor()
	{
		return vendor;
	}

	/**
	 * Method 'setVendor'
	 * 
	 * @param vendor
	 */
	public void setVendor(String vendor)
	{
		this.vendor = vendor;
		this.vendorModified = true;
	}

	/** 
	 * Sets the value of vendorModified
	 */
	public void setVendorModified(boolean vendorModified)
	{
		this.vendorModified = vendorModified;
	}

	/** 
	 * Gets the value of vendorModified
	 */
	public boolean isVendorModified()
	{
		return vendorModified;
	}

	/**
	 * Method 'getModel'
	 * 
	 * @return String
	 */
	public String getModel()
	{
		return model;
	}

	/**
	 * Method 'setModel'
	 * 
	 * @param model
	 */
	public void setModel(String model)
	{
		this.model = model;
		this.modelModified = true;
	}

	/** 
	 * Sets the value of modelModified
	 */
	public void setModelModified(boolean modelModified)
	{
		this.modelModified = modelModified;
	}

	/** 
	 * Gets the value of modelModified
	 */
	public boolean isModelModified()
	{
		return modelModified;
	}

	/**
	 * Method 'getTotalCpu'
	 * 
	 * @return String
	 */
	public String getTotalCpu()
	{
		return totalCpu;
	}

	/**
	 * Method 'setTotalCpu'
	 * 
	 * @param totalCpu
	 */
	public void setTotalCpu(String totalCpu)
	{
		this.totalCpu = totalCpu;
		this.totalCpuModified = true;
	}

	/** 
	 * Sets the value of totalCpuModified
	 */
	public void setTotalCpuModified(boolean totalCpuModified)
	{
		this.totalCpuModified = totalCpuModified;
	}

	/** 
	 * Gets the value of totalCpuModified
	 */
	public boolean isTotalCpuModified()
	{
		return totalCpuModified;
	}

	/**
	 * Method 'getProjectId'
	 * 
	 * @return long
	 */
	public long getProjectId()
	{
		return projectId;
	}

	/**
	 * Method 'setProjectId'
	 * 
	 * @param projectId
	 */
	public void setProjectId(long projectId)
	{
		this.projectId = projectId;
		this.projectIdModified = true;
	}

	/** 
	 * Sets the value of projectIdModified
	 */
	public void setProjectIdModified(boolean projectIdModified)
	{
		this.projectIdModified = projectIdModified;
	}

	/** 
	 * Gets the value of projectIdModified
	 */
	public boolean isProjectIdModified()
	{
		return projectIdModified;
	}

	/**
	 * Method 'getTimeStamp'
	 * 
	 * @return Date
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * Method 'setTimeStamp'
	 * 
	 * @param timeStamp
	 */
	public void setTimeStamp(Date timeStamp)
	{
		this.timeStamp = timeStamp;
		this.timeStampModified = true;
	}

	/** 
	 * Sets the value of timeStampModified
	 */
	public void setTimeStampModified(boolean timeStampModified)
	{
		this.timeStampModified = timeStampModified;
	}

	/** 
	 * Gets the value of timeStampModified
	 */
	public boolean isTimeStampModified()
	{
		return timeStampModified;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof CpuDto)) {
			return false;
		}
		
		final CpuDto _cast = (CpuDto) _other;
		if (cpuName == null ? _cast.cpuName != cpuName : !cpuName.equals( _cast.cpuName )) {
			return false;
		}
		
		if (cpuNameModified != _cast.cpuNameModified) {
			return false;
		}
		
		if (cpuUsage == null ? _cast.cpuUsage != cpuUsage : !cpuUsage.equals( _cast.cpuUsage )) {
			return false;
		}
		
		if (cpuUsageModified != _cast.cpuUsageModified) {
			return false;
		}
		
		if (vendor == null ? _cast.vendor != vendor : !vendor.equals( _cast.vendor )) {
			return false;
		}
		
		if (vendorModified != _cast.vendorModified) {
			return false;
		}
		
		if (model == null ? _cast.model != model : !model.equals( _cast.model )) {
			return false;
		}
		
		if (modelModified != _cast.modelModified) {
			return false;
		}
		
		if (totalCpu == null ? _cast.totalCpu != totalCpu : !totalCpu.equals( _cast.totalCpu )) {
			return false;
		}
		
		if (totalCpuModified != _cast.totalCpuModified) {
			return false;
		}
		
		if (projectId != _cast.projectId) {
			return false;
		}
		
		if (projectIdModified != _cast.projectIdModified) {
			return false;
		}
		
		if (timeStamp == null ? _cast.timeStamp != timeStamp : !timeStamp.equals( _cast.timeStamp )) {
			return false;
		}
		
		if (timeStampModified != _cast.timeStampModified) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		if (cpuName != null) {
			_hashCode = 29 * _hashCode + cpuName.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (cpuNameModified ? 1 : 0);
		if (cpuUsage != null) {
			_hashCode = 29 * _hashCode + cpuUsage.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (cpuUsageModified ? 1 : 0);
		if (vendor != null) {
			_hashCode = 29 * _hashCode + vendor.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (vendorModified ? 1 : 0);
		if (model != null) {
			_hashCode = 29 * _hashCode + model.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (modelModified ? 1 : 0);
		if (totalCpu != null) {
			_hashCode = 29 * _hashCode + totalCpu.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (totalCpuModified ? 1 : 0);
		_hashCode = 29 * _hashCode + (int) (projectId ^ (projectId >>> 32));
		_hashCode = 29 * _hashCode + (projectIdModified ? 1 : 0);
		if (timeStamp != null) {
			_hashCode = 29 * _hashCode + timeStamp.hashCode();
		}
		
		_hashCode = 29 * _hashCode + (timeStampModified ? 1 : 0);
		return _hashCode;
	}

	
	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "com.cmg.monitor.history.dto.Cpu: " );
		ret.append( "cpuName=" + cpuName );
		ret.append( ", cpuUsage=" + cpuUsage );
		ret.append( ", vendor=" + vendor );
		ret.append( ", model=" + model );
		ret.append( ", totalCpu=" + totalCpu );
		ret.append( ", projectId=" + projectId );
		ret.append( ", timeStamp=" + timeStamp );
		return ret.toString();
	}

}
