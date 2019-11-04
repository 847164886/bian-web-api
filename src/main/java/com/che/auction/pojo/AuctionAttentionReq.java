package com.che.auction.pojo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionAttentionReq extends Req {
  
	private static final long serialVersionUID = -7768385813552762864L;
	
	private Long auction_id;
	
	private Long check_car_id;
	
	private Integer type ; // //0取消关注或者未关注  1 关注
	
}
