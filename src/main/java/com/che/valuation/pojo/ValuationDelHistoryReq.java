package com.che.valuation.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValuationDelHistoryReq extends Req {

	private static final long serialVersionUID = 823478494282609142L;

	private String val_id;
	
	private Integer type = 0; // 1-全部删除，0-选择删除
	
	public static final class DelType {
		
		public static final Integer SELEDEL  = 0; // 0-选择删除
		
		public static final Integer ALLDEL = 1; // 1-全部删除
	}
	
}
