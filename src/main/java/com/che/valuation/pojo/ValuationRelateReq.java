package com.che.valuation.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValuationRelateReq extends Req {

	private static final long serialVersionUID = 823478494282609142L;

	private String modelId; // 车型号id
	private String regDate; // 待估车辆的上牌时间（格式：yyyy-MM）
	private String mile; // 待估车辆的公里数，单位万公里
	private String zone; // 城市标识

	private String title;
}
