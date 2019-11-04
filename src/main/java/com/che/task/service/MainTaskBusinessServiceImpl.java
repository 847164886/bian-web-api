package com.che.task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.che.maintenance.mapper.MaintOrderCreateMapper;

/**
 * 定时任务业务类
 *
 */
@Service
public class MainTaskBusinessServiceImpl implements MainTaskBusinessService {

	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;

	/**
	 * 维保订单超时置已关闭
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	public void changeMaintOrderToClosed() {

		// 更新所有超时未付款订单（48小时未付款）为已关闭
		maintOrderCreateMapper.updateMaintOrderToClosed();

	}
}
