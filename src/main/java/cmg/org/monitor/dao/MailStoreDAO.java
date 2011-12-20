package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.MailStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MailStoreDto;
import cmg.org.monitor.ext.model.shared.SystemDto;

public interface MailStoreDAO {
	
	/**
	 * @param mailDTO
	 * @return
	 */
	public MailStoreMonitor addMail(MailStoreDto mailDTO, SystemDto entityDto);
	
	/**
	 * @param system
	 * @return
	 * @throws Exception
	 */
	public MailStoreMonitor listLastestMailStore(SystemMonitor system)
			throws Exception;
	
	
}
