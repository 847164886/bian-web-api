package com.che.auction.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionHistoryReply extends Reply {
 
	private static final long serialVersionUID = 8389763620223604814L;

	private List<AuctionHistoryPojo> auctionHistorys;
	
	@Data
	public static class AuctionHistoryPojo{
		
		private Long auction_id					;         //bigint(20) NULL场次ID
		private Long check_car_id				; 		  //bigint(20) NULL检测车辆ID
		private String cover_image 				;         //varchar(100)	封面图片(左前45度图片，用于app竞拍list显示)
		private String series_name				;         //varchar(50) NULL车系
		private String model_name				;         //varchar(50) NULL车型
		private String city_name				;         //车辆所在城市
		private String car_plate				;         //varchar(50) NULL车牌
		private String discharge_level  		;		  //varchar(50) NULL排放标准
		private Date addTime					;		  //datetime NULL  添加时间
		private Integer status 					; //竞拍价格状态：0：价格待审核，1：价格确认，2：进行中，3：成交预约，4：预约前放弃，5：成交放弃，6：偏差放弃，7：成交,8：失效(用户放弃)
		private BigDecimal price 				; //报价
		private Date  price_time				; //竞拍时间
		
	}

	
}
