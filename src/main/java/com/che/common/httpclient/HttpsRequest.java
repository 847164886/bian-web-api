
/**
 * 
 */
package com.che.common.httpclient;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.che.common.util.XmlParserUtil;

/**
 * @author karlhell
 *
 */
public class HttpsRequest {
	
	private static final Logger logger = LogManager.getLogger(HttpsRequest.class);
	
	private static final String ERROR_RETURN = "{\"error_code\":\"-1\",\"reason\":\"请求发生异常\"}";
	private static final String UTF8 = "UTF-8";
	
    //连接超时时间，默认5秒
    private static int socketTimeout = 5000;

    //传输超时时间，默认30秒
    private static int connectTimeout = 30000;
    
    private static HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
		 public boolean retryRequest(
		            IOException exception,
		            int executionCount,
		            HttpContext context) {
			 
		        if (executionCount >= 3) {
		            return false;
		        }
		        if (exception instanceof UnknownHostException) {
		            // Unknown host
		            return false;
		        }
		        if (exception instanceof SSLException) {
		            // SSL handshake exception
		            return false;
		        }
		       
		        return true;
		    }
	};
    
	public static String sentGet(String url, Map<String,String> paramMap){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		List<NameValuePair> formParams = createformParamList(paramMap);
		String param = URLEncodedUtils.format(formParams, UTF8);
		System.out.println("param="+param);
		HttpGet httpGet = new HttpGet(url +"?"+ param);
		httpGet.setConfig(createRequestConfig());
		HttpEntity entity = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			entity = response.getEntity();
			if(null != result){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
	
	public static String sentGetNoParam(String url){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(createRequestConfig());
		HttpEntity entity = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			entity = response.getEntity();
			if(null != result){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
	
	public static String sentGetNoEncode(String url, Map<String,String> paramMap){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		
		StringBuffer sbParam = new StringBuffer();
		for(String key : paramMap.keySet()){
			sbParam.append(key+"="+paramMap.get(key)+"&");
		}
		HttpGet httpGet = new HttpGet(url +"?"+ sbParam.substring(0, sbParam.length()-1).toString());
		System.out.println(httpGet.getURI());
		httpGet.setConfig(createRequestConfig());
		HttpEntity entity = null;
		try {
			CloseableHttpResponse response = httpclient.execute(httpGet);
			entity = response.getEntity();
			if(null != result){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
		return result;
	}

	public static String sentPost(String url, Map<String,String> param){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> formParams = createformParamList(param);
		HttpEntity entity = null;
		try {
			UrlEncodedFormEntity uefEntity = createUefEntity(formParams,UTF8);
			httpPost.setEntity(uefEntity);
			httpPost.setConfig(createRequestConfig());
			
			CloseableHttpResponse response = httpclient.execute(httpPost);  
			entity = response.getEntity();
			if(null != entity){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
	
	public static String sentMultiPost(String url, Map<String,Object> param){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpEntity entity = null;
		try {
			
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
	        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
	        multipartEntityBuilder.setCharset(Charset.forName(UTF8));
	        
	        for(String key : param.keySet()){
	        	if(param.get(key) instanceof File){
	        		multipartEntityBuilder.addPart(key,new FileBody((File) param.get(key)));
	        	}else{
	        		multipartEntityBuilder.addPart(key,new StringBody(param.get(key).toString(), ContentType.TEXT_PLAIN));
	        	}
	        }
	        
			httpPost.setEntity(multipartEntityBuilder.build());
			httpPost.setConfig(createRequestConfig());
			
			CloseableHttpResponse response = httpclient.execute(httpPost);  
			entity = response.getEntity();
			if(null != entity){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
 
	public static String sentPostBody(String url, Map<String,String> param){
		String result = ERROR_RETURN;
		CloseableHttpClient httpclient = closeableHttpClient();
		HttpPost httpPost = new HttpPost(url);
 
		String xml = XmlParserUtil.mapToXML(param);
		 
		System.out.println("paramxml:"+xml);
		logger.info("paramxml:"+xml);
		 
		HttpEntity entity = null;
		try {
			httpPost.setEntity(new StringEntity(xml, UTF8));
			httpPost.setConfig(createRequestConfig());
			
			CloseableHttpResponse response = httpclient.execute(httpPost);  
			entity = response.getEntity();
			if(null != entity){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
	
	public static String sentCertPostBody(CloseableHttpClient httpclient,String url, Map<String,String> param){
		String result = ERROR_RETURN;
	 
		HttpPost httpPost = new HttpPost(url);
 
		String xml = XmlParserUtil.mapToXML(param);
		 
		System.out.println("paramxml:"+xml);
		logger.info("paramxml:"+xml);
		 
		HttpEntity entity = null;
		try {
			httpPost.setEntity(new StringEntity(xml, UTF8));
			httpPost.setConfig(createRequestConfig());
			
			CloseableHttpResponse response = httpclient.execute(httpPost);  
			entity = response.getEntity();
			if(null != entity){
				result = EntityUtils.toString(entity, UTF8);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {  
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }  
		return result;
	}
	
    private static RequestConfig createRequestConfig(){
        return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
    }
	
	public static CloseableHttpClient closeableHttpClient(){
		return HttpClients.custom().setRetryHandler(retryHandler).build();
	}
	
	public static UrlEncodedFormEntity createUefEntity(final List<? extends NameValuePair> formParams ,final String charset) throws UnsupportedEncodingException{
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, UTF8);
		return uefEntity;
	}
	
	public static List<NameValuePair> createformParamList(Map<String,String> param){
		List<NameValuePair> formParams = Lists.newArrayList();
		for(String key : param.keySet()){
			formParams.add(new BasicNameValuePair(key, param.get(key)));
		}
		return formParams;
	}

	public static void main(String[] args) throws IOException {
//		String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALo/fQ8USBV331TaAFiuBcyW/0/26TOGS4X6jUu2az1cZ7Twb8FQpS2+CQcZ7prKI7fbrCevDaUCZm3hkJByxyJBTC50haGUGloIRYWNDduHUXRmk/p8p9UQXbyStmtz9M1FR87kmuNFs7MvWwCIbaT1zgqgZWPJfF3TK+toHXOlAgMBAAECgYEAihQFmEdWBayAQzz34sbpf7s2f0czrsJSfXq6hDYynSRDso5KnAw42Ye4063zCojwIB/ZKvaJ7Bqya6Y98glV/ZhochV8obhXCWOTTMcyMxdKlKlLnURUJn3pxg30IEfDVp5vZyIFPljpWI+hoTO0YCQPljIoana2QHqYYCx2j0UCQQDiOnIdKs0yk0AilIrJEvVQLpJrbtS24JQTquYPZqVfXQXeaQ4xMPJQe91+wWjeKqFYuxavN6qpvh+qdChAYrt/AkEA0sIeu6ZY/9KTBU5tnqJdTOoS3y592mH/QdtUYLnBpQVL1jkRt+1jRsArlr08x7OK9XCvbTtK5IcIxn7q2hfy2wJBANFkr4L3vv509QQM4XjDp3QT50qxRwoOTID3ygGvTJo8C9aU/qHhYCfGvnk4o8wfQUBTyudbawe6Bq1K3obZ3BkCQCWg/QW55IgAW7mF0DKTgJ7759SrI1M7Tey/MSh34egJswmTdONm4eO+6clnDhQFrVi2/ss829lDdsCh5mJ8BiMCQQDhn2RrOCINMIXW6hgSE3CapRnUfQYe/0WHYEZFgksrW7KVaUjxO/iycoRB68tY1d4mBHCCoJF39sUsK3xGZpVv";
//		String uid = "2298884224524d649a309e2420fb0fd4";
//		String pwdMd5 = "e10adc3949ba59abbe56e057f20f883e";
//		String vin = "LSVUH65N7C2066200";
//		String time = MyDateUtils.convertDate2String(MyDateUtils.DEFAILT_DATE_TIME_PATTERN, new Date());
//		System.out.println("date=============="+time);
////		String sign = SignUtils.sign(privateKey, pwdMd5, uid+vin+time);
////		System.out.println("check=============="+SignUtils.checkSign(uid+vin+time, sign, privateKey));
//		
////		购买报告
////		Map<String,String> param = Maps.newHashMap();
////		param.put("uid", uid);
////		param.put("vin", "LSVUH65N7C2066200");
////		param.put("time", time);
////		param.put("sign", sign);
////		System.out.println(sentPost("https://test-pif.chejianding.com/rest/publicif/buy",param));
//		
////		查询订单状态
//		String orderId = "3ea8fe763c2a4955b3e2e6458d370b8d";
//		String sign = SignUtils.sign(privateKey, pwdMd5, uid+orderId+time);
//		
//		Map<String,String> param = Maps.newHashMap();
//		param.put("uid", uid);
//		param.put("orderId", orderId);
//		param.put("time", time);
//		param.put("sign", sign);
//		System.out.println(sentPost("https://test-pif.chejianding.com/rest/publicif/orderInfo",param));
//		
////		查询品牌是否支持
////		String sign = SignUtils.sign(privateKey, pwdMd5, uid+vin+time);
////		
////		Map<String,String> param = Maps.newHashMap();
////		param.put("uid", uid);
////		param.put("vin", vin);
////		param.put("time", time);
////		param.put("sign", sign);
////		System.out.println(sentPost("https://test-pif.chejianding.com/rest/publicif/checkBrand",param));
//		
//		
////		品牌可用性查询接口
////		String sign = SignUtils.sign(privateKey, pwdMd5, uid+time);
////		
////		Map<String,String> param = Maps.newHashMap();
////		param.put("uid", uid);
////		param.put("time", time);
////		param.put("sign", sign);
////		System.out.println(sentPost("https://test-pif.chejianding.com/rest/publicif/brandSynchro",param));
//		
//		
////		param.put("location", "121.464238,31.24874");
////		param.put("keyWord", "汽车配件");
////		param.put("output", "json");
////		param.put("number", "50");
////		param.put("ak", "WB5XXBO61MofcZK4nSi0qffL");
////		param.put("tag", "汽车配件");
////		param.put("mcode", "CB:E0:39:97:33:96:A1:68:8A:A3:04:E2:8F:81:4A:CF:13:D6:FA:D0;com.wswy.carzs");
//		

	}
}
 