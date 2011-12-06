package cmg.org.monitor.ext.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import cmg.org.monitor.ext.model.Component;
import cmg.org.monitor.memcache.shared.CpuDTO;
import cmg.org.monitor.memcache.shared.FileSystemCacheDto;
import cmg.org.monitor.memcache.shared.JvmDto;
import cmg.org.monitor.memcache.shared.MemoryDto;
import cmg.org.monitor.memcache.shared.ServiceMonitorDto;
import cmg.org.monitor.util.shared.Ultility;

public class XMLParserForMemCache {

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
    public static String[] COMPONENT_ITEMS = {LDAP_ITEM, DATABASE_ITEM};
    
    private static final Logger logger = Logger.getLogger(XMLMonitorParser.class
			.getName());
    
    /**
	 * @param xmlContent
	 * @return
	 */
	public List<JvmDto> getJVMForCache(String xmlContent) {
		List<JvmDto> jvmMems = new ArrayList<JvmDto>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			
			NodeList elementList = doc.getElementsByTagName(JVM_ITEM);
			int totalElements = elementList.getLength();
			
			JvmDto jvmDto = null;
			Element element = null;
			if (totalElements > 0) {
				for (int i = 0; i < totalElements; i++) {
				  element = (Element) elementList.item(i);
				  jvmDto = new JvmDto();
			      jvmDto = xmlToJVMForMemCache(jvmDto, element);
			      jvmMems.add(jvmDto);
				}
			}
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return jvmMems;
	}    
	
	/**
	 * Get every element of database and ldap tag
	 * 1. Parse the xml content.
	 * 2. Parse by database tag
	 * 3. Parse it by ldap tag
	 * @param xmlContent the content of xml.
	 * @return components list of object 
	 */
	public List<ServiceMonitorDto> getServiceComponent(String xmlContent) {
		List<ServiceMonitorDto> components = new ArrayList<ServiceMonitorDto>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			for (String item : COMPONENT_ITEMS) {
				NodeList elementList = doc.getElementsByTagName(item);
				int totalElements = elementList.getLength();
				
				ServiceMonitorDto serviceDto = null;
				Element element = null;
				if (totalElements > 0) {
					for (int i = 0; i < totalElements; i++) {
					  element = (Element) elementList.item(i);
					  serviceDto = new ServiceMonitorDto();
				      serviceDto = xmlToServiceMonitor(serviceDto, element);
				      components.add(serviceDto);
					}
				}
			}
			
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return components;
	}
	
	/**
	 * Get every element of database and ldap tag
	 * 1. Parse the xml content.
	 * 2. Parse by database tag
	 * 3. Parse it by ldap tag
	 * @param xmlContent the content of xml.
	 * @return components list of object 
	 */
	public List<Component> getComponent(String xmlContent) {
		List<Component> components = new ArrayList<Component>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			for (String item : COMPONENT_ITEMS) {
				NodeList elementList = doc.getElementsByTagName(item);
				int totalElements = elementList.getLength();
				
				Component component = null;
				Element element = null;
				if (totalElements > 0) {
					for (int i = 0; i < totalElements; i++) {
					  element = (Element) elementList.item(i);
					  component = new Component();
				      component = xmlToComponent(component, element);
				      components.add(component);
					}
				}
			}
			
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return components;
	}
	
	private Component xmlToComponent(Component component, Element element) {
		NodeList name = element.getElementsByTagName(NAME);
        if (name!= null && name.getLength() > 0) {
        	Element nameElement = (Element) name.item(0);
	        System.out.println("Name: " + getCharacterDataFromElement(nameElement));
        	component.setComponentId(getCharacterDataFromElement(nameElement));
        }
        
        NodeList value = element.getElementsByTagName(VALUE);
        if (value!= null && value.getLength() > 0) {
        	Element nameElement = (Element) value.item(0);
	        System.out.println("Value: " + getCharacterDataFromElement(nameElement));
        	component.setValueComponent(getCharacterDataFromElement(nameElement));
        }
        
        NodeList error = element.getElementsByTagName(ERROR);
        if (error!= null && error.getLength() > 0) {
        	Element nameElement = (Element) error.item(0);
	        System.out.println("Error: " + getCharacterDataFromElement(nameElement));
        	component.setError(getCharacterDataFromElement(nameElement));
        }
        
        NodeList ping = element.getElementsByTagName(PING);
        if (ping!= null && ping.getLength() > 0) {
        	Element nameElement = (Element) ping.item(0);
	        System.out.println("Ping :" + getCharacterDataFromElement(nameElement));
        	component.setPing(getCharacterDataFromElement(nameElement));
        }
        return component;
	}
	
	/**
	 * @param xmlContent
	 * @return
	 */
	public List<CpuDTO> getOriginalCPUForMem(String xmlContent) {
		List<CpuDTO> cpus = new ArrayList<CpuDTO>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			
			NodeList elementList = doc.getElementsByTagName(CPU_ITEM);
			int totalElements = elementList.getLength();
			
			CpuDTO cpuDto = null;
			Element element = null;
			if (totalElements > 0) {
				for (int i = 0; i < totalElements; i++) {
				  element = (Element) elementList.item(i);
				  cpuDto = new CpuDTO();
			      cpuDto = toOriginalCPU(cpuDto, element);
			      cpus.add(cpuDto);
				}
			}
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return cpus;
	}
	
	/**
	 * @param xmlContent
	 * @return
	 */
	public List<FileSystemCacheDto> getFileSystem(String xmlContent) {
		List<FileSystemCacheDto> fileSystems = new ArrayList<FileSystemCacheDto>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			
			NodeList elementList = doc.getElementsByTagName("filesystem");
			int totalElements = elementList.getLength();
			
			FileSystemCacheDto jvmDto = null;
			Element element = null;
			if (totalElements > 0) {
				for (int i = 0; i < totalElements; i++) {
				  element = (Element) elementList.item(i);
				  jvmDto = new FileSystemCacheDto();
			      jvmDto = toFileSystem(jvmDto, element);
			      fileSystems.add(jvmDto);
				}
			}
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return fileSystems;
	}
	
	/**
	 * @param xmlContent
	 * @return
	 */
	public List<MemoryDto> getPhysicalCPUForMem(String xmlContent) {
		List<MemoryDto> physicals = new ArrayList<MemoryDto>();
		try {

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlContent));
			Document doc = docBuilder.parse(is);

			// normalize text representation
			doc.getDocumentElement().normalize();
			
			NodeList elementList = doc.getElementsByTagName("cpu_physical");
			int totalElements = elementList.getLength();
			
			MemoryDto physicalCpuDto = null;
			Element element = null;
			if (totalElements > 0) {
				for (int i = 0; i < totalElements; i++) {
				  element = (Element) elementList.item(i);
				  physicalCpuDto = new MemoryDto();
			      physicalCpuDto = toCPUPhysical(physicalCpuDto, element);
			      physicals.add(physicalCpuDto);
				}
			}
		} catch (Throwable t) {
			logger.info(t.getCause().getMessage());
		}
		return physicals;
	}
	
	private JvmDto xmlToJVMForMemCache(JvmDto jvmDto, Element element) {
		NodeList name = element.getElementsByTagName(FREE_MEM);
        if (name!= null && name.getLength() > 0) {
        	Element nameElement = (Element) name.item(0);
	        jvmDto.setFreeMemory(Double.parseDouble(getCharacterDataFromElement(nameElement)));
        }
        
        NodeList value = element.getElementsByTagName(TOTAL_MEM);
        if (value!= null && value.getLength() > 0) {
        	Element nameElement = (Element) value.item(0);
	        jvmDto.setTotalMemory(Double.parseDouble(getCharacterDataFromElement(nameElement)));
        }
        
        NodeList error = element.getElementsByTagName(MAX_MEM);
        if (error!= null && error.getLength() > 0) {
        	Element nameElement = (Element) error.item(0);
	        jvmDto.setMaxMemory(Double.parseDouble(getCharacterDataFromElement(nameElement)));
        }
        
        NodeList ping = element.getElementsByTagName(USED_MEM);
        if (ping!= null && ping.getLength() > 0) {
        	Element nameElement = (Element) ping.item(0);
	        jvmDto.setUsedMemory(Double.parseDouble(getCharacterDataFromElement(nameElement)));
        }
        return jvmDto;
	}
	
	private ServiceMonitorDto xmlToServiceMonitor(ServiceMonitorDto component, Element element) {
		NodeList name = element.getElementsByTagName(NAME);
        if (name!= null && name.getLength() > 0) {
        	Element nameElement = (Element) name.item(0);
	        System.out.println("Name: " + getCharacterDataFromElement(nameElement));
        	component.setName(getCharacterDataFromElement(nameElement));
        }
        
        NodeList value = element.getElementsByTagName(VALUE);
        if (value!= null && value.getLength() > 0) {
        	Element nameElement = (Element) value.item(0);
	        System.out.println("Value: " + getCharacterDataFromElement(nameElement));
        	component.setDescription(getCharacterDataFromElement(nameElement));
        }
        
        NodeList error = element.getElementsByTagName(ERROR);
        if (error!= null && error.getLength() > 0) {
        	Element nameElement = (Element) error.item(0);
	        System.out.println("Error: " + getCharacterDataFromElement(nameElement));
        	component.setError(getCharacterDataFromElement(nameElement));
        }
        
        NodeList ping = element.getElementsByTagName(PING);
        if (ping!= null && ping.getLength() > 0) {
        	Element nameElement = (Element) ping.item(0);
	        System.out.println("Ping :" + getCharacterDataFromElement(nameElement));
        	component.setPing(Integer.parseInt(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
        }
        return component;
	}
	
	
	private CpuDTO toOriginalCPU(CpuDTO cpuDto, Element element) {
		NodeList name = element.getElementsByTagName("usage");
        if (name!= null && name.getLength() > 0) {
        	Element nameElement = (Element) name.item(0);
	        cpuDto.setCpuUsage(Integer.parseInt(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
        }
        
        NodeList value = element.getElementsByTagName("vendor");
        if (value!= null && value.getLength() > 0) {
        	Element nameElement = (Element) value.item(0);
	        cpuDto.setVendor(getCharacterDataFromElement(nameElement));
        }
        
        NodeList error = element.getElementsByTagName("model");
        if (error!= null && error.getLength() > 0) {
        	Element nameElement = (Element) error.item(0);
	        cpuDto.setModel(getCharacterDataFromElement(nameElement));
        }
        
        NodeList ping = element.getElementsByTagName("total");
        if (ping!= null && ping.getLength() > 0) {
        	Element nameElement = (Element) ping.item(0);
	        cpuDto.setTotalCpu(Integer.parseInt(getCharacterDataFromElement(nameElement)));
        }
        return cpuDto;
	}
	
	private FileSystemCacheDto toFileSystem(FileSystemCacheDto fileDto, Element element) {
		NodeList fileName = element.getElementsByTagName("file_name");
        if (fileName!= null && fileName.getLength() > 0) {
        	Element nameElement = (Element) fileName.item(0);
	        fileDto.setName(getCharacterDataFromElement(nameElement));
        }
        
        NodeList size = element.getElementsByTagName("size");
        if (size!= null && size.getLength() > 0) {
        	Element nameElement = (Element) size.item(0);
        	String dStrSize =  Ultility.extractDigit(getCharacterDataFromElement(nameElement));
        	double dSize = Ultility.convertKB(Double.parseDouble(dStrSize));
	        fileDto.setSize(dSize);
        }
        
        NodeList used = element.getElementsByTagName("used");
        if (used!= null && used.getLength() > 0) {
        	Element nameElement = (Element) used.item(0);
        	String dStrSize =  Ultility.extractDigit(getCharacterDataFromElement(nameElement));
        	double dSize = Ultility.convertKB(Double.parseDouble(dStrSize));
	        fileDto.setUsed(dSize);
        }
        
        NodeList available = element.getElementsByTagName("available");
        if (available!= null && available.getLength() > 0) {
        	Element nameElement = (Element) available.item(0);
        	String dStrSize =  Ultility.extractDigit(getCharacterDataFromElement(nameElement));
        	double dSize = Ultility.convertKB(Double.parseDouble(dStrSize));
	        fileDto.setAvailable(dSize);
        }
        
        NodeList percent = element.getElementsByTagName("percent_used");
        if (percent!= null && percent.getLength() > 0) {
        	Element nameElement = (Element) percent.item(0);
        	fileDto.setPercentUsed(Integer.parseInt(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
        }
        
        NodeList mount = element.getElementsByTagName("mount");
        if (mount!= null && mount.getLength() > 0) {
        	Element nameElement = (Element) mount.item(0);
	        fileDto.setMount(getCharacterDataFromElement(nameElement));
        }
        
        NodeList type = element.getElementsByTagName("type");
        if (type!= null && type.getLength() > 0) {
        	Element nameElement = (Element) type.item(0);
	        fileDto.setType(getCharacterDataFromElement(nameElement));
        }
        return fileDto;
	}
	
	private MemoryDto toCPUPhysical(MemoryDto cpuDto, Element element) {
		
        NodeList size = element.getElementsByTagName("cpu_type");
        if (size!= null && size.getLength() > 0) {
        	Element nameElement = (Element) size.item(0);
	        cpuDto.setType(getCharacterDataFromElement(nameElement));
        }
        
        NodeList used = element.getElementsByTagName("total");
        if (used!= null && used.getLength() > 0) {
        	Element nameElement = (Element) used.item(0);
	        cpuDto.setTotalMemory(Double.parseDouble(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
        }
        
        NodeList available = element.getElementsByTagName("used");
        if (available!= null && available.getLength() > 0) {
        	Element nameElement = (Element) available.item(0);
	        cpuDto.setUsedMemory(Double.parseDouble(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
        }
        
//        NodeList percent = element.getElementsByTagName("free");
//        if (percent!= null && percent.getLength() > 0) {
//        	Element nameElement = (Element) percent.item(0);
//	        cpuDto.setFree(Double.parseDouble(Ultility.extractDigit(getCharacterDataFromElement(nameElement))));
//        }
        
       
        return cpuDto;
	}
	
	public static void main(String arg[]) {
		
		String xmlRecords = "<data>" + " <employee>" + "   <name>John</name>"
				+ "   <title>Manager</title>" + " </employee>" + " <employee>"
				+ "   <name>Sara</name>" + "   <title>Clerk</title>"
				+ " </employee>" + "</data>";

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlRecords));

			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("employee");

			// iterate the employees
			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				NodeList name = element.getElementsByTagName("name");
				Element line = (Element) name.item(0);
				System.out
						.println("Name: " + getCharacterDataFromElement(line));

				NodeList title = element.getElementsByTagName("ping");
				line = (Element) title.item(0);
				System.out.println("Ping: "
						+ getCharacterDataFromElement(line));
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * output : Name: John Title: Manager Name: Sara Title: Clerk
		 */
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
