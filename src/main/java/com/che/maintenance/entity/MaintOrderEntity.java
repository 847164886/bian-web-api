/**
 *
 */
package com.che.maintenance.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MaintOrderEntity implements Serializable {

	private static final long serialVersionUID = -1572605200871900402L;

	private Long id; // 订单id

	private Long uid; // 用户id
	
	private Long usercode; // 用户code
	
	private String mobile; // 用户手机号

	private String vin; // vin码

	private String engine_id; // 发动机编号
	private String car_no; // 车牌号
	private String remark; // 备注

	private String model; // 车型
	private String year; // 年款
	private String brand; // 品牌

	private Integer goods_type; // 商品类型1加油卡2罚款单代缴3查维保4充值

	private BigDecimal payment; // 用户付款金额

	private Integer pay_status; // 0待付款，1付款中，2已付款，3退款中，4已退款,5已关闭

	private Integer look_status; // 查看状态，0-已查看，1-未查看

	private Date update_time; // 更新时间

	private Date create_time; // 创建时间

	private String trade_no; // 维保第三方订单号

	private Integer pay_type; // 付款类型1支付宝2微信支付3余额支付

	private String trade_status; // 支付反馈状态
	
	private String trans_id; // 支付宝、微信支付返回的第三方订单号

	private Integer query_status; // 查询状态（1查询中，2查询成功，4查询失败,5无维修保养记录,第三方下单失败）

	private String source; // 订单来源系统标识

	private String retcode; // 返回码

	private String retmsg; // 支付返回说明
	
	private String failure_reson; // 失败原因
	
	private Integer is_third;// 是否第三方查询 0-否；1-是
	
	private Integer comb_status; // 订单组合状态
	

}
