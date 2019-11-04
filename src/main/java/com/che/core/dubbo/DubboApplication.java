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
public class DubboApplication {

	@Value("${dubbo.name}")
	private String name;

	@Value("${dubbo.logger}")
	private String logger;
	    
	 
}
