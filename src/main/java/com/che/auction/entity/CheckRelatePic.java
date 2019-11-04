package com.che.auction.entity;

import lombok.Data;

@Data
public class CheckRelatePic {
	private Long order_id;
	private Integer category_id;
	private String pic_path;
}
