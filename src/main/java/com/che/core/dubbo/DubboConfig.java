package com.che.core.dubbo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;

/**
 * Dubbo配置
 * 
 * @author zhoufy
 * @see 2016-2-23
 */

@Configuration
public class DubboConfig {

	private static final Logger log = LogManager.getLogger(DubboConfig.class);

	@Autowired
	private DubboApplication dubboApplication;

	@Autowired
	private DubboProtocol dubboProtocol;

	@Autowired
	private DubboProvider dubboProvider;

	@Autowired
	private DubboRegistry dubboRegistry;

	@Bean
	public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packageName) {
		AnnotationBean annotationBean = new AnnotationBean();
		annotationBean.setPackage(packageName);
		log.info("AnnotationBean paceageName=" + packageName);
		return annotationBean;
	}

	@Bean
	public ApplicationConfig applicationConfig() {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName(dubboApplication.getName());
		applicationConfig.setLogger(dubboApplication.getLogger());
		applicationConfig.setOwner("programmer");
		applicationConfig.setOrganization("dubbox");
		return applicationConfig;
	}

	@Bean
	public ProtocolConfig protocolConfig() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName(dubboProtocol.getName());
		protocolConfig.setPort(dubboProtocol.getPort());
		protocolConfig.setAccesslog(String.valueOf(dubboProtocol.isAccessLog()));
		return protocolConfig;
	}

	@Bean
	public ProviderConfig providerConfig(ApplicationConfig applicationConfig, RegistryConfig registryConfig,
			ProtocolConfig protocolConfig) {
		ProviderConfig providerConfig = new ProviderConfig();
		providerConfig.setTimeout(dubboProvider.getTimeout());
		providerConfig.setRetries(dubboProvider.getRetries());
		providerConfig.setDelay(dubboProvider.getDelay());
		providerConfig.setApplication(applicationConfig);
		providerConfig.setRegistry(registryConfig);
		providerConfig.setProtocol(protocolConfig);
		return providerConfig;
	}

	@Bean
	public RegistryConfig registryConfig() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setProtocol(dubboRegistry.getProtocol());
		registryConfig.setAddress(dubboRegistry.getAddress());
		registryConfig.setRegister(dubboRegistry.isRegister());
		registryConfig.setSubscribe(dubboRegistry.isSubscribe());
		return registryConfig;
	}
}
