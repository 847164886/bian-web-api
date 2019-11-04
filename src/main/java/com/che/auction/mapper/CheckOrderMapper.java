package com.che.auction.mapper;

import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.CheckOrder;

public interface CheckOrderMapper {

	@Select("SELECT * FROM  `a_check_order` WHERE `check_car_id` =#{check_car_id} and status = 3 ")
	public CheckOrder selectByCarId(Long check_car_id);
	
	@Select("select order_id from `a_check_order_car` JOIN a_check_order b on b.id = a.order_id where a.`check_car_id`=#{check_car_id} and b.`status` = 3 ")
	public Long selectOrderIdByCarId(Long check_car_id);
	
}
