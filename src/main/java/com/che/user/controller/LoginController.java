/**
 * 
 */
package com.che.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.util.ValidateUtils;
import com.che.common.web.Constants;
import com.che.common.web.Reply;
import com.che.common.web.Req;
import com.che.user.pojo.LoginReply;
import com.che.user.pojo.LoginRequest;
import com.che.user.service.LoginService;

/**
 * @author karlhell
 *
 */
@RestController
public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login")
    public LoginReply login(HttpServletRequest request, HttpServletResponse response, @RequestBody(required=false) LoginRequest loginRequest) {
        LoginReply reply = new LoginReply();
        if (!loginValidation(request, response, loginRequest)) {
        	reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            reply.setMessage("参数有误");
            return reply;
        }
        loginService.login(loginRequest, reply, getRealIp(request), false);
        return reply;
    }

    @RequestMapping(value = "/loginforreg")
    public LoginReply login(HttpServletRequest request, HttpServletResponse response) {
        LoginReply reply = new LoginReply();
        LoginRequest loginRequest = (LoginRequest)request.getAttribute("loginRequest");
        Boolean fromReg = (Boolean)request.getAttribute("fromReg");
        if (!loginValidation(request, response, loginRequest)) {
            reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            reply.setMessage("参数有误");
            return reply;
        }
        logger.debug("loginforreg from :" + fromReg);
        loginService.login(loginRequest, reply, getRealIp(request), fromReg);
        return reply;
    }
    
    
    @RequestMapping(value = "/unlogin")
    public LoginReply unlogin() {
        LoginReply loginRep = new LoginReply();
        loginRep.setReplyCode(Constants.RESULT_ERROR_LOGIN);
        loginRep.setResultCode(Constants.RESULT_ERROR_LOGIN);
        loginRep.setMessage("请重新登陆");
        return loginRep;
    }

    @RequestMapping(value = "/unlogin-kickout")
    public LoginReply kickout() {
        LoginReply loginRep = new LoginReply();
	loginRep.setReplyCode(Constants.RESULT_ERROR_KICKOUT);
	loginRep.setResultCode(Constants.RESULT_ERROR_KICKOUT);
        loginRep.setMessage("您的账户已在另外的设备登陆");
        return loginRep;
    }

    @RequestMapping(value = "/unauth")
    public LoginReply unauth() {
        LoginReply loginRep = new LoginReply();
        loginRep.setReplyCode(Constants.RESULT_UNAUTH);
    	loginRep.setResultCode(Constants.RESULT_UNAUTH);
        loginRep.setMessage("无权限访问该功能");
        return loginRep;
    }

    @RequestMapping("/logout")
    public Reply logout(@RequestBody Req req) {
        Reply reply = new Reply();
        try {
            SecurityUtils.getSubject().logout();
        } catch (Exception e) {
            reply.setResultCode(Constants.RESULT_ERROR_SYSERROR);
            reply.setMessage("会话错误");
        }
        return reply;
    }

    private Boolean loginValidation(HttpServletRequest request, HttpServletResponse response, LoginRequest loginRequest) {
        if(loginRequest == null){
            return false;
        }

        if (null != loginRequest.getMobile()) {
            if (!ValidateUtils.validateMobile("" + loginRequest.getMobile()) || StringUtils.isBlank(loginRequest.getPassword())) {
                return false;
            }
        }
        return true;
    }

    public static String getRealIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Real-IP");
        if (ip != null) {
            if (ip.indexOf(',') == -1) {
                return ip;
            }
            return ip.split(",")[0];
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }

        return ip;
    }

}
