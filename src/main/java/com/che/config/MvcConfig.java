package com.che.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.SimpleServletHandlerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import com.che.core.CP_InitializingInterceptor;
import com.che.core.CP_PropertyEditorRegistrar;
import com.che.core.CP_SimpleMappingExceptionResolver;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.che", useDefaultFilters = false, includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
public class MvcConfig extends WebMvcConfigurationSupport {
//	private static final Logger logger = LogManager.getLogger(MvcConfig.class);

	@Override
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }
	
    @Bean
    public MappingJackson2HttpMessageConverter responseBodyConverter() {
    	MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.getObjectMapper().setSerializationInclusion(Include.NON_NULL);
		converter.setSupportedMediaTypes(Arrays.asList(
				new MediaType("application", "json", Charset.forName("UTF-8")),
				new MediaType("text", "plain", Charset.forName("UTF-8")),
				new MediaType("text", "xml", Charset.forName("UTF-8")),
				new MediaType("text", "html", Charset.forName("UTF-8"))
				));
        return converter;
    }
    

	@Bean
	public VelocityConfigurer velocityConfigurer() {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("input.encoding", "UTF-8");
		map.put("output.encoding", "UTF-8");
		VelocityConfigurer vc = new VelocityConfigurer();
		vc.setResourceLoaderPath("WEB-INF/pages/");
		vc.setVelocityPropertiesMap(map);
		return vc;
	}
	/**
	 * 描述 : velocity视图处理
	 * @return
	 */
	@Bean
	public VelocityViewResolver viewResolver1() {
		VelocityViewResolver vvr = new VelocityViewResolver();
		vvr.setContentType("text/html;charset=UTF-8");
		vvr.setViewClass(VelocityToolboxView.class);
		vvr.setOrder(1);
		return vvr;
	}
    
	/**
	 * 描述 : jsp视图处理
	 * @return
	 */
	@Bean
	public ViewResolver viewResolver2() {
		InternalResourceViewResolver irvr = new InternalResourceViewResolver();
		irvr.setPrefix("/WEB-INF/pages/");
		irvr.setContentType("text/html;charset=UTF-8");
		irvr.setViewClass(org.springframework.web.servlet.view.JstlView.class);
		irvr.setOrder(3);
		return irvr;
	}

	/**
	 * 描述 : <注册消息资源处理器>. <br>
	 * 
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
//		logger.info("MessageSource");
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("config.messages.messages");
		return messageSource;
	}

	/**
	 * 描述 : <注册servlet适配器>. <br>
	 * <p>
	 * <只需要在自定义的servlet上用@Controller("映射路径")标注即可>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public HandlerAdapter servletHandlerAdapter() {
//		logger.info("HandlerAdapter");
		return new SimpleServletHandlerAdapter();
	}

	/**
	 * 描述 : <本地化拦截器>. <br>
	 * 
	 * @return
	 */
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
//		logger.info("LocaleChangeInterceptor");
		return new LocaleChangeInterceptor();
	}

	/**
	 * 描述 : <基于cookie的本地化资源处理器>. <br>
	 * 
	 * @return
	 */
	@Bean(name = "localeResolver")
	public CookieLocaleResolver cookieLocaleResolver() {
//		logger.info("CookieLocaleResolver");
		return new CookieLocaleResolver();
	}

	/**
	 * 描述 : <注册自定义拦截器>. <br>
	 * 
	 * @return
	 */
	@Bean
	public CP_InitializingInterceptor initializingInterceptor() {
//		logger.info("CP_InitializingInterceptor");
		return new CP_InitializingInterceptor();
	}

	/**
	 * 描述 : <RequestMappingHandlerMapping需要显示声明，否则不能注册自定义的拦截器>. <br>
	 * <p>
	 * <这个比较奇怪,理论上应该是不需要的>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//		logger.info("RequestMappingHandlerMapping");
		return super.requestMappingHandlerMapping();
	}

	/**
	 * 描述 : <添加拦截器>. <br>
	 * 
	 * @param registry
	 */
	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
//		logger.info("addInterceptors start");
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(initializingInterceptor());
//		logger.info("addInterceptors end");
	}

	/**
	 * 描述 : <HandlerMapping需要显示声明，否则不能注册资源访问处理器>. <br>
	 * <p>
	 * <这个比较奇怪,理论上应该是不需要的>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public HandlerMapping resourceHandlerMapping() {
//		logger.info("HandlerMapping");
		return super.resourceHandlerMapping();
	}

	/**
	 * 描述 : <资源访问处理器>. <br>
	 * <p>
	 * <可以在jsp中使用/static/**的方式访问/WEB-INF/static/下的内容>
	 * </p>
	 * 
	 * @param registry
	 */
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//		logger.info("addResourceHandlers");
		registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/static/");
		registry.addResourceHandler("/resource/**").addResourceLocations("/resource/");
	}

	/**
	 * 描述 : <文件上传处理器>. <br>
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() {
//		logger.info("CommonsMultipartResolver");
		return new CommonsMultipartResolver();
	}

	/**
	 * 描述 : <异常处理器>. <br>
	 * <p>
	 * <系统运行时遇到指定的异常将会跳转到指定的页面>
	 * </p>
	 * 
	 * @return
	 */
	@Bean(name = "exceptionResolver")
	public CP_SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//		logger.info("CP_SimpleMappingExceptionResolver");
		CP_SimpleMappingExceptionResolver simpleMappingExceptionResolver = new CP_SimpleMappingExceptionResolver();
		simpleMappingExceptionResolver.setDefaultErrorView("common_error");
		simpleMappingExceptionResolver.setExceptionAttribute("exception");
		Properties properties = new Properties();
		properties.setProperty("java.lang.RuntimeException", "common_error");
		simpleMappingExceptionResolver.setExceptionMappings(properties);
		return simpleMappingExceptionResolver;
	}

	/**
	 * 描述 : <RequestMappingHandlerAdapter需要显示声明，否则不能注册通用属性编辑器>. <br>
	 * <p>
	 * <这个比较奇怪,理论上应该是不需要的>
	 * </p>
	 * 
	 * @return
	 */
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
//		logger.info("RequestMappingHandlerAdapter");
		return super.requestMappingHandlerAdapter();
	}

	/**
	 * 描述 : <注册通用属性编辑器>. <br>
	 * <p>
	 * <这里只增加了字符串转日期和字符串两边去空格的处理>
	 * </p>
	 * 
	 * @return
	 */
	@Override
	protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
//		logger.info("ConfigurableWebBindingInitializer");
		ConfigurableWebBindingInitializer initializer = super.getConfigurableWebBindingInitializer();
		CP_PropertyEditorRegistrar register = new CP_PropertyEditorRegistrar();
		register.setFormat("yyyy-MM-dd");
		initializer.setPropertyEditorRegistrar(register);
		return initializer;
	}

}
