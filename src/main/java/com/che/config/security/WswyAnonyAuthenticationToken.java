package com.che.config.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.che.common.web.Constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class WswyAnonyAuthenticationToken extends UsernamePasswordToken{
	private static final long serialVersionUID = 2069529236432066496L;

	private String uuid;
	
	@Setter(AccessLevel.NONE)
	private Integer account_type = Constants.ACCOUNT_TYPE_ANONY;
	
    public WswyAnonyAuthenticationToken(Long user_id, String uuid, String password, boolean rememberMe) {
        super(user_id+"", password, rememberMe);// 匿名帐户用户Id
        this.uuid = uuid;
    }
}