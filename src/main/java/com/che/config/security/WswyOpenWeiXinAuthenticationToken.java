package com.che.config.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.che.common.web.Constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class WswyOpenWeiXinAuthenticationToken extends UsernamePasswordToken{
	private static final long serialVersionUID = -2191972443695462804L;

	private String     openwx_id = null;			                          // 微信openid

	@Setter(AccessLevel.NONE)
	private Integer    account_type = Constants.ACCOUNT_TYPE_OPEN_WEIXIN;         // 帐户类型
	
    public WswyOpenWeiXinAuthenticationToken(String openwx_id, String password, boolean rememberMe) {
        super(openwx_id, password,  rememberMe);
        this.openwx_id = openwx_id;
    }
}