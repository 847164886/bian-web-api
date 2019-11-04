package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserInfoCenterReq extends Req {
	
	private static final long serialVersionUID = 526832447385994635L;

}
