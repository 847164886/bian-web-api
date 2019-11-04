package com.che.maintenance.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.maintenance.entity.MaintOrderEntity;


public interface MaintOrderCreateMapper {

	@Insert("insert into a_maintenance_order "+ 
			" (id,uid,usercode, mobile, vin,remark,car_no,engine_id,goods_type,payment,pay_status,create_time) "+
			" values "+
			" (#{id},#{uid}, #{usercode} ,#{mobile}, #{vin},#{remark},#{car_no},#{engine_id},#{goods_type},#{payment},#{pay_status},now()) ")
	public int insert(MaintOrderEntity orderEntity);
	
	@Select("select * from a_maintenance_order where id = #{orderId}")
	public MaintOrderEntity selectMaintOrderById(Long orderId);
	
	@Select("select payment from a_maintenance_order where id = #{orderId}")
	public MaintOrderEntity selectPaymentById(Long orderId);
	
	@Update("update a_maintenance_order t set t.pay_status = 5, t.update_time = now() where t.pay_status = 0 and date_add(t.create_time, interval 48 hour) <= now()")
	public int updateMaintOrderToClosed();
	
	@Update({"<script>",
		" update a_maintenance_order " ,
		" <set> " ,
		" <if test='payment!= null'> `payment`= #{payment},</if> " ,
		" <if test='pay_status!= null'> `pay_status`= #{pay_status},</if> " ,
		" <if test='look_status!= null'> `look_status`= #{look_status},</if> " ,
		" <if test='trade_no!= null'> `trade_no`= #{trade_no},</if> " ,
		" <if test='pay_type!= null'> `pay_type`= #{pay_type},</if> " ,
		" <if test='query_status!= null'> `query_status`= #{query_status},</if> " ,
		" <if test='trade_status!= null'> `trade_status`= #{trade_status},</if> " ,
		" <if test='trans_id!= null'> `trans_id`= #{trans_id},</if> " ,
		" <if test='retcode!= null'> `retcode`= #{retcode},</if> " ,
		" <if test='retmsg!= null'> `retmsg`= #{retmsg},</if> " ,
		" <if test='source!= null'> `source`= #{source},</if> " ,
		" <if test='engine_id!= null'> `engine_id`= #{engine_id},</if> " ,
		" <if test='car_no!= null'> `car_no`= #{car_no},</if> " ,
		" <if test='remark!= null'> `remark`= #{remark},</if> " ,
		" <if test='model!= null'> `model`= #{model},</if> " ,
		" <if test='year!= null'> `year`= #{year},</if> " ,
		" <if test='brand!= null'> `brand`= #{brand},</if> " ,
		" <if test='failure_reson!= null'> `failure_reson`= #{failure_reson},</if> " ,
		" <if test='is_third!= null'> `is_third`= #{is_third},</if> " ,
		" update_time = now()",
		" </set> " ,
		" where " ,
		" id=${id}",
	    "</script>"})
	public int updateMaintOrder(MaintOrderEntity maintenanceOrderEntity);
	
	@Select({"<script>", "select t.id, t.vin, t.model, t.year, t.brand, t.payment, t.remark, t.query_status, t.look_status, t.pay_status, t.create_time from a_maintenance_order t where t.uid = #{uid}", 
			"<if test='pay_status != null'> and t.pay_status = #{pay_status}</if>", "order by t.create_time desc", "</script>"})
	public List<MaintOrderEntity> selectOrderList(@Param("uid") Long uid, @Param("pay_status") Integer pay_status);
	
}
