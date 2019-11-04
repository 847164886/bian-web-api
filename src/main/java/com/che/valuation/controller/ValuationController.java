package com.che.valuation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.web.Constants;
import com.che.search.exception.CheSearchException;
import com.che.valuation.pojo.ValuationDelHistoryReply;
import com.che.valuation.pojo.ValuationDelHistoryReq;
import com.che.valuation.pojo.ValuationHistoryDetailReply;
import com.che.valuation.pojo.ValuationHistoryDetailReq;
import com.che.valuation.pojo.ValuationHistoryReply;
import com.che.valuation.pojo.ValuationHistoryReq;
import com.che.valuation.pojo.ValuationRelateReply;
import com.che.valuation.pojo.ValuationRelateReq;
import com.che.valuation.service.ValuationService;

/**
 * 
 * @author wangzhen
 * <p>估价相关接口</p>
 */
@RestController
@RequestMapping("/valuation")
public class ValuationController {
	
	@Autowired
	private ValuationService valuationService;

	/**
	 * 查询估价接口
	 * @throws Exception 
	 * @throws CheSearchException 
	 */
	@RequestMapping("/searchValuation")
	public ValuationRelateReply searchValuation(@RequestBody(required=false)ValuationRelateReq req) throws CheSearchException, Exception {
		ValuationRelateReply valuationRelateReply = valuationService.searchValuation(null, req);
		valuationRelateReply.setResultCode(Constants.RESULT_SUCCESS);
		return valuationRelateReply;
	}
	
	/**
	 * 查询用户历史估价接口
	 * @throws Exception 
	 * @throws CheSearchException 
	 */
	@RequestMapping("/historyList")
	public ValuationHistoryReply historyList(@RequestBody(required=false)ValuationHistoryReq valuationHistoryReq) throws CheSearchException, Exception {
		ValuationHistoryReply valuationHistoryReply = valuationService.historyList(valuationHistoryReq);
		valuationHistoryReply.setResultCode(Constants.RESULT_SUCCESS);
		return valuationHistoryReply;
	}
	
	/**
	 * 查询估价历史明细接口
	 * @throws Exception 
	 * @throws CheSearchException 
	 */
	@RequestMapping("/historyDetail")
	public ValuationHistoryDetailReply historyDetail(@RequestBody(required=false)ValuationHistoryDetailReq valuationHistoryDetailReq) throws CheSearchException, Exception {
		ValuationHistoryDetailReply valuationHistoryDetailReply = valuationService.historyDetail(valuationHistoryDetailReq);
		valuationHistoryDetailReply.setResultCode(Constants.RESULT_SUCCESS);
		return valuationHistoryDetailReply;
	}
	
	/**
	 * 删除估价历史记录接口
	 * @throws Exception 
	 */
	@RequestMapping("/deleteHistoryList")
	public ValuationDelHistoryReply deleteHistoryList(@RequestBody(required=false)ValuationDelHistoryReq valuationDelHistoryReq) throws Exception {
		ValuationDelHistoryReply deleteHistoryReply = valuationService.deleteHistoryList(valuationDelHistoryReq);
		deleteHistoryReply.setResultCode(Constants.RESULT_SUCCESS);
		return deleteHistoryReply;
	}
}
