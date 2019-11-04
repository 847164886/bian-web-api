package com.che.maintenance.service;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.che.common.web.Constants;
import com.che.maintenance.listener.MaintenanceListener;
import com.che.search.maintenance.api.IMaintenanceCallbakService;

@Service
public class MaintCallBackService implements InitializingBean {

	@Autowired
	private IMaintenanceCallbakService maintenanceCallbakService;
	
	@Autowired
	private MaintenanceListener maintenanceListener;
	
	@Override
	public void afterPropertiesSet() throws Exception {

		maintenanceCallbakService.addListener(Constants.SYSNAME, maintenanceListener);
	}

}
