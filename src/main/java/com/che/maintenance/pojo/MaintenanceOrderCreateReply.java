package com.che.maintenance.pojo;

import com.che.common.web.Reply;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenanceOrderCreateReply extends Reply {

	private static final long serialVersionUID = -722109647997044747L;

	private Long order_id; // 订单号

	private String payment;// 订单金额
}
