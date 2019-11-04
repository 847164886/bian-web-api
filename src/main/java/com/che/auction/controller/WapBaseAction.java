package com.che.auction.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 基类：实现接口（ApplicationContextAware）之后，类就可以方便获得ApplicationContext中的所有bean。
 *     换句话说，就是这个类可以直接获取spring配置文件中，所有有引用到的bean对象。
 * @author zhoufy
 * @date   2015-11-20
 */
public class WapBaseAction implements ApplicationContextAware {

	private final Log logger = LogFactory.getLog(WapBaseAction.class);
	
	/******************************************************************/
	private static final String REDIS_KEY_IMG_CODE = "img-code";	//图片验证码
	//private static final String REDIS_KEY_SMS_CODE = "sms-code";	//短信验证码

	
	private static ApplicationContext appContext;
	private static WebApplicationContext webAppContext;



	/**
	 * 实现接口的方法
	 */
	public void setApplicationContext(ApplicationContext context)throws BeansException {
		appContext = context;  
		webAppContext = (WebApplicationContext)context;
	}

	 /** 
     * 这是一个便利的方法，帮助我们快速得到一个BEAN 
     * @param beanName bean的名字 
     * @return 返回一个bean对象 
     */  
    public static Object getBean(String beanName) {  
        return appContext.getBean(beanName);  
    }  
    
    protected final String getRealPath(String filename){
		return webAppContext.getServletContext().getRealPath(filename);
	}
	protected final String getRealPath(String filename, boolean addSplash){
		String result = webAppContext.getServletContext().getRealPath(filename);
		if(addSplash){
			if(!result.endsWith("/") && !result.endsWith("\\")) result += "/";
		}
		return result;
	}
	 
	public ServletContext getSC(HttpServletRequest request) {
		return request.getSession().getServletContext();
	}

	protected final String forwardErrorPage(ModelMap model, String msg){
		return forwardErrorPage(model, msg, "data");
	}
	protected final String forwardErrorPage(ModelMap model, Map<String,Object> jsonMap){
		return forwardErrorPage(model, jsonMap, "data");
	}
	protected final String forwardErrorPage(ModelMap model, Map<String,Object> jsonMap, String jsname){
		if(jsonMap == null) jsonMap = new HashMap<String,Object>();
		jsonMap.put("success", false);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "vm_h5/common/error.vm";
	}
	protected final String forwardErrorPage(ModelMap model, String msg, String jsname){
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("success", false);
		jsonMap.put("html", msg);
		jsonMap.put("msg", msg);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "vm_h5/common/error.vm";
	}
	 
	protected final String showJsonError(ModelMap model, String msg){
		return showJsonError(model, msg, "data");
	}
	protected final String showJsonError(ModelMap model, Map<String,Object> jsonMap){
		return showJsonError(model, jsonMap, "data");
	}
	protected final String showJsonError(ModelMap model, Map<String,Object> jsonMap, String jsname){
		if(jsonMap == null) jsonMap = new HashMap<String,Object>();
		jsonMap.put("success", false);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "json.html";
	}
	
	protected final String showJsonError(ModelMap model, String msg, String jsname){
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("success", false);
		jsonMap.put("html", msg);
		jsonMap.put("msg", msg);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "json.html";
	}
	
	protected final String showJsonSuccess(ModelMap model){
		return showJsonSuccess(model, "");
	}
	
	protected final String showJsonSuccess(ModelMap model, String retval){
		return showJsonSuccess(model, retval, "data");
	}
	
	protected final String showJsonSuccess(ModelMap model, Object object){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("data", object);
		return showJsonSuccess(model, map);
	}
	
	protected final String showJsonSuccess(ModelMap model, Map<String,Object> jsonMap){
		return showJsonSuccess(model, jsonMap, "data");
	}
	
	protected final String showJsonSuccess(ModelMap model, Map<String,Object> jsonMap, String jsname){
		jsonMap.put("success", true);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "json.html";
	}
	
	protected final String showJsonSuccess(ModelMap model, String retval, String jsname){
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("success", true);
		jsonMap.put("html", retval);
		jsonMap.put("retval", retval);
		model.put("jsonMap", jsonMap);
		model.put("jsname", jsname);
		return "json.html";
	}
	
	protected final String showRedirect(String path, ModelMap model){
		//if(StringUtils.startsWith(path, "/")) path = path.substring(1);
		StringBuilder targetUrl = new StringBuilder(path);
		//appendQueryProperties(targetUrl, model, "utf-8");
		model.put("redirectUrl", targetUrl.toString());
		return "/common/redirect.html";
	}
	
	
	public static String getURL(HttpServletRequest request) {
		String contextPath = request.getContextPath().equals("/") ? ""
				: request.getContextPath();
		String url = "http://" + request.getServerName();
		if (request.getServerPort() != 80) {
			url = url + ":" + request.getServerPort() + contextPath;
		} else {
			url = url + contextPath;
		}
		return url;
	}

}