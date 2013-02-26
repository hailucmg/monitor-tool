package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;


public interface JvmDAO {
	/**
	 * @param jvm DOCUMENT ME!
	 */
	public void storeJvm(SystemMonitor sys, JvmMonitor jvm);
	
	/**
	 * @param jvm DOCUMENT ME!
	 */
	public JvmMonitor getJvm(SystemMonitor sys);
	
	public ArrayList<JvmMonitor> listJvm(SystemMonitor sys);
	
}
