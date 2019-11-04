package com.che.maintenance.pojo;

import com.che.common.web.Reply;
import com.che.search.maintenance.entity.MaintenanceDetail;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenanceInfoReply extends Reply {

	private static final long serialVersionUID = -722109647997044747L;

	private MaintenanceDetail maintenanceDetail;
	
}
