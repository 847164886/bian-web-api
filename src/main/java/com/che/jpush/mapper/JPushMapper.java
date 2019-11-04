package com.che.jpush.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.jpush.entity.JPushEntity;

public interface JPushMapper {

	@Select("select * from t_jpush where uid = #{uid}")
	public JPushEntity selectByUid(Long uid);
	
	@Select("select * from t_jpush where alias = #{alias}")
	public JPushEntity selectByAlias(Long alias);
	
	@Select("select * from t_jpush where alias = #{alias} and uid = #{uid}")
	public JPushEntity selectByAliasAndUid(@Param("uid") Long uid,@Param("alias") String alias);
	
	@Insert("insert into  t_jpush  ( uid,alias,main_switch_state,sync_switch_state,create_time) values( #{uid},#{alias},#{main_switch_state}, #{sync_switch_state}, now())")
	public int insert(JPushEntity jPushEntity);
	
	@Update("update t_jpush set alias = #{alias}, modify_time = now() where uid = #{uid}")
	public int UpdateByUid(@Param("uid") Long uid,@Param("alias") String alias);
	
	
	@Update({"<script>",
		" update t_jpush " ,
		" <set> " ,
		" <if test='version_code!= null'> `version_code`= #{version_code},</if> " ,
		" <if test='last_login_time!= null'> `last_login_time`= #{last_login_time},</if> " ,
		" <if test='main_switch_state != null'>main_switch_state = #{main_switch_state},</if>",
		" <if test='sync_switch_state != null'>sync_switch_state = #{sync_switch_state},</if>",
		" state_modify_time = now()", 
		" </set> " ,
		" where " ,
		" uid = #{uid} ",
	    "</script>"})
	public int update(JPushEntity jPushEntity);
	
	@Select("select t.alias from t_jpush t where t.main_switch_state = 0 and t.alias != '' and t.alias is not null")
	public List<String> selectMainSwitchOffAliases();
	
	@Select("select t.alias from t_jpush t where t.main_switch_state = 1 and t.sync_switch_state = 0 and t.alias != '' and t.alias is not null")
	public List<String> selectSyncSwitchOffAliases();
	
}
