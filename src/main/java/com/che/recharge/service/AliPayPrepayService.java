package com.che.recharge.service;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.che.common.id.IdWorker;
import com.che.common.web.Constants;
import com.che.pay.ali.pojo.AliPayContext;
import com.che.pay.ali.pojo.BaseAliPayReply;
import com.che.pay.ali.prepay.AbstractAliPayPrepayService;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.mapper.OrderEntityMapper;
import com.che.recharge.pojo.RechargeAliPayReq;
import com.che.user.service.UserCommonService;

@Service
public class AliPayPrepayService extends AbstractAliPayPrepayService<BaseAliPayReply, RechargeAliPayReq> {

	@Value("${alipay.notice}")
	private String aliPayNotice;

	@Resource
	private IdWorker idWorker;

	@Autowired
	private UserCommonService userCommonService;

	@Autowired
	private OrderEntityMapper orderEntityMapper;

	@Override
	public String aliPayNotice() {
		return aliPayNotice;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected AliPayContext createContext(RechargeAliPayReq req) throws Exception {
		AliPayContext context = new AliPayContext();

		if (!this.isValid(req)) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("参数错误");
			return context;
		}

		Long orderId = idWorker.nextId();
		Long userCode = userCommonService.getUser().getCode();
		// if (userCode == null) {
		// userCode = 102030405060L;
		// }
		Integer goodsType = req.getType();
		Integer payType = 1;// 支付宝
		BigDecimal payment = new BigDecimal(RechargeConstants.AMOUNTS[req.getAmount().intValue() - 1]);

		OrderEntity order = new OrderEntity();
		order.setOrderId(orderId);
		order.setUserCode(userCode);
		order.setGoodsType(goodsType);
		order.setPayType(payType);
		order.setPayment(payment);

		orderEntityMapper.insert(order);

		context.setOrderId(orderId);
		context.setPayment(payment);
		return context;
	}

	private boolean isValid(RechargeAliPayReq req) {
		if (req == null)
			return false;
		if (req.getType() == null)
			return false;
		if (req.getAmount() == null)
			return false;
		if (req.getType().intValue() != 1 && req.getType().intValue() != 2)
			return false;
		if (req.getAmount().intValue() < 1 || req.getAmount().intValue() > 4)
			return false;

		return true;
	}
}
