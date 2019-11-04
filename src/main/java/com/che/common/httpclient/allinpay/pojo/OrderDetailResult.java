/**
 * 
 */
package com.che.common.httpclient.allinpay.pojo;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class OrderDetailResult {
	
	private String order_id;//唯一顺序号
	private String mail_type_name;//邮寄类型名称
	private String postage;//邮费
	private String violations_mlatefee;//滞纳金总费用
	private String total_amount;//总计
	private String errror_msg;//无错误返回空 有错误返回5001
	private Map<String,List<OrderDetail>> order_detail_arrays;

	@Data
	public static class OrderDetail{
		private String temp_id;//流水号
		private String shop_sign;//车牌号
		private String voiture_type_name;//机动车类型
		private String regulation_name;//违章条例名称
		private String fine_amount;//违章罚款金额
		private String mlate_fee;//滞纳金
		private String pay_charge;//代办费
		private String charge;//小计
	}

}
