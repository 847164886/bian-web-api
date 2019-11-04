package com.che.config;

import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
/** 
*Description: <应用配置类>. <br>
*<p>
	<负责注册除Controller等web层以外的所有bean，包括aop代理，service层，dao层，缓存，等等>
 </p>
*Makedate:2014年9月3日 上午9:58:15 
* @author Administrator  
* @version V1.0                             
*/  
@Configuration
@ComponentScan(basePackages = "com.che", excludeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value = { Controller.class }) })
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AppConfig {
	/*
	 * <!-- 激活自动代理功能 参看：web.function.aop.aspect.DemoAspect -->
	 * <aop:aspectj-autoproxy proxy-target-class="true" />
	 * @EnableAspectJAutoProxy(proxyTargetClass=true) 与声明下面的bean作用相同
	 */
	@Bean
	public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
		AnnotationAwareAspectJAutoProxyCreator aspectJAutoProxyCreator = new AnnotationAwareAspectJAutoProxyCreator();
		// false:使用JDK动态代理织入增强 [基于目标类的接口] true:使用CGLib动态代理织入增强[基于目标类]
		aspectJAutoProxyCreator.setProxyTargetClass(true);
		return aspectJAutoProxyCreator;
	}
}
