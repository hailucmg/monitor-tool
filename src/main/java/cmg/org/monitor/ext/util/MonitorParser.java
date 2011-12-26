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

import cmg.org.monitor.common.Constant;
import cmg.org.monitor.entity.shared.CpuMonitor;
import cmg.org.monitor.entity.shared.FileSystemMonitor;
import cmg.org.monitor.entity.shared.JvmMonitor;
import cmg.org.monitor.entity.shared.MemoryMonitor;
import cmg.org.monitor.entity.shared.ServiceMonitor;
import cmg.org.monitor.entity.shared.SystemMonitor;
import cmg.org.monitor.util.shared.Ultility;

public class MonitorParser {

	/** The element component . */
	public static String NAME = "name";

	/** The element component . */
	public static String VALUE = "value";

	/** The element component . */
	public static String ERROR = "error";

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

	/** The element component . */
	public static String JVM_ITEM = "JVM";

	/** The element component . */
	public static String CPU_ITEM = "cpu";

	/** The element component . */
	public static String WILDCARD = "?";

	/** The array of component . */
	public static String[] COMPONENT_ITEMS = { LDAP_ITEM, DATABASE_ITEM };

	private static final Logger logger = Logger.getLogger(MonitorParser.class
			.getCanonicalName());

	public boolean checkContentType(String content) {
		return (content.toLowerCase().contains("<cmg>") && content
				.toLowerCase().contains("</cmg>"));
	}

	public SystemMonitor parserData(String content, SystemMonitor sys) {
		// BEGIN LOG
		long start = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(start, true) + sys.toString()
						+ " -> START: parse data ... ");
		// BEGIN LOG
		Date timeStamp = new Date(start);
		JvmMonitor jvm = null;
		CpuMonitor cpu = null;
		ArrayList<MemoryMonitor> memList = null;
		ArrayList<FileSystemMonitor> fileSysList = null;
		ArrayList<ServiceMonitor> serviceList = null;

		/* START GATHER INFORMATION */
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
		/* END CHECK & STORE INFORMATION */

		// END LOG
		long end = System.currentTimeMillis();
		logger.log(Level.INFO,
				MonitorUtil.parserTime(end, true) + sys.toString()
						+ " -> END: parse data. Time executed: "
						+ (end - start) + " ms.");
		// END LOG
		return sys;
	}

	/**
	 * Get every element of database and ldap tag 1. Parse the xml content. 2.
	 * Parse by database tag 3. Parse it by ldap tag
	 * 
	 * @param xmlContent
	 *            the content of xml.
	 * @return components list of object
	 */
	public ArrayList<ServiceMonitor> getServiceComponent(Document doc) {
		logger.log(Level.INFO, "START Service Information");
		ArrayList<ServiceMonitor> list = null;
		ServiceMonitor service = null;
		for (String item : COMPONENT_ITEMS) {
			NodeList elementList = doc.getElementsByTagName(item);
			int totalElements = elementList.getLength();
			Element element = null;
			if (totalElements > 0) {
				list = new ArrayList<ServiceMonitor>();
				for (int i = 0; i < totalElements; i++) {
					logger.log(Level.INFO, "START Service #" + (i + 1));
					element = (Element) elementList.item(i);
					service = new ServiceMonitor();
					NodeList temp = element.getElementsByTagName(NAME);
					if (temp != null && temp.getLength() > 0) {
						service.setName(getCharacterDataFromElement((Element) temp
								.item(0)));
						logger.log(Level.INFO, " Name: " + service.getName());
					}// if
					temp = element.getElementsByTagName(VALUE);
					if (temp != null && temp.getLength() > 0) {
						service.setStrSystemDate(getCharacterDataFromElement((Element) temp
								.item(0)));
						logger.log(Level.INFO,
								" Sys date: " + service.getStrSystemDate());
					}// if
					temp = element.getElementsByTagName(ERROR);
					if (temp != null && temp.getLength() > 0) {
						service.setDescription(getCharacterDataFromElement((Element) temp
								.item(0)));
						logger.log(Level.INFO,
								" Error: " + service.getDescription());
					}// if
					temp = element.getElementsByTagName(PING);
					if (temp != null && temp.getLength() > 0) {
						String ping = getCharacterDataFromElement((Element) temp
								.item(0));
						logger.log(Level.INFO, " Ping: " + ping);
						int pingTime = 0;
						try {
							pingTime = Integer.parseInt(Ultility
									.extractDigit(ping));
						} catch (Exception ex) {

						}
						service.setPing(pingTime);
					}// if
					list.add(service);
					logger.log(Level.INFO, "END Service #" + (i + 1));
				}// for
			}// for
		}// if
		logger.log(Level.INFO, "END Service Information");
		return list;
	}

	public ArrayList<ServiceMonitor> getServiceComponent(String webContent) {
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

				temp = strList.get(iC + 3);
				logger.log(Level.INFO, " Ping: " + temp);
				try {
					service.setPing(Integer.parseInt(Ultility.extractDigit(temp)));
				} catch (Exception ex) {
					logger.log(
							Level.SEVERE,
							" -> ERROR: parseInt ping time. Message: "
									+ ex.getMessage());
				}

				iC += 4;
				list.add(service);
				logger.log(Level.INFO, "END Service #" + count);
			}
		}

		logger.log(Level.INFO, "END Service Information");
		return list;
	}

	/**
	 * @param xmlContent
	 * @return
	 */
	public JvmMonitor getJVMComponent(Document doc) {
		logger.log(Level.INFO, "START Jvm Information");
		JvmMonitor jvm = new JvmMonitor();
		NodeList elementList = doc.getElementsByTagName(JVM_ITEM);
		Element element = null;
		if (elementList.getLength() > 0) {
			element = (Element) elementList.item(0);
			String strTemp = "";
			NodeList temp = element.getElementsByTagName(FREE_MEM);

			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Free memory: " + strTemp);
				try {
					jvm.setFreeMemory(Double.parseDouble(strTemp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if
			temp = element.getElementsByTagName(TOTAL_MEM);
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Total memory: " + strTemp);
				try {
					jvm.setTotalMemory(Double.parseDouble(strTemp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if

			temp = element.getElementsByTagName(MAX_MEM);
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Max memory: " + strTemp);
				try {
					jvm.setMaxMemory(Double.parseDouble(strTemp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if

			temp = element.getElementsByTagName(USED_MEM);
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Used memory: " + strTemp);
				try {
					jvm.setUsedMemory(Double.parseDouble(strTemp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR: parseDouble " + ex.getMessage());
				}
			}// if
		}// if
		logger.log(Level.INFO, "END Jvm Information");
		return jvm;
	}

	public JvmMonitor getJVMComponent(String webContent) {
		logger.log(Level.INFO, "START Jvm Information");
		JvmMonitor jvm = new JvmMonitor();

        Matcher matcher = Pattern.compile(
                Constant.PATTERN_MEMORY_KEY_VALUE).matcher(webContent);
        while (matcher.find()) {
            String value = matcher.group(1);
            if (value.toLowerCase().contains("free")) {
            	logger.log(Level.INFO, "Free memory: " + Ultility.extractDigit(value));
            	try {
            		jvm.setFreeMemory(Double.parseDouble(Ultility.extractDigit(value)));
            	} catch(Exception ex) {
            		logger.log(Level.SEVERE, " -> ERROR. parseDouble free memory. Message: " + ex.getMessage());
            	}
            }
            if (value.toLowerCase().contains("total")) {
            	logger.log(Level.INFO, "Total memory: " + Ultility.extractDigit(value));
            	try {
            		jvm.setTotalMemory(Double.parseDouble(Ultility.extractDigit(value)));
            	} catch(Exception ex) {
            		logger.log(Level.SEVERE, " -> ERROR. parseDouble total memory. Message: " + ex.getMessage());
            	}
            }
            if (value.toLowerCase().contains("max")) {
            	logger.log(Level.INFO, "Max memory: " + Ultility.extractDigit(value));
            	try {
            		jvm.setMaxMemory(Double.parseDouble(Ultility.extractDigit(value)));
            	} catch(Exception ex) {
            		logger.log(Level.SEVERE, " -> ERROR. parseDouble max memory. Message: " + ex.getMessage());
            	}
            }
            if (value.toLowerCase().contains("used")) {
            	logger.log(Level.INFO, "Used memory: " + Ultility.extractDigit(value));
            	try {
            		jvm.setUsedMemory(Double.parseDouble(Ultility.extractDigit(value)));
            	} catch(Exception ex) {
            		logger.log(Level.SEVERE, " -> ERROR. parseDouble used memory. Message: " + ex.getMessage());
            	}
            }
        }

		logger.log(Level.INFO, "END Jvm Information");
		return jvm;
	}

	/**
	 * @param xmlContent
	 * @return
	 */
	public CpuMonitor getOriginalCPU(Document doc) {
		logger.log(Level.INFO, "START CPU Information");
		CpuMonitor cpu = new CpuMonitor();
		NodeList elementList = doc.getElementsByTagName(CPU_ITEM);
		if (elementList.getLength() > 0) {
			Element element = (Element) elementList.item(0);
			String strTemp = "";
			NodeList temp = element.getElementsByTagName("usage");
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Usage: " + strTemp);
				try {
					cpu.setCpuUsage(Integer.parseInt(Ultility
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
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Vendor: " + strTemp);
				cpu.setVendor(strTemp);
			}// if

			temp = element.getElementsByTagName("model");
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Model: " + strTemp);
				cpu.setModel(strTemp);
			}// if

			temp = element.getElementsByTagName("total");
			if (temp != null && temp.getLength() > 0) {
				strTemp = getCharacterDataFromElement((Element) temp.item(0));
				logger.log(Level.INFO, " Total CPU: " + strTemp);
				try {
					cpu.setTotalCpu(Integer.parseInt(strTemp));
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

	public CpuMonitor getOriginalCPU(String webContent) {
		logger.log(Level.INFO, "START CPU Information");
		CpuMonitor cpu = new CpuMonitor();
		try {
			Matcher matcher = Pattern.compile(Constant.PATTERN_CPU_USAGE)
					.matcher(webContent);
			// Finds value for CPU Usage
			String value = null;
			if (matcher.find()) {
				value = matcher.group(2).replace("%", "");
				logger.log(Level.INFO, " Usage: " + value);
				try {
					cpu.setCpuUsage(Integer.parseInt(value));
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
				value = matcher.group(2);
				logger.log(Level.INFO, " Vendor: " + value);
				cpu.setVendor(value);
			} // if

			matcher = Pattern.compile(Constant.PATTERN_CPU_MODEL).matcher(
					webContent);
			if (matcher.find()) {
				value = matcher.group(2);
				logger.log(Level.INFO, " Model: " + value);
				cpu.setModel(value);
			} // if

			matcher = Pattern.compile(Constant.PATTERN_CPU_TOTAL).matcher(
					webContent);
			if (matcher.find()) {
				value = matcher.group(2);
				logger.log(Level.INFO, " Total: " + value);
				try {
					cpu.setTotalCpu(Integer.parseInt(value.trim()));
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
	 * @param xmlContent
	 * @return
	 */
	public ArrayList<FileSystemMonitor> getFileSystem(Document doc) {
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
						fileSystem.setSize(Long.parseLong(Ultility
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
						fileSystem.setUsed(Long.parseLong(Ultility
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

	public ArrayList<FileSystemMonitor> getFileSystem(String webContent) {
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
						fileSystem.setSize(Long.parseLong(temp));
					} catch (Exception ex) {
						logger.log(Level.SEVERE,
								" -> ERROR: parseLong file system size. Message: "
										+ ex.getMessage());
					}

					temp = strList.get(i + 4);
					logger.log(Level.INFO, " Used: " + temp);
					try {
						fileSystem.setUsed(Long.parseLong(temp));
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
	 * @param xmlContent
	 * @return
	 */
	public ArrayList<MemoryMonitor> getMemories(Document doc) {
		logger.log(Level.INFO, "START Memory Information");
		ArrayList<MemoryMonitor> list = new ArrayList<MemoryMonitor>();
		MemoryMonitor mem = null;
		Element element = null;
		String strTemp = "";
		NodeList elementList = doc.getElementsByTagName("cpu_physical");

		if (elementList.getLength() > 0) {
			for (int i = 0; i < elementList.getLength(); i++) {
				logger.log(Level.INFO, " -> START Memory #" + (i + 1));
				mem = new MemoryMonitor();
				element = (Element) elementList.item(i);
				NodeList temp = element.getElementsByTagName("cpu_type");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Type: " + strTemp);					
					mem.setType(strTemp.toLowerCase().equals(MemoryMonitor.SWAP) ? MemoryMonitor.SWAP : MemoryMonitor.MEM);
				}// if

				temp = element.getElementsByTagName("total");
				if (temp != null && temp.getLength() > 0) {
					strTemp = getCharacterDataFromElement((Element) temp
							.item(0));
					logger.log(Level.INFO, " Total: " + strTemp);
					try {
						mem.setTotalMemory(Double.parseDouble(Ultility
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
						mem.setUsedMemory(Double.parseDouble(Ultility
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
		} else {
			list.add(new MemoryMonitor(MemoryMonitor.SWAP));
			list.add(new MemoryMonitor(MemoryMonitor.MEM));			
		}
		logger.log(Level.INFO, "END Memory Information");
		return list;
	}

	public ArrayList<MemoryMonitor> getMemories(String webContent) {
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
				mem.setType(temp.toLowerCase().equals(MemoryMonitor.SWAP) ? MemoryMonitor.SWAP : MemoryMonitor.MEM);
				
				temp = strList.get(1);
				logger.log(Level.INFO, " Total memory: " + temp);
				try {
					mem.setTotalMemory(Double.parseDouble(temp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR parseDouble total memory. Message: "
									+ ex.getMessage());
				}

				temp = strList.get(2);
				logger.log(Level.INFO, " Used memory: " + temp);
				try {
					mem.setUsedMemory(Double.parseDouble(temp));
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
				temp = strList.get(4);
				logger.log(Level.INFO, " Type: " + temp);
				mem.setType(temp.toLowerCase().equals(MemoryMonitor.SWAP) ? MemoryMonitor.SWAP : MemoryMonitor.MEM);

				temp = strList.get(5);
				logger.log(Level.INFO, " Total memory: " + temp);
				try {
					mem.setTotalMemory(Double.parseDouble(temp));
				} catch (Exception ex) {
					logger.log(Level.SEVERE,
							" -> ERROR parseDouble total memory. Message: "
									+ ex.getMessage());
				}

				temp = strList.get(6);
				logger.log(Level.INFO, " Used memory: " + temp);
				try {
					mem.setUsedMemory(Double.parseDouble(temp));
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
