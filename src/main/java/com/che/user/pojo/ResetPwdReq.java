package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResetPwdReq extends Req {
    private static final long serialVersionUID = -5204909387232488185L;

    private Long mobile;     // 手机号
    private String code;     // 手机验证码
    private String password; // 新的密码
}
