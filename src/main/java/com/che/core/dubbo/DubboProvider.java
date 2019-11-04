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
public class DubboProvider {
	
	/**
     * 服务的超时时间,单位毫秒
     */
	@Value("${dubbo.provider.timeout}")
    private int timeout;

    /**
     * 调用失败重试次数
     */
	@Value("${dubbo.provider.retries}")
    private int retries;

    /**
     * 是否延迟暴露,-1表示不延迟暴露
     */
	@Value("${dubbo.provider.delay}")
    private int delay;
    
}
