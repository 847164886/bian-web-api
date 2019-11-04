package com.che.auction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.CheckOrderCar;

public interface CheckOrderCarMapper {

	@Select("select a.* from a_check_order_car a INNER JOIN a_check_order b on b.id = a.order_id  where a.check_car_id=#{check_car_id} and b.`status` = 3")
	public CheckOrderCar selectById(Long check_car_id);
	
	@Select("SELECT b.pic_path FROM a_check_order_car a JOIN  `a_check_relate_pic` b ON a.`order_id`=b.`order_id` JOIN a_check_order c on c.id = a.order_id WHERE b.`type`  =1 and c.`status` = 3   AND b.stauts=1  AND a.check_car_id= #{check_car_id}")
	public List<String> selectImageById(Long check_car_id);
	
	@Select("select a.* ,b.`service_fee`,b.status AS auction_relate_stauts from `a_check_order_car` a INNER JOIN `a_auction_relate_car`  b ON a.`check_car_id` =b.`check_car_id` INNER JOIN a_check_order c on a.order_id = c.id  where a.check_car_id=#{check_car_id} and act_id=#{auction_id} and c.`status` = 3 ")
	public CheckOrderCar selectPojoById(@Param("check_car_id") Long check_car_id,@Param("auction_id") Long auction_id);

	@Select("SELECT b.pic_path FROM a_check_order_car a JOIN  `a_check_relate_pic` b ON a.`order_id`=b.`order_id` JOIN a_check_order c on c.id = a.order_id WHERE b.`type`  =5 and c.`status` = 3   AND b.stauts=1  AND a.check_car_id= #{check_car_id} LIMIT 1")
	public String selectVcrById(Long check_car_id);
	@Insert("update  a_auction_share_visit_count set visit_count=visit_count+1")
	public void countVisitIncrement();
  
}
