package com.che.auction.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AuctionPriceRecord {

	private Long id			;		//bigint(20) NOT NULL
	private Long relate_id	;		//bigint(20) NULL关联ID (auction_relate_car的ID)
	private Long shop_user_id;		//bigint(20) NULL竞拍者ID
	private Integer type	;		//int(11) NULL出价类型：1：预拍，2：竞拍
	private BigDecimal price;		//decimal(10,2) NULL价格
	private Date addTime	;		//datetime NULL
	
}
