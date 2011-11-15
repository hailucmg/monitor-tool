package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.shared.ServiceDto;
import cmg.org.monitor.ext.model.shared.SystemDto;

/**
 * @author lamphan, honghai
 * @version 1.0
 */
public interface ServiceMonitorDAO {
	
	/**
	 * Add object to entity layer.<br>
	 * 1. Check object's id is exist or not
	 * 2. In case of object existence, do update else addition
	 * @param serDto service data transfer object.
	 * @param sysDto system data transfer object.
	 * 
	 * @return ServiceDto service transfer object.
	 */
	public ServiceDto updateServiceEntity( ServiceDto serDto, SystemDto sysDto) throws MonitorException;
	
	void addServiceMonitor(SystemMonitor system, ServiceMonitor serviceMonitor);
	
	ServiceMonitor[] listLastestService(SystemMonitor system) throws Exception;
	
	boolean checkStatusAllService(SystemMonitor system) throws Exception ;
}
