package com.che.config.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.che.common.web.Constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class WswyOpenWeiBoAuthenticationToken extends UsernamePasswordToken{
	private static final long serialVersionUID = -2397592419263107921L;

	private String     openwb_id = null;			                          // 微博openid
	
	@Setter(AccessLevel.NONE)
	private Integer    account_type = Constants.ACCOUNT_TYPE_OPEN_WEIBO;         // 帐户类型
	
    public WswyOpenWeiBoAuthenticationToken(String openwb_id, String password, boolean rememberMe) {
        super(openwb_id, password,  rememberMe);
        this.openwb_id = openwb_id;
    }
    
}