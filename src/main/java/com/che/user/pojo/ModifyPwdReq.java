package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModifyPwdReq extends Req {
    private static final long serialVersionUID = -5344876031429435652L;

    private String oldPwd;
    private String newPwd;
    
}
