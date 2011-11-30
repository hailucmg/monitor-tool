package cmg.org.monitor.memcache;

import java.util.ArrayList;

import cmg.org.monitor.memcache.shared.AlertMonitorDto;
import cmg.org.monitor.memcache.shared.CpuMemoryDto;
import cmg.org.monitor.memcache.shared.FileSystemDto;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;

public class SystemMonitorStore {	
	/**
	 *  Store information of CPU & Memory
	 */
	private CpuMemoryDto cpuMem;
	
	/**
	 *  Store information of JVM
	 */
	private JvmDto jvm;
	
	/**
	 *  Store information of all File system
	 */
	private ArrayList<FileSystemDto> fileSysList = new ArrayList<FileSystemDto>();
	
	/**
	 *  Store information of alert
	 */
	private AlertMonitorDto alert;
	
	/**
	 *  Store information of all service
	 */
	private ArrayList<ServiceMonitorDto> serviceMonitorList = new ArrayList<ServiceMonitorDto>();
	
	private SystemMonitorDto sysMonitor;

	private SystemMonitorStore() {
		// unused
	}
	
	protected static SystemMonitorStore create() {
		return new SystemMonitorStore();
	}

	protected CpuMemoryDto getCpuMem() {
		return cpuMem;
	}

	public void setCpuMem(CpuMemoryDto cpuMem) {
		this.cpuMem = cpuMem;
	}

	protected JvmDto getJvm() {
		return jvm;
	}

	public void setJvm(JvmDto jvm) {
		this.jvm = jvm;
	}

	protected ArrayList<FileSystemDto> getFileSysList() {
		return fileSysList;
	}

	public void setFileSysList(ArrayList<FileSystemDto> fileSysList) {
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

	public void setServiceMonitorList(ArrayList<ServiceMonitorDto> serviceMonitorList) {
		this.serviceMonitorList = serviceMonitorList;
	}

	public SystemMonitorDto getSysMonitor() {
		return sysMonitor;
	}

	public void setSysMonitor(SystemMonitorDto sysMonitor) {
		this.sysMonitor = sysMonitor;
	}
	
}
