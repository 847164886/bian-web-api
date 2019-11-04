package com.che.maintenance.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.web.Constants;
import com.che.common.web.Reply;
import com.che.maintenance.pojo.MaintAccountPayReq;
import com.che.maintenance.pojo.MaintOrderReply;
import com.che.maintenance.pojo.MaintOrderReq;
import com.che.maintenance.pojo.MaintenanceInfoReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReq;
import com.che.maintenance.pojo.MaintenanceTenPayReq;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.maintenance.service.MainaTenPayPrepayService;
import com.che.maintenance.service.MaintenanceInfoService;
import com.che.pay.ali.notice.AbstractAliPayNoticeService;
import com.che.pay.controller.AbstractBasePayNoticeController;
import com.che.pay.ten.notice.AbstractTenPayNoticeService;
import com.che.pay.ten.pojo.BaseTenPayReply;
import com.che.search.exception.CheSearchException;

/**
 * @author wangzhen
 *         <p>
 *         查维保相关接口
 *         </p>
 */
@RestController
@RequestMapping("/maintenance")
public class MaintenanceController extends AbstractBasePayNoticeController {

	@Autowired
	private MaintenanceInfoService maintenanceInfoService;

//	@Autowired
//	private MainaTenPayNoticeService mainaTenPayNoticeService;

	@Autowired
	private MainaTenPayPrepayService mainaTenPayPrepayService;
	
//	@Autowired
//	private MainaAliPayNoticeService mainaAliPayNoticeService;
//	
//	@Autowired
//	private MainaAliPayPrepayService mainaAliPayPrepayService;
	
//	@Autowired
//	private TestService testService;

	/**
	 * 查维保订单创建接口
	 * @throws Exception 
	 */
	@RequestMapping("/createOrder")
	public MaintenanceOrderCreateReply createOrder(
			@RequestBody(required = false) MaintenanceOrderCreateReq maintenanceOrderCreateReq) throws Exception {
		MaintenanceOrderCreateReply maintenanceOrderCreateReply = maintenanceInfoService
				.createOrder(maintenanceOrderCreateReq);
		maintenanceOrderCreateReply.setResultCode(Constants.RESULT_SUCCESS);
		return maintenanceOrderCreateReply;
	}
	
	/**
	 * 购买维保接口
	 */
	/*@RequestMapping("/buyReport")
	public Reply buyReport(@RequestBody(required = false) QueryReportReq queryReportReq) throws CheSearchException, Exception {
		Reply reply = maintenanceInfoService.buyReport(queryReportReq); 
		reply.setResultCode(Constants.RESULT_SUCCESS);
		return reply;
	}*/

	/**
	 * 查询订单列表接口
	 */
	@RequestMapping("/orderList")
	public MaintOrderReply orderList(@RequestBody(required = false) MaintOrderReq maintOrderReq) {
		MaintOrderReply maintOrderReply = maintenanceInfoService.orderList(maintOrderReq);
		maintOrderReply.setResultCode(Constants.RESULT_SUCCESS);
		return maintOrderReply;
	}

	/**
	 * 查询报告接口
	 * 
	 * @throws Exception
	 * @throws CheSearchException
	 */
	@RequestMapping("/queryReport")
	public MaintenanceInfoReply queryReport(@RequestBody(required = false) QueryReportReq queryReportReq) throws CheSearchException, Exception {
		MaintenanceInfoReply maintInfoReply = maintenanceInfoService.queryReport(queryReportReq);
		maintInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return maintInfoReply;
	}

	/**
	 * 微信支付接口
	 */
	@RequestMapping("/tenpay")
	public BaseTenPayReply tenpay(HttpServletRequest request, HttpServletResponse response,
			@RequestBody(required = false) MaintenanceTenPayReq req) {
		BaseTenPayReply ret = new BaseTenPayReply();
		try {
			ret = mainaTenPayPrepayService.tenPay(request, new BaseTenPayReply(), req);
		} catch (Exception e) {
			ret.setReplyCode(Constants.RESULT_ERROR_SYSERROR);
			ret.setMessage("服务端错误");
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 支付宝支付接口
	 */
//	@RequestMapping("/alipay")
//	public MaintAliPayReply alipay(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody(required = false) MaintenanceAliPayReq req) {
//		BaseAliPayReply ret = null;
//		MaintAliPayReply maintAliPayReply = new MaintAliPayReply();
//		try {
//			ret = mainaAliPayPrepayService.aliPay(request, new BaseAliPayReply(), req);
//			maintAliPayReply.setPayInfo(mainaAliPayPrepayService.getPayInfo(mainaAliPayPrepayService.getOrderInfo(ret)));
//		} catch (Exception e) {
//			ret.setReplyCode(Constants.RESULT_ERROR_SYSERROR);
//			ret.setMessage("服务端错误");
//			e.printStackTrace();
//		}
//		return maintAliPayReply;
//	}

	/**
	 * 余额支付接口
	 * @throws Exception 
	 */
	@RequestMapping("/accountPay")
	public Reply accountPay(@RequestBody(required = false) MaintAccountPayReq req) throws Exception {
		Reply accountPayReply = maintenanceInfoService.accountPay(req);
		accountPayReply.setResultCode(Constants.RESULT_SUCCESS);
		return accountPayReply;
	}


	@Override
	public AbstractAliPayNoticeService getAliNoticeService() {
		return null;
	}

	@Override
	public AbstractTenPayNoticeService getTenNoticeService() {
		return null;
	}
	
//	@RequestMapping("/testRefund")
//	public Reply testRefund(@RequestBody(required = false) QueryReportReq queryReportReq) throws Exception {
//		maintenanceInfoService.testRefund(queryReportReq);
//		Reply reply = new Reply();
//		reply.setResultCode(Constants.RESULT_SUCCESS);
//		return reply;
//	}
}