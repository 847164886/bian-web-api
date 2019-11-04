/**
 * 
 */
package com.che.common.util;
 
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
 
/**
 * Entity AdvOwner
 * @copyright {@link winwho.com}
 * @author simon.sang<Auto generate>
 * @version  2014-04-13 10:02:46
 */
public class XmlParserUtil {
 
	public static Map<String, String> parseXml(HttpServletRequest req){ 
		Map<String, String> map = new HashMap<String, String>(); 
		
		InputStream inputStream = null; 
		try { 
			inputStream = req.getInputStream(); 
			SAXReader reader = new SAXReader(); 
			Document document; 
			document = reader.read(inputStream); 
			
			Element root = document.getRootElement(); 
			List<Element> elementList = root.elements(); 
			for (Element e : elementList) 
				map.put(e.getName(), e.getText()); 
			} catch (Exception e1) { 
				e1.printStackTrace(); 
		} finally{ 
				
			if(inputStream!=null){ 
				try { 
					inputStream.close(); 
				} catch (IOException e) { 
					
				} 
			} 
		} 
			
		return map; 
	}  
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> parsexml(String xml){
		
		Map<String,String> map = new HashMap<String,String>();
		
		try {
			Document documnet = DocumentHelper.parseText(xml);
			
			Element root = documnet.getRootElement();
			
			List<Element> elements = root.elements();
			
			for(Element e: elements)
				 map.put(e.getName(), e.getText());
			
		} catch (DocumentException e) {
			 
			e.printStackTrace();
		}
 
		return map;
	}
 
	 //输出XML
   public static String mapToXML(Map map) {
	   StringBuffer sb = new StringBuffer();
       sb.append("<xml>");
       Set es = map.entrySet();
       Iterator it = es.iterator();
       while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(null != v && !"".equals(v) && !"appkey".equals(k)) {
				String val = (null == map.get(k)) ? "" :(String)map.get(k);
				sb.append("<" + k +">" + val + "</" + k + ">");
			}
		}
       sb.append("</xml>");
		return sb.toString();
	}
	
	public static void mapToXMLTest2(Map map, StringBuffer sb) {  
        Set set = map.keySet();  
        for (Iterator it = set.iterator(); it.hasNext();) {  
            String key = (String) it.next();  
            Object value = map.get(key);  
            if (null == value)  
                value = "";  
            if (value.getClass().getName().equals("java.util.ArrayList")) {  
                ArrayList list = (ArrayList) map.get(key);  
                sb.append("<" + key + ">");  
                for (int i = 0; i < list.size(); i++) {  
                    HashMap hm = (HashMap) list.get(i);  
                    mapToXMLTest2(hm, sb);  
                }  
                sb.append("</" + key + ">");  
  
            } else {  
                if (value instanceof HashMap) {  
                    sb.append("<" + key + ">");  
                    mapToXMLTest2((HashMap) value, sb);  
                    sb.append("</" + key + ">");  
                } else {  
                    sb.append("<" + key + ">" + value + "</" + key + ">");  
                }  
  
            }  
  
        }  
    }  
	
}
