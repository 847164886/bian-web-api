package com.che.banner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.banner.pojo.PageAdvertListReply;
import com.che.banner.pojo.PageAdvertListReq;
import com.che.banner.service.PageAdvertService;
import com.che.common.web.Constants;

@RestController
@RequestMapping("/advert")
public class PageAdvertController {

	@Autowired
	public PageAdvertService pageAdvertService;
	
	@RequestMapping("/list")
	public PageAdvertListReply list(@RequestBody(required=false) PageAdvertListReq pageAdvertSelectReq){
		
		PageAdvertListReply pageAdvertListReply = pageAdvertService.list(pageAdvertSelectReq);
		pageAdvertListReply.setResultCode(Constants.RESULT_SUCCESS);
		return pageAdvertListReply;
	}
	
}
