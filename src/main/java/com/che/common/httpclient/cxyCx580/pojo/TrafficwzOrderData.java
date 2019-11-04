package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

import com.che.common.httpclient.cxyCx580.pojo.CFTOrderData.Detail;

@Data
public class TrafficwzOrderData {

	private Integer error_code; //-1失败 0成功
	private String err_message;
	
	private String id;//订单ID
	private String OrderTotal;//市场价总价
	private String  preOrderTotal;//实际需支付价总价
	private List<Detail> Detail;
 
}
