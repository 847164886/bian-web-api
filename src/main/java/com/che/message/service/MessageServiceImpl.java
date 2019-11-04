package com.che.message.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.che.common.web.Constants;
import com.che.message.entity.AuctionMessage;
import com.che.message.entity.AuctionMessageText;
import com.che.message.mapper.AuctionMessageMapper;
import com.che.message.pojo.MessageAllReadReply;
import com.che.message.pojo.MessageAllReadReq;
import com.che.message.pojo.MessageInfoReply;
import com.che.message.pojo.MessageInfoReply.MessageInfoPojo;
import com.che.message.pojo.MessageInfoReq;
import com.che.message.pojo.MessageListReply;
import com.che.message.pojo.MessageListReply.MessageListPojo;
import com.che.message.pojo.MessageListReq;
import com.che.user.service.UserCommonService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private AuctionMessageMapper auctionMessageMapper;
	@Autowired
	private  UserCommonService userCommonService;
	@Override
	public MessageListReply list(MessageListReq messageListReq) {

		MessageListReply messageListReply =new MessageListReply();
	 
		List<MessageListPojo> messageListPojoList = Lists.newArrayList();
		PageHelper.startPage(messageListReq.getPage(), messageListReq.getPageSize());
		List<AuctionMessageText> messageTextList = auctionMessageMapper.selectList(userCommonService.getUserId() );
 
		Page<AuctionMessageText> page  =(Page<AuctionMessageText>) messageTextList;
		for(AuctionMessageText auctionMessageText:page.getResult()){
			MessageListPojo messageListPojo = new MessageListPojo();
			messageListPojo.setId(auctionMessageText.getId());
			messageListPojo.setContent(auctionMessageText.getContent());
			messageListPojo.setCreate_time(auctionMessageText.getCreate_time());
			messageListPojo.setRead(auctionMessageText.getRead());
			messageListPojo.setType(auctionMessageText.getType());
			messageListPojoList.add(messageListPojo);
		}
		
		messageListReply.setMessageListPojoList(messageListPojoList);
		messageListReply.setReplyCode(Constants.REPLY_SUCCESS);
		return messageListReply;
	}
	
	@Override
	public MessageInfoReply info(MessageInfoReq messageInfoReq) {

		MessageInfoReply messageInfoReply = new MessageInfoReply();
		if(null==messageInfoReq.getMessage_id()){
			messageInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			messageInfoReply.setMessage("参数不能为空");
			return messageInfoReply;
		}
		
		AuctionMessageText auctionMessageText = auctionMessageMapper.selectTextById(messageInfoReq.getMessage_id());
		
		if(null==auctionMessageText){
			messageInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			messageInfoReply.setMessage("该消息不存在");
			return messageInfoReply;
		}
		
		MessageInfoPojo  messageInfoPojo = new MessageInfoPojo();
		messageInfoPojo.setContent(auctionMessageText.getContent());
		messageInfoPojo.setCreate_time(auctionMessageText.getCreate_time());
		messageInfoPojo.setId(auctionMessageText.getId());
		messageInfoPojo.setType(auctionMessageText.getType());
		//设置成已读
		if(0==auctionMessageMapper.ifRead(messageInfoReq.getMessage_id(),userCommonService.getUserId())){
			AuctionMessage auctionMessage = new AuctionMessage();
			auctionMessage.setMessage_id(messageInfoReq.getMessage_id());
			auctionMessage.setRec_id(userCommonService.getUserId());
			auctionMessageMapper.insertRead(auctionMessage);
		} 
		messageInfoReply.setMessageInfoPojo(messageInfoPojo);
		messageInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		return messageInfoReply;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public MessageAllReadReply allRead(MessageAllReadReq messageAllReadReq) {
		MessageAllReadReply messageAllReadReply = new MessageAllReadReply();
		
		List<AuctionMessage>  auctionMessageList = auctionMessageMapper.selectUnreadList(userCommonService.getUserId());
		if(auctionMessageList.size()>0){
			for (AuctionMessage auctionMessage:auctionMessageList) {
				auctionMessage.setRec_id(userCommonService.getUserId());
			}
			auctionMessageMapper.allRead(auctionMessageList);
		}
		
		messageAllReadReply.setReplyCode(Constants.REPLY_SUCCESS);
		return messageAllReadReply;
	}
	
	

}
