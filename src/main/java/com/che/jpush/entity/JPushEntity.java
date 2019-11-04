package com.che.jpush.entity;

import java.util.Date;

import lombok.Data;

@Data
public class JPushEntity {

	private Long uid;// bigint(20) NULL用户ID
	private String alias;// bigint(20) NULL极光用户别名
	private Long last_login_time;// bigint(20) NULL用户最后一次打开app时间
	private Integer version_code;// varchar(50) NULL当前用户版本号
	private Integer main_switch_state; // 推送总开关（0-关闭，1-打开）
	private Integer sync_switch_state; // 车辆竞拍同步推送开关状态（0-关闭，1-打开）
	private Integer subs_switch_state; // 订阅信息推送开关状态（0-关闭，1-打开）
	private Date create_time; // 创建时间
	private Date modify_time; // 账户绑定更新时间
	private Date state_modify_time; // 状态更新时间
}
