package com.che.user.pojo;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserRegistReply extends Reply{
	private static final long serialVersionUID = 7137539852274810336L;
	
	private Long user_id;
	private String session;
}
