package com.che.message.entity;

import lombok.Data;

@Data
public class AuctionMessage {
 
	private Long id			;			//bigint(20) NOT NULL
	private Long rec_id		;			//bigint(20) NULL接信者ID
	private Long message_id	;			//bigint(20) NULL信件ID
	
}
