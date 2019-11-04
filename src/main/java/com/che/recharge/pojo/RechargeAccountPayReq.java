package com.che.recharge.pojo;

import com.che.common.web.Constants;
import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeAccountPayReq extends Req {
	private static final long serialVersionUID = 7048144362856418643L;
	
	private Integer page = 1;

	private Integer pageSize = Constants.PAGESIZE;

}
