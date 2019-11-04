package com.che.recharge.pojo;

import java.util.Date;
import java.util.List;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RechargeAccountPayReply extends Reply {
	
	private static final long serialVersionUID = -349341599589508314L;
	
	private List<AccountPayRecord> accountPayRecords;

	@Data
	@EqualsAndHashCode(callSuper=false)
	public static final class AccountPayRecord {
		
		private String bussinessDes; // 业务描述
		
		private String amount; // 使用金额数
		
		private String create_time; // 余额使用时间
	}
	
}
