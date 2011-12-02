package cmg.org.monitor.memcache;

import java.util.ArrayList;

import cmg.org.monitor.memcache.shared.AlertMonitorDto;
import cmg.org.monitor.memcache.shared.CpuDTO;
import cmg.org.monitor.memcache.shared.FileSystemCacheDto;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.memcache.shared.MemoryDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

public class SystemMonitorStore {

	/**
	 * Store information of CPU & Memory
	 */
	private CpuDTO cpu;

	/**
	 * Store information of JVM
	 */
	private JvmDto jvm;

	/**
	 * Store information of all File system
	 */
	private ArrayList<MemoryDto> memory = new ArrayList<MemoryDto>();

	/**
	 * Store information of all File system
	 */
	private ArrayList<FileSystemCacheDto> fileSysList = new ArrayList<FileSystemCacheDto>();

	/**
	 * Store information of alert
	 */
	private AlertMonitorDto alert;

	/**
	 * Store information of all service
	 */
	private ArrayList<ServiceMonitorDto> serviceMonitorList = new ArrayList<ServiceMonitorDto>();

	private SystemMonitorDto sysMonitor;

	private SystemMonitorStore() {
		// unused
	}

	public CpuDTO getCpu() {
		return cpu;
	}

	public void setCpu(CpuDTO cpu) {
		this.cpu = cpu;
	}

	public ArrayList<MemoryDto> getMemory() {
		return memory;
	}

	public void setMemory(ArrayList<MemoryDto> memory) {
		this.memory = memory;
	}

	protected static SystemMonitorStore create() {
		return new SystemMonitorStore();
	}

	protected JvmDto getJvm() {
		return jvm;
	}

	public void setJvm(JvmDto jvm) {
		this.jvm = jvm;
	}

	protected ArrayList<FileSystemCacheDto> getFileSysList() {
		return fileSysList;
	}

	public void setFileSysList(ArrayList<FileSystemCacheDto> fileSysList) {
		this.fileSysList = fileSysList;
	}

	protected AlertMonitorDto getAlert() {
		return alert;
	}

	public void setAlert(AlertMonitorDto alert) {
		this.alert = alert;
	}

	protected ArrayList<ServiceMonitorDto> getServiceMonitorList() {
		return serviceMonitorList;
	}

	public void setServiceMonitorList(
			ArrayList<ServiceMonitorDto> serviceMonitorList) {
		this.serviceMonitorList = serviceMonitorList;
	}

	public SystemMonitorDto getSysMonitor() {
		return sysMonitor;
	}

	public void setSysMonitor(SystemMonitorDto sysMonitor) {
		this.sysMonitor = sysMonitor;
	}

}
