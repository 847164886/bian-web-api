package com.che.auction.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AuctionPriceInfo {
	private Long id 			;			//bigint		主键ID
	private Long relate_id		;		 	//bigint		关联ID (auction_relate_car的ID)
	private Long auction_id 	; 			//bigint		场次ID 
	private Long shop_user_id 	;			//bigint		竞拍者ID
	private Long check_car_id 	;			//bigint		检测车辆ID(对应：chek_customer_car 的ID) -- 新增冗余存储
	private BigDecimal pre_price;			//DECIMAL(12,2)	预拍价格
	private BigDecimal price 	;			//DECIMAL(12,2)	竞拍价格
	private Integer status		;			//int			竞拍价格状态：0：价格待审核，1：价格确认，2：进行中，3：预约前放弃，4：成交放弃，5：偏差放弃，6：成交,7：成交预约，8：失效(用户放弃)，9：竞价放弃  10：未中标，11：流拍
	private Date price_time		;			//datetime		竞拍时间
	private Date addTime 		;			//datetime		添加时间
	private Long creater 		;			//bigint		创建者
	private Integer price_stage ;			//出价阶段：1：预拍，2：竞拍
	private Integer sure_price;             //成交价格
}
