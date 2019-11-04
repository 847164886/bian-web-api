package com.che.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author karlhell
 *
 */
@Configuration
@PropertySource(value={"classpath:prod.properties","classpath:preline.properties","classpath:test.properties","classpath:dev.properties"},ignoreResourceNotFound=true)
public class PropertyConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
}
