package com.che.valuation.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ValuationRelateInfo {

	private Long id; // bigint(20) NOT NULL
	private Long user_id; // bigint(20) NOT NULL 用户id
	private Long val_id; // bigint(20) NOT NULL 估价记录生成id
	private Integer status;// int(11) NOT NULL DEFAULT 1 状态：1-有效(默认),0-无效
	private Date create_time; // NOT NULL 创建时间
	private Date update_time;// NULL 更新时间

}
