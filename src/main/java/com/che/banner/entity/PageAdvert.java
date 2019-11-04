package com.che.banner.entity;

import java.util.Date;

import lombok.Data;

@Data
public class PageAdvert {
	
	private Long id		;		//	bigint(20) NOT NULL主键ID
	private String title	;		//	varchar(255) NULL标题
	private String pic		;		//	varchar(100) NULL图片
	private String pic2		;		//varchar(100) NULL图片2
	private String pic3		;		//varchar(100) NULL图片3
	private String link_url	;		//varchar(100) NULL链接地址
	private String groupkey	;		//varchar(50) NULL标记key
	private String source	;		//	varchar(255) NULL显示位置（app、pc、wap）
	private Integer sort		;		//int(3) NULL排序值
	private Integer status	;		//	int(1) NULL状态（0:在用，-1： 不使用）
	private String position	;		//varchar(20) NULL位置
	private Date addTime	;		//	datetime NULL添加时间
	private Date updateTim;		//	datetime NULL更新时间
	private Long creater	;		//	bigint(20) NOT NULL创建者
	private Long updater	;		//	bigint(20) NOT NULL更新者
	private Integer cityid	;		//	int(11) NULL城市ID
}
