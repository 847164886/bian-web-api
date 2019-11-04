package com.che.banner.pojo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class PageAdvertListReply extends Reply {
 
	private static final long serialVersionUID = 9061869704882307321L;

	private List<PageAdvertPojo> pageAdvertPojoList;
	
	@Data
	public static class PageAdvertPojo{
		private Long id		;		//	bigint(20) NOT NULL主键ID
		private String title	;		//	varchar(255) NULL标题
		private String pic		;		//	varchar(100) NULL图片
		private String link_url	;		//varchar(100) NULL链接地址
	}
	
}
