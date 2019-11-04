package com.che.valuation.pojo;

import java.util.List;

import com.che.common.web.Reply;
import com.che.search.valuation.entity.ValuationResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValuationHistoryReply extends Reply {

	private static final long serialVersionUID = 823478494282609142L;
	
	private List<ValuationResult> valuationResults;

}
