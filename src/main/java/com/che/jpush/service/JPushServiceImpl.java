package com.che.jpush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.che.common.web.Constants;
import com.che.jpush.entity.JPushEntity;
import com.che.jpush.mapper.JPushMapper;
import com.che.jpush.pojo.JPushReply;
import com.che.jpush.pojo.JPushReq;
import com.che.user.service.UserCommonService;

@Service
public class JPushServiceImpl implements JPushService {

//	@Autowired
//	private IdWorker idWorker;
	
	@Autowired
	private JPushMapper jPushMapper;
	
	@Autowired
    private UserCommonService userCommonService;
	
	private static final Integer STATUS_OPEN = 1;
	
//	@Override
//	public JPushReply getAlias(JPushReq jPushReq) {
// 
//		JPushReply jPushReply = new JPushReply();
// 
//		Long alias = idWorker.nextId();
//		
//		JPushEntity jPushEntity = new JPushEntity();
//		jPushEntity.setAlias(alias);
//		jPushMapper.insert(jPushEntity);
//		
//		jPushReply.setAlias(alias);
//		jPushReply.setReplyCode(Constants.REPLY_SUCCESS);
//		
//		return jPushReply;
//	}

	//极光账户绑定 
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public JPushEntity bindingsJPush(Long uid,String alias){
		
		JPushEntity jPushByUid = jPushMapper.selectByUid(uid);
		//判断此用户是否已经绑定过
		if(null!=jPushByUid){
			//判断此用户是否绑定的激光账户是否需要更新
			if(alias != null && alias.equals(jPushByUid.getAlias())){
				//此别名已经绑定该账户
				return jPushByUid;
			}
			//更新账户别名绑定
//			Long uid = userCommonService.getUserId();
			if (alias != null) {
				jPushMapper.UpdateByUid(uid, alias);
			}
			return jPushMapper.selectByUid(uid);
		}
		//别名和账户绑定
		JPushEntity jPushEntity = new JPushEntity();
		jPushEntity.setUid(uid);
		jPushEntity.setAlias(alias);
		jPushEntity.setMain_switch_state(STATUS_OPEN);
		jPushEntity.setSync_switch_state(STATUS_OPEN);
		jPushMapper.insert(jPushEntity);
		return jPushEntity;
	}

	/**
	 * 更新开关状态
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public JPushReply updateJPushState(JPushReq jPushReq) {
		JPushReply jPushReply = new JPushReply();
		// check参数
		if (jPushReq.getMain_switch_state() == null || jPushReq.getSync_switch_state() == null) {
			jPushReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			jPushReply.setMessage("参数为空");
			return jPushReply;
		}
		
		// 用户是否绑定极光别名
		Long uid = userCommonService.getUserId();
		JPushEntity jPushByUid = jPushMapper.selectByUid(uid);
		JPushEntity jPushEntity = new JPushEntity();
		jPushEntity.setUid(uid);
		jPushEntity.setMain_switch_state(jPushReq.getMain_switch_state());
		jPushEntity.setSync_switch_state(jPushReq.getSync_switch_state());
		if (jPushByUid != null) {
			// 已绑定，更新开关状态
			jPushMapper.update(jPushEntity);
		} else {
			// 未绑定，保存开关状态
			jPushEntity.setAlias(userCommonService.getUser().getMobile());
			jPushMapper.insert(jPushEntity);
		}
		jPushReply.setReplyCode(Constants.REPLY_SUCCESS);
		jPushReply.setMessage("状态更新成功");
		return jPushReply;
	}
	
	//极光账户更新
//	public void updateJPush(Integer version_code,Long alias){
//		
//		JPushEntity jPush = new JPushEntity();
//		jPush.setAlias(alias);
//		jPush.setLast_login_time(Long.parseLong(MyDateUtils.convertDate2String(MyDateUtils.DATETIME_FORMAT_YYYYMMDDHHMMSS,new Date())));
//		jPush.setVersion_code(version_code);
//		jPushMapper.Update(jPush);
//		
//	}
	
}
