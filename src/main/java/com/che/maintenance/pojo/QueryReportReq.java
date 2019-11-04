package com.che.maintenance.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class QueryReportReq extends Req {

	private static final long serialVersionUID = -1742798324856506140L;
	
	private Long order_id; // 维保订单id
	
	private String jd_order_id; //维保第三方订单id

}
