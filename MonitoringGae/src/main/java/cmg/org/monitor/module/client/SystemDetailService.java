package cmg.org.monitor.module.client;

import cmg.org.monitor.entity.shared.CpuMemory;
import cmg.org.monitor.entity.shared.SystemMonitor;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("systemdetail")
public interface SystemDetailService extends RemoteService {
	SystemMonitor getLastestDataMonitor(String sysID);
	
	boolean validSystemId(String sysID);
	
	CpuMemory[] listCpuMemoryHistory(String sysID);
}
