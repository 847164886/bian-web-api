package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserRegistReq extends Req{
	private static final long serialVersionUID = -4777933726613037578L;
	
	private String     code ;               // 手机验证码
	private Long       mobile ;				// 手机号 
	private String     password ;			// 密码 
	private String 	   name;                //姓名
	private String 	   source;                //来源 0：个人 1：车商
	private String 	   recommend_code ;		//推荐码
	private Long alias;                     //极光别名
	private Integer    app_version ;		// app版本号 
	
	
/*	private Integer    phone_type ;			// 手机类型 0：安卓，1：苹果 
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
*/	
	
	
}
