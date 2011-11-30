/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.util.ServiceForbiddenException;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.CpuMemoryDAO;
import cmg.org.monitor.dao.FileSystemDAO;
import cmg.org.monitor.dao.ServiceMonitorDAO;
import cmg.org.monitor.dao.SystemMonitorDAO;
import cmg.org.monitor.dao.impl.AlertDaoJDOImpl;
import cmg.org.monitor.dao.impl.CpuMemoryDaoJDOImpl;
import cmg.org.monitor.dao.impl.FileSystemDaoJDOImpl;
import cmg.org.monitor.dao.impl.ServiceMonitorDaoJDOImpl;
import cmg.org.monitor.dao.impl.SystemMonitorDaoJDOImpl;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.Component;
import cmg.org.monitor.ext.model.URLPageObject;
import cmg.org.monitor.ext.model.shared.AlertDto;
import cmg.org.monitor.ext.model.shared.CpuDto;
import cmg.org.monitor.ext.model.shared.CpuPhysicalDto;
import cmg.org.monitor.ext.model.shared.FileSystemDto;
import cmg.org.monitor.ext.model.shared.JVMMemoryDto;
import cmg.org.monitor.ext.model.shared.ServiceDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.ext.util.HttpUtils.Page;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.Ultility;

/**
 * Please enter a short description for this class.
 * 
 * <p>
 * Optionally, enter a longer description.
 * </p>
 * 
 * @author Lam phan
 * @version 1.0.6 June 11, 2008
 */
public class URLMonitor {

	/** Alert name */
	public static String ALERT_NAME = " alert report ";

	/** Declare email address */
	public static String EMAIL_ADMINISTRATOR = "lam.phan@c-mg.com";

	/** Time format value */
	private static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	/** Time instance */
	private Timestamp timeStamp;

	private static String ERROR = "ERROR";

	/** Log object. */
	private static final Logger logger = Logger.getLogger(URLMonitor.class
			.getCanonicalName());

	/**
	 * Default constructor.<br>
	 */
	public URLMonitor() {
		super();
	}

	/**
	 * This method do monitor the nodes and update data to database.
	 * 
	 * @param systemDto
	 *            the project to monitor
	 * 
	 * @return a list of components
	 * 
	 * @throws MonitorException
	 *             if monitor failed
	 */

	public URLPageObject generateInfo(SystemDto systemDto)
			throws MonitorException, Exception {

		Date now = new Date();

		URLPageObject obj = null;
		Component fullComponent = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
			String message = "Begin monitor...";
			logger.info(message);

			// Checks for null value
			if (systemDto == null) {
				logger.log(Level.SEVERE, "Can not get a system instance");
				throw new MonitorException("Can not get a system instance");
			}

			// Processes page
			Page page = null;
			boolean isError = false;
			String webContent = null;
			try {
				// if (Constant.USER_PROTOCOL.equals(systemDto.getProtocol())) {
				// webContent = FetchMailUsage.fetchSMTPEmail();
				// page =
				// HttpUtils.retrievePage("http://c-mg.vn:81/bpg/content/cmg_mail.html");
				// } else {
				message = "Retrieves website content from "
						+ systemDto.getUrl();
				logger.info("Retrieves website content from ");
				page = HttpUtils.retrievePage(systemDto.getRemoteUrl().trim());

				// page =
				// HttpUtils.retrievePage("https://ukpensionsint.bp.com/content/BT_monitorxml.html");
				// page =
				// HttpUtils.retrievePage("http://192.168.1.13:8080/content/BT_monitorxml.html");
				// page =
				// HttpUtils.retrievePage("http://192.168.1.111:8080/bpg/content/monitor_test_xml");
				webContent = page.getContent();

				// }
				// log the message
				message = "The website has been retrieved, content type: "
						+ page.getContentType();
				logger.info(message);
			} catch (Exception mx) {
				try {
					if (ConnectionUtil.internetAvail()) {
						// Prints out
						message = "The system can not get data from "
								+ systemDto.getRemoteUrl()
								+ ". Error occurred so that updates system's status of "
								+ systemDto.getName();
						systemDto.setSystemStatus(false);
						sendUnknownAlerts(systemDto, message);
						isError = true;
					} else {
						logger.info("Internet connection failed");
					}
				} catch (Exception e) {

					// log with message
					message = "The monitoring failed, try to update"
							+ " project's status but not success, error details: "
							+ e.getMessage();
					logger.log(Level.WARNING, message);
				}
				throw new MonitorException(
						"Unable to monitor the application, Error: "
								+ mx.getMessage());
			}

			// Reads content
			if ((page == null) || (page.getContent() == null)) {
				if ((ConnectionUtil.internetAvail()) && (!isError)) {

					// Prints out
					message = "Error occurred so that the page content is null,"
							+ " updated system's status";
					systemDto.setSystemStatus(false);
					sendUnknownAlerts(systemDto, message);
					logger.info(message);
				} else {
					logger.info("Internet connection failed");
				}

				return obj;
			}
			if ((page.getContent().equals(ERROR))
					|| (page.getContent().equals(MonitorUtil.getErrorContent()))) {

				// Prints out
				message = " Error occurred so that the page content is null,"
						+ " updated project's status";
				systemDto.setSystemStatus(false);
				sendUnknownAlerts(systemDto, message);
				logger.info(message);
			}

			// Get JVM data from xml
			JVMMemoryDto jvmMemDto = new JVMMemoryDto();

			XMLMonitorParser parse = new XMLMonitorParser();
			List<JVMMemoryDto> parseJVMs = parse.getJVMComponent(webContent);

			List<CpuDto> parseCPUs = parse.getOriginalCPU(webContent);
			List<FileSystemDto> files = parse.getFileSystem(webContent);
			List<CpuPhysicalDto> physicalCPus = parse
					.getPhysicalCPU(webContent);

			systemDto.setSystemStatus(true);
			List<Component> parseComponents = parse.getDBComponent(webContent);
			Component parseComponent = null;
			List<Component> fullComponents = new ArrayList<Component>();
			for (int i = 0; i < parseComponents.size(); i++) {

				// Initiate new full component
				fullComponent = new Component();
				parseComponent = parseComponents.get(i);
				String id = systemDto.getName() + "_"
						+ parseComponent.getValueComponent();
				fullComponent.setComponentId(id.trim());
				fullComponent.setName(parseComponent.getComponentId());
				fullComponent.setSysDate(parseComponent.getValueComponent());
				fullComponent.setError(parseComponent.getError());
				String errorStr = parseComponent.getError();
				fullComponent.setDiscription(parseComponent.getDescription());
				fullComponent.setSystemId(systemDto.getId());
				fullComponent.setPing(parseComponent.getPing());
				// Checks if the component is error and send alert email
				if (!errorStr.equals(Constant.COMPONENT_NONE_STRING)) {

					// OK, you are an error exactly
					message = "Having error with component: "
							+ fullComponent.getName()
							+ ". Update history and send email";
					logger.info(message);
				}

				// Prints out
				logger.info("Comp: " + fullComponent);

				// Adds components
				fullComponents.add(fullComponent);
			}
			
			// >>>>>> Service Monitor and JVM process <<<<<<
			if ((fullComponents != null) && (fullComponents.size() > 0)) {
				ServiceMonitorDAO serviceDao = new ServiceMonitorDaoJDOImpl();

				String ping = null;
				ServiceDto serviceDto = null;

				if (parseJVMs.size() <= 1) {
					for (JVMMemoryDto dtoJvm : parseJVMs) {
						dtoJvm = parseJVMs.get(0);
						jvmMemDto = dtoJvm;
					}
				}

				// Loop over array list to get 'ServiceMonitor' type
				for (Component comp : fullComponents) {
					serviceDto = new ServiceDto();

					// Get ping number
					ping = Ultility.extractDigit(comp.getPing());

					// Validate given object
					if (comp != null) {

						serviceDto.setName(comp.getName());
						serviceDto.setPing(Integer.parseInt(ping));
						serviceDto.setDescription(comp.getError());
						serviceDto.setSysDate(Ultility.isValidFormat(comp
								.getSysDate()));
						serviceDto.setStatus(true);
						serviceDto.setTimeStamp(now);
					}

					// Do update to Service and JVM JDO entity
					serviceDao.updateServiceEntity(serviceDto, systemDto,
							jvmMemDto);
				}
			}

			// ------- CPU -------
			String error = "";
			Timestamp currentDate = null;
			CpuMemoryDAO cpuDao = new CpuMemoryDaoJDOImpl();
			CpuDto cpuObj = null;
			if (parseCPUs != null && parseCPUs.size() > 0) { 
				cpuObj = parseCPUs.get(0);
				cpuObj.setTimeStamp(now);
			}
			if (cpuObj != null) {

				// Gets CPU usage
				String cpuUsage = String.valueOf(cpuObj.getCpuUsage());
				double cpuPerc = 0.0;
				try {
					cpuPerc = Double.parseDouble(cpuUsage.trim());
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Cannot parse the CPU usage value");
				}
				currentDate = new Timestamp(System.currentTimeMillis());
				error = "None";
				String compId = systemDto.getName() + "_CPU_Usage";

				fullComponent = new Component();
				fullComponent.setComponentId(compId);
				fullComponent.setSystemId(systemDto.getId());
				fullComponent.setName(compId);
				fullComponent.setError(error);
				fullComponent.setSysDate(dateFormat.format(now));
				try {
					cpuDao.updateCpu(cpuObj, systemDto);
					CpuPhysicalDto cpuPhysicalDto = null;
					if (physicalCPus != null) {
						for (int p = 0; p < physicalCPus.size(); p++) {
							cpuPhysicalDto = (CpuPhysicalDto) physicalCPus
									.get(p);
							cpuObj.setUsedMemory(cpuPhysicalDto.getUsed());
							cpuObj.setTotalMemory(cpuPhysicalDto.getFree());
							cpuDao.updateCpu(cpuObj, systemDto);
						}
					}
					// In case of used mem exceed allowed values.
					if (cpuPerc >= Constant.CPU_LEVEL_HISTORY_UPDATE)
						cpuDao.updateCpu(cpuObj, systemDto);

					error = "CPU: " + cpuUsage;

				} catch (Exception e) {
					logger.log(Level.SEVERE,
							"Cannot update a CPU, error: " + e.getMessage());
				}

				logger.info("CPU: " + cpuObj);

				// Gets CPU from Cpu memory for 3 times, if this values are
				// greater than 90% then send alert
				try {
					boolean cpuCritical = true;
					List<CpuDto> cpuList = new ArrayList<CpuDto>();

					final int threeLastestCpu = 3;
					cpuDao.getLastestCpuMemory(systemDto, threeLastestCpu);
					if (cpuList != null) {
						if (cpuList.size() < threeLastestCpu) {
							cpuCritical = false;
						} else {
							logger.info("CPU size on getTop3CPUUsage(): "
									+ cpuList.size());
							for (CpuDto cpu : cpuList) {
								if (cpu.getCpuUsage() < Constant.CPU_LEVEL_HISTORY_UPDATE) {
									cpuCritical = false;
									break;
								}
							}
						}
					}
					if (cpuCritical) {
						logger.info("Send alert for component "
								+ fullComponent.getName()
								+ " because the three recent CPU usage"
								+ " is greater than "
								+ Constant.CPU_LEVEL_HISTORY_UPDATE);
						sendAlerts(fullComponent, compId);

					}
				} catch (Exception e) {
					logger.info("Cannot get values for CPU , error: "
							+ e.getMessage());
				}

				try {
					logger.info("Update history CPU daily successfully!");
				} catch (Exception e) {
					logger.log(
							Level.SEVERE,
							"Cannot get values for CPU  today, error: "
									+ e.getMessage());
				}
			} else {
				cpuObj = new CpuDto();
				try {

					// Set default cpu object
					cpuObj.setCpuName("");
					cpuObj.setCpuUsage(0);
					cpuObj.setTimeStamp(now);
					cpuObj.setVendor("");
					cpuObj.setModel("");
					cpuDao.updateCpu(cpuObj, systemDto);
					// error = "CPU: " + cpuUsage;
				} catch (Exception e) {
					logger.log(
							Level.SEVERE,
							"Cannot update default CPU, error: "
									+ e.getMessage());
				}
			}
			
			// >>>>>>> File System Monitor <<<<<<<<<
			currentDate = new Timestamp(System.currentTimeMillis());
			FileSystemDAO fileSystemDao = new FileSystemDaoJDOImpl();
			if (files.size() == 0) {
				FileSystemDto fileDto = new FileSystemDto();
				try {
					fileDto.setName("Default File System");
					fileDto.setSize(1L);
					fileDto.setTimeStamp(new Date());
					fileDto.setType("Non defined");
					fileDto.setUsed(1L);
					fileDto.setTimeStamp(now);
					// Do update File System JDO
					fileSystemDao.updateFileSystem(fileDto, systemDto);
				} catch (Exception e) {
					logger.info("Cannot update data for File System, error: "
							+ e.getMessage());
				}
			} else {

				for (FileSystemDto fileDto : files) {
					String used = String.valueOf(fileDto.getUsed());
					fileDto.setTimeStamp(now);
					double percUsed = 50.0;
					try {
						if (used.equals("-")) {
							percUsed = 0;
						} else {
							percUsed = Double.parseDouble(used.trim());
						}
					} catch (Exception ex) {
						logger.info("Cannot parse disk used value: "
								+ ex.toString());
					}
					error = "None";
					String compId = systemDto.getName()
							+ "_Filesystem_"
							+ fileDto.getName().replace("\\", "")
									.replace(":", "");

					// Updates to component table
					fullComponent = new Component();
					fullComponent.setComponentId(compId);
					fullComponent.setSystemId(systemDto.getId());
					fullComponent.setName(compId);
					fullComponent.setError(error);
					fullComponent.setSysDate(dateFormat.format(currentDate));

					// Do update File System JDO
					fileSystemDao.updateFileSystem(fileDto, systemDto);

					// Check used value
					if (percUsed > Constant.DF_LEVEL_HISTORY_UPDATE) {

						// Message alert information
						error = "Error with File System " + fileDto.getName()
								+ " Used: " + used + "%";
						fullComponent.setError(error);

						// Send alert
						sendAlerts(fullComponent, compId);
						try {
							// Do update File System JDO
							fileSystemDao.updateFileSystem(fileDto, systemDto);
						} catch (Exception e) {
							logger.info("Cannot update data for File System, error: "
									+ e.getMessage());
						}
					}
					logger.info("DF: " + fileDto);
				}
			}

			return obj;
		} catch (MonitorException me) {
			throw new MonitorException(me.getLocalizedMessage());
		} catch (Exception e) {
			if (e instanceof NullPointerException) {
				systemDto.setSystemStatus(false);
				sendUnknownAlerts(systemDto, "In " + systemDto.getName()
						+ ", the system can't read monitoring data from "
						+ systemDto.getRemoteUrl());
				
				
			}
			if (e instanceof ServiceForbiddenException)
				logger.log(Level.WARNING, "Can not send alert email ");
			throw e;
		}

	}

	/**
	 * Make alert and email with given component
	 * 
	 * @param component
	 * @param compId
	 */
	private void sendAlerts(Component component, String compId)
			throws MonitorException {
		SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();

		try {
			SystemDto localSystemDto = systemDao.getSystembyID(compId).toDTO();
			if (localSystemDto != null) {
				MailService mailSrv = new MailService();
				mailSrv.sendAlertMail(component, localSystemDto);
				logger.info("An email has been delivered to "
						+ localSystemDto.getGroupEmail()
						+ " to notify about error");

			}
		} catch (MonitorException e) {
			logger.log(
					Level.SEVERE,
					"Cannot send email about error" + " of component "
							+ component.getName() + ", error: "
							+ e.getMessage());
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Exception at component " + component.getName()
							+ ", error: " + e.getMessage());
		}
	}

	/**
	 * Make alert by email.
	 * 
	 * @param sysDto
	 * @param comp
	 */
	private void sendUnknownAlerts(SystemDto sysDto, String message) throws Exception {
		MailService service = new MailService();
		try {
			AlertDao alert = new AlertDaoJDOImpl();
			AlertDto alertDTO = new AlertDto();

			// Send alert email to email group
			service.sendAlertMail(sysDto, message);

			alertDTO.setBasicInfo(
					"The monitor system don't know the url or some error happen at "
							+ sysDto.getName(), message, new Date());

			// Create an alert
			alertDTO = alert.updateAlert(alertDTO, sysDto);
		} catch (Exception e) {
			logger.log(
					Level.SEVERE,
					"Cannot send alert email" + " of component "
							+ sysDto.getName() + ", error at: "
							+ e.getMessage());
		}
	}

	/**
	 * The overridden toString method.
	 * 
	 * @return a string that describe this object
	 */
	@Override
	public String toString() {
		return super.toString();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param timeStamp
	 *            DOCUMENT ME!
	 */
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

}
