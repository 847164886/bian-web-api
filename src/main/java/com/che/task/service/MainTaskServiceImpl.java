package com.che.task.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 维保订单定时任务
 */
@Service(version = "1.0.0")
public class MainTaskServiceImpl implements MainTaskService {

	@Autowired
	private MainTaskBusinessService mainTaskBusinessService;

	/**
	 * 维保订单超时置已关闭
	 */
	@Override
	public void changeMaintOrderToClosed() {

		mainTaskBusinessService.changeMaintOrderToClosed();
	}

}
