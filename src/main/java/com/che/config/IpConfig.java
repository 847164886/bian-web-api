package com.che.config;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IpConfig {
	
	private static final Log logger = LogFactory.getLog(IpConfig.class);
	
	private static final String serverIp;
	private static final String hostname;
	private static final List<String> localIp = Arrays.asList("127.0.0.1");
	private static final List<String> testIp = Arrays.asList("192.168.1.32");
	private static final List<String> preLineIp = Arrays.asList("192.168.1.37","192.168.1.45");	//预上线ip
	private static final String[] remoteIp = new String[]{"139.196."};

	private static final List<String> officeIp = Arrays.asList("101.95.157.134", "124.74.105.54");
	private static final List<String> houtaiIp = Arrays.asList("172.28.11.20","172.28.11.40","172.28.11.50","172.28.11.60");
	
	
	static{
		String[] host = IpConfig.getServerAddr();
		serverIp = host[0];
		logger.info("host[0]="+host[0]+", host[1]="+host[1]);
		//hostname = host[1];
		String h = "";
		try {
			h = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.error("", e);
		}
		if(StringUtils.isBlank(h)){
			h = host[1];
		}
		hostname = h;
	}
 
	/**
	 * 是否本机或测试IP
	 * @return
	 */
	public static final boolean isLocalIp() {
		return isLocalIp(getServerip());
	}
	
	public static final boolean isLocalIp(String ip) {
		return localIp.contains(ip); // 本地
	}
	
	/**
	 * 是否测试环境IP
	 * @return
	 */
	public static final boolean isTestIp() {
		return isTestIp(getServerip());
	}
	
	public static final boolean isTestIp(String ip) {
		return testIp.contains(ip); // 本地
	}
	
	public static final boolean isPreLineIp(String ip) {
		return preLineIp.contains(ip); // 本地
	}
	
	public static boolean isOfficeIp(String ip){
		return officeIp.contains(ip);
	}

	public static boolean isHoutaiIp(String ip){
		return houtaiIp.contains(ip);
	}
	 
	public static String[] getServerAddr(){
		Map<String, String> hostMap = getServerAddrMap();
		
		//1.先过生产的IP
		for(String search: remoteIp){
			for(String addr: hostMap.keySet()){
				if(addr.startsWith(search)){
					return new String[]{addr, hostMap.get(addr)};
				}
			}
		}
		//2.过预上线ip
		for(String search: preLineIp){
			for(String addr: hostMap.keySet()){
				if(addr.startsWith(search)){
					return new String[]{addr, hostMap.get(addr)};
				}
			}
		}
		//3.再过测试环境的IP
		for(String search: testIp){
			for(String addr: hostMap.keySet()){
				if(addr.startsWith(search)){
					return new String[]{addr, hostMap.get(addr)};
				}
			}
		}
		return new String[]{"127.0.0.1", "localhost"};
	}
	private static Map<String, String> getServerAddrMap(){
		Map<String, String> hostMap = new TreeMap<String, String>();
		try{
			Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
			while(niList.hasMoreElements()){
				NetworkInterface ni = niList.nextElement();
				Enumeration<InetAddress> addrList = ni.getInetAddresses();
				while(addrList.hasMoreElements()){
					InetAddress addr = addrList.nextElement();
					//logger.warn("-----------"+addr.getHostAddress()+", "+addr.getHostName());
					if(addr instanceof Inet4Address) {//只做IPV4
						hostMap.put(addr.getHostAddress(), addr.getHostName());
					}
				}
			}
		}catch(Exception e){
		}
		return hostMap;
	}
	public static String getServerip() {
		return serverIp;
	}
	public static String getHostname() {
		return hostname;
	}
	
}
