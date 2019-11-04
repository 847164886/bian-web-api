package com.che.maintenance.pojo;

/**
 * 支付方式
 * @author 
 * 付款类型1支付宝2微信支付3余额支付
 */
public enum PayType {

	ALIPAY(1),
	
	TENPAY(2),
	
	ACCOUNT(3);
	
	public int value;

	private PayType(int value) {
		this.value = value;
	}
}
