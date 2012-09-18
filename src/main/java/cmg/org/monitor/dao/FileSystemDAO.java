package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

/**
 * @author HongHai
 *
 */
public interface FileSystemDAO {
	/**
	 * @param fileSystems
	 */
	public void storeFileSystems(SystemMonitor sys, ArrayList<FileSystemMonitor> fileSystems);
	
	public ArrayList<FileSystemMonitor> listFileSystems(SystemMonitor sys);
}
