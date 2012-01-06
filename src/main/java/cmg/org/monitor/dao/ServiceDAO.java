package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;


/**
 * @author lamphan, honghai
 * @version 1.0
 */
public interface ServiceDAO {
	public void storeServices(SystemMonitor sys, ArrayList<ServiceMonitor> service);
	
	public ArrayList<ServiceMonitor> listService(SystemMonitor sys);
}
