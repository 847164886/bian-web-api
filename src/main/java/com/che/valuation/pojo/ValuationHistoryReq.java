package com.che.valuation.pojo;

import com.che.common.web.Constants;
import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValuationHistoryReq extends Req {

	private static final long serialVersionUID = 5985917589646411102L;

	private Integer page = 1;

	private Integer pageSize = Constants.PAGESIZE;

}
