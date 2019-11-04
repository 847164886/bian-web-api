package com.che.recharge.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.che.common.web.Constants;
import com.che.recharge.pojo.RechargeAccountPayReply;
import com.che.recharge.pojo.RechargeAccountPayReply.AccountPayRecord;
import com.che.recharge.pojo.RechargeAccountPayReq;
import com.che.user.model.dto.AccountDetailOutputDTO;
import com.che.user.service.UserCommonService;

import core.page.Page;
import lombok.Data;

@Service
public class AccountPayServiceImpl implements AccountPayService {
	
	@Autowired
	private RechargeUserService rechargeUserService;
	
	@Autowired
	private UserCommonService userCommonService;

	/**
	 * 余额使用记录
	 */
	@Override
	public RechargeAccountPayReply queryAccountRecords(RechargeAccountPayReq rechargeAccountPayReq) {
		
		RechargeAccountPayReply rechargeAccountPayReply = new RechargeAccountPayReply();
		SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 调用大账户查询余额使用记录
		Long usercode = userCommonService.getUser().getCode(); 
		String mobile = userCommonService.getUser().getMobile();
		
		AccountQueryEntity accountQueryEntity = new AccountQueryEntity();
		accountQueryEntity.page = rechargeAccountPayReq.getPage();
		accountQueryEntity.pageSize = rechargeAccountPayReq.getPageSize();
		accountQueryEntity.usercode = usercode;
		accountQueryEntity.mobile = mobile;
		Page<AccountDetailOutputDTO> accountRecords = rechargeUserService.queryAccountRecords(accountQueryEntity);
		
		List<AccountPayRecord> accountPayRecords = new ArrayList<AccountPayRecord>();
		if (accountRecords != null && accountRecords.getResult() != null) {
			for (AccountDetailOutputDTO outputDTO : accountRecords.getResult()) {
				AccountPayRecord accountPayRecord = new AccountPayRecord();
				if (outputDTO.getOperationAccount().compareTo(new BigDecimal(0)) >= 0) {
					accountPayRecord.setAmount("+" + outputDTO.getOperationAccount());
				} else {
					accountPayRecord.setAmount(outputDTO.getOperationAccount() + "");
				}
				accountPayRecord.setBussinessDes(outputDTO.getBussinessDes());
				accountPayRecord.setCreate_time(sdf.format(outputDTO.getCreateTime()));
				accountPayRecords.add(accountPayRecord);
			}
		}
		
		rechargeAccountPayReply.setAccountPayRecords(accountPayRecords);
		rechargeAccountPayReply.setReplyCode(Constants.REPLY_SUCCESS);
		rechargeAccountPayReply.setMessage("查询余额记录成功");
		return rechargeAccountPayReply;
	}
	
	@Data
	public static final class AccountQueryEntity {
		
		private Integer page = 1;

		private Integer pageSize = Constants.PAGESIZE;
		
		private Long usercode; // 用户code
		
		private String mobile; //手机号
	}

}
