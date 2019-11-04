package com.che.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.web.Constants;
import com.che.user.pojo.UserBailRecordReply;
import com.che.user.pojo.UserBailRecordReq;
import com.che.user.pojo.UserBusinessCertifyReply;
import com.che.user.pojo.UserBusinessCertifyReq;
import com.che.user.pojo.UserCertifyReply;
import com.che.user.pojo.UserCertifyReq;
import com.che.user.pojo.UserImageUploadReply;
import com.che.user.pojo.UserImageUploadReq;
import com.che.user.pojo.UserInfoCenterReply;
import com.che.user.pojo.UserInfoCenterReq;
import com.che.user.service.UserInfoService;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

	@Autowired
	private UserInfoService userInfoService;
	
	@RequestMapping("/center")
	public UserInfoCenterReply center(@RequestBody(required=false) UserInfoCenterReq userInfoCenterReq){
		
		UserInfoCenterReply userInfoReply  = userInfoService.center(userInfoCenterReq); 
		userInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return userInfoReply;
	}
	
	@RequestMapping("/bailRecord")
	public UserBailRecordReply bailRecord(@RequestBody(required=false) UserBailRecordReq shopUserBailRecordReq){
		
		UserBailRecordReply shopUserBailRecordReply  = userInfoService.bailRecord(shopUserBailRecordReq); 
		shopUserBailRecordReply.setResultCode(Constants.RESULT_SUCCESS);
		return shopUserBailRecordReply;
	}
	
	
	@RequestMapping("/imgUpload")
	public UserImageUploadReply imgUpload(@RequestBody(required=false) UserImageUploadReq userImageUploadReq){
		
		UserImageUploadReply userImageUploadReqply = userInfoService.imgUpload(userImageUploadReq); 
		userImageUploadReqply.setResultCode(Constants.RESULT_SUCCESS);
        return userImageUploadReqply;
	}
	
	@RequestMapping("/certify")
	public UserCertifyReply certify(@RequestBody(required=false) UserCertifyReq userCertifyReq){
		
		UserCertifyReply userCertifyReply  = userInfoService.certify(userCertifyReq); 
		userCertifyReply.setResultCode(Constants.RESULT_SUCCESS);
		return userCertifyReply;
	}
	
	@RequestMapping("/businessCertify")
	public UserBusinessCertifyReply businessCertify(@RequestBody(required=false) UserBusinessCertifyReq userBusinessCertifyReq){
		
		UserBusinessCertifyReply userBusinessCertifyReply  = userInfoService.businessCertify(userBusinessCertifyReq); 
		userBusinessCertifyReply.setResultCode(Constants.RESULT_SUCCESS);
		return userBusinessCertifyReply;
	}
	
	
}
