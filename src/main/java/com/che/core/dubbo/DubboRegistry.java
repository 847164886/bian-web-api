/**
 * 
 */
package com.che.core.dubbo;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author karlhell
 *
 */
@Data
@Configuration
public class DubboRegistry {
	
	/**
     * 接口协议
     */
	@Value("${dubbo.registry.protocol}")
    private String protocol;

    /**
     * 注册中心地址
     */
	@Value("${dubbo.registry.address}")
    private String address;

    /**
     * 是否向注册中心注册服务
     */
	@Value("${dubbo.registry.register}")
    private boolean register;

    /**
     * 是否向注册中心订阅服务
     */
	@Value("${dubbo.registry.subscribe}")
    private boolean subscribe;

}
