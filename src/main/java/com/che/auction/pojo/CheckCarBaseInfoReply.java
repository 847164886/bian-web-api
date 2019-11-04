package com.che.auction.pojo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;
@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarBaseInfoReply extends Reply {
  
	private static final long serialVersionUID = -7173846206157851200L;
	 
	private CheckCarBaseInfoPojo checkCarBaseInfoPojo ;
	
	@Data
	public static class CheckCarBaseInfoPojo{
		
		private Long check_car_id				;         //bigint(20) NULL检测车辆ID
		private String factory_name  			;		  //varchar(50)	生产厂家
		private String series_name 				;         //varchar(50)	车系名称
		private String model_name				;         //varchar(50)	车型名称
		private String drive_km					;         //varchar(50) NULL表显里程
		private Date product_date				;         //datetime NULL出厂日期
		
		private String driving_mode  			;		  //varchar(50) 驱动方式
		private String engine_full_type  	    ;		  //varchar(50) 燃料类型
		
		private String function_param			;         //json NULL车辆功能配置
		private String vin_number				;         //varchar(50) NULL车架号/VIN
	
		private String  spare_tire 				; 		  //备胎
		private String other_inc				;		  //其他自增配置
		private String outer_color				;         //varchar(20) NULL车辆颜色
		private String inner_color				;         //varchar(20) NULL内饰颜色
		private String common_key				;         //varchar(20) NULL普通钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
		private String control_key				;         //varchar(20) NULL遥控钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
		private String intelligent_key			;         //varchar(20) NULL智能钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
		
		private String drive_km_single;						//表显里程 单位：公里
		private String base_param				;         //json NULL车辆配置参数
		
	}
 
}
