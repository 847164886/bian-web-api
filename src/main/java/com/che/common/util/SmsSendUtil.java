package com.che.common.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.che.user.controller.LoginController;

/**
 * 短信工具类
 * 
 * @author zhoufy
 */
public class SmsSendUtil {

	private static final Log logger = LogFactory.getLog(SmsSendUtil.class);

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 *            手机号码
	 * @param smsContent
	 *            短信内容
	 * @return
	 */
	public static boolean sendMsg(String mobile, String smsContent, HttpServletRequest request) {
		if (request == null) {
			logger.info("【短信内容】：" + smsContent);
		} else {
			logger.info("【发送短信】 ip=" + LoginController.getRealIp(request) + "，短信内容：" + smsContent);
		}
		try {
			String result = sendMsgInner(mobile, smsContent);

			JSONObject jsonObj = JSONObject.parseObject(result);
			int error_code = jsonObj.getIntValue("error");
			String error_msg = jsonObj.getString("msg");
			if (error_code == 0) {
				logger.info("【发送短信】发送信息成功.");
				return true; // 发送成功
			} else {
				logger.error("【发送短信】Send message failed, code is " + error_code + ", msg is " + error_msg);
			}
		} catch (Exception ex) {
			logger.error("发送短信", ex);
		}
		return false; // 发送失败
	}

	/**
	 * 发送短信
	 * 
	 * @param mobile
	 * @param smsContent
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String sendMsgInner(String mobile, String smsContent) throws UnsupportedEncodingException {

		smsContent += "【车城】";
		// api:bbc13729a04b8606403fe26e123bc183 原短信平台账户
		Client client = Client.create();
		/*
		 * CloseableHttpClient httpclient = HttpClients.custom().addInterceptorLast(//http请求协议拦截器 new HttpRequestInterceptor() { public void
		 * process(final HttpRequest request, final HttpContext context) throws HttpException, IOException { request.addHeader("Accept-Encoding",
		 * "gzip"); request.addHeader("Authorization","Basic"+ new BASE64Encoder().encode("api:aeb02e2eecec5183085837c2d1162758".getBytes("utf-8")));
		 * } }).build();
		 */
		client.addFilter(new HTTPBasicAuthFilter("api", "aeb02e2eecec5183085837c2d1162758"));
		// client.addFilter(new HTTPBasicAuthFilter("Authorization","Basic"+ new
		// BASE64Encoder().encode("api:aeb02e2eecec5183085837c2d1162758".getBytes("utf-8"))));

		// RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();

		// HttpPost httpPost = new HttpPost("https://sms-api.luosimao.com/v1/send.json");
		WebResource webResource = client.resource("https://sms-api.luosimao.com/v1/send.json");

		MultivaluedMapImpl formData = new MultivaluedMapImpl();
		formData.add("mobile", mobile);
		formData.add("message", smsContent);
		ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);
		String textEntity = response.getEntity(String.class);
		int status = response.getStatus();
		logger.info("【发送短信】 status=" + status);

		return textEntity;
	}

	public static void main(String[] args) {
		 // sendMsg("18616328868", "hello", null);
	}
}
