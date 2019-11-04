package com.che.user.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ShopUserBailRecord {
 
	private Long id			;			//bigint(20) NOT NULL
	private Long shop_user_id;			//bigint(20) NULL用户ID
	private Integer type		;			//int(11) NULL1:充值，2：消费
	private String price	;			//decimal(10,2) NULL金额
	private Integer pay_method	;			//int(11) NULL付款方式
	private String remark		;			//varchar(200) NULL备注
	private Date addTime		;			//datetime NULL
	private Long creater		;			//bigint(20) NULL
	
}
