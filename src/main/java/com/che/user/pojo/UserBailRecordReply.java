package com.che.user.pojo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class UserBailRecordReply extends Reply{
 
	private static final long serialVersionUID = -9001394341622529726L;

	public List<UserBailRecordPojo> userBailRecordPojoList;
	
	@Data
	public static class UserBailRecordPojo{
		private Integer type		;			//int(11) NULL1:充值，2：消费
		private String price	;			//decimal(10,2) NULL金额
		private Date addTime		;			//datetime NULL
	}
	
}
