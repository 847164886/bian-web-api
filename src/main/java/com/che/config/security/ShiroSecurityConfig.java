package com.che.config.security;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.che.core.dubbo.DubboConfig;
import com.che.user.interfaces.interf.user.ShopUserService;

@Configuration
public class ShiroSecurityConfig {
	@Value("${redis.ip}")
	private String redisIp;

	@Value("${redis.port}")
	private int redisPort;
	
	@Value("${redis.password}")
	private String password;

	@Value("${redis.expire}")
	private int redisExpire;

	@Value("${redis.timeout}")
	private int redisTimeout;

	@Value("${shiro.session.timeout}")
	private Long shiroSessionTimeout;

	@Value("${shiro.session.maxcount}")
	private int shiroSessionMaxcount;

	@Value("${shiro.kickout.url}")
	private String shiroSessionKickoutUrl;

	@Value("${shiro.login.url}")
	private String shiroLoginUrl;

	@Value("${shiro.unlogin.url}")
	private String shiroUnLoginUrl;

	@Value("${shiro.unauthorized.url}")
	private String shiroUnauthorizedUrl;

	@Autowired
	private DubboConfig dubboConfig;

	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(redisIp);
		redisManager.setPort(redisPort);
		redisManager.setPassword(password);
		redisManager.setExpire(redisExpire);
		redisManager.setTimeout(redisTimeout);
		return redisManager;
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		RedisCacheManager rcm = new RedisCacheManager();
		rcm.setRedisManager(redisManager());
		rcm.setKeyPrefix("csjp_shiro_redis_cache:");
		return rcm;
	}

	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setRedisManager(redisManager());
		redisSessionDAO.setKeyPrefix("csjp_shiro_redis_session:");
		return redisSessionDAO;
	}

	@Bean
	public CustomSecurityRealm customSecurityRealm() {
		ReferenceConfig<ShopUserService> reference = new ReferenceConfig<ShopUserService>();
		reference.setApplication(dubboConfig.applicationConfig());
		reference.setRegistry(dubboConfig.registryConfig());
		reference.setInterface(ShopUserService.class);
		reference.setVersion("2.0.1");
		ShopUserService shopUserService = reference.get();
		return new CustomSecurityRealm(shopUserService);
	}

	@Bean
	public WebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(customSecurityRealm());
		securityManager.setSessionManager(sessionManager());
		securityManager.setCacheManager(redisCacheManager());
		return securityManager;
	}

	@Bean
	public SessionManager sessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setGlobalSessionTimeout(shiroSessionTimeout);
		defaultWebSessionManager.setSessionDAO(redisSessionDAO());
		defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);
		return defaultWebSessionManager;
	}

	@Bean
	// @DependsOn(value="lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * 保证实现了Shiro内部lifecycle函数的bean执行
	 * 
	 * @return
	 */
	// @Bean
	// public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
	// return new LifecycleBeanPostProcessor();
	// }

	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		methodInvokingFactoryBean.setArguments(new Object[] { securityManager() });
		return methodInvokingFactoryBean;
	}

	@Bean
	public KickoutSessionControlFilter kickoutSessionControlFilter() {
		KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
		kickoutSessionControlFilter.setCacheManager(redisCacheManager());
		kickoutSessionControlFilter.setSessionManager(sessionManager());
		kickoutSessionControlFilter.setKickoutAfter(false);
		kickoutSessionControlFilter.setMaxSession(shiroSessionMaxcount);
		kickoutSessionControlFilter.setKickoutUrl(shiroSessionKickoutUrl);
		return kickoutSessionControlFilter;
	}

	@Bean
	public ShiroFilterFactoryBean shiroFilter() {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager());
		shiroFilter.setLoginUrl(shiroUnLoginUrl);
		shiroFilter.setUnauthorizedUrl(shiroUnauthorizedUrl);

		Map<String, Filter> filters = new HashMap<>();
		filters.put("kickout", kickoutSessionControlFilter());
		// filters.put("authc", wswyAuthenticationFilter());
		shiroFilter.setFilters(filters);

		Map<String, String> definitionsMap = new HashMap<>();

		/*
		 * definitionsMap.put("/login.jsp", "anon"); // 静态文件，允许任意访问
		 * definitionsMap.put("/admin/**", "roles[admin]"); // 只允许admin角色的用户访问
		 * definitionsMap.put("/democ/**", "roles[member]"); // 只允许member角色的用户访问
		 */
		definitionsMap.put("/maintenance/createOrder", "user");
		definitionsMap.put("/maintenance/accountPay", "user");
		definitionsMap.put("/maintenance/tenpay", "user");
		definitionsMap.put("/recharge/tenpay", "user");
		definitionsMap.put("/auction/**", "user");// anon
		definitionsMap.put("/user/modifyPwd", "user");
		definitionsMap.put("/userInfo/**", "user");
		definitionsMap.put("/message/**", "user");
		definitionsMap.put("/**", "anon");// anon
		shiroFilter.setFilterChainDefinitionMap(definitionsMap);
		return shiroFilter;
	}
}
