package com.che.recharge.pojo;

import com.che.pay.ten.pojo.BaseTenPayReq;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeTenPayReq extends BaseTenPayReq {
	private static final long serialVersionUID = 7048144362856418643L;

	private Integer type;// 充值类型 1-余额；2-保证金
	private Integer amount;// 1-50元，2-100元，3-500元，4-1000元
}
