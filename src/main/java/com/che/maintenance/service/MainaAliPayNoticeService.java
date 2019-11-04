package com.che.maintenance.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.OrderStatus;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.maintenance.pojo.QueryStatus;
import com.che.pay.ali.notice.AbstractAliPayNoticeService;
import com.che.pay.ali.pojo.AliPayCallbackContext;
import com.che.pay.ali.util.AliConstants;
import com.che.recharge.common.RechargeConstants;

@Service
public class MainaAliPayNoticeService extends AbstractAliPayNoticeService {
	private static final Logger logger = LogManager.getLogger(MainaAliPayNoticeService.class);
	
	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;
	
	@Resource
	private MaintenanceInfoService maintenanceInfoService;
	
	@Autowired
	private MaintRechargeService maintRechargeService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected boolean process(AliPayCallbackContext aliPayCallbackContext) throws Exception {
		Long orderId = Long.valueOf(aliPayCallbackContext.getOrder_id());
		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		if (maintOrderEntity == null) {
			logger.info("维保订单不存在： " + orderId);
			return false;
		}
//		orderEntity.setBuyer(aliPayCallbackContext.getBuyer_email());
		maintOrderEntity.setTrans_id(aliPayCallbackContext.getTrade_no());
		maintOrderEntity.setTrade_status(aliPayCallbackContext.getTrade_status());
		if (AliConstants.TRADE_FINISHED.equals(aliPayCallbackContext.getTrade_status())
				|| AliConstants.TRADE_SUCCESS.equals(aliPayCallbackContext.getTrade_status())) {// 成功
			logger.info("支付宝支付成功!");
			if (maintOrderEntity.getPay_status().intValue() == 0) {
				BigDecimal fee = new BigDecimal(aliPayCallbackContext.getTotal_fee());
				if (fee.compareTo(maintOrderEntity.getPayment()) != 0) {
					logger.info(orderId + " ： 该订单金额不一致！" + fee + " --- " + maintOrderEntity.getPayment());
					maintOrderEntity.setPay_status(RechargeConstants.PAY_STATUS_ERROR);
					maintOrderEntity.setRetmsg("支付宝回调金额和该订单金额不一致,实付金额为：" + aliPayCallbackContext.getTotal_fee());
					maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
					return false;
				}
				maintOrderEntity.setPay_status(OrderStatus.PAYED.value);
				maintOrderEntity.setRetmsg("订单支付成功");
//				maintOrderEntity.setQuery_status(QueryStatus.QUERYING.value); // 查询状态更新为查询中
				maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
				// 上传大用户dubbo
				try {
				maintRechargeService.uploadConsumerDetail(maintOrderEntity);
				
				} catch (Exception e) {
					
					return false;
				}
				
				// 购买维保报告
				QueryReportReq queryReportReq = new QueryReportReq();
				queryReportReq.setOrder_id(orderId);
				maintenanceInfoService.buyReport(queryReportReq);
			}
			return true;
		} else {
			logger.info(aliPayCallbackContext.getTrade_status());
			return false;
		}
	}

}
