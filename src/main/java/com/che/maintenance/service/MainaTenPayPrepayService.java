package com.che.maintenance.service;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.web.Constants;
import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.MaintenanceTenPayReq;
import com.che.maintenance.pojo.OrderStatus;
import com.che.pay.ten.pojo.BaseTenPayReply;
import com.che.pay.ten.pojo.TenPayContext;
import com.che.pay.ten.prepay.AbstractTenPayPrepayService;
import com.che.pay.ten.redis.RedisTenPayPrepayIdTool;
import com.che.pay.tx.api.ITenPayPrepayIdTool;
import com.che.payment.wx.api.IWxpayService;
import com.che.payment.wx.param.WxpayReply;
import com.che.payment.wx.param.WxpayReq;
import com.che.recharge.common.RechargeConstants;

@Service
public class MainaTenPayPrepayService extends AbstractTenPayPrepayService<BaseTenPayReply, MaintenanceTenPayReq> {
//	@Value("${tenpay.maintNotice}")
//	private String tenPayNotice; // 微信支付回调地址

	@Resource
	private RedisTenPayPrepayIdTool redisTenPayPrepayIdTool;

	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;
	
	@Reference(version="1.0.0")
	private IWxpayService iWxpayService;
	
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
	protected TenPayContext createContext(MaintenanceTenPayReq req, HttpServletRequest request) throws Exception {
		TenPayContext context = new TenPayContext();
		if (!this.isValid(req)) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("参数错误");
			return context;
		}

		Long orderId = req.getOrder_Id();
		MaintOrderEntity maintPayment = maintOrderCreateMapper.selectPaymentById(orderId);
		
		if (maintPayment == null) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("订单不存在");
			return context;
		} else {
			
			// check订单状态
			Integer pay_status = maintPayment.getPay_status();
			if (pay_status != null && pay_status.intValue() == OrderStatus.PAYED.value) {
				context.setMsg("订单已支付");
				return context;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.REFUNDED.value) {
				context.setMsg("订单已退款");
				return context;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.REFUNDING.value) {
				context.setMsg("订单退款中");
				return context;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.CLOSED.value) {
				context.setMsg("订单已关闭");
				return context;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.PAYING.value) {
				context.setMsg("订单付款中");
				return context;
			}
			
			Integer payType = RechargeConstants.PAY_TYPE_TEN;// 微信支付
			BigDecimal payment = maintPayment.getPayment();
			
			context.setOutTradeNo(orderId.toString());
			context.setTotalFee(payment);

			// 更新订单
			MaintOrderEntity maintenanceOrderEntity = new MaintOrderEntity();
			maintenanceOrderEntity.setPay_type(payType);
			maintenanceOrderEntity.setId(orderId);
			maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);

		}
		
		return context;
	}
	
	@Override
	public BaseTenPayReply tenPay(HttpServletRequest request, BaseTenPayReply reply, MaintenanceTenPayReq req) throws Exception {
		
		TenPayContext context = this.createContext(req, request);
		if (!context.isRight()) {
			reply.setReplyCode(context.getReplyCode());
			reply.setMessage(context.getMsg());
			return reply;
		}
		int retcode = 0;
		String retmsg = "";
		
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

	private boolean isValid(MaintenanceTenPayReq req) {
		if (req == null)
			return false;
		if (req.getOrder_Id() == null)
			return false;
		if (req.getAmount() == null)
			return false;
		return true;
	}
}
