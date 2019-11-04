package com.che.user.service;

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

public interface UserInfoService {

	public UserInfoCenterReply center(UserInfoCenterReq userInfoCenterReq);
	
	public UserBailRecordReply bailRecord(UserBailRecordReq shopUserBailRecordReq);
	
	public UserImageUploadReply imgUpload(UserImageUploadReq userImageUploadReq);
	
	public UserCertifyReply certify(UserCertifyReq UserCertifyReq);
	
	public UserBusinessCertifyReply businessCertify(UserBusinessCertifyReq userBusinessCertifyReq);
}
