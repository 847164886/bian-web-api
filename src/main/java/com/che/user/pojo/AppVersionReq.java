package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class AppVersionReq extends Req {

	private static final long serialVersionUID = -8551210292305245415L;

	private Integer type=0 ; //0  安卓 , 1 IOS
	
}
