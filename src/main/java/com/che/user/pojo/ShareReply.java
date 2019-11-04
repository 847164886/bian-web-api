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
public class ShareReply extends Reply{
	private static final long	serialVersionUID	= 145100953769052267L;
	
	private String title;
	private String icon;
	private String detailUrl;

}
