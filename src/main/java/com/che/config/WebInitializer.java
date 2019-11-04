package com.che.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.alibaba.druid.support.http.StatViewServlet;
import com.che.config.listener.SessionListener;
import com.che.demo.servlet.DemoServlet;

@Order(2)
public class WebInitializer implements WebApplicationInitializer {
	private static final Log logger = LogFactory.getLog(WebInitializer.class);
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
      //  servletContext.setInitParameter("isLog4jAutoInitializationDisabled", "true");
        
		// session-timeout
//		servletContext.addListener(new SessionListener());
		
		//Log4jConfigListener
//		servletContext.setInitParameter("log4jConfiguration", "classpath:log4j2.xml");
		
//		servletContext.addListener(Log4jServletContextListener.class);
//		Log4jServletFilter log4jServletFilter = new Log4jServletFilter();
//		FilterRegistration.Dynamic log4jFilter = servletContext.addFilter("log4jServletFilter", log4jServletFilter);
//		log4jFilter.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
//		
		// filter:CharacterEncodingFilter
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
		characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
		
		// servlet:druid stat view
		ServletRegistration.Dynamic druidStatView = servletContext.addServlet("druidStatView", new StatViewServlet());
		druidStatView.addMapping("/druid/*");
		
		// demoServlet
		DemoServlet demoServlet = new DemoServlet();
		ServletRegistration.Dynamic dynamic = servletContext.addServlet("demoServlet", demoServlet);
		dynamic.setLoadOnStartup(2);
		dynamic.addMapping("/demo_servlet");
		String ip = IpConfig.getServerip();
		logger.info("====serverIp=" + ip);

		boolean isLocal = true;
		if (IpConfig.isLocalIp(ip)) {
			logger.info("====加载config_local文件");
			isLocal = true;
		} else if (IpConfig.isTestIp(ip)) {
			logger.info("====加载config_test文件");
			isLocal = true;
		} else if (IpConfig.isPreLineIp(ip)) {
			logger.info("====加载config_preLine文件");
			isLocal = true;
		} else {
			logger.info("====加载config_remote文件");
			isLocal = false;
		}
		if(isLocal){
			servletContext.setAttribute("resPath","/csjpapp/resource");
			servletContext.setAttribute("cssPath","/csjpapp/resource/css");
			servletContext.setAttribute("jsPath","/csjpapp/resource/js");
			servletContext.setAttribute("staticPath","/csjpapp/resource");
			servletContext.setAttribute("imgPath","/csjpapp/resource/images");
			servletContext.setAttribute("cdnImgPath","http://img.che.com");
			servletContext.setAttribute("basePath","/csjpapp");
			servletContext.setAttribute("chePath","http://www.che.com");
			servletContext.setAttribute("mzPath","http://m.che.com");
			servletContext.setAttribute("newsPath","http://news.m.che.com");
			servletContext.setAttribute("newPath","http://new.m.che.com");
			servletContext.setAttribute("jinkouPath","http://jinkou.m.che.com");
			servletContext.setAttribute("vcrPath","http://checheng-test.oss-cn-shanghai.aliyuncs.com");

			servletContext.setAttribute("noImg","http://static.che.com/checdn/15e0b84c-83e5-4315-9a21-71b623bb7856.png"); 
		}else{
			servletContext.setAttribute("resPath","/csjpapp/resource");
			servletContext.setAttribute("cssPath","/csjpapp/resource/css");
			servletContext.setAttribute("jsPath","/csjpapp/resource/js");
			servletContext.setAttribute("imgPath","/csjpapp/resource/images");
			servletContext.setAttribute("staticPath","http://static.che.com/m");
			servletContext.setAttribute("cdnImgPath","http://img.che.com");
			servletContext.setAttribute("basePath","/csjpapp");
			servletContext.setAttribute("chePath","http://www.che.com");
			servletContext.setAttribute("mzPath","http://m.che.com");
			servletContext.setAttribute("newsPath","http://news.m.che.com");
			servletContext.setAttribute("newPath","http://new.m.che.com");
			servletContext.setAttribute("jinkouPath","http://jinkou.m.che.com");
			servletContext.setAttribute("vcrPath","http://img.che.com");
			servletContext.setAttribute("noImg","http://static.che.com/checdn/15e0b84c-83e5-4315-9a21-71b623bb7856.png");  
		}
//		 WebApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
//		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
//		Config config = ctx.getBean(Config.class);
//		config.initPageTools(isLocal);
//
//
//		if (isLocal) {
//			servletContext.setAttribute("isLocal", "true");
//		}
//		servletContext.setAttribute("webPath", config.getString("webPath"));
//		servletContext.setAttribute("imgPath", config.getString("imgPath"));
//
//		if (IpConfig.isTestIp(ip)) {
//			servletContext.setAttribute("resPath", config.getString("resPath_test"));
//		}
//		logger.info("---webPath:" + config.getString("webPath"));
//
//		servletContext.setAttribute("current_webPath", config.getString("imageWebServer"));
//		servletContext.setAttribute("imageWebServer", config.getString("imageWebServer"));

	}
}
