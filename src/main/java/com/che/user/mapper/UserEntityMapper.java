package com.che.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.che.user.entity.UserEntity;

public interface UserEntityMapper {
    @Insert({"INSERT INTO `t_user` ",
            "(`user_id`, `mobile`, `password`, `account`, `openqq_id`, `openwb_id`, `openwx_id`, `account_type`, `reg_time`, `last_login_time`, `last_login_ip`, `login_count`, `avatar`, `gender`, `city`, `phone_type`, `app_version`, `imei`, `mac`, `android_id`, `uuid`, `resolution`, `manufacture`, `model`, `carrier`, `publish_code`, `os_version`, `market`, `enabled`, `updated`)", 
            " VALUES ", 
            "( #{user_id}, #{mobile}, #{password}, #{account}, #{openqq_id}, ",
            "  #{openwb_id}, #{openwx_id}, #{account_type}, #{reg_time}, #{last_login_time}, ",
            "  #{last_login_ip}, #{login_count}, #{avatar}, #{gender}, #{city}, #{phone_type}, #{app_version}, #{imei},",
            "  #{mac}, #{android_id}, #{uuid}, #{resolution}, #{manufacture}, ",
            "  #{model}, #{carrier}, #{publish_code}, #{os_version}, #{market}, #{enabled}, #{updated}) "})
    public int insert(UserEntity user);

    @Update(" UPDATE `t_user` SET `password`=#{password} , `updated` = #{updated} WHERE `mobile` = #{mobile}")
    int resetPwd(@Param("mobile") Long mobile, @Param("password") String password, @Param("updated") Long updated);
    
	@Select(" select * from t_user where mobile = #{mobile} ")
	UserEntity getUserByMobile(Long mobile);

	@Select(" select * from t_user where user_id = #{user_id} ")
	UserEntity getUserById(Long user_id);

	@Select({"<script>",
		" select * from t_user" ,
		" <where>" ,
		"   <if test=\"openqq_id != null\"> OR openqq_id = #{openqq_id} </if>" ,
		"   <if test=\"openwb_id != null\"> OR openwb_id = #{openwb_id} </if>" ,
		"   <if test=\"openwx_id != null\"> OR openwx_id = #{openwx_id} </if>" ,
		" </where>",
		"</script>"})
	List<UserEntity> getUsersByOpenId(@Param("openqq_id") String openqq_id, @Param("openwb_id") String openwb_id, @Param("openwx_id") String openwx_id);

	@Select({" SELECT * FROM t_user WHERE openqq_id = #{openqq_id} "})
	UserEntity getUsersByOpenIdQq(@Param("openqq_id") String openqq_id);
	
	@Select({" SELECT * FROM t_user WHERE openwb_id = #{openwb_id} "})
	UserEntity getUsersByOpenIdWb(@Param("openwb_id") String openwb_id);
	
	@Select({" SELECT * FROM t_user WHERE openwx_id = #{openwx_id} "})
	UserEntity getUsersByOpenIdWx(@Param("openwx_id") String openwx_id);
	
	/*@Select({" <script>select * from t_user <where> " ,
			"  uuid = #{uuid} and account_type = 0 " ,
			"<if test=\"user_id != null \"> and user_id = #{user_id} </if>" ,
			"</where> </script>"})
	UserEntity getAnonymousUser(@Param("user_id") Long user_id, @Param("uuid") String uuid );*/

	@Select({" select * from t_user where user_id = #{user_id} and account_type = 0 limit 1"})
	UserEntity getAnonymousUser(@Param("user_id") String uuid );
	
	
	
	@Update({"<script>",
		" UPDATE `t_user`                                                                       " ,
		" <set>                                                                                   " ,
		" <if test=\"mobile		       != null\"> `mobile`			= #{mobile}		 	  , </if> " ,
		" <if test=\"password		   != null\"> `password`		= #{password}		  , </if> " ,
		" <if test=\"account		   != null\"> `account`			= #{account}		  , </if> " ,
		" <if test=\"openqq_id		   != null\"> `openqq_id`		= #{openqq_id}		  , </if> " ,
		" <if test=\"openwb_id		   != null\"> `openwb_id`		= #{openwb_id}		  , </if> " ,
		" <if test=\"openwx_id		   != null\"> `openwx_id`		= #{openwx_id}		  , </if> " ,
		" <if test=\"account_type	   != null\"> `account_type`	= #{account_type}	  , </if> " ,
		" <if test=\"reg_time		   != null\"> `reg_time`		= #{reg_time}		  , </if> " ,
		" <if test=\"last_login_time   != null\"> `last_login_time`	= #{last_login_time}  , </if> " ,
		" <if test=\"last_login_ip	   != null\"> `last_login_ip`	= #{last_login_ip}	  , </if> " ,
		" <if test=\"login_count	   != null\"> `login_count`		= #{login_count}	  , </if> " ,
		" <if test=\"avatar	   != null\"> `avatar`		= #{avatar}	  , </if> " ,
		" <if test=\"gender	   != null\"> `gender`		= #{gender}	  , </if> " ,
		" <if test=\"city	   != null\"> `city`		= #{city}	  , </if> " ,
		" <if test=\"phone_type        != null\"> `phone_type`		= #{phone_type}		  , </if> " ,
		" <if test=\"app_version	   != null\"> `app_version`		= #{app_version}	  , </if> " ,
		" <if test=\"imei			   != null\"> `imei`			= #{imei}			  , </if> " ,
		" <if test=\"mac			   != null\"> `mac`				= #{mac}			  , </if> " ,
		" <if test=\"android_id	       != null\"> `android_id`		= #{android_id}		  , </if> " ,
		" <if test=\"uuid			   != null\"> `uuid`			= #{uuid}			  , </if> " ,
		" <if test=\"resolution	       != null\"> `resolution`		= #{resolution}		  , </if> " ,
		" <if test=\"manufacture	   != null\"> `manufacture`		= #{manufacture}	  , </if> " ,
		" <if test=\"model			   != null\"> `model`			= #{model}			  , </if> " ,
		" <if test=\"carrier		   != null\"> `carrier`			= #{carrier}		  , </if> " ,
		" <if test=\"publish_code	   != null\"> `publish_code`	= #{publish_code}	  , </if> " ,
		" <if test=\"os_version	       != null\"> `os_version`		= #{os_version}		  , </if> " ,
		" <if test=\"market		       != null\"> `market`			= #{market}			  , </if> " ,
		" <if test=\"enabled		   != null\"> `enabled`			= #{enabled}		  , </if> " ,
		" <if test=\"updated		   != null\"> `updated`			= #{updated}		  , </if> " ,
		" </set> WHERE                                                                            " ,
		"	(`user_id` = #{user_id}) ",
	    "</script>"})
	void update(UserEntity user);
	
	/**
	 * 登录更新数据
	 * @param mobile
	 * @param ip
	 * @param versionCode
	 */
	@Update(" update t_user set last_login_ip = #{ip} ,login_count = login_count+1 ,app_version = #{app_version}, updated = #{updated} where user_id = #{user_id}")
	void loginUpdate(@Param("user_id") Long user_id, @Param("ip") String ip, @Param("app_version") Integer app_version, @Param("updated") Long updated);
	
	@Update(" update t_user set mobile = #{mobile}, updated = #{updated} where user_id = #{user_id}")
	void updateMobile(@Param("user_id") Long user_id, @Param("mobile") Long mobile, @Param("updated") Long updated);
	
	@Update(" update t_user set password = #{password}, updated = #{updated} where user_id = #{user_id}")
	void modifyPwd(@Param("user_id") Long user_id, @Param("password") String password, @Param("updated") Long updated);
	
	@Select(" select u.mobile from t_shop_user u where u.mobile != '' and u.mobile is not null")
	List<String> selectMobiles();
	
}
