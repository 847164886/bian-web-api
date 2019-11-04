package com.che.maintenance.pojo;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintAliPayReply extends Reply {

	private static final long serialVersionUID = -51912399197098815L;
	
	private String payInfo;

}
