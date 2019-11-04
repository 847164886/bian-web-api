/**
 * 
 */
package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

/**
 * @author karlhell
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AppVersionReply extends Reply {
	private static final long	serialVersionUID	= -4444525710146232223L;
	
	private Integer versionCode;//版本号
	private String url;//应用下载地址
	private String desc;//更新信息描述
	private boolean mustInstall;//用于标识是否用户必须安装

}
