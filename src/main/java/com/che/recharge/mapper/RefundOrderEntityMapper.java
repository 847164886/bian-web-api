package com.che.recharge.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.recharge.entity.RefundOrderEntity;

public interface RefundOrderEntityMapper {

	@Insert("insert into t_refund_order "+ 
			" (id,uid,order_id,order_type,total_fee,refund_fee,pay_type,status,create_time) "+
			" values "+
			" (#{id},#{uid}, #{order_id} ,#{order_type},#{total_fee},#{refund_fee},#{pay_type},#{status},now()) ")
	public int insert(RefundOrderEntity refundOrderEntity);
	
	
	@Update({"<script>",
		" update t_refund_order " ,
		" <set> " ,
		" <if test='return_code!= null'> `return_code`= #{return_code},</if> " ,
		" <if test='return_msg!= null'> `return_msg`= #{return_msg},</if> " ,
		" <if test='result_code!= null'> `result_code`= #{result_code},</if> " ,
		" <if test='err_code!= null'> `err_code`= #{err_code},</if> " ,
		" <if test='refund_id!= null'> `refund_id`= #{refund_id},</if> " ,
		" <if test='real_refund_fee!= null'> `real_refund_fee`= #{real_refund_fee},</if> " ,
		" <if test='status!= null'> `status`= #{status},</if> " ,
		" update_time = now()",
		" </set> " ,
		" where " ,
		" id=${id}",
	    "</script>"})
	public int updateRefundOrder(RefundOrderEntity refundOrderEntity);
	
	@Select("select * from t_refund_order where id = #{order_id}")
	public RefundOrderEntity selectRefundOrderById(Long order_id);
}
