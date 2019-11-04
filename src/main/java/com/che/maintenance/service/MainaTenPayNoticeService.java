package com.che.maintenance.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.OrderStatus;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.pay.ten.notice.AbstractTenPayNoticeService;
import com.che.pay.ten.pojo.TenPayCallbackContext;
import com.che.pay.ten.pojo.TenPayCallbackResult;
import com.che.pay.ten.util.TenConstants;
import com.che.search.exception.CheSearchException;


@Service
public class MainaTenPayNoticeService extends AbstractTenPayNoticeService {
	private static final Logger logger = LoggerFactory.getLogger(MainaTenPayNoticeService.class);
	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;
	
	@Resource
	private MaintenanceInfoService maintenanceInfoService;
	
	@Autowired
	private MaintRechargeService maintRechargeService;

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = Exception.class)
	protected TenPayCallbackResult process(TenPayCallbackContext context) throws CheSearchException, Exception {
		TenPayCallbackResult ret = new TenPayCallbackResult();
		
		ret.setSuccess(true);
		ret.setReturnMsg("OK");

		Long orderId = Long.valueOf(context.getOrderId());
		
		MaintOrderEntity maintenanceOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		if (maintenanceOrderEntity == null) {
//			logger.info("充值订单不存在： " + orderId);
			ret.setSuccess(false);
			ret.setReturnMsg("订单不存在： " + orderId);
			return ret;
		}

		maintenanceOrderEntity.setTrans_id(context.getTransactionId());
		maintenanceOrderEntity.setTrade_status(context.getResultCode());

		if (TenConstants.RETURN_CODE_SUCCESS.equals(context.getReturnCode()) && TenConstants.RESULT_CODE_SUCCESS.equals(context.getResultCode())) {
//			if (maintenanceOrderEntity.getPay_status().intValue() == OrderStatus.PAYING.value) {
				// 微信金额单位为分
				if ((maintenanceOrderEntity.getPayment().multiply(new BigDecimal(100))).compareTo(new BigDecimal(context.getTotalFee())) != 0) {
					maintenanceOrderEntity.setPay_status(OrderStatus.UNPAY.value);
					maintenanceOrderEntity.setRetmsg("腾讯支付回调金额和该订单金额不一致,实付金额为：" + context.getTotalFee());
					maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);
//					logger.info(orderId + "：腾讯支付回调金额和该订单金额不一致!实付金额为：" + context.getTotalFee());
					ret.setSuccess(false);
					ret.setReturnMsg("腾讯支付回调金额和该订单金额不一致");
					return ret;
				}

				maintenanceOrderEntity.setPay_status(OrderStatus.PAYED.value);
				maintenanceOrderEntity.setRetmsg("订单支付成功");
//				maintenanceOrderEntity.setQuery_status(QueryStatus.QUERYING.value); // 查询状态更新为查询中
				
				maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);
				
				
				// 上传大用户dubbo，与大账户系统交互
				try {
					maintRechargeService.uploadConsumerDetail(maintenanceOrderEntity);
					
				} catch (Exception e) {
					ret.setSuccess(false);
					ret.setReturnMsg("");
					return ret;
				}
				
				try {
					// 购买维保报告
					QueryReportReq queryReportReq = new QueryReportReq();
					queryReportReq.setOrder_id(orderId);
					maintenanceInfoService.buyReport(queryReportReq);
				} catch (Exception e) {
					// 购买维保抛异常，可以保证支付状态更新成功
					logger.error("订单号：" + orderId + "购买维保报告异常：", e);
				}
				
//			}
		}
		
		return ret;
	}

}
