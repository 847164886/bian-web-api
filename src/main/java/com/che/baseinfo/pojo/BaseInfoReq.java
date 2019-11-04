package com.che.baseinfo.pojo;

import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseInfoReq extends Req {

	private static final long serialVersionUID = -8900113833942890829L;

	private String brand_id; // 品牌id

	private String series_id; // 系列id

}
