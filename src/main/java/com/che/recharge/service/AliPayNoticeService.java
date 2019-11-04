package com.che.recharge.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.che.pay.ali.notice.AbstractAliPayNoticeService;
import com.che.pay.ali.pojo.AliPayCallbackContext;
import com.che.pay.ali.util.AliConstants;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.mapper.OrderEntityMapper;

@Service
public class AliPayNoticeService extends AbstractAliPayNoticeService {
	private static final Logger logger = LogManager.getLogger(AliPayNoticeService.class);
	@Autowired
	private OrderEntityMapper orderEntityMapper;
	@Resource
	private RechargeUserService rechargeUserService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected boolean process(AliPayCallbackContext aliPayCallbackContext) throws Exception {
		Long orderId = Long.valueOf(aliPayCallbackContext.getOrder_id());
		OrderEntity orderEntity = orderEntityMapper.selectByOrderId(orderId);
		if (orderEntity == null) {
			logger.info("充值订单不存在： " + orderId);
			return false;
		}
		orderEntity.setBuyer(aliPayCallbackContext.getBuyer_email());
		orderEntity.setTradeNo(aliPayCallbackContext.getTrade_no());
		orderEntity.setTradeStatus(aliPayCallbackContext.getTrade_status());
		if (AliConstants.TRADE_FINISHED.equals(aliPayCallbackContext.getTrade_status())
				|| AliConstants.TRADE_SUCCESS.equals(aliPayCallbackContext.getTrade_status())) {// 成功
			logger.info("支付宝支付成功!");
			if (orderEntity.getStatus().intValue() == 0) {
				BigDecimal fee = new BigDecimal(aliPayCallbackContext.getTotal_fee());
				if (fee.compareTo(orderEntity.getPayment()) != 0) {
					logger.info(orderId + " ： 该订单金额不一致！" + fee + " --- " + orderEntity.getPayment());
					orderEntity.setStatus(RechargeConstants.PAY_STATUS_ERROR);
					orderEntity.setRetMsg("支付宝回调金额和该订单金额不一致,实付金额为：" + aliPayCallbackContext.getTotal_fee());
					orderEntityMapper.updateById(orderEntity);
					return false;
				}
				orderEntity.setStatus(RechargeConstants.PAY_STATUS_CONFIRM);
				orderEntity.setRetMsg("订单支付成功");
				orderEntityMapper.updateById(orderEntity);
				// 上传大用户dubbo
				rechargeUserService.uploadDetail(orderEntity);
			}
			return true;
		} else {
			logger.info(aliPayCallbackContext.getTrade_status());
			return false;
		}
	}

}
