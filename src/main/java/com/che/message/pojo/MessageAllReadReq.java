package com.che.message.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageAllReadReq extends Req {

	private static final long serialVersionUID = -4579318090585495691L;

}
