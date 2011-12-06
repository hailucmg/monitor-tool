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
import cmg.org.monitor.memcache.shared.AlertMonitorDto;
import cmg.org.monitor.memcache.shared.CpuDTO;
import cmg.org.monitor.memcache.shared.MemoryDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.memcache.shared.SystemMonitorDto;
import cmg.org.monitor.ext.model.shared.CpuPhysicalDto;
import cmg.org.monitor.ext.model.shared.FileSystemDto;

import cmg.org.monitor.memcache.shared.FileSystemCacheDto;
import cmg.org.monitor.ext.model.shared.JVMMemoryDto;
import cmg.org.monitor.ext.model.shared.ServiceDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.ext.util.HttpUtils.Page;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.memcache.SystemMonitorStore;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.Ultility;

import com.google.gdata.util.ServiceForbiddenException;
import com.google.gwt.dev.util.Memory;

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
	private Component buildServiceComponent(Component fullComponent, Component parseServiceComponent, SystemMonitorDto systemDto) {
		
		// Initiate new full component
		fullComponent = new Component();
		String id = systemDto.getName() + "_"
				+ parseServiceComponent.getValueComponent();
		fullComponent.setComponentId(id.trim());
		fullComponent.setName(parseServiceComponent.getComponentId());
		fullComponent.setSysDate(parseServiceComponent.getValueComponent());
		fullComponent.setError(parseServiceComponent.getError());
		fullComponent.setDiscription(parseServiceComponent.getDescription());
		fullComponent.setSystemId(systemDto.getId());
		fullComponent.setPing(parseServiceComponent.getPing());
		return fullComponent;
	}
	
	
	public URLPageObject generateInfo(SystemMonitorDto systemDto)
	//public URLPageObject generateInfo(SystemDto systemDto)
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
						message = "The system can not achieve data from following url:/r/n"
								+ systemDto.getRemoteUrl().trim() + "/r/n"
								+ ". Please update system which has name:"
								+ systemDto.getName();
						systemDto.setStatus(false);
						AlertMonitorDto alertDto = new AlertMonitorDto();
						alertDto.setBasicInfo("", AlertMonitorDto.UNABLE_DATA_READ, message, now);
						List<AlertMonitorDto> alertList = new ArrayList<AlertMonitorDto>(); 
						alertList.add(alertDto);
						sendUnknownAlerts(systemDto, message, null, null, null, null, null, alertList);
						
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
					message = "The system can't fetch content of data from the following url:\r\n" 
							+ systemDto.getRemoteUrl()+ "\r\n"
							+ ". Please, update your system input information correctly";
					systemDto.setStatus(false);
					AlertMonitorDto alertDto = new AlertMonitorDto();
					alertDto.setBasicInfo("", AlertMonitorDto.DATA_NULL, message, now);
					List<AlertMonitorDto> alertList = new ArrayList<AlertMonitorDto>(); 
					alertList.add(alertDto);
					sendUnknownAlerts(systemDto, message, null, null, null, null, null, alertList);
					logger.info(message);
				} else {
					logger.info("Internet connection failed");
				}

				return obj;
			}
			if ((page.getContent().equals(ERROR))
					|| (page.getContent().equals(MonitorUtil.getErrorContent()))) {

				// Prints out
				message = "The system contains error information from the following url : \r\n" 
						+ systemDto.getRemoteUrl()+ "\r\n"
						+ "Please, recheck your system status or system information";
				systemDto.setStatus(false);
				AlertMonitorDto alertDto = new AlertMonitorDto();
				alertDto.setBasicInfo("", AlertMonitorDto.DATA_ERROR, message, now);
				List<AlertMonitorDto> alertList = new ArrayList<AlertMonitorDto>(); 
				alertList.add(alertDto);
				sendUnknownAlerts(systemDto, message, null, null, null, null, null, alertList);
				logger.info(message);
			}

			// Parse xml data for alert
//		    JVMMemoryDto jvmMemDto = new JVMMemoryDto();
//			XMLMonitorParser parse = new XMLMonitorParser();
//			List<JVMMemoryDto> parseJVMs = parse.getJVMComponent(webContent);
//
//			List<CpuDto> parseCPUs = parse.getOriginalCPU(webContent);
//			List<FileSystemDto> files = parse.getFileSystem(webContent);
//			List<CpuPhysicalDto> physicalCPus = parse
//					.getPhysicalCPU(webContent);

			// Parse xml data for memCache
			XMLParserForMemCache cacheParser = new XMLParserForMemCache();
			List<ServiceMonitorDto> mServices = cacheParser.getServiceComponent(webContent);
			List<JvmDto> mJVMs = cacheParser.getJVMForCache(webContent);
			List<FileSystemCacheDto> mfiles =  cacheParser.getFileSystem(webContent);
			List<MemoryDto> mMemoryList = cacheParser.getPhysicalCPUForMem(webContent);
			List<CpuDTO> mCpuList = cacheParser.getOriginalCPUForMem(webContent);
			
			systemDto.setStatus(true);
			isError = true;
			MonitorMemcache.changeFlag(isError);
			List<Component> parseServiceComponents = cacheParser.getComponent(webContent);
			
			List<Component> fullServiceComponents = new ArrayList<Component>();
			for (int i = 0; i < parseServiceComponents.size(); i++) {
				
				// Initiate new full component
				fullComponent = buildServiceComponent(fullComponent, parseServiceComponents.get(i), systemDto);
				
				// Checks if the component is error and send alert email
				String errorStr = fullComponent.getError();
				if (!errorStr.equals(Constant.COMPONENT_NONE_STRING)) {

					// OK, you are an error exactly
					message = "Having error with component: "
							+ fullComponent.getName()
							+ ". Update history and send email";
					logger.info(message);
				}

				// Prints out
				logger.info("Component : " + fullComponent);

				// Adds components
				fullServiceComponents.add(fullComponent);
			}
			List<ServiceMonitorDto> serviceMonitorList = new ArrayList<ServiceMonitorDto>();
			JvmDto jvmDto = new JvmDto();
			
			
			// >>>>>> Service Monitor and JVM process <<<<<<
			if ((fullServiceComponents != null) && (fullServiceComponents.size() > 0)) {

				Integer ping = null;
				ServiceMonitorDto serviceDto = null;

				if (mJVMs.size() == 1) {

				  jvmDto = mJVMs.get(0);
				  double percentUsedJVM = Ultility.percentageForJVM(jvmDto.getUsedMemory(), jvmDto.getMaxMemory());
				  String messageForJVM = null;
				  if (percentUsedJVM > Constant.LEVEL_JVM_PERCENTAGE) {
					  fullComponent = new Component();
					  messageForJVM = 
							  "The system currently under "+String.valueOf(percentUsedJVM)+"% of allowed percentage"
							  +" which is more than allowed level of java virtual machine percentage.\r\n" 
							  +"The system " +systemDto.getName()+ " which has java virtual machine has almost run out of memory.\r\n"
							  +"Please, re-check your system again";
					  fullComponent.setError(messageForJVM);
					  List<AlertMonitorDto> alertMonitorList = new ArrayList<AlertMonitorDto>();
					  AlertMonitorDto alertDto = new AlertMonitorDto();
					  alertDto.setBasicInfo("", AlertMonitorDto.JVM_ERROR, messageForJVM, now);
					  alertMonitorList.add(alertDto);
					  sendAlerts(fullComponent, null, systemDto, mServices, mJVMs,mCpuList.get(0), mfiles,mMemoryList, alertMonitorList);
				  }
				}
				
				int count = 1;
				StringBuffer  errorMessage = new StringBuffer();
				
				List<AlertMonitorDto> alertMonitorList = new ArrayList<AlertMonitorDto>();
				AlertMonitorDto alertMonitorDto = null;
				// Loop over array list to get 'ServiceMonitor' type
				for (Component comp : fullServiceComponents) {
					serviceDto = new ServiceMonitorDto();

					// Get ping number
					ping = Integer.parseInt(Ultility.extractDigit(comp.getPing()));
					
					if (ping > Constant.PING_LEVEL_RESPONSE) {
						
						// Describe service error.
						errorMessage.append(count).append(". The service name : ").append(comp.getName())
						.append(" has taken too much time for response.\r\n")
						.append("Please, re-check or update your system again.\r\n").append("\r\n");
						comp.setError(errorMessage.toString());
						count++;
					}
					
					if (Constant.ERROR.equalsIgnoreCase(comp.getDescription())) {
						
						// Describe service error.
						errorMessage.append(count).append(". The service name : ").append(comp.getName())
						.append(" is being under error exception.\r\n")
						.append("Please, re-check or update your system again.\r\n").append("\r\n");
						comp.setError(errorMessage.toString());
						count++;
					}
					// Validate given object
					if (comp != null) {

						serviceDto.setName(comp.getName());
						serviceDto.setPing(ping);
						serviceDto.setDescription(comp.getError());
						serviceDto.setStatus(true);
						serviceDto.setTimeStamp(now);
					}
					
				    serviceMonitorList.add(serviceDto);
				    
				    alertMonitorDto = new AlertMonitorDto();
				    alertMonitorDto.setBasicInfo(comp.getName(), AlertMonitorDto.SERVICE_ERROR, message, now);
				    alertMonitorList.add(alertMonitorDto);
					
					// Do update to Service and JVM JDO entity
//					serviceDao.updateServiceEntity(serviceDto, systemDto,
//							jvmMemDto); 
				}
				
				storeToMemCache(serviceMonitorList, mJVMs, mCpuList.get(0), mfiles, mMemoryList, alertMonitorList);
				
				// Send error message when service components have errors
				int errorLength = errorMessage.length();
				if (errorLength != 0) {
					String errorMsg = errorMessage.toString();
					sendAlerts(null, errorMsg, systemDto,
							serviceMonitorList, mJVMs, mCpuList.get(0), mfiles, mMemoryList, alertMonitorList);
				}
			}

			List<MemoryDto> mems = new ArrayList<MemoryDto>();
			
			// ------- CPU -------
			String error = "";
			Timestamp currentDate = null;
			CpuDTO cpuObj = null;
			if (mCpuList != null && mCpuList.size() > 0) { 
				cpuObj = mCpuList.get(0);
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
					List<AlertMonitorDto> alertMonitorList = new ArrayList<AlertMonitorDto>();
					AlertMonitorDto alertMonitorDto = new AlertMonitorDto();
				    
				    alertMonitorDto.setBasicInfo(fullComponent.getName(), AlertMonitorDto.SERVICE_ERROR, message, now);
				    alertMonitorList.add(alertMonitorDto);
					storeToMemCache(serviceMonitorList, mJVMs, cpuObj, mfiles, mMemoryList, alertMonitorList);
					//cpuDao.updateCpu(cpuObj, systemDto);
					
					MemoryDto memoryDto = null;
					
					if (mMemoryList != null) {
						
						for (int p = 0; p < mMemoryList.size(); p++) {
							memoryDto = (MemoryDto) mMemoryList
									.get(p);
							memoryDto.getUsedMemory();
							memoryDto.getTotalMemory();
							mems.add(memoryDto);
//							cpuDao.updateCpu(cpuObj, systemDto);
							storeToMemCache(serviceMonitorList, mJVMs, cpuObj, mfiles, mems, alertMonitorList);
						}
					}
					// In case of used mem exceed allowed values.
					if (cpuPerc >= Constant.CPU_LEVEL_HISTORY_UPDATE)
//						cpuDao.updateCpu(cpuObj, systemDto);
						storeToMemCache(serviceMonitorList, mJVMs, cpuObj, mfiles, mems, alertMonitorList);
						
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
					//cpuDao.getLastestCpuMemory(systemDto, threeLastestCpu);
					
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

//						sendAlerts(fullComponent, compId, (ArrayList<ServiceMonitorDto>)mServices,
//								mJVMs, 
//								mOrginalCpuList.get(0), 
//								mfiles, mPhysicalCpuList);
						//sendAlerts(fullComponent,null, compId);
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
				cpuObj = new CpuDTO();
				try {

					// Set default cpu object
					cpuObj.setCpuUsage(0);
					cpuObj.setTimeStamp(now);
					cpuObj.setVendor("");
					cpuObj.setModel("");
//					cpuDao.updateCpu(cpuObj, systemDto);
					List<AlertMonitorDto> alertMonitorList = new ArrayList<AlertMonitorDto>();
					AlertMonitorDto alertDto = new AlertMonitorDto();
					alertDto.setBasicInfo("", AlertMonitorDto.JVM_ERROR, "", now);
					alertMonitorList.add(alertDto);
					
					storeToMemCache(serviceMonitorList, mJVMs, cpuObj, mfiles, mems, alertMonitorList);
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
			
			List<AlertMonitorDto> alertMonitorList = new ArrayList<AlertMonitorDto>();
			List<FileSystemCacheDto> fileSystemList = new ArrayList<FileSystemCacheDto>();
			if (mfiles.size() == 0) {
				FileSystemCacheDto fileDto = new FileSystemCacheDto();
				try {
					fileDto.setName("Default File System");
					fileDto.setSize(1d);
					fileDto.setTimeStamp(new Date());
					fileDto.setType("Non defined");
					fileDto.setUsed(1d);
					fileDto.setTimeStamp(now);
					
					// Do update File System JDO
					fileSystemList.add(fileDto);
					
					// fileSystemDao.updateFileSystem(fileDto, systemDto);
				} catch (Exception e) {
					logger.info("Cannot update data for File System, error: "
							+ e.getMessage());
				}
				storeToMemCache(serviceMonitorList, mJVMs, cpuObj, fileSystemList, mems, alertMonitorList);
			} else {
				List<FileSystemCacheDto> files = new ArrayList<FileSystemCacheDto>();
				for (FileSystemCacheDto fileDto : mfiles) {
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
					// fileSystemDao.updateFileSystem(fileDto, systemDto);
					files.add(fileDto);
					storeToMemCache(serviceMonitorList, mJVMs, cpuObj, files, mems, alertMonitorList);

					// Check used value
					if (percUsed > Constant.DF_LEVEL_HISTORY_UPDATE) {

						// Message alert information
						error = "Error with file system " + fileDto.getName()
								+ " Used: " + used + "%";
						fullComponent.setError(error);

						// Send alert
						// sendAlerts(fullComponent,null, compId);
						try {
							// Do update File System JDO
							// fileSystemDao.updateFileSystem(fileDto, systemDto);
							storeToMemCache(null, mJVMs, cpuObj, files, mems, alertMonitorList);
						} catch (Exception e) {
							logger.info("Cannot update data for File System, error: "
									+ e.getMessage());
						}
					}
					logger.info("Default File System: " + fileDto);
				}
			}

			return obj;
		} catch (MonitorException me) {
			throw new MonitorException(me.getLocalizedMessage());
		} catch (Exception e) {
			String message = null;
			if (e instanceof NullPointerException) {
				
				systemDto.setStatus(false);
				message = "Unknow error has occurr at system "+systemDto.getName();
				logger.log(Level.SEVERE, e.getMessage());
			}
			if (e instanceof ServiceForbiddenException) {
				message = "Internal error exception : The system can not send mail";
				logger.log(Level.WARNING, message);
			}
			AlertMonitorDto alertDto = new AlertMonitorDto();
			alertDto.setBasicInfo("", AlertMonitorDto.DATA_ERROR, message, now);
			List<AlertMonitorDto> alertList = new ArrayList<AlertMonitorDto>(); 
			alertList.add(alertDto);
			sendUnknownAlerts(systemDto, message, null, null, null, null, null, alertList );
			throw e;
		}
	}
	
	private void storeToMemCache(List<ServiceMonitorDto> serviceMonitorList,
			List<JvmDto> jvms, 
			CpuDTO  cpuDto, 
			List<FileSystemCacheDto> files, 
			List<MemoryDto> memory, 
			List<AlertMonitorDto> alertDtoList  ) throws MonitorException {
		
		JvmDto aJvm = null;
		if (jvms.size() > 0)
			aJvm = jvms.get(0);
		
		// Create an alert in Mem Cache
		ArrayList<SystemMonitorStore> systemMonitorCaches = MonitorMemcache.getSystemMonitorStore();
		for (SystemMonitorStore systemMonitor : systemMonitorCaches) {
			systemMonitor.setJvm( aJvm);
			systemMonitor.setServiceMonitorList((ArrayList<ServiceMonitorDto> )serviceMonitorList);
			systemMonitor.setCpu(cpuDto);
			systemMonitor.setFileSysList( (ArrayList<FileSystemCacheDto>)files);
			systemMonitor.setMemory((ArrayList<MemoryDto>)memory);
			systemMonitor.setAlert((ArrayList<AlertMonitorDto>)alertDtoList);
		}
		MonitorMemcache.putSystemMonitorStore(systemMonitorCaches);
	}
	
	/**
	 * @param component
	 * @param componentId
	 * @param serviceMonitorList
	 * @param jvms
	 * @param cpuDto
	 * @param files
	 * @param memory
	 * @throws MonitorException
	 */
	private void sendAlerts(Component component, String message, SystemMonitorDto systemDto, 
			List<ServiceMonitorDto> serviceMonitorList,
			List<JvmDto> jvms, 
			CpuDTO  cpuDto, 
			List<FileSystemCacheDto> files, 
			List<MemoryDto> memory, 
			List<AlertMonitorDto> alertDtoList)
			throws MonitorException {
		SystemMonitorDAO systemDao = new SystemMonitorDaoJDOImpl();

		try {
			//SystemDto localSystemDto = systemDao.getSystembyID(componentId).toDTO();
			
			if (systemDto != null) {
				MailService mailSrv = new MailService();
				mailSrv.sendAlertMail(component, message,  systemDto);
				logger.info("An email has been delivered to "
						+ systemDto.getGroupEmail()
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
	 * @param sysDto
	 * @param message
	 * @param serviceMonitorList
	 * @param jvms
	 * @param cpuDto
	 * @param files
	 * @param memory
	 * @param alertDtoList
	 * @throws Exception
	 */
	private void sendUnknownAlerts(SystemMonitorDto sysDto, String message, List<ServiceMonitorDto> serviceMonitorList,
			List<JvmDto> jvms, 
			CpuDTO  cpuDto, 
			List<FileSystemCacheDto> files, 
			List<MemoryDto> memory, List<AlertMonitorDto> alertDtoList) throws Exception {
		MailService service = new MailService();
		try {

			// Send alert email to email group
			service.sendAlertMail(sysDto, message);

			// Create an alert
			MonitorMemcache.changeFlag(false);
			storeToMemCache(serviceMonitorList,jvms,cpuDto, files, memory, alertDtoList );
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
