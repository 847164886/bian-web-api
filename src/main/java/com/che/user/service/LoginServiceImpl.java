package com.che.user.service;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.common.util.MyDateUtils;
import com.che.common.web.Constants;
import com.che.config.security.WswyMobileAuthenticationToken;
import com.che.jpush.entity.JPushEntity;
import com.che.jpush.service.JPushService;
import com.che.user.entity.UserEntity;
import com.che.user.interfaces.interf.user.ShopUserService;
import com.che.user.mapper.UserEntityMapper;
import com.che.user.model.dto.ShopUserOutputDTO;
import com.che.user.pojo.LoginReply;
import com.che.user.pojo.LoginRequest;

/**
 * @author karlhell
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);
	
	@Autowired
	private UserEntityMapper userEntityMapper;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserCommonService userCommonService;
	
    @Autowired
	private JPushService jPushService;
    
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;

	//@Value("{shiro.login.limit}")
	private static final int loginLimit = 5;
	
	//@Value("{shiro.login.loginLimitIdle}")
	private static final Long loginLimitIdle = 30*60*1000L;

    @Value("${avatar.path.web}")
    private String avatarWebPath;
	
	@Reference(version="2.0.1")
	private ShopUserService shopUserService;
    
	@Override
	public UserEntity getUserByMobile(Long mobile) {
		return userEntityMapper.getUserByMobile(mobile);
	}
	
	@Override
	public LoginReply login(LoginRequest req, LoginReply reply, String ip, Boolean fromReg) {
		AuthenticationToken token = null;
		if(null != req.getMobile() && StringUtils.isNotBlank(req.getPassword())){ // mobile登录
			Object loginCountObject = redisTemplate.opsForHash().get(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, req.getMobile()+"");
			if(null != loginCountObject){
				Long loginCount = Long.parseLong(loginCountObject.toString());
				Long lastLoginTime = (Long)redisTemplate.opsForHash().get(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, "lastTime_"+req.getMobile());
				
				if(null != loginCount && null != lastLoginTime && loginLimit <= loginCount && lastLoginTime + loginLimitIdle >= System.currentTimeMillis()){ // 限制登录
					reply.setReplyCode(Constants.AUTHEXCEPTION_LOGINCOUNT);
					reply.setMessage("您连续登录失败已超过5次，请半个小时之后再试");
					return reply;
				}
			}
			token = new WswyMobileAuthenticationToken(req.getMobile(), req.getPassword(), false);
			
		} 
	 
		Subject currentUser = SecurityUtils.getSubject();
		
		try {
			currentUser.logout();
			currentUser.login(token);
			//登录成功
			ShopUserOutputDTO user = (ShopUserOutputDTO)currentUser.getPrincipal();
			logger.info("Login====mobile=" + user.getMobile() + "userid=" + user.getUserId() + ",sessionid=" + currentUser.getSession().getId());
			//极光账户绑定 sangyiwen 20151124
//			if(req.getAlias()!=null){
//				jPushService.bindingsJPush(user.getUserId(), req.getAlias());
//			}
			
			JPushEntity jPushEntity = jPushService.bindingsJPush(user.getUserId(), req.getMobile()+"");
			reply.setMain_switch_state(jPushEntity.getMain_switch_state());
			reply.setSync_switch_state(jPushEntity.getSync_switch_state());
			
			Boolean hasKey = redisTemplate.opsForHash().hasKey(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, user.getMobile()+"");
			if(hasKey){
				redisTemplate.opsForHash().delete(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, user.getMobile()+"");
				redisTemplate.opsForHash().delete(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, "lastTime_"+user.getMobile());
			}
            Long now = Long.parseLong(MyDateUtils.convertDate2String(MyDateUtils.DATETIME_LONG,new Date()));
			//预留
            //userEntityMapper.loginUpdate(user.getUser_id(), ip, req.getVersionCode(), now);
			reply.setSession(userCommonService.getSessionId());
			reply.setUid(user.getUserId());
			reply.setAccount(user.getAccount() == null? null:user.getAccount().toString());
			reply.setMobile(Long.parseLong(user.getMobile()));
			reply.setCertifyStatus(user.getCertificationStatus());
			reply.setName(user.getTruename());
			reply.setReplyCode(Constants.RESULT_SUCCESS);
		} catch (AuthenticationException e) {
			e.printStackTrace();
			String mes = e.getMessage();
			reply.setReplyCode(-1);
			reply.setMessage("登录系统错误");
			Boolean flag = false;
			if(StringUtils.equals(mes, Constants.AUTHEXCEPTION_ERRORTOKEN+"")){
				reply.setReplyCode(Constants.AUTHEXCEPTION_ERRORTOKEN);
				reply.setMessage("帐号信息有误，请您核对后再重试。");
				flag = true;
			}else if(StringUtils.equals(mes, Constants.AUTHEXCEPTION_UNKOWNSOURCE+"")){
				reply.setReplyCode(Constants.AUTHEXCEPTION_UNKOWNSOURCE);
				reply.setMessage("未知的用户 类型");
			}else if(StringUtils.equals(mes, Constants.AUTHEXCEPTION_ACCOUNTDISABLED+"")){
				reply.setReplyCode(Constants.AUTHEXCEPTION_ACCOUNTDISABLED);
				reply.setMessage("用户不可用");
			}else if(StringUtils.equals(mes, Constants.AUTHEXCEPTION_ACCOUNTNOTEXISTS+"")){
				reply.setReplyCode(Constants.AUTHEXCEPTION_ACCOUNTNOTEXISTS);
				reply.setMessage("帐户不存在");
			}else{
				flag = true;
				reply.setReplyCode(Constants.AUTHEXCEPTION_OTHER);				
				reply.setMessage("帐号或密码错误，请您核对后再重试。");
			}
			
			if(flag && null != req.getMobile() && StringUtils.isNotBlank(req.getPassword())){ // mobile登录
				Boolean hasKey = redisTemplate.opsForHash().hasKey(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, req.getMobile()+"");
				if(hasKey){
					Long i = redisTemplate.opsForHash().increment(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, req.getMobile()+"", 1L);
					System.out.println(i);
				}else{
					redisTemplate.opsForHash().put(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, req.getMobile()+"", 1L);
				}
				redisTemplate.opsForHash().put(Constants.CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT, "lastTime_"+req.getMobile() , System.currentTimeMillis());
				
			}
		}
		
		return reply;
	}
	
}
