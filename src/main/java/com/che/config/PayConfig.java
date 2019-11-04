package com.che.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.che.pay.ali.config.AliPayInit;
import com.che.pay.ten.config.TenPayInit;
import com.che.pay.ten.redis.RedisTenPayPrepayIdTool;

@Configuration
public class PayConfig {
	@Resource
	private RedisTemplate redisTemplate;

	@Resource
	private AliPayInit aliPayInit;

	@Resource
	private TenPayInit tenPayInit;

	@Bean
	public RedisTenPayPrepayIdTool redisTenPayPrepayIdTool() {
		RedisTenPayPrepayIdTool ret = new RedisTenPayPrepayIdTool();
		ret.setRedisTemplate(redisTemplate);
//		System.out.println(AlipayConfig.seller_email);
//		System.out.println(TenPayConfig.PARTNER);
		return ret;
	}

}
