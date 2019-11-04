package com.che.message.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Constants;
import com.che.common.web.Req;


@Data
@EqualsAndHashCode(callSuper=false)
public class MessageListReq extends Req {

	private static final long serialVersionUID = -2936802006978412037L; 

	private Integer page = 1 ;
	
	private Integer pageSize = Constants.PAGESIZE;
	
}
