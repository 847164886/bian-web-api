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
public class OrdersaveReturn {
	
	private String order_id;//唯一顺序号
	private String trans_no;//通联内部流水号
	private String bill_id;//业务订单号
	private String customer_id;//会员ID
	private String real_charge;//支付金额
	private String errror_msg;//有错误返回5001
	private List<ViolationsIDList> violations_id_list_array;//违章明细信息
	private String order_sn;
	private String amount;
	
	@Data
	public static class ViolationsIDList{
		private String violations_id;//订单明细序号ID
		private String sign_id;//每一笔违章标识ID
	}

}
