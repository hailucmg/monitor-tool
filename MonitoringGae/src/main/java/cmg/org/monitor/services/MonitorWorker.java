package cmg.org.monitor.services;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.ext.model.FreeObject;
import cmg.org.monitor.ext.model.MemoryObject;
import cmg.org.monitor.ext.model.shared.CpuDto;

public class MonitorWorker {
	
	/** log instance for the class*/
	private static final Logger logger = Logger.getLogger(MonitorWorker.class
			.getCanonicalName());

	/**
	 * Gets property.
	 * 
	 * @param webContent
	 *            the parameter
	 * 
	 * @return the return value
	 */
	// public CPUObject getCPUMonitor(String webContent) {
	public CpuDto getCPUMonitor(String webContent) {
		CpuDto obj = null;
		// CPUObject obj = null;
		try {
			Pattern pattern = Pattern.compile(Constant.PATTERN_CPU_USAGE);
			Matcher matcher = pattern.matcher(webContent);

			// Finds value for CPU Usage
			String value = null;
			if (matcher.find()) {
				value = matcher.group(2).replace("%", "");
				obj = new CpuDto();
				obj.setUsedMemory(Double.parseDouble(value));
				// obj = new CPUObject();
				// obj.setUsedCPU(value);

			} // while

			// Clear controls
			pattern = null;
			matcher = null;

			// Finds value for CPU Vendor
			pattern = Pattern.compile(Constant.PATTERN_CPU_VENDOR);
			matcher = pattern.matcher(webContent);
			if (matcher.find()) {
				value = matcher.group(2);
				if (obj == null) {
					// obj = new CPUObject();
					obj = new CpuDto();
				}
				obj.setVendor(value);
				// obj.setVendor(value);
			} // while

			// Clear controls
			pattern = null;
			matcher = null;

			// Finds value for CPU Vendor
			pattern = Pattern.compile(Constant.PATTERN_CPU_MODEL);
			matcher = pattern.matcher(webContent);
			if (matcher.find()) {
				value = matcher.group(2);
				if (obj == null) {
					obj = new CpuDto();
					// obj = new CPUObject();
				}
				obj.setModel(value);
				// obj.setModel(value);
			} // while

			// Clear controls
			pattern = null;
			matcher = null;

			// Finds value for CPU Vendor
			pattern = Pattern.compile(Constant.PATTERN_CPU_TOTAL);
			matcher = pattern.matcher(webContent);
			if (matcher.find()) {
				value = matcher.group(2);
				if (obj == null) {
					obj = new CpuDto();
					// obj = new CPUObject();
				}
				obj.setTotalCpu(Integer.parseInt(value.trim()));
				// obj.setTotalCPUs(Integer.parseInt(value.trim()));
			} // while
		} catch (Exception ex) {
			logger.log(
					Level.SEVERE,
					"Failed to get CPU values from web page, error: "
							+ ex.getMessage());
		}

		return obj;
	}

	/**
	 * Gets property.
	 * 
	 * @param webContent
	 *            the parameter
	 * 
	 * @return the return value
	 */
	public MemoryObject getMemObjectMonitor(String webContent) {
		MemoryObject memObj = null;
		try {
			Pattern pattern = Pattern.compile(Constant.PATTERN_SYSTEM_MEMORY);
			Matcher matcher = pattern.matcher(webContent);

			// Finds value for System memory
			String value = null;
			List<String> list = new ArrayList<String>();
			while (matcher.find()) {
				value = matcher.group(2).replace("%", "");
				list.add(value);
			} // while
			FreeObject mem = null;
			FreeObject swap = null;
			if ((list != null) && (list.size() > 0)) {
				// Initializes Memory Object
				memObj = new MemoryObject();

				// Initializes MEM and assigns
				mem = new FreeObject();
				mem.setTypeName(list.get(0));
				mem.setTotal(list.get(1));
				mem.setUsed(list.get(2));
				mem.setFree(list.get(3));
				memObj.setMem(mem);

				// Initializes SWAP and assigns
				swap = new FreeObject();
				swap.setTypeName(list.get(4));
				swap.setTotal(list.get(5));
				swap.setUsed(list.get(6));
				swap.setFree(list.get(7));
				memObj.setSwap(swap);

				// RAM
				memObj.setRam(list.get(9));
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"Failed to get system memory values from web page, error: "
							+ ex.getMessage());
		}

		return memObj;
	}

}
