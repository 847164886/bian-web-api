package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

@Data
public class CFTOrderData {
 
	private String id;//订单ID
	private String OrderTotal;//市场价总价
	private String  preOrderTotal;//实际需支付价总价
	private List<Detail> details;
	
	@Data
	public static class Detail{
		 private String ViolationId;//违章ID
		 private String Fine;//罚金
		 private String ActualPoundage;//实际需支付手续费
		 private String LateFine;//滞纳金
	}
 
}
