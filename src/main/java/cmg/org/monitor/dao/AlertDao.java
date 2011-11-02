package cmg.org.monitor.dao;

import cmg.org.monitor.ext.model.dto.AlertDto;
import cmg.org.monitor.ext.model.dto.SystemEntityDto;

/**
 * @author lamphan
 * @version 1.0
 */
public interface AlertDao {
	
	/**
	 * @param alertDTO
	 * @return
	 */
	public AlertDto updateAlert(AlertDto alertDTO, SystemEntityDto entityDto);
	
	/**
	 * @param id
	 * @return
	 */
	public AlertDto getAlert(String id) ;
	
	
}
