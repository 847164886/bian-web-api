package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;
@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarInfoReq extends Req {
  
	private static final long serialVersionUID = 2482424060886417916L;

	private Long check_car_id;
	
	private Long auction_id;

}
