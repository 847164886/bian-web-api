package com.che.user.service;


import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.id.IdWorker;
import com.che.common.util.MyDateUtils;
import com.che.common.util.MyFileUtil;
import com.che.common.util.SendMessage;
import com.che.common.util.SmsSendUtil;
import com.che.common.util.ValidateUtils;
import com.che.common.web.ConstantUser;
import com.che.common.web.Constants;
import com.che.config.listener.SessionListener;
import com.che.jpush.service.JPushService;
import com.che.user.entity.UserEntity;
import com.che.user.interfaces.interf.user.ShopUserService;
import com.che.user.mapper.UserEntityMapper;
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
import com.che.user.pojo.UserPojo;
import com.che.user.pojo.UserRegistReply;
import com.che.user.pojo.UserRegistReq;
import com.che.user.pojo.ValidateMobileMsgCodeReply;
import com.che.user.pojo.ValidateMobileMsgCodeReq;

import core.DTO.OutputDTO;
 
@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LogManager.getLogger(SessionListener.class);
	
	@Autowired
	private UserEntityMapper userEntityMapper;
	
	@Autowired
	private SendMessage sendMessage;
	
	@Autowired
	private RedisTemplate<Object,Object> redisTemplate;
	
	@Autowired
	private IdWorker idWorker;
	
	@Autowired
	private JPushService jPushService;
	
	@Autowired
	private UserCommonService userCommonService;
	
	@Reference(version="2.0.1")
	private ShopUserService shopUserService;
	
	@Value("${avatar.path}")
	private String avatarPath ;
	
    @Value("${avatar.path.web}")
    private String avatarWebPath;
	
	private static final Integer replyCode_11 = -11;  // "手机号，密码和姓名不能为空"
	//private static final Integer replyCode_15 = -15;  // "该帐号已被禁用"
	private static final Integer replyCode_16 = -16;  // "该手机号的验证码不存在"
	private static final Integer replyCode_17 = -17;  // "验证码不正确"
	private static final Integer replyCode_18 = -18;  // "该手机号已经被注册"
	private static final Integer replyCode_19 = -19;  // "用户的密码不能为空"
 
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public UserRegistReply registByMobile(UserRegistReq userRegistReq){
		UserRegistReply userRegistReply = new UserRegistReply();
		String name = userRegistReq.getName();
		Long mobile = userRegistReq.getMobile();
		String password = userRegistReq.getPassword();
		if(StringUtils.isBlank(name) || null == mobile || StringUtils.isBlank(password)){
			userRegistReply.setReplyCode(replyCode_11);
			userRegistReply.setMessage("手机号，密码和姓名不能为空");
			return userRegistReply;
		}
		
		Object code = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_REGIST+"_"+mobile);
		if(null == code ){
			userRegistReply.setReplyCode(replyCode_16);
			userRegistReply.setMessage("该手机号的验证码不存在");
			return userRegistReply;
		}
		
		if( StringUtils.isBlank(userRegistReq.getCode()) || !(String.valueOf(code)).equals(userRegistReq.getCode())){
			userRegistReply.setReplyCode(replyCode_17);
			userRegistReply.setMessage("验证码不正确");
			return userRegistReply;
		}
		
		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(userRegistReq.getMobile().toString());
		OutputDTO<Boolean> outputDTO = shopUserService.isRepeatMobile(param);
		if(!"0".equals(outputDTO.getCode())){
			userRegistReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			userRegistReply.setMessage(outputDTO.getMessage());
 			return userRegistReply;
    	}
		if(outputDTO.getData()){
			userRegistReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			userRegistReply.setMessage("该手机号已注册");
 			return userRegistReply;
		}
		
		param.setPassword(password);
		param.setTruename(name);
		param.setReferralCode(userRegistReq.getRecommend_code());
		String cooperationType="1".equals(userRegistReq.getSource())?ConstantUser.cooperation_type.auction_person:ConstantUser.cooperation_type.auction;
		param.setCooperationType(cooperationType);
		OutputDTO<Long>  idReply = shopUserService.addShopUser(param);
		if(!"0".equals(idReply.getCode())){
			userRegistReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			userRegistReply.setMessage(idReply.getMessage());
 			return userRegistReply;
    	}
        //极光账户绑定 sangyiwen 20151124
//		if(userRegistReq.getAlias()!=null){
//			jPushService.bindingsJPush(idReply.getData(), userRegistReq.getAlias());
//		}
		
		userRegistReply.setUser_id(idReply.getData());
		userRegistReq.setUid(idReply.getData());
		userRegistReply.setReplyCode(Constants.REPLY_SUCCESS);
		return userRegistReply;
	}
 
	@Override
	public OutputDTO<ShopUserOutputDTO> getUser(ShopUserInputDTO param) {
		return   shopUserService.getShopUserByConditions(param);
	}



	@Override
    public void modifyUser(ModifyUserReq modifyUserReq, ModifyUserReply modifyUserReply) {
//      req.getUid();      //从客户端传过来的userId,不可信任
        //获取当登录用户的id
        Long userId = userCommonService.getUserId();
        if (userId == null){//当前session已失效，需要重新登录
            modifyUserReply.setMessage("当前Session已经失败，请登录之后再试");
            modifyUserReply.setResultCode(Constants.RESULT_ERROR_LOGIN);
            modifyUserReply.setReplyCode(-1);
            return ;
        }
	    
	    UserEntity user = new UserEntity();
	    user.setUser_id(userId);
	    user.setGender(modifyUserReq.getGender());
	    user.setCity(modifyUserReq.getCity());
	    if(StringUtils.isNotBlank(modifyUserReq.getAvatar()) && StringUtils.isNotBlank(modifyUserReq.getPic_type())){
//	        logger.debug("Avatar="+modifyUserReq.getAvatar());
	        Long time = System.currentTimeMillis();
            String filepath = avatarPath+userId+time+modifyUserReq.getPic_type();
	        MyFileUtil.createPic(modifyUserReq.getAvatar(), filepath);
	        String av = ""+userId+time+modifyUserReq.getPic_type();
	        user.setAvatar(av);
	    }
        
        Long now = Long.parseLong(MyDateUtils.convertDate2String(MyDateUtils.DATETIME_LONG,new Date()));
        user.setUpdated(now);
        userEntityMapper.update(user);
        
        UserEntity userEnttiy = userEntityMapper.getUserById(userId);
        BeanCopier copierUser = BeanCopier.create(UserEntity.class,UserPojo.class, false);
        UserPojo pojo = new UserPojo();
        copierUser.copy(userEnttiy, pojo, null);
        if(StringUtils.isNotBlank(pojo.getAvatar())){
            modifyUserReply.setAvatar(avatarWebPath + pojo.getAvatar());
        }
        modifyUserReply.setReplyCode(Constants.REPLY_SUCCESS);
    }

    @Override
    public void sendMobileCode(ValidateMobileMsgCodeReq validateMobileMsgCodeReq, ValidateMobileMsgCodeReply validateMobileMsgCodeReply) {
	    Long mobile = validateMobileMsgCodeReq.getMobile();
        Object code = redisTemplate.opsForValue().get(mobile);
        if(null == code ){
            validateMobileMsgCodeReply.setReplyCode(replyCode_16);
            validateMobileMsgCodeReply.setMessage("该手机号的验证码不存在");
            return ;
        }
        
        if( !(String.valueOf(code)).equals(validateMobileMsgCodeReq.getCode())){
            validateMobileMsgCodeReply.setReplyCode(replyCode_17);
            validateMobileMsgCodeReply.setMessage("验证码不正确");
            return ;
        }

        validateMobileMsgCodeReply.setReplyCode(Constants.REPLY_SUCCESS);
        return;
    }

    @Override
	public ForgetPwdReply forgetPwd(ForgetPwdReq forgetPwdReq) {
    	ForgetPwdReply forgetPwdReply = new ForgetPwdReply();
		 if(null==forgetPwdReq.getMobile()||StringUtils.isBlank(forgetPwdReq.getNewpwd()) || StringUtils.isBlank(forgetPwdReq.getCode())){
			 forgetPwdReply.setReplyCode(replyCode_19);
			 forgetPwdReply.setMessage("手机号，密码，验证码不能为空");
	         return forgetPwdReply;
	     }
    	
    	Object code = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_FORGET_PWD+"_"+forgetPwdReq.getMobile());
 		if(null == code ){
 			forgetPwdReply.setReplyCode(replyCode_16);
 			forgetPwdReply.setMessage("该手机号的验证码不存在");
 			return forgetPwdReply;
 		}
 		
 		if( !(String.valueOf(code)).equals(forgetPwdReq.getCode())){
 			forgetPwdReply.setReplyCode(replyCode_17);
 			forgetPwdReply.setMessage("验证码不正确");
 			return forgetPwdReply;
 		}
    	
 		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(forgetPwdReq.getMobile().toString());
		param.setNewPassword(forgetPwdReq.getNewpwd());
		OutputDTO<Object>  outputDTO = shopUserService.updatePasswordByMobile(param);
    	if(!"0".equals(outputDTO.getCode())){
    		forgetPwdReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
 			forgetPwdReply.setMessage(outputDTO.getMessage());
 			return forgetPwdReply;
    	}
    	forgetPwdReply.setReplyCode(Constants.REPLY_SUCCESS);
    	return forgetPwdReply;
	}
 
    @Override
    public void modifyPwd(ModifyPwdReq modifyPwdReq, ModifyPwdReply modifyPwdReply) {
    	ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setId(userCommonService.getUserId());
		param.setOldPassword(modifyPwdReq.getOldPwd());
		param.setNewPassword(modifyPwdReq.getNewPwd());
		//待修改
		OutputDTO<Object>  userEntityReply = shopUserService.updatePasswordByOld(param);
		if(!"0".equals(userEntityReply.getCode())){
			modifyPwdReply.setMessage(userEntityReply.getMessage());
            modifyPwdReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            return;
		}
		 
        modifyPwdReply.setReplyCode(Constants.REPLY_SUCCESS);
        return;
    }

    @Override
	public SendMessageReply reset(SendMessageReq sendMessageReq) {
    	SendMessageReply sendMessageReply = new SendMessageReply();
    	redisTemplate.opsForHash().delete(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, sendMessageReq.getMobile().toString());
    	redisTemplate.delete(Constants.CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT+"_"+sendMessageReq.getMobile().toString());
    	redisTemplate.delete(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT+"_"+sendMessageReq.getMobile().toString());
    	sendMessageReply.setReplyCode(Constants.REPLY_SUCCESS);
    	return sendMessageReply;
	}

    @Override
	public SendMessageReply registSendMessage(SendMessageReq sendMessageReq) {
		SendMessageReply sendMessageReply = new SendMessageReply();
		if(null == sendMessageReq.getMobile() || !ValidateUtils.validateMobile(sendMessageReq.getMobile().toString()) ){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("手机号空或者格式不正确");
			return sendMessageReply;
		}
		int code = 0;
		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(sendMessageReq.getMobile().toString());
		OutputDTO<Boolean> outputDTO = shopUserService.isRepeatMobile(param);
		if(!"0".equals(outputDTO.getCode())){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage(outputDTO.getMessage());
 			return sendMessageReply;
    	}
		if(outputDTO.getData()){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("该手机号已注册");
 			return sendMessageReply;
		}
		Object limitcode = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT+"_"+sendMessageReq.getMobile());
		if(null!= limitcode){
			if((int)limitcode>=5){
				sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
				sendMessageReply.setMessage("您半个小时内连续发短信已超过5次，请稍后再试");
	 			return sendMessageReply;
			}
			redisTemplate.opsForValue().increment(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 1L);
		}else{
			redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 1);
			redisTemplate.expire(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 30L, TimeUnit.MINUTES);  //10
		}
		
		Object expirecode = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_REGIST+"_"+sendMessageReq.getMobile());
		if(null!= expirecode){
			code = (int)expirecode;
		}else{
			//生成随机四位验证码
			code = (int)(Math.random()*9000) + 1000;
			//存储四位验证码到redis
			redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_REGIST+"_"+sendMessageReq.getMobile(), code);
			redisTemplate.expire(Constants.CSJP_SHIRO_USER_REGIST+"_"+sendMessageReq.getMobile(), 10L, TimeUnit.MINUTES);  //10
		}
		StringBuffer content = new StringBuffer("注册账号的验证码是");
		content.append(code).append("，10分钟内有效。");//10
		//发送验证码短信
		SmsSendUtil. sendMsg(sendMessageReq.getMobile().toString(),content.toString(), null);
		sendMessageReply.setReplyCode(Constants.REPLY_SUCCESS);
		return sendMessageReply;
	}
	


	@Override
	public SendMessageReply forgetPwdSendMessage(SendMessageReq sendMessageReq) {
		SendMessageReply sendMessageReply = new SendMessageReply();
		if(null == sendMessageReq.getMobile() || !ValidateUtils.validateMobile(sendMessageReq.getMobile().toString()) ){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("手机号空或者格式不正确");
			return sendMessageReply;
		}
		int code = 0;
		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(sendMessageReq.getMobile().toString());
		OutputDTO<Boolean> outputDTO = shopUserService.isRepeatMobile(param);
		if(!"0".equals(outputDTO.getCode())){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage(outputDTO.getMessage());
 			return sendMessageReply;
    	}
		if(!outputDTO.getData()){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("该手机号未注册");
 			return sendMessageReply;
		}
		Object limitcode = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT+"_"+sendMessageReq.getMobile());
		if(null!= limitcode){
			if((int)limitcode>=5){
				sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
				sendMessageReply.setMessage("您半个小时内连续发短信已超过5次，请稍后再试");
	 			return sendMessageReply;
			}
			redisTemplate.opsForValue().increment(Constants.CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 1L);
		}else{
			redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 1);
			redisTemplate.expire(Constants.CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT+"_"+sendMessageReq.getMobile(), 30L, TimeUnit.MINUTES);  //10
		}
		
		Object expirecode = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_FORGET_PWD+"_"+sendMessageReq.getMobile());
		if(null!= expirecode){
			code = (int)expirecode;
		}else{
			//生成随机四位验证码
			code = (int)(Math.random()*9000) + 1000;
			//存储四位验证码到redis
			redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_FORGET_PWD+"_"+sendMessageReq.getMobile(), code);
			redisTemplate.expire(Constants.CSJP_SHIRO_USER_FORGET_PWD+"_"+sendMessageReq.getMobile(), 10L, TimeUnit.MINUTES);  //10
		}
		StringBuffer content = new StringBuffer("忘记密码的验证码是");
		content.append(code).append("，10分钟内有效。");//10
		//发送验证码短信
		SmsSendUtil. sendMsg(sendMessageReq.getMobile().toString(),content.toString(), null);
		sendMessageReply.setReplyCode(Constants.REPLY_SUCCESS);
		return sendMessageReply;
	}

    
	/*@Override
	public SendMessageReply sendMessage(SendMessageReq sendMessageReq) {
		SendMessageReply sendMessageReply = new SendMessageReply();
		if(null == sendMessageReq.getMobile() || !ValidateUtils.validateMobile(sendMessageReq.getMobile().toString()) ){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("手机号空或者格式不正确");
			return sendMessageReply;
		}
		if(null == sendMessageReq.getType()){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("接口类型不能为空");
			return sendMessageReply;
		}
		
		int code = 0;
		
		ShopUserInputDTO  param = new ShopUserInputDTO ();
		param.setMobile(sendMessageReq.getMobile().toString());
		OutputDTO<Boolean> outputDTO = shopUserService.isRepeatMobile(param);
		if(!"0".equals(outputDTO.getCode())){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage(outputDTO.getMessage());
 			return sendMessageReply;
    	}
		
		//从redis取出验证码 注册验证码
		if(0==sendMessageReq.getType()){
			if(outputDTO.getData()){
				sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
				sendMessageReply.setMessage("该手机号已注册");
	 			return sendMessageReply;
			}
			
			Object loginCountObject = redisTemplate.opsForHash().get(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT, sendMessageReq.getMobile()+"");
			if(null != loginCountObject){
				Long loginCount = Long.parseLong(loginCountObject.toString());
				Long lastLoginTime = (Long)redisTemplate.opsForHash().get(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT, "lastTime_"+sendMessageReq.getMobile());
				
				if(null != loginCount && null != lastLoginTime && Constants.SEND_MESSAGE_LIMIT <= loginCount && lastLoginTime + Constants.SEND_MESSAGE_LIMIT_IDLE >= System.currentTimeMillis()){ // 限制发短信
					sendMessageReply.setReplyCode(Constants.AUTHEXCEPTION_LOGINCOUNT);
					sendMessageReply.setMessage("您连续发短信已超过5次，请半个小时之后再试");
					return sendMessageReply;
				}
			}
			
			if(null!= loginCountObject){
				code = (int)loginCountObject;
			}else{
				//生成随机四位验证码
				code = (int)(Math.random()*9000) + 1000;
				//存储四位验证码到redis
				//redisTemplate.delete(sendMessageReq.getMobile());
				redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_REGIST+sendMessageReq.getMobile(), code);
				redisTemplate.expire(Constants.CSJP_SHIRO_USER_REGIST+sendMessageReq.getMobile(), 10L, TimeUnit.MINUTES);  //10
			}
			
			Boolean hasKey = redisTemplate.opsForHash().hasKey(Constants.CSJP_SHIRO_USER_REGIST, sendMessageReq.getMobile()+"");
			if(hasKey){
				Long i = redisTemplate.opsForHash().increment(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT, sendMessageReq.getMobile()+"", 1L);
			}else{
				redisTemplate.opsForHash().put(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT, sendMessageReq.getMobile()+"", 1L);
			}
			redisTemplate.opsForHash().put(Constants.CSJP_SHIRO_USER_REGIST_LIMIT_COUNT, "lastTime_"+sendMessageReq.getMobile() , System.currentTimeMillis());
			
		}
		//忘记密码验证码
		if(1==sendMessageReq.getType()){
			if(!outputDTO.getData()){
				sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
				sendMessageReply.setMessage("该手机号未注册");
	 			return sendMessageReply;
			}
			Object expirecode = redisTemplate.opsForValue().get(Constants.CSJP_SHIRO_USER_FORGET_PWD+sendMessageReq.getMobile());
			if(null!= expirecode){
				code = (int)expirecode;
			}else{
				//生成随机四位验证码
				code = (int)(Math.random()*9000) + 1000;
				//存储四位验证码到redis
				redisTemplate.opsForValue().set(Constants.CSJP_SHIRO_USER_FORGET_PWD+sendMessageReq.getMobile(), code);
				redisTemplate.expire(Constants.CSJP_SHIRO_USER_FORGET_PWD+sendMessageReq.getMobile(), 10L, TimeUnit.MINUTES);  //10
			}
		}
		if(0==code){
			sendMessageReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			sendMessageReply.setMessage("参数不正确");
			return sendMessageReply;
		}
		StringBuffer content = new StringBuffer("注册账号的验证码是");
		content.append(code).append("，10分钟内有效。");//10
		//发送验证码短信
		SmsSendUtil. sendMsg(sendMessageReq.getMobile().toString(),content.toString(), null);
		sendMessageReply.setReplyCode(Constants.REPLY_SUCCESS);
		return sendMessageReply;
	}*/

	@Override
    public void resetPwd(ResetPwdReq resetPwdReq, ResetPwdReply resetPwdReply) {
        Long mobile = resetPwdReq.getMobile();
        String password = resetPwdReq.getPassword();
        if(StringUtils.isBlank(password)){
            resetPwdReply.setReplyCode(replyCode_19);
            resetPwdReply.setMessage("密码不能为空");
            return;
        }
        Object code = redisTemplate.opsForValue().get(mobile);
        if(null == code ){
            resetPwdReply.setReplyCode(replyCode_16);
            resetPwdReply.setMessage("该手机号的验证码不存在");
            return ;
        }
        
        if( !(String.valueOf(code)).equals(resetPwdReq.getCode())){
            resetPwdReply.setReplyCode(replyCode_17);
            resetPwdReply.setMessage("验证码不正确");
            return ;
        }

        Long now = Long.parseLong(MyDateUtils.convertDate2String(MyDateUtils.DATETIME_LONG,new Date()));
        userEntityMapper.resetPwd(mobile, password, now);
        
        resetPwdReply.setReplyCode(Constants.REPLY_SUCCESS);
        return;
    }
    
    @Override
    public void modifyMobile(ModifyMobileReq modifyMobileReq, ModifyMobileReply modifyMobileReply) {
//      req.getUid();      //从客户端传过来的userId,不可信任
        //获取当登录用户的id
        Long userId = userCommonService.getUserId();
        if (userId == null){//当前session已失效，需要重新登录
            modifyMobileReply.setMessage("当前Session已经失败，请登录之后再试");
            modifyMobileReply.setResultCode(Constants.RESULT_ERROR_LOGIN);
            modifyMobileReply.setReplyCode(-1);
            return ;
        }
        
        Long mobile = modifyMobileReq.getMobile();
        Object code = redisTemplate.opsForValue().get(mobile);
        if(null == code ){
            modifyMobileReply.setReplyCode(replyCode_16);
            modifyMobileReply.setMessage("该手机号的验证码不存在");
            return ;
        }
        
        if( !(String.valueOf(code)).equals(modifyMobileReq.getCode())){
            modifyMobileReply.setReplyCode(replyCode_17);
            modifyMobileReply.setMessage("验证码不正确");
            return ;
        }

        Long now = Long.parseLong(MyDateUtils.convertDate2String(MyDateUtils.DATETIME_LONG,new Date()));
        userEntityMapper.updateMobile(userId, mobile, now);
        modifyMobileReply.setReplyCode(Constants.REPLY_SUCCESS);
        return;
        
    }
	 
}
