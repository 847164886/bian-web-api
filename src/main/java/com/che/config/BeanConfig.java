package com.che.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.che.common.id.IdWorker;

@Configuration
public class BeanConfig {
    @Value("${idworker.workerId}")
    private Long workerId;
    
    @Value("${app.versionCode}")
    private Integer versionCode;
    
    @Bean
    public IdWorker idWorker(){
        IdWorker idWorker = new IdWorker(workerId);
        return idWorker;
    }
    
//    @Bean
//    public TenRefundService tenRefundService() {
//    	TenRefundService tenRefundService = new TenRefundService();
//    	return tenRefundService;
//    }
     
}
