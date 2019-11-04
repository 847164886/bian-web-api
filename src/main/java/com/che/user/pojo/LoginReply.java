package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

/**
 * @author karlhell
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class LoginReply extends Reply {
	private static final long serialVersionUID = 4729872597038110004L;
	
	private Long uid; // 用户id
	private Long mobile; // 手机号
	private String account; // 余额
	private String session; // sessionid
	
	private Integer certifyStatus; // 认证状态
	private String name; // 姓名
	
	private Integer main_switch_state; // 推送总开关（0-关闭，1-打开）
	private Integer sync_switch_state; // 车辆竞拍同步推送开关状态（0-关闭，1-打开）

	// 登录成功返回用户信息
//	private UserPojo userPojo;
/*	
	private String avatar ; // 头像信息
	private int    gender ; // 性别 
	private String city   ; // 城市
*/}
