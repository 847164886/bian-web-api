package com.che.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class WeiXinUtils {

	static String appId = "wx4c6779f0a76feb07";
	static String appSecret = "5a9e474f97fd738ae5cbb31700cea25c";
	
	public static String getAccessToken() {
		String url = "https://api.weixin.qq.com/cgi-bin/token?appid=AppId&secret=AppSecret&grant_type=client_credential";
		url = url.replace("AppId", appId).replace("AppSecret", appSecret);
		String jsonResult = HttpGet.getUrl(url);
		Map<String,String> map = (Map<String,String>) JSONObject.parse(jsonResult);
		
		return map.get("access_token");
	}
	
	
	public static String getTicket(String token) {
		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+token+"&type=jsapi";
		String jsonResult = HttpGet.getUrl(url);
		Map<String,String> map = (Map<String,String>) JSONObject.parse(jsonResult);
		return map.get("ticket");
	}
	
	public static String getSignature(String ticket, Long times, String randStr, String url){
		String content = "jsapi_ticket="+ticket+"&noncestr="+randStr+"&timestamp="+ times+"&url="+url;
		return ShaUtils.SHA1(content);
	}
	
	public static boolean isInTime(Long nowTimes, String timestamps){
		if(StringUtils.isBlank(timestamps)){
			return false;
		}
		Long timeStamps = Long.parseLong(timestamps);
		if((nowTimes - timeStamps) > 7100){
			return false;
		}
		return true;
	}
	
	public static void main(String[] args){
		String token = getAccessToken();
		System.out.println(token);
		
		System.out.println(getTicket(token));
	}
	
}
