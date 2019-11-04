package com.che.common.util;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.shcm.bean.SendResultBean;
import com.shcm.send.OpenApi;

/**
 * @author Chinafish
 *
 */
@Component
public class SendMessage
{
	@Value("${sms.sOpenUrl}")
	private String sOpenUrl ;
	
	@Value("${sms.sDataUrl}")
	private String sDataUrl ;
	
	// 接口帐号
	@Value("${sms.account}")
	private  String account ;
	
	// 接口密钥
	@Value("${sms.authkey}")
	private  String authkey;
	
	// 通道组编号
	@Value("${sms.cgid}")
	private  int cgid ;
	
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
/*	@Value("${sms.csid}")
	private  int csid;
 */
	public  List<SendResultBean> sendOnce(String mobile, String content) throws Exception
	{
		// 发送短信
		return OpenApi.sendOnce(mobile, content, 0, 0, null);
		//return OpenApi.sendOnce(new String[]{"18297974783","15102110086"}, "测试内容", 0, 0, null);
		//return OpenApi.sendBatch("18297974783,15102110086", "测试内容{|}测试内容", 0, 0, null);
		//return OpenApi.sendBatch(new String[]{"18297974783","15102110086"}, new String[]{"测试内容","测试内容"}, 0, 0, null);
		//return OpenApi.sendParam("18297974783,15102110086", "测试内容{p1}", new String[]{"a{|}b"}, 0, 0, null);
		//return OpenApi.sendParam(new String[]{"18297974783","15102110086"}, "测试内容{p1}", new String[]{"a{|}b"}, 0, 0, null);
	}
	
	@PostConstruct
	private void init(){
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, 0);	
	}
	
	/*public static void main(String[] args) throws Exception
	{
		// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		
		// 状态及回复参数
		//DataApi.initialzeAccount(sDataUrl, account, authkey);
		
		// 取帐户余额
		BalanceResultBean br = OpenApi.getBalance();
		if(br == null)
		{
			System.out.println("获取可用余额时发生异常!");
			return;
		}
		
		if(br.getResult() < 1)
		{
			System.out.println("获取可用余额失败: " + br.getErrMsg());
			return;
		}
		System.out.println("可用条数: " + br.getRemain());
		
		
		// 更新接口密钥
		UpdateResultBean ur = OpenApi.updateKey();
		if(ur.getResult() < 1)
		{
			System.out.println("更新接口密钥失败: " + br.getErrMsg());
			return;
		}
		System.out.println("已成功更新密钥,新接口密钥为: " + ur.getAuthKey());
		
		
		List<SendResultBean> listItem = sendOnce("18616328868", "Java接口测试内容2");
		if(listItem != null)
		{
			for(SendResultBean t:listItem)
			{
				
				if(t.getResult() < 1)
				{
					System.out.println("发送提交失败: " + t.getErrMsg());
					return;
				}
				
				System.out.println("发送成功: 消息编号<" + t.getMsgId() + "> 总数<" + t.getTotal() + "> 余额<" + t.getRemain() + ">");
			}
		}
		
		
		// 循环获取状态报告和回复
		while(true)
		{
			List<SendStateBean> listSendState = DataApi.getSendState();
			if(listSendState != null)
			{
				for(SendStateBean t:listSendState)
				{
					System.out.println("状态报告 => 序号<" + t.getId() + "> 消息编号<" + t.getMsgId() + "> 手机号码<" + t.getMobile() + "> 结果<" + (t.getStatus() > 1 ? "发送成功" : t.getStatus() > 0 ? "发送失败" : "未知状态") + "> 运营商返回<" + t.getDetail() + ">");
				}
			}
			
			// 取回复
			List<ReplyBean> listReply = DataApi.getReply();
			if(listReply != null)
			{
				for(ReplyBean t:listReply)
				{
					System.out.println("回复信息 => 序号<" + t.getId() + "> 消息编号<" + t.getMsgId() + "> 回复时间<" + t.getReplyTime() + "> 手机号码<" + t.getMobile() + "> 回复内容<" + t.getContent() + ">");
				}
			}
			Thread.sleep(6000);
		}
		
	}*/
}
