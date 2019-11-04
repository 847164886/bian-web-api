package com.che.maintenance.pojo;

import java.math.BigDecimal;

import com.che.pay.ali.pojo.BaseAliPayReq;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenanceAliPayReq extends BaseAliPayReq {
	private static final long serialVersionUID = 7048144362856418643L;

//	private Integer type; // 支付方式 1-支付宝支付，2-微信支付，3-余额支付
	private BigDecimal amount;// 付款金额
}
