package com.che.maintenance.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.che.maintenance.service.MaintRefundService;
import com.che.search.exception.CheSearchException;
import com.che.search.maintenance.api.IMaintenanceListener;

@Component
public class MaintenanceListener implements IMaintenanceListener {
	
	@Autowired
	private MaintRefundService maintRefundService;

	@Override
	public boolean changed(Long orderId, String tradeNo, Integer state) throws CheSearchException, Exception {

		return maintRefundService.changed(orderId, tradeNo, state);
	}
	
}
