package com.che.recharge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.web.Constants;
import com.che.recharge.common.RechargeConstants;
import com.che.recharge.entity.OrderEntity;
import com.che.recharge.service.AccountPayServiceImpl.AccountQueryEntity;
import com.che.user.interfaces.interf.account.AccountDetailService;
import com.che.user.interfaces.interf.account.ShopBailDetailService;
import com.che.user.model.constant.ConstantUser;
import com.che.user.model.dto.AccountDetailInputDTO;
import com.che.user.model.dto.AccountDetailOutputDTO;
import com.che.user.model.dto.ShopBailInputDTO;
import com.che.user.model.dto.ShopBailOutputDTO;
import com.che.user.service.UserCommonService;

import core.DTO.OutputDTO;
import core.page.Page;

/**
 * 充值数据发送大用户中心
 * 
 * @author Administrator
 *
 */
@Service
public class RechargeUserService {
	@Reference(check = false, version = "2.0.1")
	private AccountDetailService accountDetailService;

	@Reference(check = false, version = "2.0.1")
	private ShopBailDetailService shopBailDetailService;
	
	private Logger logger = LoggerFactory.getLogger(RechargeUserService.class);

	public void uploadDetail(OrderEntity orderEntity) throws Exception {
		if (orderEntity.getGoodsType().intValue() == RechargeConstants.GOODS_TYPE_YE.intValue()) {
			this.addAccountDetail(orderEntity);
		} else if (orderEntity.getGoodsType().intValue() == RechargeConstants.GOODS_TYPE_BZJ.intValue()) {
			this.addShopBailDetail(orderEntity);
		}
	}

	private void addAccountDetail(OrderEntity orderEntity) throws Exception {
		OutputDTO result = accountDetailService.addAccountDetailB(this.createAccountDetailInputDTO(orderEntity));
		if (!"0".equals(result.getCode())) {
			logger.error("充值余额失败：" + "resultcode：" + result.getCode() + " resultmessage：" + result.getMessage());
			throw new Exception("发送充余额数据失败");
		}
	}

	private AccountDetailInputDTO createAccountDetailInputDTO(OrderEntity orderEntity) {
		AccountDetailInputDTO ret = new AccountDetailInputDTO();
		ret.setMobile(orderEntity.getMobile());//用户手机号  非空
		ret.setBussinessName(Constants.ACCOUNTRECHARGE);//用于查询明细显示
		ret.setSysName(Constants.SYSNAME);//定义自己的系统
//		ret.setUserCode(orderEntity.getUserCode());
		ret.setBussinessCode(orderEntity.getOrderId());
		ret.setAccountType(ConstantUser.account_type.pay);
		ret.setBussinessDes(Constants.ACCOUNTRECHARGE);
		if (orderEntity.getPayType().intValue() == RechargeConstants.PAY_TYPE_ALI.intValue()) {
			ret.setBussinessType(ConstantUser.account_bussiness_type.aliPay);
		} else if (orderEntity.getPayType().intValue() == RechargeConstants.PAY_TYPE_TEN.intValue()) {
			ret.setBussinessType(ConstantUser.account_bussiness_type.WeChat);
		}
		ret.setOperationAccount(orderEntity.getPayment());
		logger.info("usercode:"+ ret.getUserCode() + " bussinessDes："+ret.getBussinessDes() + " bussinessType："+ret.getBussinessType() + " operationAccount："+ret.getOperationAccount());
		return ret;
	}

	private void addShopBailDetail(OrderEntity orderEntity) throws Exception {
		OutputDTO<ShopBailOutputDTO> result = shopBailDetailService
				.addShopBailDetail(this.createShopBailInputDTO(orderEntity));
		if (!"0".equals(result.getCode())) {
			logger.error("充值保证金失败：" + "resultcode：" + result.getCode() + " resultmessage：" + result.getMessage());
			throw new Exception("发送充保证金数据失败");
		}
	}

	private ShopBailInputDTO createShopBailInputDTO(OrderEntity orderEntity) {
		ShopBailInputDTO ret = new ShopBailInputDTO();
		ret.setShopUserId(orderEntity.getUid());
		if (orderEntity.getPayType().intValue() == RechargeConstants.PAY_TYPE_ALI.intValue()) {
			ret.setPayMethod(ConstantUser.pay_method.aliPay);
		} else if (orderEntity.getPayType().intValue() == RechargeConstants.PAY_TYPE_TEN.intValue()) {
			ret.setPayMethod(ConstantUser.pay_method.WeChat);
		}
		ret.setPrice(orderEntity.getPayment());
		ret.setType(ConstantUser.pay_type.recharge);
		ret.setRemark("充值保证金 " + orderEntity.getPayment() + " 元");
		logger.info("ShopUserId:"+ ret.getShopUserId() + " PayMethod："+ret.getPayMethod() + " Price："+ret.getPrice() + "type："+ret.getType() + "remark："+ ret.getRemark());
		return ret;
	}
	
	/**
	 * 余额使用记录
	 */
	public Page<AccountDetailOutputDTO> queryAccountRecords(AccountQueryEntity accountQueryEntity) {
		OutputDTO<Page<AccountDetailOutputDTO>> result = accountDetailService.queryAccountDetailB(createAccountDetailInputDTO(accountQueryEntity));
		if (!"0".equals(result.getCode())) {
			throw new RuntimeException("查询余额使用记录失败");
		}
		return result.getData();
	}
	
	private AccountDetailInputDTO createAccountDetailInputDTO(AccountQueryEntity accountQueryEntity) {
		AccountDetailInputDTO inputDTO = new AccountDetailInputDTO();
//		inputDTO.setBussinessType(ConstantUser.account_bussiness_type.weibaoService);
		inputDTO.setMobile(accountQueryEntity.getMobile());
//      inputDTO.setUserCode(accountQueryEntity.getUsercode());
        inputDTO.setCurrentPage(accountQueryEntity.getPage());
        inputDTO.setPageSize(accountQueryEntity.getPageSize());
        return inputDTO;
	}
}
