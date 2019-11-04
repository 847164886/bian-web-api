package com.che.user.pojo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoCenterReply extends Reply {
	private static final long serialVersionUID = -1110611721443429650L;

	private String name ;//姓名
	
	private Integer certify_tatus;//审核状态NULL未认证 0待审核 1审核通过 2审核失败 
	
	private String account ;//帐户余额
	
	private Integer unread; //未读
	
	private Integer history;//竞拍历史
	
	private Integer procedure;//竞拍车辆
	
	private Long points ;//积分
	
	private String bail; //保证金
	
}

