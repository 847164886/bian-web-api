package com.che.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.che.user.entity.ShopUserBailRecord;

public interface ShopUserBailRecordMapper {

	@Select("select * from t_shop_user_bail_record where shop_user_id=#{user_id}   ORDER BY `addTime` DESC ")
	public List<ShopUserBailRecord> selectByUserId(Long user_id);
}
