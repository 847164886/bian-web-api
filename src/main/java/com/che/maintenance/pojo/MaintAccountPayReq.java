package com.che.maintenance.pojo;

import java.math.BigDecimal;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MaintAccountPayReq extends Req {
	
	private static final long serialVersionUID = -3772683195793624751L;
	
	private Long order_Id; // 订单号
	
	private BigDecimal amount;// 付款金额

}
