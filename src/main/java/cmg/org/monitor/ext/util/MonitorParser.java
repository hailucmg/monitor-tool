/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.ext.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.ConnectionPoolDAO;
import cmg.org.monitor.dao.CpuDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.JvmDAO;
import cmg.org.monitor.dao.MemoryDAO;
import cmg.org.monitor.dao.ServiceDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.ConnectionPoolDaoImpl;
import cmg.org.monitor.dao.impl.CpuDaoImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoImpl;
import cmg.org.monitor.dao.impl.JvmDaoImpl;
import cmg.org.monitor.dao.impl.MemoryDaoImpl;
import cmg.org.monitor.dao.impl.ServiceDaoImpl;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.ConnectionPool;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.Constant;

// TODO: Auto-generated Javadoc
/**
 * The Class MonitorParser.
 */
public class MonitorParser {

	/** The element component . */
	public static String NAME = "name";

	/** The element component . */
	public static String VALUE = "value";

	/** The element component . */
	public static String ERROR = "error";

	/** The none. */
	public static String NONE = "none";

	/** The element component . */
	public static String PING = "ping";

	/** The element component . */
	public static String FREE_MEM = "Free_Memory";

	/** The element component . */
	public static String TOTAL_MEM = "Total_Memory";

	/** The element component . */
	public static String MAX_MEM = "Max_Memory";

	/** The element component . */
	public static String USED_MEM = "Used_Memory";

	/** The element component . */
	public static String LDAP_ITEM = "ldap";

	/** The element component . */
	public static String DATABASE_ITEM = "database";
	
	public static String CONNECTION_POOL = "service";

	/** The element component . */
	public static String JVM_ITEM = "JVM";

	/** The element component . */
	public static String CPU_ITEM = "cpu";

	/** The element component . */
	public static String WILDCARD = "?";
	
	/**
	 *  the current number of objects that are active
	 */
	public static String CURRENT_ACTIVE = "current_active";
	/**
	 * the maximum number of objects that can be borrowed from the pool
	 */
	public static String MAX_ACTIVE = "max_active";
	
	public static String MIN_ACTIVE = "min_active";
	/**
	 * the minimum number of objects that will kept connected
	 */
	public static String MIN_IDLE = "min_idle";
	/**
	 *  the maximum number of objects that can sit idled in the pool
	 */
	public static String MAX_IDLE = "max_idle";
	/**
	 *  the current number of objects that are idle
	 */
	public static String CURRENT_IDLE = "current_idle";
	
	public static String POOL_NAME = "poolname";

	/** The array of component . */
	public static String[] COMPONENT_ITEMS = { LDAP_ITEM, DATABASE_ITEM };

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(MonitorParser.class
			.getCanonicalName());

	/**
	 * Check content type.
	 *
	 * @param content the content
	 * @return true, if successful
	 */
	private static boolean checkContentType(String content) {
		return (content.toLowerCase().contains("<cmg>") && content
				.toLowerCase().contains("</cmg>"));
	}

	/**
	 * Parses the data.
	 *
	 * @param content the content
	 * @param sys the sys
	 * @return the system monitor
	 */
	public static SystemMonitor parseData(String content, SystemMonitor sys) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parseTime(start, true) + sys.toString()
						+ " -> START: parse data ... ");
		// BEGIN LOG

		/* START GATHER INFORMATION */
		Date timeStamp = new Date(start);
		JvmMonitor jvm = null;
		CpuMonitor cpu = null;
		ArrayList<MemoryMonitor> memList = null;
		ArrayList<FileSystemMonitor> fileSysList = null;
		ArrayList<ServiceMonitor> serviceList = null;
		ArrayList<ConnectionPool> connectionPools = null;

		if (checkContentType(content)) {
			logger.log(Level.INFO, "Content type: XML");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			try {
				docBuilder = docBuilderFactory.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(content));
				Document doc = docBuilder.parse(is);
				// normalize text representation
				doc.getDocumentElement().normalize();
				jvm = getJVMComponent(doc);
				cpu = getOriginalCPU(doc);
				memList = getMemories(doc);
				fileSysList = getFileSystem(doc);
				serviceList = getServiceComponent(doc);
				connectionPools = getConnectionPools(doc);
			} catch (ParserConfigurationException e) {
				logger.log(
						Level.SEVERE,
						" -> ERROR: ParserConfigurationException "
								+ e.getMessage());
			} catch (SAXException e) {
				logger.log(Level.SEVERE,
						" -> ERROR: SAXException " + e.getMessage());
			} catch (IOException e) {
				logger.log(Level.SEVERE,
						" -> ERROR: IOException " + e.getMessage());
			}
		} else {
			logger.log(Level.INFO, "Content type: HTML");
			jvm = getJVMComponent(content);
			cpu = getOriginalCPU(content);
			memList = getMemories(content);
			fileSysList = getFileSystem(content);
			serviceList = getServiceComponent(content);
		}// if-else
		/* END GATHER INFORMATION */

		/* START CHECK & STORE INFORMATION */
		AlertDao alertDAO = new AlertDaoImpl();
		CpuDAO cpuDAO = new CpuDaoImpl();
		FileSystemDAO fileSysDAO = new FileSystemDaoImpl();
		JvmDAO jvmDAO = new JvmDaoImpl();
		MemoryDAO memDAO = new MemoryDaoImpl();
		ServiceDAO serviceDAO = new ServiceDaoImpl();
		ConnectionPoolDAO poolDAO = new ConnectionPoolDaoImpl();

		AlertMonitor alert = null;

		boolean checkCpu = true;
		boolean checkService = true;
		boolean checkJvm = true;
		boolean checkFileSys = true;
		boolean checkMem = true;
		boolean checkPools = true;
		
		boolean isDone = false;
		int lastestMemUsage = -1;
		int lastestCpuUsage = -1;

		if (cpu != null) {
			if (cpu.getCpuUsage() > Constant.CPU_LEVEL_HISTORY_UPDATE) {
				checkCpu = false;
				alert = new AlertMonitor(AlertMonitor.HIGH_USAGE_LEVEL_CPU,
						"High CPU usage", "Cpu usage is " + cpu.getCpuUsage()
								+ "%", timeStamp);
				alertDAO.storeAlert(sys, alert);
			}
			lastestCpuUsage = cpu.getCpuUsage();
		} else {
			cpu = new CpuMonitor();
		}
		cpu.setTimeStamp(timeStamp);
		// store CPU information
		cpuDAO.storeCpu(sys, cpu);

		if (jvm != null) {
			isDone = true;
			jvm.setTimeStamp(timeStamp);
			if (jvm.getPercentUsage() > Constant.LEVEL_JVM_PERCENTAGE) {
				checkJvm = false;
				alert = new AlertMonitor(AlertMonitor.HIGH_USAGE_LEVEL_JVM,
						"High Java Virtual Machine memory usage",
						"Java Virtual Machine memory usage is "
								+ jvm.getPercentUsage()
								+ "% ("
								+ MonitorUtil.convertMemoryToString(jvm
										.getUsedMemory())
								+ " of "
								+ MonitorUtil.convertMemoryToString(jvm
										.getTotalMemory()) + ")"

						, timeStamp);
				// store alert
				alertDAO.storeAlert(sys, alert);
			}// if
				// store JVM information
			jvmDAO.storeJvm(sys, jvm);
		}// if

		if (fileSysList != null) {
			for (FileSystemMonitor fileSys : fileSysList) {
				fileSys.setTimeStamp(timeStamp);
				if (fileSys.getPercentUsage() > Constant.DF_LEVEL_HISTORY_UPDATE) {

					alert = new AlertMonitor(AlertMonitor.HIGH_USAGE_LEVEL_JVM,
							"High File system memory usage", fileSys.getName()
									+ "("
									+ fileSys.getType()
									+ ")"
									+ "memory usage is "
									+ fileSys.getPercentUsage()
									+ "% ("
									+ MonitorUtil.convertMemoryToString(fileSys
											.getUsed())
									+ " of "
									+ MonitorUtil.convertMemoryToString(fileSys
											.getSize()) + ")", timeStamp);
					// store alert
					alertDAO.storeAlert(sys, alert);
					// change status
					checkFileSys = false;
				}// if
			}// for
			fileSysDAO.storeFileSystems(sys, fileSysList);
		}// if

		if (memList != null) {
			for (MemoryMonitor mem : memList) {
				if (mem.getType() == MemoryMonitor.MEM) {
					lastestMemUsage = mem.getPercentUsage();
				}
				mem.setTimeStamp(timeStamp);
				if (mem.getPercentUsage() > Constant.MEMORY_LEVEL_HISTORY_UPDATE) {
					alert = new AlertMonitor(
							AlertMonitor.HIGH_USAGE_LEVEL_MEMORY,
							"High memory usage",
							(mem.getType() == MemoryMonitor.SWAP ? "Swap"
									: "Ram")
									+ " memory usage is "
									+ mem.getPercentUsage()
									+ "% ("
									+ MonitorUtil.convertMemoryToString(mem
											.getUsedMemory())
									+ " of "
									+ MonitorUtil.convertMemoryToString(mem
											.getTotalMemory()) + ")", timeStamp);
					// store alert
					alertDAO.storeAlert(sys, alert);
					// change status
					checkMem = false;
				}

				// store Memory information
				memDAO.storeMemory(sys, mem);
			}// for
		} else {
			MemoryMonitor mem = new MemoryMonitor();
			mem.setType(MemoryMonitor.MEM);
			mem.setTimeStamp(timeStamp);
			memDAO.storeMemory(sys, mem);
			mem.setType(MemoryMonitor.SWAP);
			memDAO.storeMemory(sys, mem);
		}

		if (serviceList != null) {
			isDone = true;
			for (ServiceMonitor service : serviceList) {
				service.setTimeStamp(timeStamp);
				if (!service.getStatus()) {
					alert = new AlertMonitor(AlertMonitor.SERVICE_ERROR_STATUS,
							"Service error", (service.getName() == null ? "N/A"
									: service.getName())
									+ " have some errors. Description: "
									+ service.getDescription(), timeStamp);
					// store alert
					alertDAO.storeAlert(sys, alert);
					checkService = false;
				}// if
				if (service.getPing() > Constant.PING_LEVEL_RESPONSE) {
					alert = new AlertMonitor(
							AlertMonitor.SERVICE_HIGH_LEVEL_PING_TIME,
							"High service ping time",
							(service.getName() == null ? "N/A"
									: service.getName())
									+ " ping time is "
									+ service.getPing(), timeStamp);
					// store alert
					alertDAO.storeAlert(sys, alert);
					checkService = false;
				}// if

			}// for
			serviceDAO.storeServices(sys, serviceList);
		}// if
		
		
		if (connectionPools != null) {
			for (ConnectionPool pool : connectionPools) {
				if (pool.getCurrentActive() >= pool.getMaxActive()) {
					alert = new AlertMonitor(
							AlertMonitor.CONNECTION_POOL_IS_EXHAUSTED,
							"Connection pool is exhausted",
							(pool.getName() == null ? "N/A"
									: pool.getName())
									+ " is exhausted. Active connection: " + pool.getCurrentActive() + "/" + pool.getMaxActive(), timeStamp);
					// store alert
					alertDAO.storeAlert(sys, alert);
					checkPools = false;
				}
				
			}
			try {
				poolDAO.storePools(sys, connectionPools);
			} catch (Exception e) {
				
			}
		}
		
		sys.setStatus(isDone);
		sys.setLastestMemoryUsage(lastestMemUsage);
		sys.setLastestCpuUsage(lastestCpuUsage);
		sys.setHealthStatus(sys.getStatus() && sys.isActive() ? (checkPools && checkMem
				&& checkFileSys && checkService && checkCpu && checkJvm ? SystemMonitor.STATUS_SMILE
				: SystemMonitor.STATUS_BORED)
				: SystemMonitor.STATUS_DEAD);

		
		
		
		/* END CHECK & STORE INFORMATION */

		// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parseTime(end, true) + sys.toString()
						+ " -> END: parse data. Time executed: "
						+ (end - start) + " ms.");
		// END LOG
		return sys;
	}
	
	
	private static ArrayList<ConnectionPool> getConnectionPools(Document doc) {
		logger.log(Level.INFO, "START Connection Pool");
		ArrayList<ConnectionPool> list = null;
		NodeList elementList = doc.getElementsByTagName(CONNECTION_POOL);
		int totalElements = elementList.getLength();
		ConnectionPool pool= null;
		Element element = null;
		if (totalElements > 0) {
			if (list == null) {
				list = new ArrayList<ConnectionPool>();
			}
			for (int i = 0; i < totalElements; i++) {
				pool = new ConnectionPool();
				element = (Element) elementList.item(i);
				NodeList temp = element.getElementsByTagName(MAX_ACTIVE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " max active: " + data);						
					try {
						pool.setMaxActive(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				temp = element.getElementsByTagName(MIN_ACTIVE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " min active: " + data);						
					try {
						pool.setMinActive(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				temp = element.getElementsByTagName(CURRENT_ACTIVE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " current active: " + data);						
					try {
						pool.setCurrentActive(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				temp = element.getElementsByTagName(MAX_IDLE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " max idle: " + data);						
					try {
						pool.setMaxIdle(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				temp = element.getElementsByTagName(MIN_IDLE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " min idle: " + data);						
					try {
						pool.setMinIdle(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				temp = element.getElementsByTagName(CURRENT_IDLE);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " current idle: " + data);						
					try {
						pool.setCurrentIdle(Integer.parseInt(MonitorUtil
								.extractDigit(data)));
					} catch (Exception ex) {

					}						
				}// if
				
				
				temp = element.getElementsByTagName(POOL_NAME);
				if (temp != null && temp.getLength() > 0) {
					String data = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " pool name: " + data);						
					try {
						pool.setName(data);
					} catch (Exception ex) {

					}						
				}// if
				list.add(pool);
			}
		}
		return list;
	}

	/**
	 * Get every element of database and ldap tag 1. Parse the xml content. 2.
	 * Parse by database tag 3. Parse it by ldap tag
	 *
	 * @param doc the doc
	 * @return components list of object
	 */
	private static ArrayList<ServiceMonitor> getServiceComponent(Document doc) {
		logger.log(Level.INFO, "START Service Information");
		ArrayList<ServiceMonitor> list = null;
		ServiceMonitor service = null;
		for (String item : COMPONENT_ITEMS) {
			NodeList elementList = doc.getElementsByTagName(item);
			int totalElements = elementList.getLength();
			Element element = null;
			if (totalElements > 0) {
				if (list == null) {
					list = new ArrayList<ServiceMonitor>();
				}
				for (int i = 0; i < totalElements; i++) {
					logger.log(Level.INFO, "START Service #" + (i + 1));
					String strTemp = "";
					element = (Element) elementList.item(i);
					service = new ServiceMonitor();
					NodeList temp = element.getElementsByTagName(NAME);
					if (temp != null && temp.getLength() > 0) {
						strTemp = getCharacterDataFromElement((Element) temp
								.item(0));
						service.setName(strTemp);
						logger.log(Level.INFO, " Name: " + strTemp);
					}// if
					temp = element.getElementsByTagName(VALUE);
					if (temp != null && temp.getLength() > 0) {
						strTemp = getCharacterDataFromElement((Element) temp
								.item(0));
						service.setStrSystemDate(strTemp);
						logger.log(Level.INFO, " Sys date: " + strTemp);
					}// if
					temp = element.getElementsByTagName(ERROR);
					if (temp != null && temp.getLength() > 0) {
						strTemp = getCharacterDataFromElement((Element) temp
								.item(0));
						service.setDescription(strTemp);
						logger.log(Level.INFO, " Error: " + strTemp);
						service.setStatus(NONE.equalsIgnoreCase(strTemp));
					}// if
					temp = element.getElementsByTagName(PING);
					if (temp != null && temp.getLength() > 0) {
						String ping = getCharacterDataFromElement((Element) temp
								.item(0));
						logger.log(Level.INFO, " Ping: " + ping);
						int pingTime = 0;
						try {
							pingTime = Integer.parseInt(MonitorUtil
									.extractDigit(ping));
						} catch (Exception ex) {

						}
						service.setPing(pingTime);
					}// if
					
					service.setSystemDate(MonitorUtil.parseDate(service
							.getStrSystemDate()));
					list.add(service);
					logger.log(Level.INFO, "END Service #" + (i + 1));
				}// for
			}// for
		}// if
		logger.log(Level.INFO, "END Service Information");
		return list;
	}

	/**
	 * Gets the service component.
	 *
	 * @param webContent the web content
	 * @return the service component
	 */
	private static ArrayList<ServiceMonitor> getServiceComponent(
			String webContent) {
		logger.log(Level.INFO, "START Service Information");
		ArrayList<ServiceMonitor> list = null;
		ServiceMonitor service = null;

		List<String> strList = new ArrayList<String>();
		String item = "";
		Matcher matcher = Pattern.compile(Constant.PATTERN_COMPONENT).matcher(
				webContent);

		// Checks if existing any string that match with the pattern
		while (matcher.find()) {
			item = StringUtils.getValueInTDTag(matcher.group().replace(
					Constant.PERCENTAGE_SYMBOL, "Perc"));
			// Adds to the list
			strList.add(item);
		}

		int iC = 0;
		int count = 0;
		if (strList.size() > 0) {
			list = new ArrayList<ServiceMonitor>();
			while (iC < (strList.size())) {
				++count;
				logger.log(Level.INFO, "START Service #" + count);
				service = new ServiceMonitor();

				String temp = strList.get(iC);
				logger.log(Level.INFO, " Name: " + temp);
				service.setName(temp);

				temp = strList.get(iC + 1);
				logger.log(Level.INFO, " Sys date: " + temp);
				service.setStrSystemDate(temp);

				temp = strList.get(iC + 2);
				logger.log(Level.INFO, " Error: " + temp);
				service.setDescription(temp);

				service.setStatus(NONE.equalsIgnoreCase(service
						.getDescription()));

				temp = strList.get(iC + 3);
				logger.log(Level.INFO, " Ping: " + temp);
				try {
					service.setPing(Integer.parseInt(MonitorUtil
							.extractDigit(temp)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt ping time. Message: "
									+ ex.getMessage());
				}

				iC += 4;
				service.setSystemDate(MonitorUtil.parseDate(service
						.getStrSystemDate()));
				list.add(service);
				logger.log(Level.INFO, "END Service #" + count);
			}
		}

		logger.log(Level.INFO, "END Service Information");
		return list;
	}

	/**
	 * Gets the jVM component.
	 *
	 * @param doc the doc
	 * @return the jVM component
	 */
	private static JvmMonitor getJVMComponent(Document doc) {
		logger.log(Level.INFO, "START Jvm Information");
		JvmMonitor jvm = null;
		NodeList elementList = doc.getElementsByTagName(JVM_ITEM);
		Element element = null;
		if (elementList.getLength() > 0) {
			element = (Element) elementList.item(0);
			String strTemp = "";
			NodeList temp = element.getElementsByTagName(FREE_MEM);

			if (temp != null && temp.getLength() > 0) {
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Free memory: " + strTemp);
				try {
					jvm.setFreeMemory(Double.parseDouble(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if
			temp = element.getElementsByTagName(TOTAL_MEM);
			if (temp != null && temp.getLength() > 0) {
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Total memory: " + strTemp);
				try {
					jvm.setTotalMemory(Double.parseDouble(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if

			temp = element.getElementsByTagName(MAX_MEM);
			if (temp != null && temp.getLength() > 0) {
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Max memory: " + strTemp);
				try {
					jvm.setMaxMemory(Double.parseDouble(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if

			temp = element.getElementsByTagName(USED_MEM);
			if (temp != null && temp.getLength() > 0) {
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Used memory: " + strTemp);
				try {
					jvm.setUsedMemory(Double.parseDouble(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if
		}// if
		logger.log(Level.INFO, "END Jvm Information");
		return jvm;
	}

	/**
	 * Gets the jVM component.
	 *
	 * @param webContent the web content
	 * @return the jVM component
	 */
	private static JvmMonitor getJVMComponent(String webContent) {
		logger.log(Level.INFO, "START Jvm Information");
		JvmMonitor jvm = null;

		Matcher matcher = Pattern.compile(Constant.PATTERN_MEMORY_KEY_VALUE)
				.matcher(webContent);
		while (matcher.find()) {
			String value = matcher.group(1);
			if (value.toLowerCase().contains("free")) {
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				logger.log(Level.INFO,
						"Free memory: " + MonitorUtil.extractDigit(value));
				try {
					jvm.setFreeMemory(Double.parseDouble(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR. parseDouble free memory. Message: "
									+ ex.getMessage());
				}
			}
			if (value.toLowerCase().contains("total")) {
				logger.log(Level.INFO,
						"Total memory: " + MonitorUtil.extractDigit(value));
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				try {
					jvm.setTotalMemory(Double.parseDouble(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR. parseDouble total memory. Message: "
									+ ex.getMessage());
				}
			}
			if (value.toLowerCase().contains("max")) {
				logger.log(Level.INFO,
						"Max memory: " + MonitorUtil.extractDigit(value));
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				try {
					jvm.setMaxMemory(Double.parseDouble(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR. parseDouble max memory. Message: "
									+ ex.getMessage());
				}
			}
			if (value.toLowerCase().contains("used")) {
				logger.log(Level.INFO,
						"Used memory: " + MonitorUtil.extractDigit(value));
				if (jvm == null) {
					jvm = new JvmMonitor();
				}
				try {
					jvm.setUsedMemory(Double.parseDouble(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR. parseDouble used memory. Message: "
									+ ex.getMessage());
				}
			}
		}

		logger.log(Level.INFO, "END Jvm Information");
		return jvm;
	}

	/**
	 * Gets the original cpu.
	 *
	 * @param doc the doc
	 * @return the original cpu
	 */
	private static CpuMonitor getOriginalCPU(Document doc) {
		logger.log(Level.INFO, "START CPU Information");
		CpuMonitor cpu = null;
		NodeList elementList = doc.getElementsByTagName(CPU_ITEM);
		if (elementList.getLength() > 0) {
			Element element = (Element) elementList.item(0);
			String strTemp = "";
			NodeList temp = element.getElementsByTagName("usage");
			if (temp != null && temp.getLength() > 0) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Usage: " + strTemp);
				try {
					cpu.setCpuUsage(Integer.parseInt(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt CPU usage. Message: "
									+ ex.getMessage());
				}
			}// if
			temp = element.getElementsByTagName("vendor");
			if (temp != null && temp.getLength() > 0) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Vendor: " + strTemp);
				cpu.setVendor(strTemp);
			}// if

			temp = element.getElementsByTagName("model");
			if (temp != null && temp.getLength() > 0) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Model: " + strTemp);
				cpu.setModel(strTemp);
			}// if

			temp = element.getElementsByTagName("total");
			if (temp != null && temp.getLength() > 0) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Total CPU: " + strTemp);
				try {
					cpu.setTotalCpu(Integer.parseInt(MonitorUtil
							.extractDigit(strTemp)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt total CPU. Message: "
									+ ex.getMessage());
				}
			}// if
		}// if

		logger.log(Level.INFO, "END CPU Information");
		return cpu;
	}

	/**
	 * Gets the original cpu.
	 *
	 * @param webContent the web content
	 * @return the original cpu
	 */
	private static CpuMonitor getOriginalCPU(String webContent) {
		logger.log(Level.INFO, "START CPU Information");
		CpuMonitor cpu = null;
		try {
			Matcher matcher = Pattern.compile(Constant.PATTERN_CPU_USAGE)
					.matcher(webContent);
			// Finds value for CPU Usage
			String value = null;
			if (matcher.find()) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				value = matcher.group(2).replace("%", "");
				logger.log(Level.INFO, " Usage: " + value);
				try {
					cpu.setCpuUsage(Integer.parseInt(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt Cpu usage. Message: "
									+ ex.getMessage());
				}
			}// if

			matcher = Pattern.compile(Constant.PATTERN_CPU_VENDOR).matcher(
					webContent);
			if (matcher.find()) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				value = matcher.group(2);
				logger.log(Level.INFO, " Vendor: " + value);
				cpu.setVendor(value);
			} // if

			matcher = Pattern.compile(Constant.PATTERN_CPU_MODEL).matcher(
					webContent);
			if (matcher.find()) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				value = matcher.group(2);
				logger.log(Level.INFO, " Model: " + value);
				cpu.setModel(value);
			} // if

			matcher = Pattern.compile(Constant.PATTERN_CPU_TOTAL).matcher(
					webContent);
			if (matcher.find()) {
				if (cpu == null) {
					cpu = new CpuMonitor();
				}
				value = matcher.group(2);
				logger.log(Level.INFO, " Total: " + value);
				try {
					cpu.setTotalCpu(Integer.parseInt(MonitorUtil
							.extractDigit(value)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt Total CPU. Message: "
									+ ex.getMessage());
				}
			} // if
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					" -> ERROR: Failed to get CPU values from web page, error: "
							+ ex.getMessage());
		}
		logger.log(Level.INFO, "END CPU Information");
		return cpu;
	}

	/**
	 * Gets the file system.
	 *
	 * @param doc the doc
	 * @return the file system
	 */
	private static ArrayList<FileSystemMonitor> getFileSystem(Document doc) {
		logger.log(Level.INFO, "START File System Information");
		ArrayList<FileSystemMonitor> list = null;
		FileSystemMonitor fileSystem = null;

		String strTemp = "";
		NodeList elementList = doc.getElementsByTagName("filesystem");
		Element element = null;
		if (elementList.getLength() > 0) {
			list = new ArrayList<FileSystemMonitor>();
			for (int i = 0; i < elementList.getLength(); i++) {
				logger.log(Level.INFO, " -> START Filesystem #" + (i + 1));
				fileSystem = new FileSystemMonitor();
				element = (Element) elementList.item(i);
				NodeList temp = element.getElementsByTagName("file_name");

				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					fileSystem.setName(strTemp);
					logger.log(Level.INFO, " Name: " + strTemp);
				}// if

				temp = element.getElementsByTagName("size");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Size: " + strTemp);
					try {
						fileSystem.setSize(Long.parseLong(MonitorUtil
								.extractDigit(strTemp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR: parseLong, size of filesystem. Message: "
										+ ex.getMessage());
					}
				}// if

				temp = element.getElementsByTagName("used");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Used: " + strTemp);
					try {
						fileSystem.setUsed(Long.parseLong(MonitorUtil
								.extractDigit(strTemp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR: parseLong, used of filesystem. Message: "
										+ ex.getMessage());
					}
				}// if

				temp = element.getElementsByTagName("mount");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Mount: " + strTemp);
					fileSystem.setMount(strTemp);
				}// if

				temp = element.getElementsByTagName("type");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Type: " + strTemp);
					fileSystem.setType(strTemp);
				}// if
				list.add(fileSystem);
				logger.log(Level.INFO, " -> END Filesystem #" + (i + 1));
			}// for
		}// if
		logger.log(Level.INFO, "END File System Information");
		return list;
	}

	/**
	 * Gets the file system.
	 *
	 * @param webContent the web content
	 * @return the file system
	 */
	private static ArrayList<FileSystemMonitor> getFileSystem(String webContent) {
		logger.log(Level.INFO, "START File System Information");
		ArrayList<FileSystemMonitor> list = null;
		FileSystemMonitor fileSystem = null;

		try {
			Matcher matcher = Pattern.compile(Constant.PATTERN_FILESYSTEM)
					.matcher(webContent);
			// Finds value for CPU Usage
			String value = null;
			List<String> strList = new ArrayList<String>();
			while (matcher.find()) {
				value = matcher.group(2).replace("%", "");
				strList.add(value);
			} // while

			int i = 0;
			int count = 0;
			if (strList.size() > 0) {
				list = new ArrayList<FileSystemMonitor>();
				String temp = "";
				while (i < strList.size()) {
					++count;
					logger.log(Level.INFO, "START File system #" + count);
					fileSystem = new FileSystemMonitor();
					temp = strList.get(i);
					logger.log(Level.INFO, " Name: " + temp);
					fileSystem.setName(temp);

					temp = strList.get(i + 1);
					logger.log(Level.INFO, " Size: " + temp);
					try {
						fileSystem.setSize(Long.parseLong(MonitorUtil
								.extractDigit(temp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR: parseLong file system size. Message: "
										+ ex.getMessage());
					}

					temp = strList.get(i + 2);
					logger.log(Level.INFO, " Used: " + temp);
					try {
						fileSystem.setUsed(Long.parseLong(MonitorUtil
								.extractDigit(temp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR: parseLong file system used. Message: "
										+ ex.getMessage());
					}

					temp = strList.get(i + 6);
					fileSystem.setType(temp);

					i += 7;
					logger.log(Level.INFO, "END File system #" + count);
					list.add(fileSystem);
				}// while
			}// if
		} catch (Exception ex) {
			logger.log(Level.ALL,
					"Failed to get File system values from web page, error: "
							+ ex.getMessage());
		}

		logger.log(Level.INFO, "END File System Information");
		return list;
	}

	/**
	 * Gets the memories.
	 *
	 * @param doc the doc
	 * @return the memories
	 */
	private static ArrayList<MemoryMonitor> getMemories(Document doc) {
		logger.log(Level.INFO, "START Memory Information");
		ArrayList<MemoryMonitor> list = null;
		MemoryMonitor mem = null;
		Element element = null;
		String strTemp = "";
		NodeList elementList = doc.getElementsByTagName("cpu_physical");

		if (elementList.getLength() > 0) {
			list = new ArrayList<MemoryMonitor>();
			for (int i = 0; i < elementList.getLength(); i++) {
				logger.log(Level.INFO, " -> START Memory #" + (i + 1));
				mem = new MemoryMonitor();
				element = (Element) elementList.item(i);
				NodeList temp = element.getElementsByTagName("cpu_type");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Type: " + strTemp);
					mem.setType(strTemp.toLowerCase().equals("swap") ? MemoryMonitor.SWAP
							: MemoryMonitor.MEM);
				}// if

				temp = element.getElementsByTagName("total");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Total: " + strTemp);
					try {
						mem.setTotalMemory(Double.parseDouble(MonitorUtil
								.extractDigit(strTemp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR parseDouble total memory. Message: "
										+ ex.getMessage());
					}
				}// if

				temp = element.getElementsByTagName("used");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Used: " + strTemp);
					try {
						mem.setUsedMemory(Double.parseDouble(MonitorUtil
								.extractDigit(strTemp)));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR parseDouble used memory. Message: "
										+ ex.getMessage());
					}
				}// if
				list.add(mem);
				logger.log(Level.INFO, " -> END Memory #" + (i + 1));
			}// for
		}// if
		logger.log(Level.INFO, "END Memory Information");
		return list;
	}

	/**
	 * Gets the memories.
	 *
	 * @param webContent the web content
	 * @return the memories
	 */
	private static ArrayList<MemoryMonitor> getMemories(String webContent) {
		logger.log(Level.INFO, "START Memory Information");
		ArrayList<MemoryMonitor> list = null;
		MemoryMonitor mem = null;

		try {
			Matcher matcher = Pattern.compile(Constant.PATTERN_SYSTEM_MEMORY)
					.matcher(webContent);
			String value = null;
			List<String> strList = new ArrayList<String>();
			while (matcher.find()) {
				value = matcher.group(2).replace("%", "");
				strList.add(value);
			} // while

			if ((strList != null) && (strList.size() > 0)) {
				// Initializes Memory Object
				list = new ArrayList<MemoryMonitor>();
				mem = new MemoryMonitor();
				String temp = strList.get(0);

				logger.log(Level.INFO, "START Memory #1");
				logger.log(Level.INFO, " Type: " + temp);
				mem.setType(temp.toLowerCase().equals("swap") ? MemoryMonitor.SWAP
						: MemoryMonitor.MEM);

				temp = strList.get(1).trim();
				logger.log(Level.INFO, " Total memory: " + temp);
				try {
					mem.setTotalMemory(Double.parseDouble(MonitorUtil
							.extractDigit(temp)) * 1024);
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR parseDouble total memory. Message: "
									+ ex.getMessage());
				}

				temp = strList.get(2).trim();
				logger.log(Level.INFO, " Used memory: " + temp);
				try {
					mem.setUsedMemory(Double.parseDouble(MonitorUtil
							.extractDigit(temp)) * 1024);
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR parseDouble used memory. Message: "
									+ ex.getMessage());
				}

				list.add(mem);
				logger.log(Level.INFO, "END Memory #1");

				logger.log(Level.INFO, "START Memory #2");
				mem = new MemoryMonitor();
				temp = strList.get(4).trim();
				logger.log(Level.INFO, " Type: " + temp);
				mem.setType(temp.toLowerCase().equals("swap") ? MemoryMonitor.SWAP
						: MemoryMonitor.MEM);

				temp = strList.get(5).trim();
				logger.log(Level.INFO, " Total memory: " + temp);
				try {
					mem.setTotalMemory(Double.parseDouble(MonitorUtil
							.extractDigit(temp)) * 1024);
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR parseDouble total memory. Message: "
									+ ex.getMessage());
				}

				temp = strList.get(6).trim();
				logger.log(Level.INFO, " Used memory: " + temp);
				try {
					mem.setUsedMemory(Double.parseDouble(MonitorUtil
							.extractDigit(temp)) * 1024);
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR parseDouble used memory. Message: "
									+ ex.getMessage());
				}

				list.add(mem);
				logger.log(Level.INFO, "END Memory #2");

			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE,
					"Failed to get system memory values from web page, error: "
							+ ex.getMessage());
		}

		logger.log(Level.INFO, "END Memory Information");
		return list;
	}

	/**
	 * Gets the character data from element.
	 *
	 * @param e the e
	 * @return the character data from element
	 */
	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			String data = cd.getData();
			if (data.contains("-"))
				return data.replace("-", "0");
			return data;
		}
		return WILDCARD;
	}

}
