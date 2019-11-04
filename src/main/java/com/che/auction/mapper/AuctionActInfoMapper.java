package com.che.auction.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.AuctionActInfo;

public interface AuctionActInfoMapper {

	@Select("select * from a_auction_act_info where id=#{id} and status =1")
	public AuctionActInfo selectById(Long id);
	
	@Select("select end_time from a_auction_act_info where id=#{id} ")
	public Date selectEndTimeById(Long id);

	@Select("select count(*) from a_auction_act_info where id=#{id} and status =1 and end_time<now()")
	public int ifEnd(Long id);
	
	@Select("SELECT COUNT(1) FROM `a_auction_act_info` a JOIN  `a_auction_relate_car` b ON a.`id` =b.`act_id` WHERE    NOW() BETWEEN a.`star_time` AND a.`end_time`  AND a.`status` =1 ")
	public int selectCarnum();
	
	@Select("SELECT  a.*,COUNT(b.`id`) AS carnum FROM `a_auction_act_info` a JOIN  `a_auction_relate_car` b ON a.`id` =b.`act_id` JOIN `a_check_order_car` c   ON c.`check_car_id` =b.`check_car_id` JOIN a_check_order d on d.id = c.order_id  WHERE    NOW() BETWEEN a.`star_time` AND a.`end_time`  AND a.`status` =1 and d.`status` = 3 GROUP BY a.`id` ")
	public List<AuctionActInfo> selectActInfo();
	
 
}
