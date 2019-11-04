package com.che.auction.entity;

import java.util.Date;

import lombok.Data;

@Data
public class AuctionRelateCar {
 
	private Long id				;		//bigint(20) NOT NULL
	private Long act_id			;		//bigint(20) NULL场次ID
	private Long check_car_id	;		//bigint(20) NULL检测车辆ID(对应：chek_customer_car 的ID)
	private String service_fee		;		//decimal(10,2) NULL手续费(百分比)
	private Integer sort			;		//int(11) NULL排序
	private Integer status			;		//int(11) NULL状态：0：价格待审核，1：价格已审核，2：流拍，3：成交
	private Date price_pass_time	;		//datetime NULL价格审核通过时间（后三天为：客户确认价格截止时间）
	private Integer customer_status	;		//int(11) NULL客户状态：0：待确认，1：确认价格，-1：放弃价格，2：确认看车时间、地点
	private String see_car_addr	;		//varchar(100) NULL看车地址
	private Date see_car_time	;		//datetime NULL看车时间
	private Date addTime			;		//datetime NULL
	private Long creater			;		//bigint(20) NULL
	
}
