package com.che.jpush.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.che.config.JPushConfig;
import com.che.jpush.mapper.JPushMapper;
import com.che.push.api.IPushService;
import com.che.push.param.AuctionSynPushReq;
import com.che.user.mapper.UserEntityMapper;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReceivedsResult;

@Service(version = "1.0.0")
public class IPushServceImpl implements IPushService {
	
	private static final Logger logger = LoggerFactory.getLogger(IPushServceImpl.class);
	
	@Autowired
	private JPushConfig jPushConfig;
	
	private JPushClient jpushClient;
	
	@Autowired
	private JPushMapper jPushMapper;
	
	@Autowired
	private UserEntityMapper userEntityMapper;
	
	@Override
	public void auctionSynPush(AuctionSynPushReq auctionSynPushReq) throws Exception {
		
		logger.info("调用推送接口参数：" + " title：" + auctionSynPushReq.getTitle() + "===========" + "content：" + auctionSynPushReq.getContent());
		try {
			
			List<List<String>> pushAliases = this.filtratePushAlias(Type.SYNC); // 需推送的所有alias
			
			 // For push, all you need do is to build PushPayload object.
			// check params
			if (pushAliases == null || auctionSynPushReq == null || auctionSynPushReq.getTitle() ==null || auctionSynPushReq.getContent() == null) {
				return;
			}
			
			for (List<String> pushAlias : pushAliases) {
				
				if (pushAlias == null) return;
				
				PushPayload payload = buildPushObject_ios_audienceMore_messageWithExtras(pushAlias, Type.SYNC, auctionSynPushReq);
				
				try {
					PushResult result = jpushClient.sendPush(payload);
					logger.info("推送成功：" + result.toString());
					
					ReceivedsResult receivedsResult = jpushClient.getReportReceiveds(result.msg_id + "");
					
					logger.debug("report：" + receivedsResult.received_list);
//					System.out.println("推送成功：" + result.toString());
				} catch (APIConnectionException e) {
					// Connection error, should retry later
					logger.error("Connection error, should retry later", e);
					continue;
					
				} catch (APIRequestException e) {
					// Should review the error, and fix the request
					logger.error("Should review the error, and fix the request", e);
					continue;
				}
			}
			
		} catch (Exception e) {
			logger.error("发生异常.", e);
			return;
		}
		
	}
	
	/**
	 * 推送参数设定
	 * @param aliases
	 * @param auctionSynPushReq
	 * @return
	 */
	private PushPayload buildPushObject_ios_audienceMore_messageWithExtras(List<String> aliases, Type type, AuctionSynPushReq auctionSynPushReq) {
		return PushPayload.newBuilder().setPlatform(Platform.android_ios())
				.setAudience(Audience.newBuilder().addAudienceTarget(AudienceTarget.alias(aliases)).build()).setNotification(Notification.newBuilder()
						.addPlatformNotification(IosNotification.newBuilder().setAlert(auctionSynPushReq.getTitle()+auctionSynPushReq.getContent())
						.setSound("").addExtra("type", type.toString()).build())
						.addPlatformNotification(AndroidNotification.newBuilder().setTitle(auctionSynPushReq.getTitle()).setAlert(auctionSynPushReq.getContent())
						.addExtra("type", type.toString()).build())
						.build())
				.setOptions(Options.newBuilder().setApnsProduction(jPushConfig.isApnsProduction()).setTimeToLive(jPushConfig.getTimeToLive()).build()).build();
	}
	
	/**
	 * 筛选车辆竞拍推送别名
	 * @return
	 */
	private List<List<String>> filtratePushAlias(Type type) throws Exception {
		
		// 获取所有手机号，即手机号作为alias
		List<String> mobiles = userEntityMapper.selectMobiles();
		
		// 1.过滤 main_switch_state = 0的alias
		List<String> mainSwitchOffAliases = jPushMapper.selectMainSwitchOffAliases();
		if (mainSwitchOffAliases != null && mobiles != null) {
			for (Iterator<String> it = mainSwitchOffAliases.iterator(); it.hasNext();) {
				
				String alias = it.next();
				
				for (Iterator<String> mit = mobiles.iterator(); mit.hasNext();) {
					
					if (mit.next().equals(alias)) {
						mit.remove();
						break;
					}
				}
			}
		}
		
		mainSwitchOffAliases = null;
		
		// 2.过滤 sync_switch_state = 0的alias
		if (type != null && type.equals(Type.SYNC)) {
			List<String> syncSwitchOffAliases = jPushMapper.selectSyncSwitchOffAliases();
			if (syncSwitchOffAliases != null && mobiles != null) {
				for (Iterator<String> it = syncSwitchOffAliases.iterator(); it.hasNext();) {
					
					String alias = it.next();
					
					for (Iterator<String> mit = mobiles.iterator(); mit.hasNext();) {
						
						if (mit.next().equals(alias)) {
							mit.remove();
							break;
						}
					}
				}
			}
		}
		
		// 极光推送有别名数限制，一次推送最多 1000 个
		List<List<String>> aliasesList = new ArrayList<List<String>>();
		for (int i=0; i < (new Double(Math.ceil(new Integer(mobiles.size()).doubleValue() / new Long(jPushConfig.getMaxAlias()).doubleValue())).intValue()); i++) {
			aliasesList.add(new ArrayList<String>());
		}
		
		int i=0; // 计数器
		for (int j = 0; j < aliasesList.size(); j++) {
			
			for (; i < mobiles.size(); i++) {
				aliasesList.get(j).add(mobiles.get(i));
				if ((i + 1) == (j + 1) * jPushConfig.getMaxAlias()) {
					i += 1;
					break;
				}
			}
		}
		
		return aliasesList;
	}
	
	private static enum Type {
		SYNC, SUBS // SYNC-车辆竞拍同步推送，SUBS-订阅信息推送
	}

	@PostConstruct
	public void initJpush() {
		jpushClient = new JPushClient(jPushConfig.getMasterSecret(), jPushConfig.getAppKey());
	}
}
