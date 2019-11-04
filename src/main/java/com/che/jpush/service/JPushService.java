package com.che.jpush.service;

import com.che.jpush.entity.JPushEntity;
import com.che.jpush.pojo.JPushReply;
import com.che.jpush.pojo.JPushReq;

public interface JPushService {

//	public JPushReply getAlias(JPushReq jPushReq);
	
	public JPushEntity bindingsJPush(Long uid,String alias);
	
	public JPushReply updateJPushState(JPushReq jPushReq);
 
//	public void updateJPush(Integer version_code,Long alias);
	
}
