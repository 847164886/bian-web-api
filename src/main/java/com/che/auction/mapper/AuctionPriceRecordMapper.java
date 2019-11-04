package com.che.auction.mapper;

import org.apache.ibatis.annotations.Insert;

import com.che.auction.entity.AuctionPriceRecord;

public interface AuctionPriceRecordMapper {

	@Insert("insert into a_auction_price_record  (`relate_id`,`shop_user_id`,`type`,`price`,`addTime`)"
			+ "values( #{relate_id},#{shop_user_id},#{type},#{price},#{addTime})")
	public int insert(AuctionPriceRecord auctionPriceRecord);
	
}
