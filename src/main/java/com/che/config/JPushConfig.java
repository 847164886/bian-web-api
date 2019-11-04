package com.che.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class JPushConfig {

	@Value("${jpush.appKey}")
	private String appKey;

	@Value("${jpush.masterSecret}")
	private String masterSecret;

	@Value("${jpush.timeToLive}")
	private Long timeToLive;

	@Value("${jpush.apnsProduction}")
	private boolean apnsProduction;

	@Value("${jpush.maxAlias}")
	private Long maxAlias;

}
