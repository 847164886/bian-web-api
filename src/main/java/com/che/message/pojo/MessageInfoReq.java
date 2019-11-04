package com.che.message.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Constants;
import com.che.common.web.Req;


@Data
@EqualsAndHashCode(callSuper=false)
public class MessageInfoReq extends Req {
 
	private static final long serialVersionUID = -597940937805275601L;

	private Long  message_id  ;
 
}
