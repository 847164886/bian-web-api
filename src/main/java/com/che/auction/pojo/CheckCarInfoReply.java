package com.che.auction.pojo;

 
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarInfoReply extends Reply { 
	 
	private static final long serialVersionUID = 2342860472537046841L;
 
	private BigDecimal price;     //成交价格
	private BigDecimal user_price; //用户出价价格
	private Integer  bonus;  	//名次
	private Integer status; // 竞拍价格状态：0：价格待审核，1：价格确认，2：进行中，3：预约前放弃，4：成交放弃，5：偏差放弃，6：成交,7：成交预约，8：失效(用户放弃)，9：竞价放弃  10：未中标，11：流拍 
	
	private Date end_time	;		//datetime NULL开始时间
	
	private List<String> pic_path;//图片路径
	private String vcr_path;//vcr路径
	private CheckCarPojo  checkCarPojo;
	private CheckOrderPojo checkOrderPojo;
	private MaintReportPojo maintReportPojo;
	
	private CheckTypeReportOverviewPojo waiguan = new CheckTypeReportOverviewPojo();// 外观
	private CheckTypeReportOverviewPojo jiegou = new CheckTypeReportOverviewPojo();// 结构
	private CheckTypeReportOverviewPojo jixie = new CheckTypeReportOverviewPojo();// 机械
	private CheckTypeReportOverviewPojo dianqi = new CheckTypeReportOverviewPojo();// 电气
	private CheckTypeReportOverviewPojo neishi = new CheckTypeReportOverviewPojo();// 内饰
	private CheckTypeReportOverviewPojo dipan = new CheckTypeReportOverviewPojo();// 底盘
	
	@Data
	public static class CheckCarPojo{
		private Long auction_id				;         //bigint(20) NULL场次ID
		private Long check_car_id				;         //bigint(20) NULL检测车辆ID
		private String brand_name 				;         //varchar(50)	品牌名称
		private String series_name 				;         //varchar(50)	车系名称
		private String model_name				;         //varchar(50)	车型名称
		private String driving_mode  			;		  //varchar(50) 驱动方式
		private String body_structure  			;		  // 车身机构
		private String gearbox  			;		  // 变速箱
		private String engine_supply_mode  			;		  //供油方式
		private String engine_ai_form  			;		  //进气方式
		private String engine_full_type  	    ;		  //varchar(50) 燃料类型
		private String service_fee			;   	  //decimal(10,2)手续费(百分比)
		private Date first_register		 		;         //datetime NULL初次登记
		private String drive_km					;         //varchar(50) NULL表显里程 单位：万公里
		private String drive_km_single;						//表显里程 单位：公里
		private Date expires_date				;         //datetime NULL年检有效期
		private String city_name				;		  //varhcar(50)	车辆所在城市名称
		private String base_param				;         //json NULL车辆配置参数
		private Integer ifAuctionPass; // 是否流拍 1-是		0-否
	}
	
	@Data
	public static class CheckOrderPojo{
		private Integer check_grade		;			//int(11) NULL检测等级（1,2,3,4,5,6）
		private String comment_wg		;			//varchar(300) NULL外观评语
		private String comment_jg		;			//varchar(300) NULL结构评语
		private String comment_jx		;			//varchar(300) NULL机械评语
		private String comment_dq		;			//varchar(300) NULL电气评语
		private String comment_ns		;			//varchar(300) NULL内饰评语
		private String comment_dp		;			//varchar(300) NULL底盘评语
		private String extra;						//补充=其他自增配置+其他备注(异常备注)
		private String check_score	;			//decimal(3,1) NULL检测分数
		private Integer ifShowScore; // 是否显示评分 1-显示 		0-不显示

	}
	
	@Data
	public static class CheckTypeReportOverviewPojo {

		private Integer type_id;

		private Integer num = 0;// 缺陷个数

		private Integer imgNum = 0;// 照片张数

	}
	
	@Data
	public static class MaintReportPojo {
		
		private String jd_order_id; // 维保查询订单ID，第三方订单号
		
		private Integer jd_status; // 维保报告查询状态，1-查询中，2-查询成功，4-查询失败,5-无维修保养记录，6-第三方下单失败
		
		private Integer count = 0; // 维修保养记录数量
		
		private Integer type; // 1-此车无事故记录，2-此车有事故记录，3-此车无维修保养记录
		
		public static final class Type {
			
			public static final Integer NO_ACCIDENT = 1; // 1-此车无事故记录
			
			public static final Integer HAS_ACCIDENT = 2; // 2-此车有事故记录
			
			public static final Integer NO_MAINTRECORDS = 3; // 3-此车无维修保养记录
		}
		
	}
}
