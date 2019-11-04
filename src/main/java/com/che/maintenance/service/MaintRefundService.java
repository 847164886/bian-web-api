package com.che.maintenance.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.id.IdWorker;
import com.che.common.util.SendMessage;
import com.che.common.web.Constants;
import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.GoodsType;
import com.che.maintenance.pojo.LookStatus;
import com.che.maintenance.pojo.OrderStatus;
import com.che.maintenance.pojo.PayType;
import com.che.maintenance.pojo.QueryStatus;
import com.che.payment.wx.api.IWxpayService;
import com.che.payment.wx.param.WxRefundReply;
import com.che.payment.wx.param.WxpayReq;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.RefundOrderEntity;
import com.che.recharge.mapper.RefundOrderEntityMapper;
import com.che.search.exception.CheSearchException;
import com.che.search.maintenance.api.IMaintenanceService;
import com.che.search.maintenance.entity.MaintenanceDetail;

/**
 * 
 *维保处理业务类
 */
@Service
public class MaintRefundService {
	
	@Autowired
	private MaintRechargeService maintRechargeService;

//	@Autowired
//	private TenRefundService tenRefundService;
	
	@Reference(version="1.0.0")
	private IWxpayService iWxpayService;
	
	@Autowired
	private RefundOrderEntityMapper refundOrderEntityMapper;
	
	@Resource
	private IdWorker idWorker;
	
	@Autowired
	private SendMessage sendMessage;
	
	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;
	
	@Reference(version = "1.0.0")
	private IMaintenanceService iMaintenanceService;
	
	private static final Logger logger = LoggerFactory.getLogger(MaintRefundService.class);
	
	public boolean changed(Long orderId, String tradeNo, Integer state) throws CheSearchException, Exception {

		// 更新订单
		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		if (maintOrderEntity != null) {
			// 订单存在
			if (maintOrderEntity.getPay_status() == null && maintOrderEntity.getQuery_status() == null) {
				return false;
			} else {
				// 当支付状态为已付款，查询状态为查询中时，更新订单状态
				maintOrderEntity.setTrade_no(tradeNo);
				maintOrderEntity.setQuery_status(state);
				
				if (state.intValue() == QueryStatus.QUERYFAIL.value || state.intValue() == QueryStatus.THIRD_ORDER_FAIL.value) {
					// 查询失败或第三方下单失败，退款
					Integer pay_type = maintOrderEntity.getPay_type();
					Integer pay_status = maintOrderEntity.getPay_status();
					if (pay_type != null && pay_type.intValue() == PayType.ACCOUNT.value) {
						if (pay_status != null && pay_status.intValue() == OrderStatus.PAYED.value) {
							
							// 余额退款成功，更新订单状态为已退款
							maintOrderEntity.setPay_status(OrderStatus.REFUNDED.value); 
							maintOrderEntity.setQuery_status(QueryStatus.QUERYFAIL.value);
							maintOrderEntity.setRetmsg("查询失败，已退款");
							maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
							// 如果是余额退款，直接走大账户
							maintRechargeService.accountRefund(maintOrderEntity);
							// 发送退款提醒短信
							this.sendRefundMessage(orderId);
						}
						
					} else {
						// 创建退款订单（针对第三方支付，微信支付、支付宝支付）
						if (maintOrderEntity.getPay_type() != null && maintOrderEntity.getPay_type().intValue() == PayType.TENPAY.value) {
							
//							maintOrderEntity.setQuery_status(state);
							// 微信、支付宝退款，上传大账户系统
							maintRechargeService.uploadRefundDetail(maintOrderEntity);
							
							this.tenRefund(maintOrderEntity.getId());
						}
						
						// 发送退款提醒短信
						this.sendRefundMessage(orderId);
					}
					
					return true;
				} else if (state.intValue() == QueryStatus.QUERYSUCC.value) {
					// 查询成功
					MaintenanceDetail maintenanceDetail = iMaintenanceService.queryReport(Constants.SYSNAME, tradeNo);
					if (maintenanceDetail != null) {
						maintOrderEntity.setModel(maintenanceDetail.getModel());
						maintOrderEntity.setYear(maintenanceDetail.getYear());
						maintOrderEntity.setBrand(maintenanceDetail.getBrand());
						maintOrderEntity.setLook_status(LookStatus.UNLOOK.value);
					}
					maintOrderEntity.setRetmsg("查询成功");
					maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
					return true;
				} else if (state.intValue() == QueryStatus.NO_MAINTEN_RECORD.value) {
					// 无维保记录
					maintOrderEntity.setRetmsg("无维保记录");
					maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
					return true;
				} else {
					// 查询中
					maintOrderEntity.setRetmsg("查询中");
					maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
					return true;
				}
			}
		}
		return true;
	}
	
	/**
	 * 微信退款接口
	 * @throws Exception
	 */
	private void tenRefund(Long orderId) throws Exception {
		
		/*MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		// 余额退款成功，更新订单状态为已退款
		maintOrderEntity.setPay_status(OrderStatus.REFUNDED.value); 
		maintOrderEntity.setQuery_status(QueryStatus.QUERYFAIL.value);
		maintOrderEntity.setRetmsg("查询失败，已退款");
		maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
		// 如果是余额退款，直接走大账户
		maintRechargeService.accountRefund(maintOrderEntity);*/
		
		// check是否为可退款状态
		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		
		if (!ObjectUtils.isEmpty(maintOrderEntity)) {
			// check是否为可退款状态
			if (maintOrderEntity.getPay_status() != OrderStatus.PAYED.value) {
				logger.error("维保订单号：" + maintOrderEntity.getId() + "当前订单为不可退款状态");
				return;
			} else {
				// 创建退款单
				RefundOrderEntity refundOrderEntity = new RefundOrderEntity();
				Long refund_orderid = idWorker.nextId();
				refundOrderEntity.setId(refund_orderid);
				refundOrderEntity.setUid(maintOrderEntity.getUid());
				refundOrderEntity.setOrder_id(maintOrderEntity.getId());
				refundOrderEntity.setOrder_type(maintOrderEntity.getGoods_type());
				refundOrderEntity.setTotal_fee(maintOrderEntity.getPayment());
				refundOrderEntity.setRefund_fee(maintOrderEntity.getPayment());
				refundOrderEntity.setPay_type(maintOrderEntity.getPay_type());
				refundOrderEntity.setStatus(RechargeConstants.REFUND_STATUS_ING);
				refundOrderEntityMapper.insert(refundOrderEntity);
				
				// 调用订单支付中心退款接口
				WxpayReq wxpayReq = new WxpayReq();
				wxpayReq.setOrderId(maintOrderEntity.getId() + "");
				wxpayReq.setAmount(maintOrderEntity.getPayment());
				WxRefundReply wxRefundReply = iWxpayService.refund(Constants.SYSNAME, wxpayReq);
				
				if (!ObjectUtils.isEmpty(wxRefundReply)) {
					
					if (wxRefundReply.isSuccess()) {
						refundOrderEntity.setReal_refund_fee(wxRefundReply.getRefundFee());
						
						if (refundOrderEntity.getRefund_fee().compareTo(wxRefundReply.getRefundFee()) == 0) {
							// 退款金额一致，退款成功
							// 更新退款订单状态
							refundOrderEntity.setStatus(RechargeConstants.REFUND_STATUS_ED);
							// 更新查维保订单状态为已退款
//							MaintOrderEntity _maintOrderEntity = new MaintOrderEntity();
							maintOrderEntity.setPay_status(OrderStatus.REFUNDED.value); 
							maintOrderEntity.setQuery_status(QueryStatus.QUERYFAIL.value);
							maintOrderEntity.setRetmsg("查询失败，已退款");
//							maintOrderEntity.setId(maintOrderEntity.getId());
							maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
						} else {
							// 退款金额不一致，退款失败
							refundOrderEntity.setStatus(RechargeConstants.REFUND_STATUS_FAL);
							maintOrderEntity.setPay_status(OrderStatus.REFUNDING.value); 
							maintOrderEntity.setQuery_status(QueryStatus.QUERYFAIL.value);
							maintOrderEntity.setRetmsg("查询失败，退款中");
							maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
						}
						
					} else {
						// 退款失败
						refundOrderEntity.setStatus(RechargeConstants.REFUND_STATUS_FAL);
						maintOrderEntity.setPay_status(OrderStatus.REFUNDING.value); 
						maintOrderEntity.setQuery_status(QueryStatus.QUERYFAIL.value);
						maintOrderEntity.setRetmsg("查询失败，退款中");
						maintOrderCreateMapper.updateMaintOrder(maintOrderEntity);
					}
					
					refundOrderEntityMapper.updateRefundOrder(refundOrderEntity);
				}
			}
		}
	}
	
	//短信通知
	private void sendRefundMessage(Long orderId) {
		if (orderId == null) return;
		StringBuffer content = new StringBuffer("很抱歉，您的订单号为");
		String mobile = "";
		MaintOrderEntity maintOrderEntity = maintOrderCreateMapper.selectMaintOrderById(orderId);
		if (maintOrderEntity != null) {
			if(GoodsType.QUERYMAINT.value == maintOrderEntity.getGoods_type()){
				mobile= maintOrderEntity.getMobile();
				content.append(maintOrderEntity.getId())
				.append("的维修保养记录查询失败，订单费用:")
				.append(maintOrderEntity.getPayment())
				.append("元会退回到原账户，请注意查收。");
			}
			try {
				sendMessage.sendOnce(mobile, content.toString());
			} catch (Exception e) {
				logger.info("订单号为"+maintOrderEntity.getId()+"退款，短信未发送成功！");
			}
			
		}
	}

}
