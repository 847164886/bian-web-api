package com.che.maintenance.pojo;

import com.che.common.web.Req;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenanceOrderCreateReq extends Req {

	private static final long serialVersionUID = 8010870555580111409L;

	private String vin; // 汽车vin码

	private String engineId; // 发动机号

	private String carNo; // 车牌号

	private String remark; // 备注

	private String payment; // 金额

}
