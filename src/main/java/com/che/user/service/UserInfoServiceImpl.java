package com.che.user.service;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.auction.mapper.AuctionPriceInfoMapper;
import com.che.common.util.ImageOSSUtil;
import com.che.common.util.MyFileUtil;
import com.che.common.web.Constants;
import com.che.common.web.GlobalInfo;
import com.che.message.mapper.AuctionMessageMapper;
import com.che.user.entity.ShopUserBailRecord;
import com.che.user.interfaces.interf.certification.CertificationService;
import com.che.user.interfaces.interf.user.ShopUserService;
import com.che.user.mapper.ShopUserBailRecordMapper;
import com.che.user.model.constant.ConstantUser;
import com.che.user.model.dto.CertificationInputDTO;
import com.che.user.model.dto.CertificationOutputDTO;
import com.che.user.model.dto.ShopUserInputDTO;
import com.che.user.model.dto.ShopUserOutputDTO;
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
import com.che.user.pojo.UserBailRecordReply.UserBailRecordPojo;
import com.google.common.collect.Lists;

import core.DTO.OutputDTO;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Reference(version="2.0.1")
	private CertificationService certificationService;
 
	@Autowired
	private UserCommonService userCommonService;
	
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	
	@Value("${image.server.path.certify}")
	private String imagePath;
	
	@Reference(version="2.0.1")
	private ShopUserService shopUserService;
	
	@Autowired
	private AuctionMessageMapper auctionMessageMapper;
	
	@Autowired
	private AuctionPriceInfoMapper auctionPriceInfoMapper;
	
	@Autowired
	private ShopUserBailRecordMapper shopUserBailRecordMapper;
  
	@Override
	public UserInfoCenterReply center(UserInfoCenterReq userInfoCenterReq) {
		UserInfoCenterReply userInfoCenterReply = new UserInfoCenterReply();

		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(userCommonService.getUser().getMobile());
		OutputDTO<ShopUserOutputDTO>  outputDTO = shopUserService.getShopUserByConditions(param);
		if(!"0".equals(outputDTO.getCode())){
			userInfoCenterReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			userInfoCenterReply.setMessage(outputDTO.getMessage());
 			return userInfoCenterReply;
    	}
		ShopUserOutputDTO shopUserOutputDTO = outputDTO.getData();
		userInfoCenterReply.setAccount( shopUserOutputDTO.getAccount()== null? "0":shopUserOutputDTO.getAccount().toString());
		userInfoCenterReply.setName(shopUserOutputDTO.getTruename());
		
		if(0!=shopUserOutputDTO.getCertificationType()){
			userInfoCenterReply.setCertify_tatus((3==shopUserOutputDTO.getCertificationStatus())?0:shopUserOutputDTO.getCertificationStatus());
			userInfoCenterReply.setName(shopUserOutputDTO.getCertificationName());
		}
		
		userInfoCenterReply.setBail((null==shopUserOutputDTO.getBail())?"0":shopUserOutputDTO.getBail().toString());
		
		userInfoCenterReply.setUnread(auctionMessageMapper.selectUnread(userCommonService.getUserId()));
		
		userInfoCenterReply.setHistory(auctionPriceInfoMapper.selectHistoryNumByUserId(userCommonService.getUserId()));
		
		userInfoCenterReply.setProcedure(auctionPriceInfoMapper.selectTonedNumByUserId(userCommonService.getUserId()));
		
		return userInfoCenterReply;
		
	}

	@Override
	public UserBailRecordReply bailRecord(
			UserBailRecordReq userBailRecordReq) {
		UserBailRecordReply userBailRecordReply = new UserBailRecordReply();
		List<UserBailRecordPojo> userBailRecordPojoList = Lists.newArrayList();
		//System.out.println("userCommonService.getUserId()="+userCommonService.getUserId());
		List<ShopUserBailRecord> shopUserBailRecordList = shopUserBailRecordMapper.selectByUserId(userCommonService.getUserId());
		for(ShopUserBailRecord shopUserBailRecord :shopUserBailRecordList){
			UserBailRecordPojo userBailRecordPojo = new UserBailRecordPojo();
			userBailRecordPojo.setType(shopUserBailRecord.getType());
			userBailRecordPojo.setPrice(shopUserBailRecord.getPrice());
			userBailRecordPojo.setAddTime(shopUserBailRecord.getAddTime());
			userBailRecordPojoList.add(userBailRecordPojo);
		}
		
		userBailRecordReply.setUserBailRecordPojoList(userBailRecordPojoList);
		userBailRecordReply.setReplyCode(Constants.REPLY_SUCCESS);
		return userBailRecordReply;
	}

	@Override
	public UserImageUploadReply imgUpload(UserImageUploadReq userImageUploadReq) {
		
		UserImageUploadReply userImageUploadReply = new UserImageUploadReply();
		if(StringUtils.isBlank(userImageUploadReq.getImage())||StringUtils.isBlank(userImageUploadReq.getPic_type())||null==userImageUploadReq.getSort()){
			userImageUploadReply.setMessage("参数不能为空");
			userImageUploadReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			return userImageUploadReply;
		}
		
		//保存本地
		String imageName =  userCommonService.getUserId()+"_"+userImageUploadReq.getSort()+userImageUploadReq.getPic_type();
		String sourceFile = imagePath+"/"+imageName;
		MyFileUtil.createPic(userImageUploadReq.getImage(), sourceFile);
		
		//上传 OSS
		String targetFile =GlobalInfo.getInstance().ossPathCertify+"/"+imageName;
		ImageOSSUtil.upload(targetFile, sourceFile);
		redisTemplate.opsForHash().put(Constants.CSJP_CERTITY+userCommonService.getUserId(),userImageUploadReq.getSort(),targetFile); 
		//上传成功删除
		File file =new File(sourceFile);
		if(file.exists()){
			file.delete();
		}
		
		userImageUploadReply.setReplyCode(Constants.REPLY_SUCCESS);
		
		return userImageUploadReply;
	
	}

	@Override
	public UserCertifyReply certify(UserCertifyReq userCertifyReq) {

		UserCertifyReply userCertifyReply = new UserCertifyReply();
	
		if(StringUtils.isBlank(userCertifyReq.getCity())||StringUtils.isBlank(userCertifyReq.getId_number())||StringUtils.isBlank(userCertifyReq.getName())){
			userCertifyReply.setMessage("参数不能为空");
			userCertifyReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			return userCertifyReply;
		}
		CertificationInputDTO certificationInputDTO  = new CertificationInputDTO ();
		
		certificationInputDTO.setCode(userCommonService.getUser().getCode());
		certificationInputDTO.setCity(userCertifyReq.getCity());
		certificationInputDTO.setIdNumber(userCertifyReq.getId_number());
		certificationInputDTO.setName(userCertifyReq.getName());
		certificationInputDTO.setCertificationType(ConstantUser.certification_type.certification);
		Object frontUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),1);
		Object backUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),2);
		Object handheUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),3);
		certificationInputDTO.setFrontUrl(frontUrl!=null?(String)frontUrl:"");
		certificationInputDTO.setBackUrl(backUrl!=null?(String)backUrl:"");
		certificationInputDTO.setHandheUrl(handheUrl!=null?(String)handheUrl:"");
		
		@SuppressWarnings("unchecked")	
		OutputDTO<CertificationOutputDTO> outputDTO = certificationService.addCertification(certificationInputDTO);
		if(!"0".equals(outputDTO.getCode())){
			userCertifyReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
    		userCertifyReply.setMessage(outputDTO.getMessage());
 			return userCertifyReply;
    	}
		userCertifyReply.setReplyCode(Constants.REPLY_SUCCESS);
		return userCertifyReply;
	
	}

	@Override
	public UserBusinessCertifyReply businessCertify(
			UserBusinessCertifyReq userCertifyReq) {
		
		UserBusinessCertifyReply userCertifyReply = new UserBusinessCertifyReply();
	
		if(StringUtils.isBlank(userCertifyReq.getCity())||StringUtils.isBlank(userCertifyReq.getEnterpriseNumber())||StringUtils.isBlank(userCertifyReq.getName())){
			userCertifyReply.setMessage("参数不能为空");
			userCertifyReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			return userCertifyReply;
		}
		CertificationInputDTO certificationInputDTO  = new CertificationInputDTO ();
		
		certificationInputDTO.setCode(userCommonService.getUser().getCode());
		certificationInputDTO.setCity(userCertifyReq.getCity());
		certificationInputDTO.setEnterpriseNumber(userCertifyReq.getEnterpriseNumber());
		certificationInputDTO.setName(userCertifyReq.getName());
		certificationInputDTO.setCertificationType(ConstantUser.certification_type.enterprise);
		Object enterpriseUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),1);
		Object organizationUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),2);
		Object attorneyUrl = redisTemplate.opsForHash().get(Constants.CSJP_CERTITY+userCommonService.getUserId(),3);
		certificationInputDTO.setEnterpriseUrl(enterpriseUrl!=null?(String)enterpriseUrl:"");
		certificationInputDTO.setOrganizationUrl(organizationUrl!=null?(String)organizationUrl:"");
		certificationInputDTO.setAttorneyUrl(attorneyUrl!=null?(String)attorneyUrl:"");
		
		@SuppressWarnings("unchecked")	
		OutputDTO<CertificationOutputDTO> outputDTO = certificationService.addCertification(certificationInputDTO);
		if(!"0".equals(outputDTO.getCode())){
			userCertifyReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
    		userCertifyReply.setMessage(outputDTO.getMessage());
 			return userCertifyReply;
    	}
		userCertifyReply.setReplyCode(Constants.REPLY_SUCCESS);
		return userCertifyReply;
	 
	}
	

	
}
