package com.che.recharge.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.che.common.web.Constants;
import com.che.pay.ali.notice.AbstractAliPayNoticeService;
import com.che.pay.controller.AbstractBasePayNoticeController;
import com.che.pay.ten.notice.AbstractTenPayNoticeService;
import com.che.pay.ten.pojo.BaseTenPayReply;
import com.che.recharge.pojo.RechargeAccountPayReply;
import com.che.recharge.pojo.RechargeAccountPayReq;
import com.che.recharge.pojo.RechargeAliPayReq;
import com.che.recharge.pojo.RechargeTenPayReq;
import com.che.recharge.service.AccountPayService;
import com.che.recharge.service.AliPayNoticeService;
import com.che.recharge.service.AliPayPrepayService;
import com.che.recharge.service.TenPayPrepayService;

@RestController
@RequestMapping("/recharge")
public class RechargePayController extends AbstractBasePayNoticeController {
	@Resource
	AliPayNoticeService aliPayNoticeService;
//	@Resource
//	TenPayNoticeService tenPayNoticeService;
	@Resource
	TenPayPrepayService tenPayPrepayService;
	@Resource
	AliPayPrepayService aliPayPrepayService;
	
	@Autowired
	private AccountPayService accountPayService;

	@Override
	public AbstractAliPayNoticeService getAliNoticeService() {
		return null;
	}

	@Override
	public AbstractTenPayNoticeService getTenNoticeService() {
		return null;
	}

	/**
	 * 充值接口（微信支付）
	 */
	@RequestMapping("/tenpay")
	public BaseTenPayReply tenpay(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = false) RechargeTenPayReq req) {
		BaseTenPayReply ret = new BaseTenPayReply();
		try {
			ret = tenPayPrepayService.tenPay(request, new BaseTenPayReply(), req);
		} catch (Exception e) {
			ret.setReplyCode(Constants.RESULT_ERROR_SYSERROR);
			ret.setMessage("服务端错误");
			e.printStackTrace();
		}
		return ret;

	}

	/**
	 * 充值接口（支付宝支付）
	 */
//	@RequestMapping("/alipay")
//	public BaseAliPayReply alipay(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody(required = false) RechargeAliPayReq req) {
//		BaseAliPayReply ret = new BaseAliPayReply();
//		try {
//			ret = aliPayPrepayService.aliPay(request, new BaseAliPayReply(), req);
//		} catch (Exception e) {
//			ret.setReplyCode(Constants.RESULT_ERROR_SYSERROR);
//			ret.setMessage("服务端错误");
//			e.printStackTrace();
//		}
//		return ret;
//	}
	
	/**
	 * 余额使用记录接口
	 */
	@RequestMapping("/accountRecords")
	public RechargeAccountPayReply accountRecords(@RequestBody(required = false) RechargeAccountPayReq rechargeAccountPayReq) {
		RechargeAccountPayReply rechargeAccountPayReply = accountPayService.queryAccountRecords(rechargeAccountPayReq);
		rechargeAccountPayReply.setResultCode(Constants.RESULT_SUCCESS);
		return rechargeAccountPayReply;
	}

	public static void main(String[] args) {
		RechargeAliPayReq aliReq = new RechargeAliPayReq();
		aliReq.setType(1);
		aliReq.setAmount(1);
		System.out.println(JSON.toJSON(aliReq));
	}
}