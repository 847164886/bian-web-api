package com.che.config.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.che.common.web.Constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class WswyMobileAuthenticationToken extends UsernamePasswordToken{
	private static final long serialVersionUID = -6190089497623075624L;
	private Long       mobile = null;				// 手机号 
	private String     code = null;                 // 手机验证码
	
	@Setter(AccessLevel.NONE)
	private Integer    account_type = Constants.ACCOUNT_TYPE_MOBILE;  // 帐户类型
	
    public WswyMobileAuthenticationToken(Long mobile, String password, boolean rememberMe) {
        super(mobile+"", password, rememberMe);
        this.mobile = mobile;
    }
}