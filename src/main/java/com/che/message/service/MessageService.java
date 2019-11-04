package com.che.message.service;

import com.che.message.pojo.MessageAllReadReply;
import com.che.message.pojo.MessageAllReadReq;
import com.che.message.pojo.MessageInfoReply;
import com.che.message.pojo.MessageInfoReq;
import com.che.message.pojo.MessageListReply;
import com.che.message.pojo.MessageListReq;

public interface MessageService {
	
	public MessageListReply list(MessageListReq messageListReq);
	
	public MessageInfoReply info( MessageInfoReq messageInfoReq);
	
	public MessageAllReadReply allRead( MessageAllReadReq messageAllReadReq);
	
}
