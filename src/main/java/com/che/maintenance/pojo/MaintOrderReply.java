package com.che.maintenance.pojo;

import java.util.List;

import com.che.common.web.Reply;
import com.che.maintenance.entity.MaintOrderEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MaintOrderReply extends Reply {

	private static final long serialVersionUID = -3890264381886484107L;
	
	private List<MaintOrderEntity> maintOrderEntities;
}
