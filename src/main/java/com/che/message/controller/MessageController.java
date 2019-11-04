package com.che.message.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.common.web.Constants;
import com.che.message.pojo.MessageAllReadReply;
import com.che.message.pojo.MessageAllReadReq;
import com.che.message.pojo.MessageInfoReply;
import com.che.message.pojo.MessageInfoReq;
import com.che.message.pojo.MessageListReply;
import com.che.message.pojo.MessageListReq;
import com.che.message.service.MessageService;

@RestController
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@RequestMapping("/list")
	public MessageListReply list(@RequestBody(required=false) MessageListReq messageListReq){

		MessageListReply messageListReply =  messageService.list(messageListReq);
		messageListReply.setResultCode(Constants.RESULT_SUCCESS);
		return messageListReply;
		
	}
	
	@RequestMapping("/info")
	public MessageInfoReply info(@RequestBody(required=false) MessageInfoReq messageInfoReq){

		MessageInfoReply messageInfoReply =  messageService.info(messageInfoReq);
		messageInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return messageInfoReply;
		
	}
	
	@RequestMapping("/allRead")
	public MessageAllReadReply allRead(@RequestBody(required=false) MessageAllReadReq messageAllReadReq){

		MessageAllReadReply messageAllReadReply =  messageService.allRead(messageAllReadReq);
		messageAllReadReply.setResultCode(Constants.RESULT_SUCCESS);
		return messageAllReadReply;
		
	}
	
	
}
