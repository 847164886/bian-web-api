package com.che.maintenance.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.che.common.httpclient.HttpsRequest;
import com.che.common.httpclient.jianding.JiandingConstants;
import com.che.common.web.Constants;
import com.che.common.web.Reply;
import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.GoodsType;
import com.che.maintenance.pojo.LookStatus;
import com.che.maintenance.pojo.MaintAccountPayReq;
import com.che.maintenance.pojo.MaintOrderReply;
import com.che.maintenance.pojo.MaintOrderReq;
import com.che.maintenance.pojo.MaintOrderReq.StatusType;
import com.che.maintenance.pojo.MaintenanceInfoReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReq;
import com.che.maintenance.pojo.OrderStatus;
import com.che.maintenance.pojo.OrderStatus.OrderCombStatus;
import com.che.payment.order.api.IOrderService;
import com.che.payment.order.common.ProductType;
import com.che.payment.order.param.OrderReq;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.maintenance.pojo.QueryStatus;
import com.che.recharge.common.RechargeConstants;
import com.che.search.exception.CheSearchException;
import com.che.search.maintenance.api.IMaintenanceService;
import com.che.search.maintenance.entity.MaintenanceBill;
import com.che.search.maintenance.entity.MaintenanceDetail;
import com.che.user.model.dto.ShopUserOutputDTO;
import com.che.user.service.UserCommonService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.tgsit.common.rsa.security.SignUtils;

import param.MaintenanceReq;

@Service
public class MaintenanceInfoServiceImpl implements MaintenanceInfoService {

	private static final Logger logger = LoggerFactory.getLogger(MaintenanceInfoServiceImpl.class);

	@Reference(version = "1.0.0")
	private IMaintenanceService iMaintenanceService;

//	@Autowired
//	private IdWorker idWorker;
	
	@Reference(version="1.0.0")
	private IOrderService iOrderService;

	@Autowired
	private UserCommonService userCommonService;

	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;

	@Autowired
	private MaintRefundService maintRefundService;

	@Autowired
	private MaintRechargeService maintRechargeService;

	private static final String BRAND_STATUS_01 = "1"; // 品牌数据维护中=”1”

	private static final String BRAND_STATUS_02 = "2"; // 品牌可查询=”2”

	private static final String ACCOUNT_NO_ENOUGH_MESSAGE = "账户余额不足";

	private static final String PAYMENT = "24"; // 查维保费用

	/**
	 * 创建查维保订单
	 * @throws Exception 
	 */
	@Override
	public MaintenanceOrderCreateReply createOrder(MaintenanceOrderCreateReq maintOrderCreateReq) throws Exception {
		MaintenanceOrderCreateReply reply = new MaintenanceOrderCreateReply();
		if (maintOrderCreateReq.getVin() == null || maintOrderCreateReq.getPayment() == null
				|| maintOrderCreateReq.getCarNo() == null || maintOrderCreateReq.getEngineId() == null) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数为空");
			return reply;
		}

		if (!maintOrderCreateReq.getPayment().equals(PAYMENT)) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("支付金额有误");
			return reply;
		}

		BigDecimal payment = new BigDecimal(maintOrderCreateReq.getPayment());
		// 检查品牌是否支持查询
		Reply checkReply = this.checkBrand(maintOrderCreateReq);
		if (checkReply.getReplyCode() != Constants.REPLY_SUCCESS) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage(checkReply.getMessage());
			return reply;
		}

		ShopUserOutputDTO user = userCommonService.getUser();
//		orderEntity.setId(idWorker.nextId());
		
		OrderReq orderReq = new OrderReq();
		orderReq.setAllAmount(payment);
		orderReq.setAmount(payment);
		orderReq.setDeposit(new BigDecimal(0));
		orderReq.setMobile(user.getMobile());
		orderReq.setProductName(Constants.MAINTENANCEQUERY);
		orderReq.setProductDesc(Constants.MAINTENANCEQUERY);
		
		String orderId = iOrderService.nextId(Constants.SYSNAME, ProductType.MAINTENANCE, orderReq);
		if (StringUtils.isNotBlank(orderId)) {
			// 创建订单
			MaintOrderEntity orderEntity = new MaintOrderEntity();
			orderEntity.setId(Long.parseLong(orderId));
			orderEntity.setUid(user.getUserId());
			orderEntity.setUsercode(user.getCode());
			orderEntity.setMobile(user.getMobile());
			orderEntity.setVin(maintOrderCreateReq.getVin());
			orderEntity.setRemark(maintOrderCreateReq.getRemark());
			orderEntity.setCar_no(maintOrderCreateReq.getCarNo());
			orderEntity.setEngine_id(maintOrderCreateReq.getEngineId());
			orderEntity.setGoods_type(GoodsType.QUERYMAINT.value);
			orderEntity.setPayment(payment);
			orderEntity.setPay_status(OrderStatus.UNPAY.value); // 默认待付款
			maintOrderCreateMapper.insert(orderEntity);
			reply.setOrder_id(orderEntity.getId());
			reply.setPayment(payment + "");
			reply.setReplyCode(Constants.REPLY_SUCCESS);
		} else {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("创建订单失败");
		}
		
		return reply;
	}

	/**
	 * 检查品牌是否支持
	 */
	@Override
	public Reply checkBrand(MaintenanceOrderCreateReq maintenanceOrderCreateReq) {
		Reply reply = new Reply();
		String vin = maintenanceOrderCreateReq.getVin();
		if (vin != null) {

			String url = JiandingConstants.JIANDING_URL + JiandingConstants.BRAND_CHECK;
			String time = JiandingConstants.JIANDING_DATEFORMAT.format(new Date());

			String data = JiandingConstants.JIANDING_ACCOUNT_UID + vin + time;
			String sign = SignUtils.sign(JiandingConstants.TGSXX_PRIVATEKEY, JiandingConstants.JIANDING_ACCOUNT_PWD,
					data);

			Map<String, String> param = new HashMap<String, String>();
			param.put("uid", JiandingConstants.JIANDING_ACCOUNT_UID); // 用户ID
			param.put("vin", vin.toUpperCase());
			param.put("time", time); // 时间戳
			param.put("sign", sign); // 签名串

			String result = HttpsRequest.sentPost(url, param);
			logger.info("------检查品牌是否支持-------vin=" + vin + ": " + result);
			JSONObject object = (JSONObject) JSONObject.parse(result);
			JSONObject info = (JSONObject) object.get("info");

			String status = (String) info.get("status");
			String brandStatus = (String) info.get("brandStatus");
			String message = (String) info.get("message");

			if (StringUtils.isNotEmpty(status) && status.equals(BRAND_STATUS_01) && StringUtils.isNotEmpty(brandStatus)
					&& brandStatus.equals(BRAND_STATUS_02)) { // 品牌可查询
				reply.setReplyCode(Constants.REPLY_SUCCESS);
			} else {
				reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			}
			reply.setMessage(message);
		}

		return reply;
	}

	/**
	 * 查询订单列表
	 */
	@Override
	public MaintOrderReply orderList(MaintOrderReq maintOrderReq) {

		MaintOrderReply maintOrderReply = new MaintOrderReply();

		// 分页
		PageHelper.startPage(maintOrderReq.getPage(), maintOrderReq.getPageSize());
		List<MaintOrderEntity> orderList = null;

		if (maintOrderReq.getType().intValue() == StatusType.ALLSTATUS) {
			// 查询全部状态订单
			orderList = maintOrderCreateMapper.selectOrderList(userCommonService.getUser().getUserId(), null);
		} else {
			// 查询相应状态订单
			orderList = maintOrderCreateMapper.selectOrderList(userCommonService.getUser().getUserId(),
					maintOrderReq.getPay_status());
		}

		Page<MaintOrderEntity> page = (Page<MaintOrderEntity>) orderList;

		if (page.getPages() < maintOrderReq.getPage()) {
			maintOrderReply.setReplyCode(Constants.REPLY_SUCCESS);
			maintOrderReply.setMaintOrderEntities(null);
			maintOrderReply.setMessage("没有更多数据");
			return maintOrderReply;
		}

		if (page.getResult() != null) {

			for (MaintOrderEntity orderEntity : page.getResult()) {

				if (orderEntity.getPay_status() != null
						&& orderEntity.getPay_status().intValue() == OrderStatus.UNPAY.value) {
					orderEntity.setComb_status(OrderCombStatus.UNPAY.value);
				} else if (orderEntity.getPay_status() != null
						&& orderEntity.getPay_status().intValue() == OrderStatus.PAYED.value
						&& orderEntity.getQuery_status() != null
						&& orderEntity.getQuery_status().intValue() == QueryStatus.QUERYING.value) {
					orderEntity.setComb_status(OrderCombStatus.QUERYING.value);
				} else if (orderEntity.getQuery_status() != null
						&& orderEntity.getQuery_status().intValue() == QueryStatus.QUERYSUCC.value
						&& orderEntity.getLook_status() != null
						&& orderEntity.getLook_status().intValue() == LookStatus.UNLOOK.value) {
					orderEntity.setComb_status(OrderCombStatus.QUERYSUC_NOLK.value);
				} else if (orderEntity.getQuery_status() != null
						&& orderEntity.getQuery_status().intValue() == QueryStatus.QUERYSUCC.value
						&& orderEntity.getLook_status() != null
						&& orderEntity.getLook_status().intValue() == LookStatus.LOOKED.value) {
					orderEntity.setComb_status(OrderCombStatus.QUERYSUC_LKED.value);
				} else if (orderEntity.getPay_status() != null
						&& orderEntity.getPay_status().intValue() == OrderStatus.REFUNDING.value
						&& orderEntity.getQuery_status() != null
						&& orderEntity.getQuery_status().intValue() == QueryStatus.QUERYFAIL.value) {
					orderEntity.setComb_status(OrderCombStatus.QUERYFAL_REFUING.value);
				} else if (orderEntity.getQuery_status() != null
						&& orderEntity.getQuery_status().intValue() == QueryStatus.QUERYFAIL.value
						&& orderEntity.getPay_status() != null
						&& orderEntity.getPay_status().intValue() == OrderStatus.REFUNDED.value) {
					orderEntity.setComb_status(OrderCombStatus.QUERYFAL_REFUED.value);
				} else if (orderEntity.getPay_status() != null
						&& orderEntity.getPay_status().intValue() == OrderStatus.CLOSED.value) {
					orderEntity.setComb_status(OrderCombStatus.CLOSED.value);
				}
			}
		}

		maintOrderReply.setMaintOrderEntities(page.getResult());
		maintOrderReply.setReplyCode(Constants.REPLY_SUCCESS);
		maintOrderReply.setMessage("查询订单列表成功");
		return maintOrderReply;
	}

	/**
	 * 购买维保
	 */
	@Override
	public Reply buyReport(QueryReportReq queryReportReq) throws CheSearchException, Exception {

		Reply reply = new Reply();

		// check参数
		if (queryReportReq != null && queryReportReq.getOrder_id() == null) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数为空");
			return reply;
		}

		// 购买查维保
		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(queryReportReq.getOrder_id());

		if (maintOrderEntity != null && (maintOrderEntity.getPay_status().intValue() == OrderStatus.PAYED.value)) {

			MaintenanceReq req = new MaintenanceReq();
			req.setCarNo(maintOrderEntity.getCar_no());
			req.setEffectiveDays(Constants.EFFECTIVEDAYS); // 表示有效期为365天
			req.setEngineId(maintOrderEntity.getEngine_id());
			req.setOrderId(maintOrderEntity.getId());
			req.setVin(maintOrderEntity.getVin());
			MaintenanceBill maintenanceBill = iMaintenanceService.buyReport(Constants.SYSNAME, req);

			if (maintenanceBill != null) {
				if (maintenanceBill.getOrder_id() != null && maintenanceBill.getTrade_no() != null
						&& maintenanceBill.getState() != null) {
					// 更新订单状态
					MaintOrderEntity _maintOrderEntity = new MaintOrderEntity();
					// _maintOrderEntity.setQuery_status(maintenanceBill.getState());
					_maintOrderEntity.setIs_third(maintenanceBill.getIs_third());
					_maintOrderEntity.setFailure_reson(maintenanceBill.getFailure_reson());
					_maintOrderEntity.setId(queryReportReq.getOrder_id());
					maintOrderCreateMapper.updateMaintOrder(_maintOrderEntity);
					maintRefundService.changed(maintenanceBill.getOrder_id(), maintenanceBill.getTrade_no(),
					maintenanceBill.getState());
				}
			}

		}
		reply.setReplyCode(Constants.REPLY_SUCCESS);
		return reply;
	}

	/**
	 * 查询维保报告
	 */
	@Override
	public MaintenanceInfoReply queryReport(QueryReportReq queryReportReq) throws CheSearchException, Exception {

		MaintenanceInfoReply maintenanceInfoReply = new MaintenanceInfoReply();

		if ((queryReportReq.getOrder_id() == null || "".equals(queryReportReq.getOrder_id()))
				&& (queryReportReq.getJd_order_id() == null || "".equals(queryReportReq.getJd_order_id()))) {
			maintenanceInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			maintenanceInfoReply.setMessage("参数为空");
			return maintenanceInfoReply;
		}

		MaintenanceDetail maintenanceDetail = null;

		if (queryReportReq.getOrder_id() != null) {
			// 维保订单id
			// 查询订单
			MaintOrderEntity maintOrderEntity = maintOrderCreateMapper
					.selectMaintOrderById(queryReportReq.getOrder_id());

			if (maintOrderEntity != null) {
				
				maintenanceDetail = iMaintenanceService.queryReport(Constants.SYSNAME, maintOrderEntity.getTrade_no());

				// 更新查看状态
				MaintOrderEntity _maintOrderEntity = new MaintOrderEntity();
				_maintOrderEntity.setLook_status(LookStatus.LOOKED.value);
				_maintOrderEntity.setId(maintOrderEntity.getId());
				maintOrderCreateMapper.updateMaintOrder(_maintOrderEntity);

			} else {
				// 订单不存在
				maintenanceInfoReply.setMessage("维保订单不存在");
				maintenanceInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
			}
		} else if (queryReportReq.getJd_order_id() != null) {

			maintenanceDetail = iMaintenanceService.queryReport(Constants.SYSNAME, queryReportReq.getJd_order_id());
		}

		maintenanceInfoReply.setMaintenanceDetail(maintenanceDetail);
		maintenanceInfoReply.setReplyCode(Constants.REPLY_SUCCESS);

		return maintenanceInfoReply;
	}

	/**
	 * 余额支付
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Reply accountPay(MaintAccountPayReq req) throws Exception {
		Reply reply = new Reply();

		if (req.getAmount() == null || req.getOrder_Id() == null) {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数为空");
			return reply;
		}

		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(req.getOrder_Id());

		if (maintOrderEntity != null) {
			
			// check订单状态
			Integer pay_status = maintOrderEntity.getPay_status();
			if (pay_status != null && pay_status.intValue() == OrderStatus.PAYED.value) {
				reply.setMessage("订单已支付");
				return reply;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.REFUNDED.value) {
				reply.setMessage("订单已退款");
				return reply;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.REFUNDING.value) {
				reply.setMessage("订单退款中");
				return reply;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.CLOSED.value) {
				reply.setMessage("订单已关闭");
				return reply;
			} else if (pay_status != null && pay_status.intValue() == OrderStatus.PAYING.value) {
				reply.setMessage("订单付款中");
				return reply;
			}
			// 更新订单
			// maintOrderEntity = new MaintOrderEntity();
			// maintOrderEntity.setPay_type(RechargeConstants.PAY_TYPE_ACCOUNT);
			// // 余额支付
			// 更改订单状态为支付中
			// maintOrderEntity.setPay_status(OrderStatus.PAYING.value);
			// maintOrderEntity.setId(req.getOrder_Id());
			// maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);

			try {

				// 上传大账户
				maintRechargeService.accountConsume(maintOrderEntity);

			} catch (Exception e) {
				// 账户余额不足
				if (ACCOUNT_NO_ENOUGH_MESSAGE.equals(e.getMessage())) {
					reply.setReplyCode(Constants.ACCOUNT_NO_ENOUGH);
					reply.setMessage(ACCOUNT_NO_ENOUGH_MESSAGE);
					return reply;
				} else {
					// do nothing
				}
			}
			
			// 支付成功
			MaintOrderEntity _maintOrderEntity = new MaintOrderEntity();
			_maintOrderEntity.setPay_type(RechargeConstants.PAY_TYPE_ACCOUNT); // 余额支付
			_maintOrderEntity.setPay_status(OrderStatus.PAYED.value);
			_maintOrderEntity.setRetmsg("订单支付成功");
			_maintOrderEntity.setId(maintOrderEntity.getId());
			// maintOrderEntity.setQuery_status(QueryStatus.QUERYING.value);
			// // 查询状态更新为查询中

			maintOrderCreateMapper.updateMaintOrder(_maintOrderEntity);

			reply.setReplyCode(Constants.REPLY_SUCCESS);
			reply.setMessage("余额支付成功");
			
			try {
				// 购买维保报告
				QueryReportReq queryReportReq = new QueryReportReq();
				queryReportReq.setOrder_id(req.getOrder_Id());
				this.buyReport(queryReportReq);
			} catch (Exception e) {
				logger.error("订单号：" + req.getOrder_Id() + "购买维保报告异常：", e);
			}
			
		} else {
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("维保订单不存在");
			return reply;
		}

		return reply;
	}

//	@Override
//	public void testRefund(QueryReportReq queryReportReq) {
//		try {
//			maintRefundService.tenRefund(queryReportReq.getOrder_id());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
