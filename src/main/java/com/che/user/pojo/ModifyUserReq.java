package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModifyUserReq extends Req {
    private static final long serialVersionUID = -3909440507615564032L;
    
    private String avatar;
    private String pic_type;         //头像的后缀名
    private Integer gender;
    private String city;
    
}
