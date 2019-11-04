package com.che.pay.notice.service;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.che.common.web.Constants;
import com.che.payment.notice.api.IPaymentConfirmService;
import com.che.payment.notice.param.PaymentConfirmReq;
import com.che.payment.notice.param.PaymentNoticeReq;
import com.che.payment.notice.param.PaymentResult;

/**
 * 统一支付回调（发布成dubbo服务）
 * 
 * @author wangzhen
 */
@Service(version = "1.0.0", group = Constants.SYSNAME)
public class PaymentConfirmServiceImpl implements IPaymentConfirmService {

	@Resource
	PaymentNoticeService paymentNoticeService;

	@Override
	public PaymentResult canPay(PaymentConfirmReq req) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResult notice(PaymentNoticeReq req) throws Exception {
		
		PaymentResult paymentResult = paymentNoticeService.notice(req);
		return paymentResult;
	}
}
