package com.che.recharge.common;

/**
 * 常量
 */
public abstract class RechargeConstants {

	public static final Integer[] AMOUNTS = new Integer[] { 50, 100, 500, 1000 };// 充值金额

	public static final Integer PAY_TYPE_ALI = 1;// 支付宝支付
	public static final Integer PAY_TYPE_TEN = 2;// 微信支付
	public static final Integer PAY_TYPE_ACCOUNT = 3; // 余额支付
	
	public static final Integer REFUND_STATUS_ING = 1; // 退款中
	public static final Integer REFUND_STATUS_ED = 2; // 已退款
	public static final Integer REFUND_STATUS_FAL = 3; // 退款失败

	public static final Integer PAY_STATUS_CONFIRM = 1;// 支付回调成功
	public static final Integer PAY_STATUS_ERROR = 2;// 支付回调失败

	public static final Integer GOODS_TYPE_YE = 1;// 余额充值
	public static final Integer GOODS_TYPE_BZJ = 2;// 保证金充值

}
