/*package com.che.recharge.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.che.pay.ten.notice.AbstractTenPayNoticeService;
import com.che.pay.ten.pojo.TenPayCallbackContext;
import com.che.pay.ten.pojo.TenPayCallbackResult;
import com.che.pay.ten.util.TenConstants;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.mapper.OrderEntityMapper;

@Service
public class TenPayNoticeService extends AbstractTenPayNoticeService {
	private static final Logger logger = LogManager.getLogger(AliPayNoticeService.class);
	@Autowired
	private OrderEntityMapper orderEntityMapper;
	@Resource
	private RechargeUserService rechargeUserService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected TenPayCallbackResult process(TenPayCallbackContext context) throws Exception {
		TenPayCallbackResult ret = new TenPayCallbackResult();
		ret.setSuccess(true);
		ret.setReturnMsg("OK");

		Long orderId = Long.valueOf(context.getOrderId());
		OrderEntity orderEntity = orderEntityMapper.selectByOrderId(orderId);
		if (orderEntity == null) {
			logger.info("充值订单不存在： " + orderId);
			ret.setSuccess(false);
			ret.setReturnMsg("充值订单不存在： " + orderId);
			return ret;
		}

		orderEntity.setTradeNo(context.getTransactionId());
		orderEntity.setTradeStatus(context.getResultCode());

		if (TenConstants.RETURN_CODE_SUCCESS.equals(context.getReturnCode())
				&& TenConstants.RESULT_CODE_SUCCESS.equals(context.getResultCode())) {
			if (orderEntity.getStatus().intValue() == 0) {
				// 微信金额单位为分
				if ((orderEntity.getPayment().multiply(new BigDecimal(100)))
						.compareTo(new BigDecimal(context.getTotalFee())) != 0) {
					orderEntity.setStatus(RechargeConstants.PAY_STATUS_ERROR);
					orderEntity.setRetMsg("腾讯支付回调金额和该订单金额不一致,实付金额为：" + context.getTotalFee());
					orderEntityMapper.updateById(orderEntity);
					logger.info(orderId + "：腾讯支付回调金额和该订单金额不一致!实付金额为：" + context.getTotalFee());
					ret.setSuccess(false);
					ret.setReturnMsg("腾讯支付回调金额和该订单金额不一致");
					return ret;
				}

				orderEntity.setStatus(RechargeConstants.PAY_STATUS_CONFIRM);
				orderEntity.setRetMsg("订单支付成功");
				orderEntityMapper.updateById(orderEntity);
				// 上传大用户dubbo
				rechargeUserService.uploadDetail(orderEntity);
			}
		}
		return ret;
	}

}
*/