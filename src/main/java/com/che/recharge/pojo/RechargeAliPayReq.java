package com.che.recharge.pojo;

import com.che.pay.ali.pojo.BaseAliPayReq;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeAliPayReq extends BaseAliPayReq {
	private static final long serialVersionUID = -6477371281174157736L;

	private Integer type;// 充值类型 1-余额；2-保证金
	private Integer amount;// 1-50元，2-100元，3-500元，4-1000元
}
