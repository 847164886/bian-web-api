package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Constants;
import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class AuctionHistoryReq extends Req {

	private static final long serialVersionUID = 8606804659624570978L;
	
	private Integer page = 1 ;
	
	private Integer pageSize = Constants.PAGESIZE;
 
}
