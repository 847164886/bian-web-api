package com.che.auction.entity;

import java.util.Date;

import lombok.Data;

@Data
public class CheckOrder {
 
	private Long id					;			//bigint(20) NOT NULL
	private Long customer_id		;			//bigint(20) NULL客户ID
	private Long check_car_id		;			//bigint(20) NULL检测车辆ID
	private String order_num		;			//varchar(50) NULL检测订单编号
	private String check_address	;			//varchar(100) NULL检测地址
	private Integer demand_type		;			//int(11) NULL需求类型：1：竞拍（默认），2：渠道，3：代检，4：寄售
	private Long check_user_id		;			//bigint(20) NULL检测师ID
	private String check_user_name	;			//varchar(50) NULL检测师名称
	private Date book_time			;			//datetime NULL预约时间
	private Integer status			;			//int(11) NULL检测状态：0：待分配(默认)，1：已分配待检测，2：已检测待审核，3：已审核，-1：取消
	private Date distribute_time	;			//datetime NULL分配时间
	private Date check_time			;			//datetime NULL已检测时间
	private Integer check_grade		;			//int(11) NULL检测等级（1,2,3,4,5,6）
	private String comment_wg		;			//varchar(300) NULL外观评语
	private String comment_jg		;			//varchar(300) NULL结构评语
	private String comment_jx		;			//varchar(300) NULL机械评语
	private String comment_dq		;			//varchar(300) NULL电气评语
	private String comment_ns		;			//varchar(300) NULL内饰评语
	private String comment_dp		;			//varchar(300) NULL底盘评语
	private String check_score	;			//decimal(3,1) NULL检测分数
	private Date verify_time		;			//datetime NULL已审核时间
	private Date cancel_time		;			//datetime NULL取消时间
	private Date addTime			;			//datetime NULL
	private Long creater			;			//bigint(20) NULL
	private Date updateTime			;			//datetime NULL
	private Long updater			;			//bigint(20) NULL
}
