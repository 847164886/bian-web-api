package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class SendMessageReq extends Req {
 
	private static final long serialVersionUID = -4635862821856288925L;
 
	private Long mobile;
	
}
