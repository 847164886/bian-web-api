package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;
@Data
@EqualsAndHashCode(callSuper=false)
public class UserBusinessCertifyReq extends Req {
 
	private static final long serialVersionUID = -2854746365971436694L;

	private String name  ; 	   //企业名称
	
	private String city  ;	   //城市
	
	private String enterpriseNumber;  //营业执照号码 
 
}
