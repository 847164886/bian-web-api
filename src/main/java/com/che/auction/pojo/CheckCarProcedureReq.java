package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarProcedureReq extends Req {
 
	private static final long serialVersionUID = 5804956041671296065L;
	
	private Long check_car_id;
	
}
