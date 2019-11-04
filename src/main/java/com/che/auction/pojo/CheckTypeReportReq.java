package com.che.auction.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class CheckTypeReportReq extends Req {

	private static final long serialVersionUID = -2337033426163989065L;

	private Long check_car_id;
}
