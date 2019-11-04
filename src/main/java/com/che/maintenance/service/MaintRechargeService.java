package com.che.maintenance.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.web.Constants;
import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.pojo.GoodsType;
import com.che.maintenance.pojo.PayType;
import com.che.recharge.pojo.RechargeAccountPayReq;
import com.che.user.interfaces.interf.account.AccountDetailService;
import com.che.user.interfaces.interf.account.ThrConsumerService;
import com.che.user.model.constant.ConstantUser;
import com.che.user.model.dto.AccountDetailInputDTO;
import com.che.user.model.dto.AccountDetailOutputDTO;
import com.che.user.model.dto.ThrConsumerInputDTO;

import core.DTO.OutputDTO;
import core.page.Page;

/**
 * 查维保支付记录上传大账户系统
 * 
 * @author
 *
 */
@Service
public class MaintRechargeService {

	@Reference(version = "2.0.1")
	private ThrConsumerService thrConsumerService;
	
	@Reference(version = "2.0.1")
	private AccountDetailService accountDetailService;
	
	private static final String ACCOUNT_NO_ENOUGH_MESSAGE = "账户余额不足";
	

	/**
	 *  查维保缴费
	 */
	public void uploadConsumerDetail(MaintOrderEntity maintOrderEntity) throws Exception {

		if (maintOrderEntity.getGoods_type().intValue() == GoodsType.QUERYMAINT.value) {
			OutputDTO<Long> outputDTO = thrConsumerService.consumer(this.createThrConsumerInputDTO(maintOrderEntity));
			if (!"0".equals(outputDTO.getCode())) {
				throw new Exception("发送查维保缴费记录失败");
			}
		}

	}

	private ThrConsumerInputDTO createThrConsumerInputDTO(MaintOrderEntity maintOrderEntity) {
		ThrConsumerInputDTO inputDTO = new ThrConsumerInputDTO();
		inputDTO.setBussinessCode(maintOrderEntity.getId());
        inputDTO.setBussinessType(ConstantUser.account_bussiness_type.weibaoService);
        inputDTO.setBussinessDes(Constants.MAINTENANCEQUERY);
        inputDTO.setOperationAccount(maintOrderEntity.getPayment().multiply(new BigDecimal(-1)));
        inputDTO.setUserCode(maintOrderEntity.getUsercode());
        inputDTO.setPayType(maintOrderEntity.getPay_type().intValue() == PayType.TENPAY.value ? ConstantUser.pay_method.WeChat : ConstantUser.pay_method.aliPay);
		return inputDTO;
	}
	
	/**
	 * 查维保退款
	 */
	public void uploadRefundDetail(MaintOrderEntity maintOrderEntity) throws Exception {
		if (maintOrderEntity.getGoods_type().intValue() == GoodsType.QUERYMAINT.value) {
			OutputDTO<Long> outputDTO = thrConsumerService.refund(this.createThrRefundInputDTO(maintOrderEntity));
			if (!"0".equals(outputDTO.getCode())) {
				throw new Exception("发送查维保退款记录失败");
			}
		}
	}
	
	private ThrConsumerInputDTO createThrRefundInputDTO(MaintOrderEntity maintOrderEntity) {
		ThrConsumerInputDTO inputDTO = new ThrConsumerInputDTO();
		inputDTO.setBussinessCode(maintOrderEntity.getId());
        inputDTO.setBussinessType(ConstantUser.account_bussiness_type.weibaoService);
        inputDTO.setBussinessDes(Constants.MAINTENANCEREFUND);
        inputDTO.setOperationAccount(maintOrderEntity.getPayment());
        inputDTO.setUserCode(maintOrderEntity.getUsercode());
        inputDTO.setPayType(maintOrderEntity.getPay_type().intValue() == PayType.TENPAY.value ? ConstantUser.pay_method.WeChat : ConstantUser.pay_method.aliPay);
		return inputDTO;
	}
	
	/**
	 * 余额支付（消费）
	 */
	public void accountConsume(MaintOrderEntity maintOrderEntity) throws Exception {
		if (maintOrderEntity.getGoods_type().intValue() == GoodsType.QUERYMAINT.value) {
			OutputDTO outputDTO = accountDetailService.addAccountDetailB(createAccountConsumeInputDTO(maintOrderEntity));
			if ("-1".equals(outputDTO.getCode()) && ACCOUNT_NO_ENOUGH_MESSAGE.equals(outputDTO.getMessage())) {
				throw new Exception(ACCOUNT_NO_ENOUGH_MESSAGE);
			} else if (!"0".equals(outputDTO.getCode())) {
				throw new Exception("余额支付失败");
			}
		}
	}
	
	private AccountDetailInputDTO createAccountConsumeInputDTO(MaintOrderEntity maintOrderEntity) {
		AccountDetailInputDTO inputDTO = new AccountDetailInputDTO();
		inputDTO.setMobile(maintOrderEntity.getMobile());
		inputDTO.setBussinessName(Constants.MAINTENANCEQUERY);
		inputDTO.setSysName(Constants.SYSNAME);//定义自己的系统
		inputDTO.setBussinessType(ConstantUser.account_bussiness_type.weibaoService);
		inputDTO.setBussinessDes(Constants.MAINTENANCEQUERY);
		inputDTO.setOperationAccount(maintOrderEntity.getPayment().multiply(new BigDecimal(-1)));
		inputDTO.setBussinessCode(maintOrderEntity.getId());
		inputDTO.setAccountType(ConstantUser.account_type.consumer);
		inputDTO.setUserCode(maintOrderEntity.getUsercode());
		return inputDTO;
	}
	
	/**
	 * 余额支付（退款）
	 */
	public void accountRefund(MaintOrderEntity maintOrderEntity) throws Exception {
		if (maintOrderEntity.getGoods_type().intValue() == GoodsType.QUERYMAINT.value) {
			OutputDTO<Long> outputDTO = accountDetailService.accountRefund(createAccountRefundInputDTO(maintOrderEntity));
			if (!"0".equals(outputDTO.getCode())) {
				throw new Exception("余额退款失败");
			}
		}
		
	}
	
	
	private AccountDetailInputDTO createAccountRefundInputDTO (MaintOrderEntity maintOrderEntity) {
		AccountDetailInputDTO inputDTO = new AccountDetailInputDTO();
		inputDTO.setBussinessName(Constants.MAINTENANCEREFUND);
		inputDTO.setMobile(maintOrderEntity.getMobile());
		inputDTO.setSysName(Constants.SYSNAME);//定义自己的系统
		inputDTO.setBussinessType(ConstantUser.account_bussiness_type.weibaoService);
		inputDTO.setBussinessDes(Constants.MAINTENANCEREFUND);
		inputDTO.setOperationAccount(maintOrderEntity.getPayment());
		inputDTO.setBussinessCode(maintOrderEntity.getId());
		inputDTO.setAccountType(ConstantUser.account_type.refund);
		inputDTO.setUserCode(maintOrderEntity.getUsercode());
		return inputDTO;
	}
	
}
