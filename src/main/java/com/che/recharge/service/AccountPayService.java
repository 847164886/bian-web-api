package com.che.recharge.service;

import com.che.recharge.pojo.RechargeAccountPayReply;
import com.che.recharge.pojo.RechargeAccountPayReq;

public interface AccountPayService {

	public RechargeAccountPayReply queryAccountRecords(RechargeAccountPayReq rechargeAccountPayReq);
}
