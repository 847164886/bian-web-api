package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionActinfoReq extends Req { 
	private static final long serialVersionUID = -1715091260083458727L;
  
	private String  city;
}
