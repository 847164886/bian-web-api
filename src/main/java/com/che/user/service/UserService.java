package com.che.user.service;


import com.che.user.model.dto.ShopUserInputDTO;
import com.che.user.model.dto.ShopUserOutputDTO;
import com.che.user.pojo.ForgetPwdReply;
import com.che.user.pojo.ForgetPwdReq;
import com.che.user.pojo.ModifyMobileReply;
import com.che.user.pojo.ModifyMobileReq;
import com.che.user.pojo.ModifyPwdReply;
import com.che.user.pojo.ModifyPwdReq;
import com.che.user.pojo.ModifyUserReply;
import com.che.user.pojo.ModifyUserReq;
import com.che.user.pojo.ResetPwdReply;
import com.che.user.pojo.ResetPwdReq;
import com.che.user.pojo.SendMessageReply;
import com.che.user.pojo.SendMessageReq;
import com.che.user.pojo.UserRegistReply;
import com.che.user.pojo.UserRegistReq;
import com.che.user.pojo.ValidateMobileMsgCodeReply;
import com.che.user.pojo.ValidateMobileMsgCodeReq;

import core.DTO.OutputDTO;

public interface UserService {
	
	public UserRegistReply registByMobile(UserRegistReq userRegistReq);
	
	public SendMessageReply registSendMessage(SendMessageReq sendMessageReq);
	
	public SendMessageReply forgetPwdSendMessage(SendMessageReq sendMessageReq);
	
	public SendMessageReply reset(SendMessageReq sendMessageReq);
	
	public void sendMobileCode(ValidateMobileMsgCodeReq validateMobileMsgCodeReq, ValidateMobileMsgCodeReply validateMobileMsgCodeReply);
	
	public void resetPwd(ResetPwdReq resetPwdReq, ResetPwdReply resetPwdReply);
	  
	public OutputDTO<ShopUserOutputDTO> getUser(ShopUserInputDTO param) ;
	
	public ForgetPwdReply forgetPwd(ForgetPwdReq forgetPwdReq) ;
	  
    public void modifyMobile(ModifyMobileReq modifyMobileReq, ModifyMobileReply modifyMobileReply);

    public void modifyPwd(ModifyPwdReq modifyPwdReq, ModifyPwdReply modifyPwdReply);

    public void modifyUser(ModifyUserReq modifyUserReq, ModifyUserReply modifyUserReply); 
	
}
