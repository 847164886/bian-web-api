/**
 * 
 */
package com.che.common.httpclient.allinpay.pojo;

import java.util.List;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class Regulations {

	private List<Regulation> regulationList;
	private String error_code;
	
	@Data
	public static class Regulation{
		private String violation_sn;//fineNo;//违章单号
		private String violation_time;//fineTime(需要转换);//违章时间 例如：2015-07-04 13:40:00
		private String violation_road;//fineLocation;//违章地点
		private String regulation_sn;//fineCode;//交警部违章编号
		private String regulation_name;//fineDetail;//违章地点
		private String porint;//fineDeductPoints;//扣分(由于各个地区实际情况不同，该字段仅供参考)
		private String fine_amount;//fineFee;//罚金
		private String special_charge;//delayFee;//滞纳金
		private String pay_charge;//proxyFee;//第三方平台代办费
		private String serviceFee;//服务费(目前等于pay_charge)（传递给手机端做呈现）
		private Integer deal_id;//canPay;//是否可缴费,1为可以,0不可缴费,(扣分,含滞纳金的罚单无法通过平台缴费)
		private String temp_id;//unique;//罚单查询唯一码(下单时需传入)
		private String city;//fineCity;//违章所在城市
		private String canPayMsg;//不可缴费说明（可缴费时为空）
		private String itemId;//违章交通队编号(下单时需传入)
		
		private String regulation_id;//违章条例ID
		private String province_id;//违章归属地省份ID
		private String city_id;//违章归属地城市ID
		private String city_area_id;//违章归属地区域ID
		private String reference;//违章时间标识(暂时没用)
		private String authority;//执法机关
		private String dealaddress;//违章办理地址
		private String needdays;//办理周期
		private String is_on_site_single;//是否现场单0-否  1-是
	}
	
}
