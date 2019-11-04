package com.che.common.web;

import lombok.Data;

/**
 * Entity Friend
 * @copyright {@link winwho.com}
 * @author Quenton.Zhou<Auto generate>
 * @version  2014-04-15 16:56:57
 */
@Data
public class Req implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	//columns
	/**
	 * 用户 id
	 */
	private Long uid;
    
    /**
     * 版本号
     */
    private Integer versionCode;
  
}

