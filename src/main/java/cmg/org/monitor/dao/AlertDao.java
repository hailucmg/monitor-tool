package cmg.org.monitor.dao;

import java.util.ArrayList;

import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;

/**
 * @author lamphan
 * @version 1.0
 */
public interface AlertDao {

	public void storeAlert(SystemMonitor sys, AlertMonitor alert);

	public ArrayList<AlertStoreMonitor> listAlertStore(String sysId);

	public AlertStoreMonitor getLastestAlertStore(SystemMonitor sys);

	public void clearTempStore(SystemMonitor sys);

	public void putAlertStore(AlertStoreMonitor store);
}