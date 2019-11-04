package com.che.config.security;

import org.apache.shiro.authc.UsernamePasswordToken;

import com.che.common.web.Constants;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper=false)
public class WswyOpenQQAuthenticationToken extends UsernamePasswordToken{
	private static final long serialVersionUID = 1651221936196068995L;
	
	private String openqq_id = null;    // QQ openid

	@Setter(AccessLevel.NONE)
	private Integer account_type = Constants.ACCOUNT_TYPE_OPEN_QQ;    // 帐户类型
	
    public WswyOpenQQAuthenticationToken(String openqq_id, String password, boolean rememberMe) {
        super(openqq_id, password,  rememberMe);
        this.openqq_id = openqq_id;
    }
}