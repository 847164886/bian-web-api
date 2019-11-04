package com.che.auction.pojo;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionBidReq extends Req {
  
	private static final long serialVersionUID = -3716138956786870388L;

	private Long auction_id;
	
	private Long check_car_id;
	
	private BigDecimal price;
	
}
