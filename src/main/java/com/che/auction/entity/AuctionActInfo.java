package com.che.auction.entity;

import java.util.Date;

import lombok.Data;

@Data
public class AuctionActInfo {
                                  
	private Long id			;		//bigint(20) NOT NULL
	private Integer city_id	;		//int(11) NULL城市ID
	private String title	;		//varchar(200) NULL场次标题
	private Date star_time	;		//datetime NULL开始时间
	private Date end_time	;		//datetime NULL结束时间
	private String remark	;		//varchar(300) NULL描述
	private Integer status	;		//int(11) NULL状态：0：准备中(默认),1:上线，-1：下线，-2：取消
	private Date addTime	;		//datetime NULL
	private Long creater	;		//bigint(20) NULL
	private Date updateTime	;		//datetime NULL
	private Long updater	;		//bigint(20) NULL
	
	private Integer carnum ; 
	
}
