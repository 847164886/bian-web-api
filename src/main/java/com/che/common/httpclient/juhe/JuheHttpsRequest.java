/**
 * 
 */
package com.che.common.httpclient.juhe;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.che.common.httpclient.HttpsRequest;

/**
 * @author karlhell
 *
 */
public class JuheHttpsRequest {
	
	public static final String DTYPE_JSON = "json";

	public static JuheData postJuhe(String url, Map<String,String> paramMap){
		return handleError(JSON.parseObject(HttpsRequest.sentPost(url,paramMap), JuheData.class));
	}
	
	public static JuheData sentMultiPost(String url, Map<String,Object> paramMap){
		return handleError(JSON.parseObject(HttpsRequest.sentMultiPost(url,paramMap), JuheData.class));
	}
	
	public static JuheData postJuheWithError(String url, Map<String,String> paramMap){
		return JSON.parseObject(HttpsRequest.sentPost(url,paramMap), JuheData.class);
	}
	
	public static JuheData getJuhe(String url, Map<String,String> paramMap){
		
		return handleError(JSON.parseObject(HttpsRequest.sentGet(url,paramMap), JuheData.class));
	}
	
	private static JuheData handleError(JuheData data){
		if(data.getError_code().equals(JuheConstantsCode.E10001)
		||data.getError_code().equals(JuheConstantsCode.E10002)		
		||data.getError_code().equals(JuheConstantsCode.E10003)		
		||data.getError_code().equals(JuheConstantsCode.E10004)		
		||data.getError_code().equals(JuheConstantsCode.E10005)		
		||data.getError_code().equals(JuheConstantsCode.E10007)		
		||data.getError_code().equals(JuheConstantsCode.E10008)		
		||data.getError_code().equals(JuheConstantsCode.E10009)		
		||data.getError_code().equals(JuheConstantsCode.E10011)		
		||data.getError_code().equals(JuheConstantsCode.E10012)		
		||data.getError_code().equals(JuheConstantsCode.E10013)		
		||data.getError_code().equals(JuheConstantsCode.E10014)		
		||data.getError_code().equals(JuheConstantsCode.E10020)		
		||data.getError_code().equals(JuheConstantsCode.E10021)		
		){
			data.setError_code("-1");
		}
		return data;
	}
	
}
