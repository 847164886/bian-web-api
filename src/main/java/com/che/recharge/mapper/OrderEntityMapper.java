package com.che.recharge.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.recharge.entity.OrderEntity;

public interface OrderEntityMapper {
	@Insert(" insert into  `a_auction_order` (`order_id`, `uid`, `user_code`, `mobile`, `goods_type`,`payment`,`pay_type`) values (#{orderId},#{uid},#{userCode},#{mobile},#{goodsType},#{payment},#{payType})")
	int insert(OrderEntity orderEntity);

	@Select("select id, order_id orderId, uid, user_code userCode, mobile, goods_type goodsType, payment, status, ret_msg retMsg, pay_type payType, buyer, trade_no tradeNo, trade_status tradeStatus, create_time createTime, update_time updateTime from a_auction_order where order_id = #{orderId}")
	OrderEntity selectByOrderId(Long orderId);

	@Update({ "<script>", " update a_auction_order ", 
			" <set> ",
			" <if test='status!= null'> `status`= #{status},</if> ",
			" <if test='tradeNo!= null'> `trade_no`= #{tradeNo},</if> ",
			" <if test='tradeStatus!= null'> `trade_status`= #{tradeStatus},</if> ",
			" <if test='buyer!= null'> `buyer`= #{buyer},</if> ",
			" <if test='retMsg!= null'> `ret_msg`= #{retMsg}</if> ", 
			" </set> ", 
			" where ", " id = #{id} ",
			"</script>" })
	int updateById(OrderEntity orderEntity);
}
