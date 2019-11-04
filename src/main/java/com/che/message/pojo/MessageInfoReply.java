package com.che.message.pojo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageInfoReply  extends Reply { 
	
	private static final long serialVersionUID = -6579454235252133639L;

	private MessageInfoPojo messageInfoPojo;
	
	@Data
	public static class MessageInfoPojo{
		
		private Long id			;				//bigint(20) NOT NULL
		private Integer type	;				//tinyint(4) NULL消息类型1公告、2竞价开始提醒、3中标通知、4预约通知、5签约通知、6付款通知、7办证通知、8退费通知
		private String content	;				//text NULL消息内容
		private Date create_time;				//datetime NULL创建时间
		
	}
	
	
}
