package com.che.user.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Req;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserImageUploadReq extends Req{
 
	private static final long serialVersionUID = 5799444976653644281L;

	private Integer sort; //1 身份证正面照或营业执照证件照,2身份证反面照或组织机构代码证件照,3身份证手持照或授权委托书
	
	private String image;  //图片编码 
	
	private String pic_type;  //图片的后缀名
	
}
