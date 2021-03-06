package cmg.org.monitor.services;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import cmg.org.monitor.dao.AlertDao;
import cmg.org.monitor.dao.MailMonitorDAO;
import cmg.org.monitor.dao.SystemDAO;
import cmg.org.monitor.dao.UtilityDAO;
import cmg.org.monitor.dao.impl.AlertDaoImpl;
import cmg.org.monitor.dao.impl.MailMonitorDaoImpl;
import cmg.org.monitor.dao.impl.SystemDaoImpl;
import cmg.org.monitor.dao.impl.UtilityDaoImpl;
import cmg.org.monitor.entity.shared.AlertMonitor;
import cmg.org.monitor.entity.shared.MailMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.exception.MonitorException;
import cmg.org.monitor.ext.util.MonitorParser;
import cmg.org.monitor.ext.util.MonitorUtil;
import cmg.org.monitor.ext.util.URLMonitor;
import cmg.org.monitor.util.shared.Constant;
import cmg.org.monitor.util.shared.MonitorConstant;

public class MonitorService {

	/** Default monitor logger */
	private static final Logger logger = Logger.getLogger(MonitorService.class
			.getCanonicalName());

	public static void monitor() throws MonitorException {
		SystemDAO systemDao = new SystemDaoImpl();
		UtilityDAO utilDao = new UtilityDaoImpl();
		ArrayList<SystemMonitor> systems = null;
		ArrayList<SystemMonitor> tempList = null;
		try {
			systems = systemDao.listSystems(false);
		} catch (Exception me) {
			logger.log(Level.INFO, " -> ERROR when list all system. Message: "
					+ me.getMessage());
		}
		if (systems != null && systems.size() > 0) {
			tempList = new ArrayList<SystemMonitor>();
			// loop all system
			DateTime currentTime = new DateTime(System.currentTimeMillis());
			String currentZone = utilDao.getCurrentTimeZone();
			currentTime = currentTime.withZone(DateTimeZone.forID(currentZone));
			logger.log(
					Level.INFO,
					"Current Zone: " + currentZone + "\nDay: "
							+ currentTime.getDayOfMonth() + "\nHour: "
							+ currentTime.getHourOfDay() + "\nMinute: "
							+ currentTime.getMinuteOfHour() + "\nMinutes: "
							+ currentTime.getMinuteOfDay());
			for (SystemMonitor aSystem : systems) {
				if (!aSystem.isActive()) {
					logger.log(
							Level.INFO,
							MonitorUtil.parseTime(System.currentTimeMillis(),
									true)
									+ "The system "
									+ aSystem.toString()
									+ " is existed but is not active. "
									+ " The monitor skips this system now");
				} else {
					logger.log(
							Level.INFO,
							"System exclusion time: " + "\n Start: "
									+ aSystem.getStartHour() + ":"
									+ aSystem.getStartMinute() + "\n End: "
									+ aSystem.getEndHour() + ":"
									+ aSystem.getEndMinute()
									+ "\n Start Minutes: "
									+ aSystem.getStartMinutes()
									+ "\n End Minutes: "
									+ aSystem.getEndMinutes());
					boolean check = false;
					if ((aSystem.getStartHour() == aSystem.getEndHour())
							&& (aSystem.getStartMinute() == aSystem
									.getEndMinute())) {
						check = true;
					} else if (aSystem.getStartMinutes() == aSystem
							.getEndMinutes()) {
						check = true;
					} else {
						if (currentTime.getMinuteOfDay() < aSystem
								.getStartMinutes()
								|| currentTime.getMinuteOfDay() > aSystem
										.getEndMinutes()) {
							check = true;
						}
					}

					if (check) {
						logger.log(
								Level.INFO,
								MonitorUtil.parseTime(
										System.currentTimeMillis(), true)
										+ "START monitoring system "
										+ aSystem.toString());
						logger.log(Level.INFO,
								"System url: " + aSystem.getUrl());
						logger.log(Level.INFO,
								"Remote url: " + aSystem.getRemoteUrl());

						try {
							String infoContent = null;
							if (MonitorConstant.SMTP_PROTOCOL.equals(aSystem
									.getProtocol())) {
								MailMonitorDAO mailDAO = new MailMonitorDaoImpl();
								MailMonitor mail = mailDAO
										.getMailMonitor(aSystem
												.getEmailRevice().toLowerCase());
								// clear store after get data
								mailDAO.clearMailStore(aSystem.getEmailRevice());
								if (mail != null) {
									infoContent = mail.getContent();
								}
							} else {
								infoContent = URLMonitor
										.retrievesContent(aSystem
												.getRemoteUrl());
								if (infoContent == null
										|| infoContent.trim().length() == 0) {
									// try with GAE URLFetchService
									infoContent = URLMonitor
											.retrievesContentWithFetchService(aSystem
													.getRemoteUrl());

									if (infoContent == null
											|| infoContent.trim().length() == 0) {
										infoContent = URLMonitor
												.retrievesContent(Constant.REDIRECTOR_WORKER_URL
														+ "?url="
														+ URLEncoder.encode(
																aSystem.getRemoteUrl(),
																Constant.ENCODING_ISO_8859_1));
									}

								}
							}// if-else

							if (infoContent == null || infoContent.equals("")) {
								aSystem.setStatus(false);

								logger.log(
										Level.WARNING,
										MonitorUtil.parseTime(
												System.currentTimeMillis(),
												true)
												+ "Fetch content of data from "
												+ aSystem.toString()
												+ ": NO CONTENT FOUND");
							} else {
								logger.log(
										Level.INFO,
										MonitorUtil.parseTime(
												System.currentTimeMillis(),
												true)
												+ "Fetch content of data from "
												+ aSystem.toString()
												+ ": \r\n"
												+ infoContent);
								aSystem = MonitorParser.parseData(infoContent,
										aSystem);
							}// if-else

						} catch (Exception e) {
							aSystem.setStatus(false);
							logger.log(
									Level.INFO,
									" ->ERROR: when revice content"
											+ e.getMessage());
						}
						if (!aSystem.getStatus()) {
							AlertDao alertDAO = new AlertDaoImpl();
							AlertMonitor alert = new AlertMonitor(
									AlertMonitor.CANNOT_GATHER_DATA,
									"System is down",
									"Unable to retrieve data from the remote system ("
											+ (aSystem
													.getProtocol()
													.equals(MonitorConstant.HTTP_PROTOCOL) ? ("remote url: " + aSystem
													.getRemoteUrl())
													: ("Remote mail: " + aSystem
															.getEmailRevice()))
											+ "). The system is not working as expected. Please check the system immediately.",
									new Date());
							alertDAO.storeAlert(aSystem, alert);
						}// if

					} else {
						logger.log(
								Level.INFO,
								MonitorUtil.parseTime(
										System.currentTimeMillis(), true)
										+ "The system "
										+ aSystem.toString()
										+ " is active. "
										+ " But The monitor skips this system by exclusion time");
					}
				}
				aSystem.setTimeStamp(new Date(System.currentTimeMillis()));
				try {
					systemDao.updateSystem(aSystem);
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: cannot update system. Message: "
									+ ex.getMessage());
				}

				tempList.add(aSystem);
			}// for
		} else {
			logger.log(Level.INFO,
					MonitorUtil.parseTime(System.currentTimeMillis(), true)
							+ " -> END Monitoring: No system found!");
		}
		systemDao.storeSysList(tempList);
	}
}
