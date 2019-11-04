package com.che.auction.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CheckOrderCar {
	 
	private Long id							;         //bigint(20) NOT NULL
	private Long order_id					;         //bigint(20) NULL检测订单ID
	private Long customer_id				;         //bigint(20) NULL客户ID
	private Long check_car_id				;         //bigint(20) NULL检测车辆ID
	private Integer car_brand				;         //int(11) NULL品牌
	private String  brand_name				;         //varchar(50)	品牌名称
	private Integer car_series				;         //int(11) NULL车系
	private String series_name 				;         //	varchar(50)	车系名称
	private Integer car_model				;         //int(11) NULL车型
	private String model_name				;         //varchar(50)	车型名称
	private Integer city_id					;         //车辆所在城市
	private String city_name				;		  //varhcar(50)	车辆所在城市名称
	private String car_plate				;         //varchar(50) NULL车牌
	private String plate_status				;         //varchar(10) NULL车牌状态：有，无
	private String plate_city				;         //varchar(50) NULL车牌所在地(手填)
	private Integer car_year					;         //int(11) NULL年份
	private String remark					;         //varchar(300) NULL车辆备注
	private String quantity_sell			;         //varchar(10) NULL带额度出售：是，否
	private Date product_date				;         //datetime NULL出厂日期
	private Date first_register			;         //datetime NULL初次登记
	private Date expires_date				;         //datetime NULL年检有效期
	private Date insure_date				;         //datetime NULL交强险
	private Date invalid_date				;         //datetime NULL强制报废年限
	private String use_property				;         //varchar(20) NULL使用性质：营转非，运营，非运营
	private String drive_license			;         //varchar(20) NULL机动车行驶证：需变更，有，无
	private String register_certificate		;         //varchar(20) NULL机动车登记证书：需变更，有，无
	private String transfer					;         //varchar(20) NULL过户转籍限制：可过户，异常
	private String buy_tax					;         //varchar(20) NULL购置税完税证明：有，无
	private String property_lease			;         //varchar(20) NULL产权曾属租赁：有，否
	private String master_info				;         //varchar(20) NULL车主信息：公车，私车
	private String master_register			;         //varchar(20) NULL车主户籍：沪籍，非沪籍
	private String property_change			;         //varchar(50) NULL产证变更信息
	private String use_instruction			;         //varchar(20) NULL车辆使用说明书：有，无
	private String protect_instruction		;         //varchar(20) NULL车辆保养手册：有，无
	private String common_key				;         //varchar(20) NULL普通钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
	private String control_key				;         //varchar(20) NULL遥控钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
	private String intelligent_key			;         //varchar(20) NULL智能钥匙：1把，2把，3把，无，后配1把，后配2把，后配3把
	private String outer_color				;         //varchar(20) NULL车辆颜色
	private String inner_color				;         //varchar(20) NULL内饰颜色
	private String property_color			;         //varchar(20) NULL产证颜色
	private String engine_number			;         //varchar(50) NULL发动机号
	private String vin_number				;         //varchar(50) NULL车架号/VIN
	private String brand_model				;         //varchar(50) NULL品牌型号
	private Integer transfer_times			;         //int(11) NULL过户次数
	private String drive_km					;         //varchar(50) NULL表显里程
	private String discharge_level			;         //varchar(50) NULL排放标准：国二，国三，国四，国五
	private String base_param				;         //json NULL车辆配置参数
	private String function_param			;         //json NULL车辆功能配置
	private String other_inc				;         //json  自增配置
	private String other_remark				;         //varchar(300) NULL其他备注
	private Date addTime					;         //datetime NULL添加时间
	private String cover_image				;         //varchar(300) NULL图片
	private String jd_order_id; // 维保查询订单ID，第三方订单号
	private Integer jd_status; // 维保报告查询状态，1-查询中，2-查询成功，4-查询失败,5-无维修保养记录
	
	private String service_fee ; //手续费
	private Integer auction_relate_stauts; // 状态：0：价格待审核，1：价格已审核，2：流拍，3：成交
}
