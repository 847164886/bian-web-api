package com.che.common.httpclient;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.che.common.crypt.Base64;
import com.che.common.crypt.SecurityUtil;

public class ViolationHttpUtils {

	public static String sendAndReceive(LinkedHashMap<String, String> dataMap, String URL)
	        throws Exception { 

		// 构建请求参数
		String xmlRequestString = buildViolationXMLRequestString(dataMap);
		// 请求URL
		HttpClient httpClient = new HttpClient();
		PostMethod post = new PostMethod(URL);
		post.addRequestHeader("Content-Type", "application/json");

		RequestEntity re = new StringRequestEntity(xmlRequestString);
		post.setRequestEntity(re);
		post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));

		try {
			httpClient.executeMethod(post);
			String responseText = post.getResponseBodyAsString();
			return base64DecodeRule(responseText);
		} catch (Exception e) {
			throw e;
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}
	
	private static String buildViolationXMLRequestString(LinkedHashMap<String, String> dataMap) {

		StringBuilder content_head = new StringBuilder("<CXY>");
		content_head.append("<REQKEY>").append(SecurityUtil.USERNAME).append("</REQKEY>");
		content_head.append("<VERSION>3</VERSION>");

		StringBuilder xmlParams = new StringBuilder("");
		xmlParams.append("<REQDATA>");
		for (Entry<String, String> entry : dataMap.entrySet()) {
			xmlParams.append("<").append(entry.getKey()).append(">").append(entry.getValue())
			         .append("</").append(entry.getKey()).append(">");
		}
		xmlParams.append("</REQDATA>");
		System.out.println("xmlParams.toString()="+xmlParams.toString());
		// 请求内容加密
		String reqDate = SecurityUtil.encryptByPassword(SecurityUtil.PASSWORD, xmlParams.toString());
		StringBuilder content_reqdata = new StringBuilder("<REQDATA>");
		content_reqdata.append(reqDate).append("</REQDATA>");
		StringBuilder content_foot = new StringBuilder("</CXY>");

		return content_head.append(content_reqdata.toString()).append(content_foot.toString()).toString();
	}

	
	private static String base64DecodeRule(String content) {
		String result = null;
		if(content != null){
			try {
	            content = new String(Base64.decode(content),"UTF-8");
	            // 去掉后32位MD5加密信息
	            String contentStr = content.substring(0, content.length() - 32);
	            // 去掉前16位随机数，截取明文信息
            	result = contentStr.substring(16, contentStr.length());
            } catch (Exception e) {
	            e.printStackTrace();
            }
		}
		return result;
	}
}
