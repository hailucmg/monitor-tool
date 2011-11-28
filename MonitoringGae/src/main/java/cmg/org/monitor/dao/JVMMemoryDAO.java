package cmg.org.monitor.dao;

import cmg.org.monitor.entity.shared.JVMMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;

public interface JVMMemoryDAO {

	JVMMemory getLastestJvm(SystemMonitor system);
}
