package com.che.valuation.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValuationHistoryDetailReq extends Req {

	private static final long serialVersionUID = 5985917589646411102L;

	private Long val_id;

}
