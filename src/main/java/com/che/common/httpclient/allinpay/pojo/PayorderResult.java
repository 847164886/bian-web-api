package com.che.common.httpclient.allinpay.pojo;

import lombok.Data;

@Data
public class PayorderResult {
	
	private String trans_no;//违章处理订单号
	private String pay_trans_no;//违章支付流水号
	private String pay_code;//返回码
	

}
