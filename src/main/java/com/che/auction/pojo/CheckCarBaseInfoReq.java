package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarBaseInfoReq extends Req {
 
	private static final long serialVersionUID = -3942042865390696394L;

	private Long check_car_id;
	 
}
