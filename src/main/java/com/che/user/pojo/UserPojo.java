package com.che.user.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class UserPojo implements Serializable{
	private static final long serialVersionUID = 876504223920738503L;
	private Long       user_id ;			// 用户id 
	private Long       mobile ;				// 手机号 
	private String account;				// 账户余额 2位小数
	private String     openqq_id ;			// QQ登录openid
	private String     openwb_id ;			// 微博登录openid 
	private String     openwx_id ;			// 微信登录openid 
	private Integer    account_type ;		// 账户类型账户类型0:anonymous,1:mobile, 2:openqq_id,3:openwb_id, 4:openwx_id
	private Long       reg_time ;			// 注册时间eg.20150909090909000 
	private Long       last_login_time ;	// 最近一次登录时间 
	private String     last_login_ip ;		// 最近一次登录ip 
	private Integer    login_count ;		// 登录次数 
	private Integer    phone_type ;			// 手机类型 0：安卓，1：苹果 
	private Integer    app_version ;		// app版本号 
	private String     imei ;				// imei 
	private String     mac ;				// mac 
	private String     android_id ;			// 安卓id 
	private String     uuid ;				// 安卓、苹果唯一生成码UUID 
	private String     resolution ;			// 手机分辨率 
	private String     manufacture ;		// 厂商 
	private String     model ;				// 机型 
	private String     carrier ;			// 运营商 
	private String     publish_code ;		// 手机发行编号 
	private String     os_version ;			// 系统版本 
	private String     market ;				// 市场 
	private Integer    enabled ;			// 是否有效
	
    private String avatar;                  
    private Integer gender;                 // 0:女, 1:男, null:未知
    private String city;
	
}
