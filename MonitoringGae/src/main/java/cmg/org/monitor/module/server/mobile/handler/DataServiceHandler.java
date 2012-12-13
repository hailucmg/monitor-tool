/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package cmg.org.monitor.module.server.mobile.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmg.org.monitor.entity.shared.AlertStoreMonitor;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.ext.model.shared.MonitorMessage;
import cmg.org.monitor.ext.model.shared.UserLoginDto;
import cmg.org.monitor.ext.util.StringUtils;
import cmg.org.monitor.module.server.MonitorGwtServiceImpl;
import cmg.org.monitor.services.MonitorLoginService;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.MonitorConstant;

import com.google.gson.Gson;

/**
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public class DataServiceHandler extends HttpServlet {
	private HttpServletRequest req;
	/**
	 * 
	 */
	private static final long serialVersionUID = -7331712821078280168L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
		response.setHeader("Last-Modified", new Date().toString());
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		req = request;
		String _type = getParameter("type");
		String _method = getParameter("method");
		String _id = getParameter("id");
		String _sid = getParameter("sid");
		String _item = getParameter("item");

		MonitorGwtServiceImpl service = new MonitorGwtServiceImpl();

		MonitorMessage mes = null;
		if (_type.equalsIgnoreCase("system-monitor")) {
			if (_method.equalsIgnoreCase("list")) {
				SystemMonitor[] list = service.listSystems();
				if (list != null && list.length > 0) {
					out.print(gson.toJson(list));
				} else {
					mes = new MonitorMessage("No system found");
					out.print(gson.toJson(mes));
				}
			} else if (_method.equalsIgnoreCase("read")) {
				SystemMonitor sys = new SystemMonitor();
				sys.setId(_sid);
				if (_item.equalsIgnoreCase("service")) {
					ServiceMonitor[] list = service.listServices(sys);
					if (list != null && list.length > 0) {
						out.print(gson.toJson(list));
					} else {
						mes = new MonitorMessage("No service found");
						out.print(gson.toJson(mes));
					}
				} else if (_item.equalsIgnoreCase("jvm")) {
					JvmMonitor[] list = service.listJvms(sys);
					if (list != null && list.length > 0) {
						JvmMonitor jvm = list[list.length - 1];
						jvm.setStrMaxMemory(StringUtils.humanReadableByteCount((long) jvm.getMaxMemory(), true));
						jvm.setStrTotalMemory(StringUtils.humanReadableByteCount((long) jvm.getTotalMemory(), true));
						jvm.setStrFreeMemory(StringUtils.humanReadableByteCount((long) jvm.getFreeMemory(), true));
						jvm.setStrUsedMemory(StringUtils.humanReadableByteCount((long) jvm.getUsedMemory(), true));
						out.print(gson.toJson(jvm));
					} else {
						mes = new MonitorMessage("No JVM found");
						out.print(gson.toJson(mes));
					}

				} else if (_item.equalsIgnoreCase("cpu")) {
					CpuMonitor[] list = service.listCpus(sys);
					if (list != null && list.length > 0) {
						CpuMonitor[] temp = null;
						if (list.length < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
							temp = new CpuMonitor[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH];
							for (int i = 0; i < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length; i++) {
								temp[i] = new CpuMonitor();
								temp[i].setCpuUsage(-1);
							}
							for (int i = 0; i < list.length; i++) {
								temp[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length + i] = list[i];
							}
						} else {
							temp = new CpuMonitor[list.length];
							for (int i = 0; i < list.length; i++) {
								temp[i] = list[i];
							}
						}
						out.print(gson.toJson(temp));
					} else {
						mes = new MonitorMessage("No CPU information found");
						out.print(gson.toJson(mes));
					}
				} else if (_item.equalsIgnoreCase("mem")) {
					MemoryMonitor[] list = service.listMems(sys).getRams();
					if (list != null && list.length > 0) {
						MemoryMonitor[] temp = null;
						if (list.length < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH) {
							temp = new MemoryMonitor[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH];
							for (int i = 0; i < MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length; i++) {
								temp[i] = new MemoryMonitor();
								temp[i].setUsedMemory(-1);
							}
							for (int i = 0; i < list.length; i++) {
								temp[MonitorConstant.CPU_MEMORY_HISTORY_LENGTH - list.length + i] = list[i];
							}
						} else {
							temp = new MemoryMonitor[list.length];
							for (int i = 0; i < list.length; i++) {
								temp[i] = list[i];
							}
						}

						out.print(gson.toJson(temp));
					} else {
						mes = new MonitorMessage("No Memory information found");
						out.print(gson.toJson(mes));
					}
				} else if (_item.equalsIgnoreCase("file-system")) {
					FileSystemMonitor[] list = service.listFileSystems(sys);
					if (list != null && list.length > 0) {
						for (FileSystemMonitor f : list) {
							f.setStrSize(StringUtils.humanReadableByteCount(f.getSize(), true));
							f.setStrUsed(StringUtils.humanReadableByteCount(f.getUsed(), true));
							f.setFree(f.getSize() - f.getUsed());
							f.setStrFree(StringUtils.humanReadableByteCount(f.getFree(), true));
						}
						out.print(gson.toJson(list));
					} else {
						mes = new MonitorMessage("No File system information found");
						out.print(gson.toJson(mes));
					}
				} else if (_item.equalsIgnoreCase("issue")) {
					AlertStoreMonitor[] list = service.listAlertStore(sys);
					List<AlertStoreMonitor> stores = new ArrayList<AlertStoreMonitor>();
					if (list != null && list.length > 0) {
						for (AlertStoreMonitor a : list) {
							if (a.getAlerts() != null && a.getAlerts().size() > 0) {
								stores.add(a);
							}
						}
						if (stores.isEmpty()) {
							mes = new MonitorMessage("No issue found");
							out.print(gson.toJson(mes));
						} else {
							List<AlertStoreMonitor> temp = new ArrayList<AlertStoreMonitor>();
							for (int i = stores.size() - 1; i >= 0; i--) {
								temp.add(stores.get(i));
							}
							out.print(gson.toJson(temp));
						}

					} else {
						mes = new MonitorMessage("No issue found");
						out.print(gson.toJson(mes));
					}
				} else {
					sys = service.validSystemId(_id);
					if (sys == null) {
						sys = new SystemMonitor();
						sys.setId(_id);
						sys.setDeleted(true);
					}
					out.print(gson.toJson(sys));
				}
			} else if (_method.equalsIgnoreCase("create")) {

			} else if (_method.equalsIgnoreCase("update")) {

			} else if (_method.equalsIgnoreCase("delete")) {

			}
		} else if (_type.equalsIgnoreCase("user")) {
			if (_method.equalsIgnoreCase("read")) {
				MonitorLoginService s = new MonitorLoginService();
				s.setSessionID(request.getSession(true).getId());
				UserLoginDto user = s.getUserLogin();
				if (user != null) {
					out.print(gson.toJson(user));
				} else {
					mes = new MonitorMessage("Must login");
					out.print(gson.toJson(mes));
				}
			} else if (_method.equalsIgnoreCase("create")) {
				boolean done = false;
				try {
					MailService mailService = new MailService();
					String _model = getParameter("model");
					UserLoginDto user = gson.fromJson(_model, UserLoginDto.class);
					System.out.println(_model);
					done = mailService.requestPermission(user.getEmail(), user.getFirstName(), user.getLastName(), user.getDescription());
				} catch (Exception ex) {
					System.out.println("Error: " + ex.getMessage());
				}
				mes = new MonitorMessage(done ? "true" : "false");

				out.print(gson.toJson(mes));
			}
		}
		out.close();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	private String getParameter(String para) {
		String in = req.getParameter(para);
		return (in != null && in.length() > 0) ? in : "";
	}
}
