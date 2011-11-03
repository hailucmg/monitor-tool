/*
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package cmg.org.monitor.ext.util;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.impl.AlertDaoJDOImpl;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.model.CPUObject;
import cmg.org.monitor.ext.model.Component;
import cmg.org.monitor.ext.model.MemoryObject;
import cmg.org.monitor.ext.model.URLPageObject;
import cmg.org.monitor.ext.model.dto.AlertDto;
import cmg.org.monitor.ext.model.dto.CpuDto;
import cmg.org.monitor.ext.model.dto.CpuUsageDto;
import cmg.org.monitor.ext.model.dto.SystemDto;
import cmg.org.monitor.ext.util.HttpUtils.Page;
import cmg.org.monitor.services.MonitorWorker;

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

	private Timestamp timeStamp;
	private final int RUNNING = 1;
	private final int WARNING = 2;
	private final int CRITICAL = 3;

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
	 * @param proj
	 *            the project to monitor
	 * 
	 * @return a list of components
	 * 
	 * @throws MonitorException
	 *             if monitor failed
	 */
	public URLPageObject generateInfo(SystemDto proj) throws MonitorException {
		URLPageObject obj = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss.S");
			String message = "Begin monitor...";
			logger.info(message);

			// Checks for null value
			if (proj == null) {
				logger.log(Level.SEVERE, "Can not get a system instance");
				throw new MonitorException("Can not get a system instance");
			}

			// Checks the URL to monitor
			List<String> list = new ArrayList<String>();
			// ComponentService cs = (ComponentService) ctx.getBean(
			// Constant.SERVICE_COMPONENT_NAME);
			// UserManager userService = (UserManager) ctx.getBean(
			// Constant.SERVICE_USER_NAME);
			// MailService mailService = (MailService) ctx.getBean(
			// Constant.SERVICE_MAIL_NAME);
			Pattern pattern = Pattern.compile(Constant.PATTERN_COMPONENT);

			// Processes page
			Page page = null;
			boolean isError = false;
			try {
				message = "Retrieves website content from " + proj.getUrl();
				logger.info("Retrieves website content from ");
				
				page = HttpUtils.retrievePage(proj.getUrl());
				
				// Prints out
				message = "The website has been retrieved, content type: "
						+ page.getContentType();
				logger.info(message);
			} catch (Exception mx) {
				try {
					// updateProjectStatus(proj, false);
					if (ConnectionUtil.internetAvail()) {
						sendAlerts(proj);

						// Prints out
						message = "Error occurred so that updates project's status";

						isError = true;
					} else {
						logger.info("Internet connection failed");
					}
				} catch (Exception e) {

					// Don't say anything to the user
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
					// updateProjectStatus(proj, false);
					sendAlerts( proj);

					// Prints out
					message = "Error occurred so that the page content is null,"
							+ " updated project's status";
					logger.info(message);
				} else {
					logger.info("Internet connection failed");
				}

				return obj;
			}
			if ((page.getContent().equals("ERROR"))
					|| (page.getContent().equals(MonitorUtil.getErrorContent()))) {
				// updateProjectStatus(proj, false);
				sendAlerts( proj);

				// Prints out
				message = "Error occurred so that the page content is null,"
						+ " updated project's status";
				logger.info(message);
			}
			sendMail(proj);
			sendAlerts( proj);
			
			String webContent = page.getContent();
			String item = null;
			Matcher matcher = pattern.matcher(webContent);

			// Checks if existing any string that match with the pattern
			while (matcher.find()) {
				item = StringUtils.getValueInTDTag(matcher.group().replace("%",
						"Perc"));

				// Adds to the list
				list.add(item);
			} // while

			// Checks if the list contain any items
			int ioc = 0;
			Component component = null;
			List<Component> components = new ArrayList<Component>();
			obj = new URLPageObject();
			while (ioc < (list.size())) {
				// Declares new component
				component = new Component();
				String id = proj.getName()
						+ "_"
						+ list.get(ioc).replaceAll("sysdate", "").trim()
								.replaceAll("\\s", "_");
				component.setComponentId(id.trim());
				component.setName(list.get(ioc));
				component.setSysDate(list.get(ioc + 1));
				String errorStr = list.get(ioc + 2);
				component.setError(errorStr);
				component.setDiscription(list.get(ioc + 3));
				component.setProjectId(Long.parseLong(proj.getId()));

				// Checks if the component is error and send email
				if (!errorStr.equals(Constant.COMPONENT_NONE_STRING)) {
					// OK, you are an error exactly
					message = "Having error with component: "
							+ component.getName()
							+ ". Update history and send email";
					logger.info(message);

					// HistoryManager.addHistory(component);

					// Update history and Send email here
					// sendAlerts(userService, proj, mailService, component,
					// id);
				}

				// Prints out
				logger.info("Comp: " + component);
				// printf("Comp: " + component);

				// Checks if component existing or not
				// updateComponent(cs, component, id);

				// Increases count variable
				ioc += 4;

				// Adds components
				components.add(component);
			} // for

			// ------- CPU Monitor -------
			String error = "";
			Timestamp currentDate = null;
			MonitorWorker worker = new MonitorWorker();
			CPUObject cpuObj = worker.getCPUMonitor(webContent);

			// System memory Monitor
			MemoryObject memObj = worker.getMemObjectMonitor(webContent);

			if (cpuObj != null) {
				// Gets CPU usage
				String cpuUsage = cpuObj.getUsedCPU();
				double cpuPerc = 0.0;
				try {
					cpuPerc = Double.parseDouble(cpuUsage.trim());
				} catch (Exception ex) {
					logger.log(Level.SEVERE, "Cannot parse the CPU usage value");
				}
				currentDate = new Timestamp(System.currentTimeMillis());
				error = "None";
				String compId = proj.getName() + "_CPU_Usage";

				// Updates to component table
				component = new Component();
				component.setComponentId(compId);
				component.setProjectId(Long.parseLong(proj.getId()));
				component.setName(compId);
				component.setError(error);
				component.setSysDate(dateFormat.format(currentDate));
				// HistoryCpuDao dao = null;
				// CpuusageDao cpuDao = null;
				if (cpuPerc >= Constant.CPU_LEVEL_HISTORY_UPDATE) {
					try {
						// dao = DaoFactory.createHistoryCpuDao();
						// HistoryCpu dto = getHistory(cpuObj,
						// proj.getProjectId(), compId,
						// currentDate);

						// Updates back to database
						// dao.insert(dto);
						error = "CPU: " + cpuUsage;
					} catch (Exception e) {
						logger.log(Level.SEVERE,
								"Cannot update history for CPU usage, error: "
										+ e.getMessage());
					}
				}
				logger.info("CPU: " + cpuObj);
				obj.setCpu(cpuObj);

				// Update CPU table
				CpuDto cpuDto = new CpuDto();
				cpuDto.setCpuName(compId);
				cpuDto.setCpuUsage(cpuObj.getUsedCPU());
				cpuDto.setVendor(cpuObj.getVendor());
				cpuDto.setModel(cpuObj.getModel());
				cpuDto.setTotalCpu(String.valueOf(cpuObj.getTotalCPUs()));
				cpuDto.setTimeStamp(currentDate);
				cpuDto.setProjectId(Long.parseLong(proj.getId()));
				// worker.updateCPU(cpuDto);

				// Gets CPU from history for 3 times, if this values are greater
				// than 90% Then send alert
				try {
					boolean cpuCritical = true;
					List<CpuUsageDto> cpuList = new ArrayList<CpuUsageDto>();
					// cpuDao = DaoFactory.createCpuusageDao();
					// cpuList = cpuDao.getTop3CPUUsage();
					if (cpuList != null) {
						if (cpuList.size() < 3) {
							cpuCritical = false;
						} else {
							logger.info("CPU size on getTop3CPUUsage(): "
									+ cpuList.size());
							for (CpuUsageDto cpu : cpuList) {
								if (cpu.getCpuUsage() < Constant.CPU_LEVEL_HISTORY_UPDATE) {
									cpuCritical = false;

									break;
								}
							}
						}
					}
					if (cpuCritical) {
						logger.info("Send alert for component "
								+ component.getName()
								+ " because the three recent CPU usage"
								+ " is greater than "
								+ Constant.CPU_LEVEL_HISTORY_UPDATE);
						// sendAlerts(userService, proj, mailService, component,
						// compId);
					}
				} catch (Exception e) {
					logger.log(Level.SEVERE,
							"Cannot get values for CPU history today, error: "
									+ e.getMessage());
				}

				// Update component
				// updateComponent(cs, component, compId);

				// Update history
				// List<HistoryCpu> hcList = null;
				try {
					// dao = DaoFactory.createHistoryCpuDao();
					// hcList = dao.getTodayHistories();
					// if ((hcList == null) || (hcList.size() <= 0)
					// || (hcList.size() < 3)) {
					// Update history for CPU usage
					// HistoryCpu dto = getHistory(cpuObj,
					// proj.getProjectId(), compId, currentDate);
					// dao.insert(dto);
					logger.info("Update history CPU daily successfully!");
					// }
				} catch (Exception e) {
					logger.log(Level.SEVERE,
							"Cannot get values for CPU history today, error: "
									+ e.getMessage());
				}

				// Update CPU usages timely
				try {
					currentDate = new Timestamp(System.currentTimeMillis());
					// cpuDao = DaoFactory.createCpuusageDao();
					CpuUsageDto dto = new CpuUsageDto();
					dto.setCpuUsage(Double.parseDouble(cpuObj.getUsedCPU()));
					dto.setTimeStamp(dateFormat.format(currentDate));
					dto.setProject_id(Long.parseLong(proj.getId()));
					// cpuDao.insert(dto);
				} catch (Exception e) {
					logger.log(Level.SEVERE,
							"Cannot get values for CPU history today, error: "
									+ e.getMessage());
				}
			}
			
			// -------

			if ((components != null) && (components.size() > 0)) {
				// proj.setComponents(compList);
				// obj.setProject(proj);
			}
			if (((components != null) && (components.size() > 0))) {
				// updateProjectStatus(proj, true);
			}

			return obj;
		} catch (Exception ex) {
			throw new MonitorException(ex.getLocalizedMessage());
		}
	}

	private void sendAlerts(SystemDto sysDto) {

		try {
			AlertDao alert = new AlertDaoJDOImpl();
			AlertDto alertDTO = new AlertDto();

			alertDTO.setBasicInfo("SQLException", "database error", new Date());
			
			// Create an alert
			alertDTO = alert.updateAlert(alertDTO, sysDto);
			sendMail(sysDto);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Cannot send alert about error"
					+ " of component " + ", error: " + e.getMessage());
		}
	}

	private void sendMail(SystemDto systemDto) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = "Your account is testing for alert send mail";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("lam.phan@c-mg.com",
					"Example.com Admin"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					systemDto.getGroupEmail(), "Mr. User"));
			msg.setSubject("alert email sending");
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			// ...
		} catch (MessagingException e) {
			// ...
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
