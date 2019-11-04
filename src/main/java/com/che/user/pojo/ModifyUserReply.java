package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class ModifyUserReply extends Reply {
    private static final long serialVersionUID = -6333489916896022563L;

    private String avatar; 
}
