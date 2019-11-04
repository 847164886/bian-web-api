package com.che.auction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.AuctionAttention;

public interface AuctionAttentionMapper {

	@Insert(" insert into  `a_auction_attention` (`car_id`, `auction_id`, `user_id`) values (#{car_id},#{auction_id},#{user_id})")
	public int insert(AuctionAttention auctionAttention);
	
	@Select("select * from a_auction_attention where user_id=#{user_id} and auction_id=#{auction_id} and car_id=#{car_id} ")
	public AuctionAttention selectByAll(AuctionAttention auctionAttention );
	
	@Select("select * from a_auction_attention where user_id=#{user_id} and auction_id=#{auction_id}")
	public List<AuctionAttention> selectUserAttentionList(@Param("user_id") Long user_id, @Param("auction_id") Long auction_id);
	

	@Delete("delete  from a_auction_attention where user_id=#{user_id} and auction_id=#{auction_id}  and car_id=#{car_id} ")
	public int delete(AuctionAttention auctionAttention);
	
}
