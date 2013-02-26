package cmg.org.monitor.module.servlet;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class DemoDataHandlerServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		PrintWriter pw = response.getWriter();
		response.setContentType("text/xml");
		String xmlTitle = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>";
		try {
			pw.println(xmlTitle);
			pw.println("<CMG>");
			pw.println("<Monitor>");
			pw.println("<server>");
			pw.println("<ip>14.160.33.2</ip>");
			
			pw.println("<database>");
			pw.println("<name> Oracle</name>");
			pw.println("<value>2012-11-01 05:07:55.0</value>");
			pw.println("<error>None</error>");
			pw.println("<ping>Ping time = 0 milliseconds. </ping>");
			pw.println("</database>");
			
			pw.println("<database>");
			pw.println("<name>MSSQL</name>");
			pw.println("<value>2012-11-01 05:06:04.607</value>");
			pw.println("<error>None</error>");
			pw.println("<ping>Ping time = 0 milliseconds. </ping>");
			pw.println("</database>");
			
			pw.println("<ldap>");
			pw.println("<value>2012-06-01 05:06:04:756</value>");
			pw.println("<error>None</error>");
			pw.println("<ping>Ping time = 297 milliseconds. </ping>");
			pw.println("</ldap>");
			
			pw.println("<JVM>");
			pw.println("<Free_Memory>433237128</Free_Memory>");
			pw.println("<Total_Memory>531103744</Total_Memory>");
			pw.println("<Max_Memory>1061814272</Max_Memory> ");
			pw.println("<Used_Memory>97866616</Used_Memory>");
			pw.println("</JVM> ");
			
			pw.println("<filesystem>");
			pw.println("<file_name>C:\\</file_name>");
			pw.println("<size>7.8G</size>");
			pw.println("<used>6.0G</used>");
			pw.println("<available>1.8G</available>");
			pw.println("<percent_used>77%</percent_used>");
			pw.println("<mount>C:\\</mount>");
			pw.println("<type>NTFS/local</type>");
			pw.println("</filesystem>");
			
			pw.println("<filesystem>");
			pw.println("<file_name>D:\\</file_name>");
			pw.println("<size> 12G</size>");
			pw.println("<used>9.5G</used>");
			pw.println("<available>2.2G</available>");
			pw.println("<percent_used>82%</percent_used>");
			pw.println("<mount>D:\\</mount>");
			pw.println("<type>NTFS/local</type>");
			pw.println("</filesystem>");
			
			pw.println("<filesystem>");
			pw.println("<file_name>E:\\</file_name>");
			pw.println("<size> 49G</size>");
			pw.println("<used> 42G</used>");
			pw.println("<available>6.4G</available>");
			pw.println("<percent_used>87%</percent_used>");
			pw.println("<mount>E:\\</mount>");
			pw.println("<type>NTFS/local</type>");
			pw.println("</filesystem>");
			
			pw.println("<filesystem>");
			pw.println("<file_name>F:\\</file_name>");
			pw.println("<size> 17G</size>");
			pw.println("<used> 13G</used>");
			pw.println("<available>4.4G</available>");
			pw.println("<percent_used>74%</percent_used>");
			pw.println("<mount>F:\\</mount>");
			pw.println("<type>NTFS/local</type>");
			pw.println("</filesystem>");
			
			pw.println("<filesystem>");
			pw.println("<file_name>Z:\\</file_name>");
			pw.println("<size>  0 </size>");
			pw.println("<used>  0 </used>");
			pw.println("<available>  0 </available>");
			pw.println("<percent_used>-</percent_used>");
			pw.println("<mount>Z:\\</mount>");
			pw.println("<type>cdrom/cdrom</type>");
			pw.println("</filesystem>");
			
			pw.println("<cpu>");
			pw.println("<usage>28.4%</usage>");
			pw.println("<vendor>Intel</vendor>");
			pw.println("<model>Xeon</model>");
			pw.println("<total>4</total>");
			pw.println("</cpu>");
			
			pw.println("<cpu_physical>");
			pw.println("<cpu_type>Swap</cpu_type>");
			pw.println("<total>2531920</total>");
			pw.println("<used>2129804</used>");
			pw.println("<free>402116</free>");
			pw.println("</cpu_physical>");
			
			pw.println("<cpu_physical>");
			pw.println("<cpu_type>Mem</cpu_type>");
			pw.println("<total>1048044</total>");
			pw.println("<used>917692</used>");
			pw.println("<free>130352</free>");
			pw.println("</cpu_physical>");
			
			
			pw.println("</server>");
			pw.println("</Monitor>");
			pw.println("</CMG>");
			pw.close();
		} catch (Exception e) {
			pw.println(e.getMessage());
			pw.close();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		doGet(request, response);
	}
}
