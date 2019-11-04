package com.che.auction.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CheckTypeReport {
 
	private Long id			;		//bigint(20) NOT NULL
	private Long order_id	;		//bigint(20) NULL检测订单ID
	private Integer type_id		;		//int(11) NULL检测类型ID
	private String check_score	;		//decimal(5,1) NULL检测分数
	private Integer check_unusual;		//int(11) NULL检测异常项数量(默认0)
	private Integer status		;		//int(11) NULL检测状态（0：待检测[默认]，1：完成）
	private Date addTime		;		//datetime NULL
	private Long creater		;		//bigint(20) NULL
	
	private String pic_path; 	//图片信息
}
