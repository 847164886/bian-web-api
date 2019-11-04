/**
 * 
 */
package com.che.user.service;

import com.che.user.entity.UserEntity;
import com.che.user.pojo.LoginReply;
import com.che.user.pojo.LoginRequest;

/**
 * @author karlhell
 *
 */
public interface LoginService {

	/**
	 * 通过手机号查询用户
	 * @param mobile
	 * @return
	 */
	UserEntity getUserByMobile(Long mobile);
	
	/**
	 * 登录
	 * @param req
	 * @return
	 */
	LoginReply login(LoginRequest req, LoginReply reply, String ip, Boolean fromReg);
	
}
