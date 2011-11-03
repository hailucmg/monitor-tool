package cmg.org.monitor.dao;

import cmg.org.monitor.ext.model.dto.AlertDto;
import cmg.org.monitor.ext.model.dto.SystemDto;

/**
 * @author lamphan
 * @version 1.0
 */
public interface AlertDao {
	
	/**
	 * @param alertDTO
	 * @return
	 */
	public AlertDto updateAlert(AlertDto alertDTO, SystemDto entityDto);
	
	/**
	 * @param id
	 * @return
	 */
	public AlertDto getAlert(String id) ;
	
	
}
