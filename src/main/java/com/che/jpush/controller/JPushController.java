package com.che.jpush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.web.Constants;
import com.che.jpush.pojo.JPushReply;
import com.che.jpush.pojo.JPushReq;
import com.che.jpush.service.JPushService;

@RestController
@RequestMapping("/jpush")
public class JPushController {

	@Autowired
	private JPushService jPushService;

	/**
	 * 推送开关接口
	 * @param jPushReq
	 * @return
	 */
	@RequestMapping("/jpushState")
	public JPushReply jpushState(@RequestBody(required = false) JPushReq jPushReq) {
		JPushReply pushReply = jPushService.updateJPushState(jPushReq);
		pushReply.setResultCode(Constants.RESULT_SUCCESS);
		return pushReply;
	}

	// @RequestMapping("/alias")
	// public JPushReply getAlias(@RequestBody(required=false) JPushReq
	// jPushReq) throws Exception{

	// JPushReply jPushReply = jPushService.getAlias(jPushReq);
	// jPushReply.setResultCode(Constants.RESULT_SUCCESS);
	// iPushService.auctionSynPush(null);
	// return null;
	// }

}
