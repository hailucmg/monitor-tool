package cmg.org.monitor.dao;

import java.util.Date;

import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.SystemMonitor;

public interface FileSystemDAO {
	void addFileSystem(SystemMonitor system, FileSystem fileSystem);
	
	FileSystem[] listLastestFileSystem(SystemMonitor system) throws Exception;
	
	Date getLastestTimeStamp(SystemMonitor system);
}
