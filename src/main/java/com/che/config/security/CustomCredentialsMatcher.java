package com.che.config.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 自定义密码验证类
 * 
 * @author quenton
 *
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		if (authcToken instanceof WswyOpenQQAuthenticationToken) { // QQ第三方登录
			// account_type = Constants.ACCOUNT_TYPE_OPEN_QQ;
			// WswyOpenQQAuthenticationToken token = ((WswyOpenQQAuthenticationToken) authcToken);
			//记录登录日志
			
			return true;
			
		} else if (authcToken instanceof WswyOpenWeiBoAuthenticationToken) { // 微博第三方登录
			// account_type = Constants.ACCOUNT_TYPE_OPEN_WEIBO;
			// WswyOpenWeiBoAuthenticationToken token = ((WswyOpenWeiBoAuthenticationToken) authcToken);
			
			return true;

		} else if (authcToken instanceof WswyOpenWeiXinAuthenticationToken) { // 微信第三方登录
			// account_type = Constants.ACCOUNT_TYPE_OPEN_WEIXIN;
			// WswyOpenWeiXinAuthenticationToken token = ((WswyOpenWeiXinAuthenticationToken) authcToken);
			return true;

		} else if (authcToken instanceof WswyMobileAuthenticationToken) { // 通过手机号登录
			// account_type = Constants.ACCOUNT_TYPE_MOBILE;
			WswyMobileAuthenticationToken token = ((WswyMobileAuthenticationToken) authcToken);
			Object tokenCredentials = String.valueOf(token.getPassword());
			Object accountCredentials = getCredentials(info);
			return equals(tokenCredentials, accountCredentials);

		} else if (authcToken instanceof WswyAnonyAuthenticationToken) { // 匿名登录
			// account_type = Constants.ACCOUNT_TYPE_ANONY;
			// WswyAnonyAuthenticationToken token = ((WswyAnonyAuthenticationToken) authcToken);
			return true;
			
		} else {
			return false;
		}
	}

	// 将传进来密码加密方法
//	private String encrypt(String data) {
//		String sha384Hex = new Sha384Hash(data).toBase64();// 这里可以选择自己的密码验证方式
//		return sha384Hex;
//	}
    
}
