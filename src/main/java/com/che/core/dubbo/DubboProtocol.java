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
public class DubboProtocol {
	 /**
     * 接口协议
     */
	@Value("${dubbo.protocol.name}")
    private String name;
    /**
     * 暴露服务的端口
     */
	@Value("${dubbo.protocol.port}")
    private int port;

    /**
     * 是否记录接口日志
     */
	@Value("${dubbo.protocol.accessLog}")
    private boolean accessLog;

}
