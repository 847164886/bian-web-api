package com.che.pay.notice.service;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.OrderStatus;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.maintenance.service.MaintRechargeService;
import com.che.maintenance.service.MaintenanceInfoService;
import com.che.payment.notice.param.PaymentNoticeReq;
import com.che.payment.notice.param.PaymentResult;
import com.che.payment.order.common.ProductType;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.mapper.OrderEntityMapper;
import com.che.recharge.service.RechargeUserService;

/**
 * 支付回调业务处理类
 * 
 * @author wangzhen
 */
@Service
public class PaymentNoticeService {

	private static final Logger logger = LogManager.getLogger(PaymentNoticeService.class);

	@Autowired
	private OrderEntityMapper orderEntityMapper;

	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;

	@Resource
	private RechargeUserService rechargeUserService;

	@Autowired
	private MaintenanceInfoService maintenanceInfoService;

	@Autowired
	private MaintRechargeService maintRechargeService;
	

	public PaymentResult notice(PaymentNoticeReq req) throws Exception {
		PaymentResult paymentResult = new PaymentResult();

		if (!ObjectUtils.isEmpty(req)) {
			String productType = req.getProductType();
			if (StringUtils.isNotBlank(productType)) {
				if (ProductType.BALANCE.name().equals(productType) || ProductType.DEPOSIT.name().equals(productType)) {

					paymentResult = this.recharge(req);

				} else if (ProductType.MAINTENANCE.name().equals(productType)) {

					paymentResult = this.maintenancePay(req);
				}
			}
		}

		return paymentResult;
	}

	/**
	 * 充值余额、保证金
	 * 
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private PaymentResult recharge(PaymentNoticeReq req) throws Exception {
		PaymentResult paymentResult = new PaymentResult();
		paymentResult.setSuccess(true);
		paymentResult.setMsg("OK");

		Long orderId = Long.valueOf(req.getOrderId());
		OrderEntity orderEntity = orderEntityMapper.selectByOrderId(orderId);
		if (orderEntity == null) {
			logger.info("充值订单不存在： " + orderId);
			paymentResult.setSuccess(false);
			paymentResult.setMsg("充值订单不存在： " + orderId);
			return paymentResult;
		}

//		orderEntity.setTradeNo(req.getBillNo());
//		orderEntity.setTradeStatus(req.isSuccess() + "");

		if (req.isSuccess()) {
			if (orderEntity.getStatus().intValue() == 0) {
				if (orderEntity.getPayment().compareTo(req.getAmount()) != 0) {
					orderEntity.setStatus(RechargeConstants.PAY_STATUS_ERROR);
					orderEntity.setRetMsg("腾讯支付回调金额和该订单金额不一致,实付金额为：" + req.getAmount());
					orderEntityMapper.updateById(orderEntity);
					paymentResult.setSuccess(false);
					paymentResult.setMsg("腾讯支付回调金额和该订单金额不一致");
					return paymentResult;
				}

				orderEntity.setStatus(RechargeConstants.PAY_STATUS_CONFIRM);
				orderEntity.setRetMsg("订单支付成功");
				orderEntityMapper.updateById(orderEntity);
				// 上传大用户dubbo
				rechargeUserService.uploadDetail(orderEntity);
			}
		} else {
			orderEntity.setStatus(RechargeConstants.PAY_STATUS_ERROR);
			orderEntity.setRetMsg("订单支付失败");
			orderEntityMapper.updateById(orderEntity);
			paymentResult.setSuccess(false);
			paymentResult.setMsg("订单支付失败");
			return paymentResult;
		}
		return paymentResult;
	}

	/**
	 * 维保支付
	 * 
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private PaymentResult maintenancePay(PaymentNoticeReq req) throws Exception {

		PaymentResult paymentResult = new PaymentResult();

		paymentResult.setSuccess(true);
		paymentResult.setMsg("OK");

		Long orderId = Long.valueOf(req.getOrderId());

		MaintOrderEntity maintenanceOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		if (maintenanceOrderEntity == null) {
			// logger.info("充值订单不存在： " + orderId);
			paymentResult.setSuccess(false);
			paymentResult.setMsg("订单不存在： " + orderId);
			return paymentResult;
		}
//		maintenanceOrderEntity.setTrans_id(req.getBillNo());
//		maintenanceOrderEntity.setTrade_status(req.isSuccess() + "");

		if (req.isSuccess()) {
			if (maintenanceOrderEntity.getPayment().compareTo(req.getAmount()) != 0) {
				maintenanceOrderEntity.setPay_status(OrderStatus.UNPAY.value);
				maintenanceOrderEntity.setRetmsg("腾讯支付回调金额和该订单金额不一致,实付金额为：" + req.getAmount());
				maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);
				paymentResult.setSuccess(false);
				paymentResult.setMsg("腾讯支付回调金额和该订单金额不一致");
				return paymentResult;
			}

			maintenanceOrderEntity.setPay_status(OrderStatus.PAYED.value);
			maintenanceOrderEntity.setRetmsg("订单支付成功");

			maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);

			// 上传大用户dubbo，与大账户系统交互
			try {
				maintRechargeService.uploadConsumerDetail(maintenanceOrderEntity);

			} catch (Exception e) {
				paymentResult.setSuccess(false);
				paymentResult.setMsg("");
				return paymentResult;
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

		} else {
			paymentResult.setSuccess(false);
			paymentResult.setMsg("订单支付失败");
			return paymentResult;
		}

		return paymentResult;
	}
}
