package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class ValidateMobileMsgCodeReq extends Req {
    private static final long serialVersionUID = 7530272491723798861L;
    
    private Long mobile;    // 手机号
    private String code;    // 手机验证码
}
