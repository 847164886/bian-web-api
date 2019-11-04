package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResetPwdReply extends Reply {
    private static final long serialVersionUID = 38566624475139655L;

}
