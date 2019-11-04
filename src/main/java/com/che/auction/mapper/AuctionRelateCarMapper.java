package com.che.auction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.AuctionRelateCar;
import com.che.auction.pojo.AuctionActinfoReply.CheckOrderCarPojo;

public interface AuctionRelateCarMapper {

	@Select("select * from  a_auction_relate_car where act_id=#{act_id} and check_car_id=#{check_car_id}")
	public AuctionRelateCar selectByCarIdAndActId(@Param("act_id") Long act_id, @Param("check_car_id") Long check_car_id);
 
	@Select("SELECT b.check_car_id,b.cover_image,b.series_name,b.model_name,b.city_name,left(b.car_plate,2) AS car_plate,b.first_register,b.drive_km,b.discharge_level,b.`addTime`,c.check_grade  FROM  a_auction_relate_car a JOIN `a_check_order_car` b ON a.`check_car_id` =b.`check_car_id` left JOIN `a_check_order` c ON b.`order_id` =c.`id` WHERE a.`act_id` =#{act_id} and c.`status` = 3 ")
	public List<CheckOrderCarPojo> selectPojoByActId(@Param("act_id") Long act_id);
	
	@Select("select  vin_number  from a_check_order_car where check_car_id =#{0}")
	public String selectVinByCarId(Long check_car_id);
   
}
