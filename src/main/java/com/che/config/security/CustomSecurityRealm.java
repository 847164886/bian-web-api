package com.che.config.security;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.che.common.web.Constants;
import com.che.user.interfaces.interf.user.ShopUserService;
import com.che.user.model.dto.ShopUserInputDTO;
import com.che.user.model.dto.ShopUserOutputDTO;

import core.DTO.OutputDTO;

@Component("customSecurityRealm")
public class CustomSecurityRealm extends AuthorizingRealm {
	 private static final Logger logger = LogManager.getLogger(AuthorizingRealm.class);
	@Resource
	private CacheManager shiroCacheManager;

	private ShopUserService shopUserService;
	/*
	 * @Autowired private UserService userService;
	 */
	
	public CustomSecurityRealm(ShopUserService shopUserService) {
		setName("WswyShiroDbRealm");
		this.shopUserService = shopUserService;
		// 认证
		// super.setAuthenticationCacheName(Constants.AUTHENTICATIONCACHENAME);
		// super.setAuthenticationCachingEnabled(false);

		// 授权
		// super.setAuthorizationCacheName(Constants.AUTHORIZATIONCACHENAME);
	}

	/**
	 * 为当前登录的Subject授予角色和权限
	 * 
	 * @see 经测试:本例中该方法的调用时机为需授权资源被访问时
	 * @see 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例未启用AuthorizationCache
	 * @see web层可以有shiro的缓存，dao层可以配有hibernate的缓存（后面介绍）
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return null;
	}

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		if (null != SecurityUtils.getSubject() && !SecurityUtils.getSubject().isAuthenticated()) {
			// logger.debug("Q.Zh >> drop it, 因为它是非正常退出遗留下来的");
			doClearCache(SecurityUtils.getSubject().getPrincipals());
		}
		// logger.debug("WSWY >> shiro username do authentication ...");
		if (authcToken == null) {
			throw new AuthenticationException(Constants.AUTHEXCEPTION_ERRORTOKEN + "");
		}

		WswyMobileAuthenticationToken token = ((WswyMobileAuthenticationToken) authcToken);
		if (null == token || null == token.getMobile()) {
			throw new AuthenticationException(Constants.AUTHEXCEPTION_ERRORTOKEN + "");
		}

		ShopUserInputDTO param = new ShopUserInputDTO();
		param.setMobile(token.getMobile().toString());
		// OutputDTO<ShopUserOutputDTO> userEntityReply =
		// userService.getUser(param);
		OutputDTO<ShopUserOutputDTO> userEntityReply = shopUserService.getShopUserByConditions(param);
		System.out.println("userEntityReply=" + userEntityReply);
		
		logger.info("code:" + userEntityReply.getCode() + ", message:"+userEntityReply.getMessage());
		if (null == userEntityReply || !"0".equals(userEntityReply.getCode())) {
			throw new AuthenticationException(Constants.AUTHEXCEPTION_ERRORTOKEN + "");
		}

		ShopUserOutputDTO shopUser = userEntityReply.getData();
		if (shopUser == null || null == shopUser.getUserId()) {
			throw new AuthenticationException(Constants.AUTHEXCEPTION_ACCOUNTNOTEXISTS + "");
		}
		
		logger.info("mobile=" + userEntityReply.getData().getMobile() + "userid:" + userEntityReply.getData().getUserId());
		
		return new SimpleAuthenticationInfo(shopUser, shopUser.getPassword(), getName());
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		// setCredentialsMatcher(new CustomCredentialsMatcher());
	}
}
