package com.che.valuation.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.web.Constants;
import com.che.search.exception.CheSearchException;
import com.che.search.valuation.api.IValuationService;
import com.che.search.valuation.entity.ValuationResult;
import com.che.search.valuation.param.ValuationReq;
import com.che.user.service.UserCommonService;
import com.che.valuation.mapper.ValuationRelateMapper;
import com.che.valuation.pojo.ValuationDelHistoryReply;
import com.che.valuation.pojo.ValuationDelHistoryReq;
import com.che.valuation.pojo.ValuationDelHistoryReq.DelType;
import com.che.valuation.pojo.ValuationHistoryDetailReply;
import com.che.valuation.pojo.ValuationHistoryDetailReq;
import com.che.valuation.pojo.ValuationHistoryReply;
import com.che.valuation.pojo.ValuationHistoryReq;
import com.che.valuation.pojo.ValuationRelateReply;
import com.che.valuation.pojo.ValuationRelateReq;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 估价
 * 
 * @author wangzhen
 *
 */
@Service
public class ValuationServiceImpl implements ValuationService {

	private static final Logger logger = LoggerFactory.getLogger(ValuationServiceImpl.class);

	@Reference(version = "1.0.0")
	private IValuationService iValuationService;

	@Autowired
	private UserCommonService userCommonService;

	@Autowired
	private ValuationRelateMapper valuationRelateMapper;

	/**
	 * 查询估价信息
	 * 
	 * @throws Exception
	 * @throws CheSearchException，抛出去，进行统一异常处理
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ValuationRelateReply searchValuation(String sysName, ValuationRelateReq req)
			throws CheSearchException, Exception {
		if (sysName == null || "".equals(sysName)) {
			sysName = Constants.SYSNAME;
		}
		ValuationResult valuationResult = new ValuationResult();
		ValuationRelateReply reply = new ValuationRelateReply();
		// check参数
		if (null == req.getMile() || null == req.getModelId() || null == req.getRegDate() || null == req.getZone()
				|| null == req.getTitle()) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数不能为空");
			logger.error("参数有误，查询参数：" + req.toString() + "系统标识：" + Constants.SYSNAME);
			return reply;
		}
		ValuationReq valuationReq = new ValuationReq();
		BeanCopier valuationReqCopy = BeanCopier.create(ValuationRelateReq.class, ValuationReq.class, false);
		valuationReqCopy.copy(req, valuationReq, null);
		// 调用估价查询接口
		// try {
		valuationResult = iValuationService.searchValuation(sysName, valuationReq);
		// 维护用户估价关联
		if (valuationResult == null) {
			reply.setReplyCode(Constants.REPLY_SUCCESS);
			reply.setMessage("无估价报告");
			return reply;
		}
		valuationResult.setUser_id(userCommonService.getUserId().longValue());
		valuationRelateMapper.insert(valuationResult);
		reply.setValuationResult(valuationResult);
		reply.setReplyCode(Constants.REPLY_SUCCESS);
		reply.setMessage("估价报告查询成功");
		logger.info("调用估价查询接口成功，返回结果：" + valuationResult.toString() + "系统标识：" + Constants.SYSNAME);
		return reply;
		// } catch (Exception e) {
		// logger.error("调用估价查询接口失败，查询参数：" + req.toString() + "系统标识：" +
		// ValuationConstant.SYSNAME, e.getMessage());
		// reply.setReplyCode(Constants.RESULT_ERROR_REQUEST);
		// reply.setMessage("服务端处理异常");
		// return reply;
		// }

	}

	/**
	 * 查询用户历史估价查询信息
	 * 
	 * @throws Exception
	 * @throws CheSearchException，抛出去，进行统一异常处理
	 */
	@Override
	public ValuationHistoryReply historyList(ValuationHistoryReq valuationHistoryReq)
			throws CheSearchException, Exception {
		ValuationHistoryReply reply = new ValuationHistoryReply();

		if (valuationHistoryReq.getPage() == 0) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数不正确");
			return reply;
		}

		// 分页
		PageHelper.startPage(valuationHistoryReq.getPage(), valuationHistoryReq.getPageSize());
		// 查询历史估价关联
		List<Long> valIds = valuationRelateMapper.selectValHistoryByUserId(userCommonService.getUserId().longValue());

		Page<Long> page = (Page<Long>) valIds;

		if (page.getPages() < valuationHistoryReq.getPage()) {
			reply.setReplyCode(Constants.REPLY_SUCCESS);
			reply.setValuationResults(null);
			return reply;
		}

		// 调用用户历史估价查询信息接口
		// try {
		List<ValuationResult> historyList = iValuationService.historyList(Constants.SYSNAME, page.getResult());
		
		// 倒序排序
		Collections.sort(historyList, new Comparator<ValuationResult>() {

			@Override
			public int compare(ValuationResult valuationResult1, ValuationResult valuationResult2) {
				long val_order = valuationResult1.getVal_id() - valuationResult2.getVal_id();
				
				if (val_order > 0) {
					return -1;
				} else if (val_order == 0) {
					return 0;
				} else {
					return 1;
				}
			}
			
		});

		reply.setValuationResults(historyList);

		reply.setReplyCode(Constants.REPLY_SUCCESS);
		reply.setMessage("查询历史估价成功");
		// logger.info("调用历史估价查询接口成功，返回结果：" + historyList.toString() + "系统标识：" +
		// Constants.SYSNAME);
		return reply;
		// } catch (Exception e) {
		// logger.error("调用历史估价查询接口失败，查询参数：" + page.getResult() + "系统标识：" +
		// ValuationConstant.SYSNAME, e.getMessage());
		// reply.setReplyCode(Constants.RESULT_ERROR_REQUEST);
		// reply.setMessage("服务端处理异常");
		// return reply;
		// }

	}

	/**
	 * 历史查询明细
	 * 
	 * @throws Exception
	 * @throws CheSearchException
	 */
	@Override
	public ValuationHistoryDetailReply historyDetail(ValuationHistoryDetailReq valuationHistoryDetailReq)
			throws CheSearchException, Exception {
		ValuationHistoryDetailReply reply = new ValuationHistoryDetailReply();

		// check参数
		if (null == valuationHistoryDetailReq.getVal_id()) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数不能为空");
			logger.error("参数有误，查询参数：" + valuationHistoryDetailReq.getVal_id() + "系统标识：" + Constants.SYSNAME);
			return reply;
		}

		// 查询估价历史明细
		ValuationResult historyDetail = iValuationService.historyDetail(Constants.SYSNAME,
				valuationHistoryDetailReq.getVal_id());

		reply.setValuationResult(historyDetail);
		reply.setReplyCode(Constants.REPLY_SUCCESS);
		reply.setMessage("查询历史估价明细成功");
		// logger.info("调用历史估价明细查询接口成功，返回结果：" + historyDetail.toString() +
		// "系统标识：" + Constants.SYSNAME);
		return reply;
	}

	/**
	 * 删除估价历史记录
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ValuationDelHistoryReply deleteHistoryList(ValuationDelHistoryReq valuationDelHistoryReq) throws Exception {
		ValuationDelHistoryReply reply = new ValuationDelHistoryReply();
		if ((valuationDelHistoryReq.getVal_id() == null || "".equals(valuationDelHistoryReq.getVal_id())) && valuationDelHistoryReq.getType().intValue() == DelType.SELEDEL) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数为空");
			return reply;
		}
		
		if (valuationDelHistoryReq.getType().intValue() == DelType.ALLDEL) {
			// 全部删除
			valuationRelateMapper.updateBatchValHistoryByUserIdAndVals(userCommonService.getUserId().longValue(), null);
			reply.setReplyCode(Constants.REPLY_SUCCESS);
			reply.setMessage("删除全部估价历史记录成功");
//			logger.info("删除估价历史记录成功");
		} else if (valuationDelHistoryReq.getType().intValue() == DelType.SELEDEL) {
			// 选择删除
			String val_idStr = valuationDelHistoryReq.getVal_id();
			
			String[] val_ids = val_idStr.split(",");
			
			List<Long> valList = new ArrayList<Long>();
			
			for (String val_id : val_ids) {
				valList.add(Long.parseLong(val_id.trim()));
			}
			
			valuationRelateMapper.updateBatchValHistoryByUserIdAndVals(userCommonService.getUserId().longValue(), valList);
			reply.setReplyCode(Constants.REPLY_SUCCESS);
			reply.setMessage("删除估价历史记录成功");
//			logger.info("删除估价历史记录成功");
			
		}

		return reply;
	}

}
