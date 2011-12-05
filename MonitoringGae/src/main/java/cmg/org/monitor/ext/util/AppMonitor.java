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
import cmg.org.monitor.ext.model.shared.CpuPhysicalDto;
import cmg.org.monitor.ext.model.shared.FileSystemDto;
import cmg.org.monitor.ext.model.shared.JVMMemoryDto;
import cmg.org.monitor.ext.model.shared.ServiceDto;
import cmg.org.monitor.ext.model.shared.SystemDto;
import cmg.org.monitor.ext.util.HttpUtils.Page;
import cmg.org.monitor.memcache.MonitorMemcache;
import cmg.org.monitor.memcache.SystemMonitorStore;
import cmg.org.monitor.memcache.shared.AlertMonitorDto;
import cmg.org.monitor.memcache.shared.CpuDTO;
import cmg.org.monitor.memcache.shared.FileSystemCacheDto;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.memcache.shared.MemoryDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.services.email.MailService;
import cmg.org.monitor.util.shared.Ultility;

import com.google.gdata.util.ServiceForbiddenException;

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
public class AppMonitor {

	

}
