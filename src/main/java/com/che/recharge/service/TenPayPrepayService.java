package com.che.recharge.service;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.id.IdWorker;
import com.che.common.web.Constants;
import com.che.pay.ten.pojo.BaseTenPayReply;
import com.che.pay.ten.pojo.TenPayContext;
import com.che.pay.ten.prepay.AbstractTenPayPrepayService;
import com.che.pay.ten.redis.RedisTenPayPrepayIdTool;
import com.che.pay.tx.api.ITenPayPrepayIdTool;
import com.che.payment.order.api.IOrderService;
import com.che.payment.order.common.ProductType;
import com.che.payment.order.param.OrderReq;
import com.che.payment.wx.api.IWxpayService;
import com.che.payment.wx.param.WxpayReply;
import com.che.payment.wx.param.WxpayReq;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.mapper.OrderEntityMapper;
import com.che.recharge.pojo.RechargeTenPayReq;
import com.che.user.service.UserCommonService;

@Service
public class TenPayPrepayService extends AbstractTenPayPrepayService<BaseTenPayReply, RechargeTenPayReq> {
//	@Value("${tenpay.notice}")
//	private String tenPayNotice;
	
	@Reference(version="1.0.0")
	private IOrderService iOrderService;
	
	@Reference(version="1.0.0")
	private IWxpayService iWxpayService;

	@Resource
	private RedisTenPayPrepayIdTool redisTenPayPrepayIdTool;

	@Resource
	private IdWorker idWorker;

	@Autowired
	private UserCommonService userCommonService;

	@Autowired
	private OrderEntityMapper orderEntityMapper;

	@Override
	public String tenpayNotice() {
		return null;
	}

	@Override
	public ITenPayPrepayIdTool getTenPayPrepayIdTool() {
		return redisTenPayPrepayIdTool;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected TenPayContext createContext(RechargeTenPayReq req, HttpServletRequest request) throws Exception {
		TenPayContext context = new TenPayContext();
		if (!this.isValid(req)) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("参数错误");
			return context;
		}
//		Long orderId = idWorker.nextId();
		Long userCode = userCommonService.getUser().getCode();
		Long uid = userCommonService.getUserId();
		String mobile = userCommonService.getUser().getMobile();
		
		String orderId = "";
		Integer goodsType = req.getType();
		BigDecimal payment = new BigDecimal(RechargeConstants.AMOUNTS[req.getAmount().intValue() - 1]);
		String productName = goodsType.intValue() == 1 ? "余额充值" : "保证金充值";
		
		OrderReq orderReq = new OrderReq();
		orderReq.setAllAmount(payment);
		orderReq.setAmount(payment);
		orderReq.setDeposit(new BigDecimal(0));
		orderReq.setMobile(mobile);
		orderReq.setProductName(productName);
		orderReq.setProductDesc(productName);
		
		if (goodsType.intValue() == 1) {
			// 余额充值
			orderId = iOrderService.nextId(Constants.SYSNAME, ProductType.BALANCE, orderReq);
		} else if (goodsType.intValue() == 2) {
			// 保证金充值
			orderId = iOrderService.nextId(Constants.SYSNAME, ProductType.DEPOSIT, orderReq);
		}

		if (StringUtils.isNotBlank(orderId)) {
			
			Integer payType = RechargeConstants.PAY_TYPE_TEN;// 微信
			
			context.setOutTradeNo(orderId);
			context.setTotalFee(payment);
			
			// 生成prepayId 根据实际场景。这里充值订单入数据库,和生成prepayid需要绑定在一个事务中
			// 充值订单入库
			OrderEntity order = new OrderEntity();
			order.setOrderId(Long.parseLong(orderId));
			order.setUid(uid);
			order.setUserCode(userCode);
			order.setGoodsType(goodsType);
			order.setPayType(payType);
			order.setMobile(mobile);//手机号
			order.setPayment(payment);
			orderEntityMapper.insert(order);
			
		}

		return context;
	}
	
	@Override
	public BaseTenPayReply tenPay(HttpServletRequest request, BaseTenPayReply reply, RechargeTenPayReq req) throws Exception {
		TenPayContext context = this.createContext(req, request);
		if (!context.isRight()) {
			reply.setReplyCode(context.getReplyCode());
			reply.setMessage(context.getMsg());
			return reply;
		}
		int retcode = 0;
		String retmsg = "ok";
		
		if (StringUtils.isNotBlank(context.getOutTradeNo()) && StringUtils.isNotBlank(context.getTotalFee() + "")) {
			
			WxpayReq wxpayReq = new WxpayReq();
			wxpayReq.setAmount(context.getTotalFee());
			wxpayReq.setOrderId(context.getOutTradeNo());
			WxpayReply wxpayReply = iWxpayService.app(Constants.SYSNAME, wxpayReq);
			if (!ObjectUtils.isEmpty(wxpayReply)) {
				
				reply.setParams(wxpayReply.getParams());
			} else {
				retcode = Constants.RESULT_ERROR_PARAM;
				retmsg = "参数错误";
			}
		}
		
		reply.setMessage(retmsg);
		reply.setReplyCode(retcode);
		return reply;
	}

	private boolean isValid(RechargeTenPayReq req) {
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
