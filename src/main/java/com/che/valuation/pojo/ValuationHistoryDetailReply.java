package com.che.valuation.pojo;

import com.che.common.web.Reply;
import com.che.search.valuation.entity.ValuationResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValuationHistoryDetailReply extends Reply {

	private static final long serialVersionUID = 823478494282609142L;
	
	private ValuationResult valuationResult;

}
