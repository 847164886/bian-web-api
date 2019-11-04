package com.che.task.service;

/**
 * 定时任务，走统一调度系统，需发布成dubbo服务
 */
public interface MainTaskService {

	public void changeMaintOrderToClosed();
}
