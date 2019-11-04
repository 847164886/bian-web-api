package com.che.recharge.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class RefundOrderEntity {

	private Long id; // 退款订单id
	private Long uid; // 用户id
	private Long order_id; // 关联业务订单号
	private Integer order_type; // 订单类型，1-查维保
	private BigDecimal total_fee; // 总金额
	private BigDecimal refund_fee; // 退款总金额
	private Integer pay_type; // 支付方式,1-支付宝2-微信支付
	private Integer status; // 退款状态 0-未退款，1-退款中，2-已退款，3-退款失败
	private String return_code; // 返回状态码
	private String return_msg; // 返回信息
	private String result_code; // 业务结果
	private String err_code; // 错误代码
	private String refund_id; // 第三方退款单号
	private BigDecimal real_refund_fee; // 实际退款总金额
	private Date create_time; // 创建时间
	private Date update_time; // 更新时间
}
