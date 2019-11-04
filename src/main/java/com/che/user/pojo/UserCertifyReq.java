package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;
@Data
@EqualsAndHashCode(callSuper=false)
public class UserCertifyReq extends Req {
 
	private static final long serialVersionUID = -2854746365971436694L;

	private String name  ; 	   //姓名 
	
	private String city  ;	   //城市
	
	private String id_number;  //身份证号码 
 
}
