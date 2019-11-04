package com.che.valuation.service;

import com.che.search.exception.CheSearchException;
import com.che.valuation.pojo.ValuationDelHistoryReply;
import com.che.valuation.pojo.ValuationDelHistoryReq;
import com.che.valuation.pojo.ValuationHistoryDetailReply;
import com.che.valuation.pojo.ValuationHistoryDetailReq;
import com.che.valuation.pojo.ValuationHistoryReply;
import com.che.valuation.pojo.ValuationHistoryReq;
import com.che.valuation.pojo.ValuationRelateReply;
import com.che.valuation.pojo.ValuationRelateReq;

/**
 * 估价
 * @author wangzhen
 *
 */
public interface ValuationService {

	ValuationRelateReply searchValuation(String sysName, ValuationRelateReq req) throws CheSearchException, Exception;
	
	ValuationHistoryReply historyList(ValuationHistoryReq valuationHistoryReq) throws CheSearchException, Exception;
	
	ValuationHistoryDetailReply historyDetail(ValuationHistoryDetailReq valuationHistoryDetailReq) throws CheSearchException, Exception;
	
	ValuationDelHistoryReply deleteHistoryList(ValuationDelHistoryReq valuationDelHistoryReq) throws Exception;
	
}
