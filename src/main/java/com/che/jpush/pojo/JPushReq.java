package com.che.jpush.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class JPushReq extends Req {

	private static final long serialVersionUID = -9056858514975787582L;

	private Integer main_switch_state; // 推送总开关（0-关闭，1-打开）

	private Integer sync_switch_state; // 车辆竞拍同步推送开关状态（0-关闭，1-打开）

	// private Integer subs_switch_state; // 订阅信息推送开关状态（0-关闭，1-打开）
}
