package com.che.recharge.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class OrderEntity {
	private Long id;
	private Long orderId;
	private Long uid;
	private Long userCode;
	private Integer goodsType;
	private BigDecimal payment;
	private Integer payType;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	private String retMsg;
	private String buyer;
	private String tradeNo;
	private String tradeStatus;
	private String mobile;
}
