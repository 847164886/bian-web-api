package com.che.auction.entity;

import lombok.Data;

@Data
public class AuctionAttention {
 
private Long id			;		//bigint(20) NOT NULL
private Long car_id		;		//bigint(20) NULL
private Long auction_id	;		//bigint(20) NULL
private Long user_id	;		//bigint(20) NULL
	
}
