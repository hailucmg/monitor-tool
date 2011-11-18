package cmg.org.monitor.entity.shared;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import cmg.org.monitor.ext.model.shared.JVMMemoryDto;

@SuppressWarnings("serial")
@PersistenceCapable
public class JVMMemory implements Model {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
	private String encodedKey;

	@Persistent
	private double freeMemory;

	@Persistent
	private double totalMemory;
	
	@Persistent
	private double maxMemory;
	
	@Persistent
	private double usedMemory;
	
	@Persistent
	private SystemMonitor systemMonitor;


	/**
	 * Default constructor.<br>
	 */
	public JVMMemory() {

	}
	
	/**
	 * @param jvmDto
	 */
	public JVMMemory(JVMMemoryDto jvmDto) {

		this();
		this.setBasicInfo(jvmDto.getFreeMemory(), jvmDto.getTotalMemory(),
				jvmDto.getMaxMemory(), jvmDto.getUsedMemory()
				);
	}

	/**
	 * Method 'setBasicInfo' set basic class properties.<br>
	 * @param freeMemory
	 * @param totalMemory
	 * @param maxMemory
	 * @param usedMemory
	 */
	public void setBasicInfo(double freeMemory, double totalMemory, double maxMemory, 
			double usedMemory) {

		this.freeMemory = totalMemory;
		this.totalMemory = usedMemory;
		this.maxMemory = maxMemory;
		this.usedMemory = usedMemory;
	}
	
	/**
	 * Convert entity to data transfer object.
	 * 
	 * @return data transfer object type
	 */
	public JVMMemoryDto toDTO() {
		JVMMemoryDto jvmDTO = new JVMMemoryDto(this.getFreeMemory(), this.getTotalMemory(),
				this.getMaxMemory(), this.getUsedMemory());
		return jvmDTO;
	}

	
	/**
	 * update existing jvm object based on System DTO
	 * 
	 * @param JVM object
	 */
	public void updateFromDTO(JVMMemoryDto dto) {

		this.totalMemory = dto.getUsedMemory();
		this.usedMemory = dto.getUsedMemory();
		this.freeMemory = dto.getFreeMemory();
		this.maxMemory = dto.getMaxMemory();

	}
	
	@Override
	public String getId() {
		return encodedKey;
	}

	public double getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(double freeMemory) {
		this.freeMemory = freeMemory;
	}

	public double getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(double totalMemory) {
		this.totalMemory = totalMemory;
	}

	public double getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(double maxMemory) {
		this.maxMemory = maxMemory;
	}

	public double getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(double usedMemory) {
		this.usedMemory = usedMemory;
	}

	public SystemMonitor getSystemMonitor() {
		return systemMonitor;
	}

	public void setSystemMonitor(SystemMonitor systemMonitor) {
		this.systemMonitor = systemMonitor;
	}
	
	
	

}
