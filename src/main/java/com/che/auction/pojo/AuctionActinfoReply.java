package com.che.auction.pojo;

import java.util.Date;
import java.util.List;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionActinfoReply extends Reply { 
 
	private static final long serialVersionUID = -3524365908736749036L;

	private List<AuctionActinfoPojo> auctionActinfos;
	
	@Data
	public static class AuctionActinfoPojo{
		
		private Long id			;		//bigint(20) NOT NULL
		private String title	;		//varchar(200) NULL场次标题
		private Date star_time	;		//datetime NULL开始时间
		private Date end_time	;		//datetime NULL结束时间
		private Integer carnum  ; 
		private List<CheckOrderCarPojo> checkOrderCarPojoList;
	}
	
	@Data
	public static class CheckOrderCarPojo{
		
		private Long check_car_id				;         //bigint(20) NULL检测车辆ID
		private String cover_image 				;         //varchar(100)	封面图片(左前45度图片，用于app竞拍list显示)
		private String series_name				;         //varchar(50) NULL车系
		private String model_name				;         //varchar(50) NULL车型
		private String city_name				;         //车辆所在城市
		private String car_plate				;         //varchar(50) NULL车牌
		private Date first_register				;         //datetime NULL初次登记 上牌时间
		private String drive_km				;         //Decimal(5,1) NULL表显里程 单位：万公里
		private String drive_km_single;						//表显里程 单位：公里
		private String discharge_level  		;		  //varchar(50) NULL排放标准
		private Integer  check_grade            ; 		  //int(11) NULL评测等级
		private Date addTime					;		  //datetime NULL  添加时间
		private Integer ifAttention				;   	  //是否关注0取消关注或者未关注1关注
		private Integer ifOffer					;		  //是否报价，0-未报价；1-已报价
	}
}
