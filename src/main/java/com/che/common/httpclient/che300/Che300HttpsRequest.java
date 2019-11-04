/**
 * 
 */
package com.che.common.httpclient.che300;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.che.common.httpclient.HttpsRequest;

/**
 * @author star
 *
 */
public class Che300HttpsRequest {
	
	public static JSONObject sentChe300(String url, Map<String,String> paramMap) {
		String interfaceData = HttpsRequest.sentGet(url, paramMap);
		JSONObject jsonObject = JSON.parseObject(interfaceData);
		if ("0".equals(jsonObject.getString("status")))
			return null;
		
		return jsonObject;
	}
}