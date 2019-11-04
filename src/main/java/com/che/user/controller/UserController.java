package com.che.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.che.common.util.ValidateUtils;
import com.che.common.web.Constants;
import com.che.user.pojo.ForgetPwdReply;
import com.che.user.pojo.ForgetPwdReq;
import com.che.user.pojo.LoginRequest;
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
import com.che.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * // 前置条件：uuid不能为空
     * 
     * @param userRegistReq
     * @return
     */

    @RequestMapping(value = "/loginafterregistError")
    public UserRegistReply registHasError(HttpServletRequest request, HttpServletResponse response) {
        // request.getAttribute("registHasError");
        UserRegistReply userRegistReply = (UserRegistReply) request.getAttribute("userRegistReply");
        return userRegistReply;
    }

    @RequestMapping(value = "/regist")
    public ModelAndView registEnter(HttpServletRequest request, HttpServletResponse response, @RequestBody UserRegistReq userRegistReq) {
        request.setAttribute("userRegistReq", userRegistReq);
       // 注册成功之后跳转到登录
        UserRegistReply userRegistReply = userService.registByMobile(userRegistReq );
        userRegistReply.setResultCode(Constants.RESULT_SUCCESS);
        
        if (userRegistReply.getReplyCode() == 0) {
            LoginRequest loginRequest = new LoginRequest();
            BeanCopier copierUser = BeanCopier.create(UserRegistReq.class, LoginRequest.class, false);
            copierUser.copy(userRegistReq, loginRequest, null);
            request.setAttribute("loginRequest", loginRequest);
            request.setAttribute("fromReg", true);
            return new ModelAndView("forward:/loginforreg");
        } else {
            request.setAttribute("userRegistReply", userRegistReply);
            return new ModelAndView("forward:/user/loginafterregistError");
        }
    }

    /**
     * 忘记密码
     * 
     * @return
     */
    @RequestMapping(value = "/forgetPwd")
    public ForgetPwdReply forgetPwd(@RequestBody(required=false) ForgetPwdReq forgetPwdReq) {
    	ForgetPwdReply forgetPwdReply = userService.forgetPwd(forgetPwdReq);
    	forgetPwdReply.setResultCode(Constants.RESULT_SUCCESS);
        return forgetPwdReply;
    }
    
    /**
     * 修改密码
     * 
     * @return
     */
    @RequestMapping(value = "/modifyPwd")
    public ModifyPwdReply modifyPwd(@RequestBody(required=false) ModifyPwdReq modifyPwdReq) {
        ModifyPwdReply modifyPwdReply = new ModifyPwdReply();
        if (!validationModifyPwd(modifyPwdReq, modifyPwdReply)) {
            modifyPwdReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            modifyPwdReply.setMessage("参数有误");
            return modifyPwdReply;
        }
        userService.modifyPwd(modifyPwdReq, modifyPwdReply);
        modifyPwdReply.setResultCode(Constants.RESULT_SUCCESS);
        return modifyPwdReply;
    }
 
    @RequestMapping(value = "/registSendMessage")
    public SendMessageReply registSendMessage(@RequestBody(required=false) SendMessageReq sendMessageReq) {

        SendMessageReply sendMessageReply = userService.registSendMessage(sendMessageReq);

        sendMessageReply.setResultCode(Constants.RESULT_SUCCESS);

        return sendMessageReply;

    }
    
    @RequestMapping(value = "/forgetPwdSendMessage")
    public SendMessageReply forgetPwdSendMessage(@RequestBody(required=false) SendMessageReq sendMessageReq) {

        SendMessageReply sendMessageReply = userService.forgetPwdSendMessage(sendMessageReq);

        sendMessageReply.setResultCode(Constants.RESULT_SUCCESS);

        return sendMessageReply;

    }
    
    @RequestMapping(value = "/reset")
    public SendMessageReply reset(@RequestBody(required=false) SendMessageReq sendMessageReq) {

        SendMessageReply sendMessageReply = userService.reset(sendMessageReq);

        sendMessageReply.setResultCode(Constants.RESULT_SUCCESS);

        return sendMessageReply;

    }
    /**
     * 修改用户资料
     * 
     * @return
     */
    @RequestMapping(value = "/modifyUser")
    public ModifyUserReply modifyUser(@RequestBody(required=false) ModifyUserReq modifyUserReq) {
        ModifyUserReply modifyUserReply = new ModifyUserReply();
        if (!validationModifyUser(modifyUserReq, modifyUserReply)) {
            modifyUserReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            modifyUserReply.setMessage("参数有误");
            return modifyUserReply;
        }
        userService.modifyUser(modifyUserReq, modifyUserReply);

        modifyUserReply.setResultCode(Constants.RESULT_SUCCESS);
        return modifyUserReply;
    }

    /**
     * 忘记密码之验证手机验证码
     * 
     * @return
     */
    @RequestMapping(value = "/validateMobileMsgCode")
    public ValidateMobileMsgCodeReply validateMobileMsgCode(@RequestBody(required=false) ValidateMobileMsgCodeReq validateMobileMsgCodeReq) {
        ValidateMobileMsgCodeReply validateMobileMsgCodeReply = new ValidateMobileMsgCodeReply();
        if (!validationSendMobileMsg(validateMobileMsgCodeReq, validateMobileMsgCodeReply)) {
            validateMobileMsgCodeReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            validateMobileMsgCodeReply.setMessage("参数有误");
            return validateMobileMsgCodeReply;
        }

        userService.sendMobileCode(validateMobileMsgCodeReq, validateMobileMsgCodeReply);

        validateMobileMsgCodeReply.setResultCode(Constants.RESULT_SUCCESS);
        return validateMobileMsgCodeReply;
    }

    /**
     * 重置密码
     * 
     * @return
     */
    @RequestMapping(value = "/resetPwd")
    public ResetPwdReply restpwd(@RequestBody(required=false) ResetPwdReq resetPwdReq) {
        ResetPwdReply resetPwdReply = new ResetPwdReply();
        if (!validationResetPwd(resetPwdReq, resetPwdReply)) {
            resetPwdReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            resetPwdReply.setMessage("参数有误");
            return resetPwdReply;
        }
        userService.resetPwd(resetPwdReq, resetPwdReply);

        resetPwdReply.setResultCode(Constants.RESULT_SUCCESS);
        return resetPwdReply;
    }

    /**
     * 修改手机号
     * 
     * @return
     */
    @RequestMapping(value = "/modifyMobile")
    public ModifyMobileReply modifyMobile(@RequestBody ModifyMobileReq modifyMobileReq) {
        ModifyMobileReply modifyMobileReply = new ModifyMobileReply();
        if (!validationModifyMobile(modifyMobileReq, modifyMobileReply)) {
            modifyMobileReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
            modifyMobileReply.setMessage("参数有误");
            return modifyMobileReply;
        }
        userService.modifyMobile(modifyMobileReq, modifyMobileReply);
        modifyMobileReply.setResultCode(Constants.RESULT_SUCCESS);
        return modifyMobileReply;
    }


    private Boolean registValidation(HttpServletRequest request, HttpServletResponse response, UserRegistReq userRegistReq) {
   
        if (null != userRegistReq.getMobile()) {
            if (!ValidateUtils.validateMobile("" + userRegistReq.getMobile()) || StringUtils.isBlank(userRegistReq.getPassword()) || StringUtils.isBlank(userRegistReq.getCode())) {
                return false;
            }
        }
        return true;
    }

    private boolean validationModifyUser(ModifyUserReq modifyUserReq, ModifyUserReply modifyUserReply) {
        if (null == modifyUserReq) {
            return false;
        }
        if (null == modifyUserReq.getUid()) {
            return false;
        }
        return true;
    }

    private Boolean validationSendMobileMsg(ValidateMobileMsgCodeReq validateMobileMsgCodeReq, ValidateMobileMsgCodeReply validateMobileMsgCodeReply) {
        if (null == validateMobileMsgCodeReq) {
            return false;
        }
        if (!ValidateUtils.validateMobile("" + validateMobileMsgCodeReq.getMobile()) || StringUtils.isBlank(validateMobileMsgCodeReq.getCode())) {
            return false;
        }
        return true;
    }

    private Boolean validationResetPwd(ResetPwdReq resetPwdReq, ResetPwdReply resetPwdReply) {
        if (null == resetPwdReq) {
            return false;
        }
        if (!ValidateUtils.validateMobile("" + resetPwdReq.getMobile()) || StringUtils.isBlank(resetPwdReq.getCode()) || StringUtils.isBlank(resetPwdReq.getPassword())) {
            return false;
        }
        return true;
    }

    private boolean validationModifyMobile(ModifyMobileReq modifyMobileReq, ModifyMobileReply modifyMobileReply) {
        if (null == modifyMobileReq) {
            return false;
        }
        if (!ValidateUtils.validateMobile("" + modifyMobileReq.getMobile()) || StringUtils.isBlank(modifyMobileReq.getCode()) || null == modifyMobileReq.getUid()) {
            return false;
        }
        return true;
    }

    private boolean validationModifyPwd(ModifyPwdReq modifyPwdReq, ModifyPwdReply modifyPwdReply) {
        if (null == modifyPwdReq) {
            return false;
        }

        if (  StringUtils.isBlank(modifyPwdReq.getOldPwd()) || StringUtils.isBlank(modifyPwdReq.getNewPwd())) {
            return false;
        }
        return true;
    }
}
