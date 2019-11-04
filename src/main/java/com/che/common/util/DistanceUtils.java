/**
 * 
 */
package com.che.common.util;

/**
 * @author karlhell
 *
 */
public class DistanceUtils {
	/** 
	 * 计算地球上任意两点(经纬度)距离 
	 *  
	 * @param long1 
	 *            第一点经度 
	 * @param lat1 
	 *            第一点纬度 
	 * @param long2 
	 *            第二点经度 
	 * @param lat2 
	 *            第二点纬度 
	 * @return 返回距离 单位：米 
	 */  
	public static double distance(double long1, double lat1, double long2, double lat2) {  
	    double a, b, R;  
	    R = 6378137; // 地球半径  
	    lat1 = lat1 * Math.PI / 180.0;  
	    lat2 = lat2 * Math.PI / 180.0;  
	    a = lat1 - lat2;  
	    b = (long1 - long2) * Math.PI / 180.0;  
	    double d;  
	    double sa2, sb2;  
	    sa2 = Math.sin(a / 2.0);  
	    sb2 = Math.sin(b / 2.0);  
	    d = 2  
	            * R  
	            * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)  
	                    * Math.cos(lat2) * sb2 * sb2));
	    
	    int r = (int) d;
	    
	    if(r >= 1000){
	    	
	    }else{
	    	
	    }
	    
	    return d;  
	}  
	
	/**
	 * 根据距离判断是返回m还是km
	 * @param long1
	 * @param lat1
	 * @param long2
	 * @param lat2
	 * @return
	 */
	public static String distanceToString(double long1, double lat1, double long2, double lat2){
		double d = distance(long1, lat1, long2, lat2);
		int r = (int) d;
		if(r >= 1000){
			return CalcUtil.div(d, 1000D, 1)+" km";
		}else{
			return r+" m";
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(DistanceUtils.distanceToString(121.464211, 31.248786, 121.464581, 31.249759));
	}

}
