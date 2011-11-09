package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.FileSystem;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.FileSystemDto;
import cmg.org.monitor.ext.model.shared.SystemDto;

public interface FileSystemDAO {
	
	/**
	 * @param fileSysDTO
	 * @param sysDto
	 * @return
	 */
	public FileSystemDto updateFileSystem(FileSystemDto fileSysDTO, SystemDto sysDto) ;
	
	/**
	 * @param id
	 * @return
	 */
	public SystemDto getSystem(String id) ;
	
	void addFileSystem(SystemMonitor system, FileSystem fileSystem);
	
	FileSystem[] listLastestFileSystem(SystemMonitor system) throws Exception;
}
