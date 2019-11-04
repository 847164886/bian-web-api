package com.che.auction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.auction.entity.AuctionPriceInfo;
import com.che.auction.pojo.AuctionHistoryReply.AuctionHistoryPojo;

public interface AuctionPriceInfoMapper {

	@Insert(" insert into a_auction_price_info (`relate_id`,auction_id,`check_car_id`,`shop_user_id`,`pre_price`,`price`,price_stage,`status`,`price_time`,`addTime`) "
			+ "values (#{relate_id},#{auction_id},#{check_car_id},#{shop_user_id},#{pre_price},#{price},#{price_stage},#{status},#{price_time},#{addTime}) ")
	public int insert(AuctionPriceInfo auctionPriceInfo);
	
	@Select(" select * from a_auction_price_info where  relate_id=#{relate_id} and shop_user_id=#{shop_user_id} ")
	public AuctionPriceInfo ifExist(@Param("relate_id") Long relate_id  ,@Param("shop_user_id") Long shop_user_id);
	
	@Update({"<script>",
		" update a_auction_price_info " ,
		" <set> " ,
		" <if test='pre_price!= null'>pre_price =#{pre_price},</if> " ,
		" <if test='price!= null'> `price`= #{price},</if> " ,
		" <if test='price_time!= null'> `price_time`= #{price_time},</if> " ,
		" <if test='price_stage!= null'> `price_stage`= #{price_stage},</if> " ,
		" </set> " ,
		" where " ,
		" relate_id=#{relate_id} and shop_user_id=#{shop_user_id}  ",
	    "</script>"})
	public int updatePrice(AuctionPriceInfo auctionPriceInfo);
	
	@Select("select * from `a_auction_price_info` where  check_car_id=#{check_car_id} and auction_id=#{auction_id} and shop_user_id=#{shop_user_id}  ")
	public AuctionPriceInfo selectByUserId(@Param("check_car_id") Long check_car_id,@Param("auction_id") Long auction_id,@Param("shop_user_id") Long shop_user_id) ;
	
	@Select("select shop_user_id , price ,`status`, sure_price from `a_auction_price_info` where check_car_id=#{check_car_id} and auction_id=#{auction_id} order by price desc, price_time asc limit 3")
	public List<AuctionPriceInfo> selectPrice(@Param("check_car_id") Long check_car_id,@Param("auction_id") Long auction_id) ;
	
	@Select("select sure_price from `a_auction_price_info` where check_car_id=#{check_car_id} and auction_id=#{auction_id} and sure_price is not null order by sure_price desc limit 1")
	public AuctionPriceInfo selectSurePrice(@Param("check_car_id") Long check_car_id,@Param("auction_id") Long auction_id);
	
	@Select(" SELECT a.`check_car_id`,a.`auction_id`,a.`price`,a.`price_time`,a.`status`,a.`addTime`,b.series_name,LEFT(b.car_plate,2) AS car_plate,b.model_name,b.city_name,b.discharge_level,b.cover_image FROM a_auction_price_info a JOIN `a_check_order_car` b ON a.`check_car_id` =b.`check_car_id` JOIN a_check_order c on c.id = b.order_id where shop_user_id=#{shop_user_id} and c.`status` = 3 and a.status in (0,9,10,11) order by a.`addTime` desc")
	public List<AuctionHistoryPojo> selectHistoryListByUserId( Long shop_user_id);
	
	@Select(" SELECT a.`check_car_id`,a.`auction_id`,a.`price`,a.`price_time`,a.`status`,a.`addTime`,b.series_name,LEFT(b.car_plate,2) AS car_plate,b.model_name,b.city_name,b.discharge_level,b.cover_image FROM a_auction_price_info a JOIN `a_check_order_car` b ON a.`check_car_id` =b.`check_car_id` JOIN a_check_order c on c.id = b.order_id where shop_user_id=#{shop_user_id} and a.status in (1,2,3,4,5,6,7,8) and c.`status` = 3 order by a.`addTime` desc ")
	public List<AuctionHistoryPojo> selectTonedListByUserId( Long shop_user_id);
	
	@Select(" SELECT  count(1) FROM a_auction_price_info a JOIN `a_check_order_car` b ON a.`check_car_id` =b.`check_car_id` JOIN a_check_order c on c.id = b.order_id where shop_user_id=#{shop_user_id} and a.status in (0,9,10,11) and c.`status` = 3")
	public Integer selectHistoryNumByUserId( Long shop_user_id);
	
	@Select(" SELECT count(1) FROM a_auction_price_info a JOIN `a_check_order_car` b ON a.`check_car_id` =b.`check_car_id` JOIN a_check_order c on c.id = b.order_id where shop_user_id=#{shop_user_id} and a.status in (1,2,3,4,5,6,7,8) and c.`status` = 3")
	public Integer selectTonedNumByUserId( Long shop_user_id);
	
	@Select(" select * from a_auction_price_info where auction_id=#{auction_id} and shop_user_id=#{shop_user_id}")
	public List<AuctionPriceInfo> selectAuctionByUserId(@Param("auction_id") Long auction_id,
			@Param("shop_user_id") Long shop_user_id);

}
