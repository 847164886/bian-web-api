package com.che.valuation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.search.valuation.entity.ValuationResult;

public interface ValuationRelateMapper {

	@Insert("insert into `a_valuation_relate` (`user_id`, `val_id`, `create_time`) values(#{user_id}, #{val_id}, now())")
	public int insert(ValuationResult valuationResult);

	@Select("select a.val_id from `a_valuation_relate` a where a.user_id = #{user_id} and a.status = 1 order by create_time desc")
	public List<Long> selectValHistoryByUserId(Long user_id);

	@Update({ "<script>", "update `a_valuation_relate` a", "<set>", "a.status = 0", " , a.update_time = now()", "</set>",
			"<where>", "a.user_id = #{user_id} and a.status = 1", "<if test='val_ids != null'>and a.val_id in <foreach collection='val_ids' index='index' item='val_id' open='(' separator=',' close=')'>#{val_id}</foreach> </if></where>",
			"</script>" })
	public int updateBatchValHistoryByUserIdAndVals(@Param("user_id") Long user_id, @Param("val_ids") List<Long> val_ids);
}
