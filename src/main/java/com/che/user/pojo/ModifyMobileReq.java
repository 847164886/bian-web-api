package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModifyMobileReq extends Req {
    private static final long serialVersionUID = 3254261523147930546L;
    private Long mobile;     // 手机号
    private String code;     // 手机验证码
}
