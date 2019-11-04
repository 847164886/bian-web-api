package com.che.message.pojo;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageListReply extends Reply {

	private static final long serialVersionUID = -3452446116442195804L;

	private List<MessageListPojo> messageListPojoList;
	@Data
	public static class MessageListPojo{
		private Long id			;				//bigint(20) NOT NULL
		private Integer type	;				//tinyint(4) NULL消息类型1公告、2竞价开始提醒、3中标通知、4预约通知、5签约通知、6付款通知、7办证通知、8退费通知
		private String content	;				//text NULL消息内容
		private Date create_time;				//datetime NULL创建时间
		private Integer read     ;				// 0未读1已读
	}
	
}
