package com.che.message.entity;

import java.util.Date;

import lombok.Data;

@Data
public class AuctionMessageText {
 
	private Long id			;				//bigint(20) NOT NULL
	private Long send_id	;				//bigint(20) NULL发件者ID
	private Long rec_id		;				//bigint(20) NULL接信者ID（如为0，则接受者为所有人）
	private Integer type	;				//tinyint(4) NULL消息类型1公告、2竞价开始提醒、3中标通知、4预约通知、5签约通知、6付款通知、7办证通知、8退费通知
	private String content	;				//text NULL消息内容
	private Date start_time	;				//datetime NULL开始时间
	private Date end_time	;				//datetime NULL结束时间
	private Date create_time;				//datetime NULL创建时间
	private Long reference_id;				//关联ID 用于修改短信内容
	
	private Integer read     ;				// 0未读1已读
}
