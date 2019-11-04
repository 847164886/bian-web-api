package com.che.auction.entity;

import lombok.Data;

/**
 * 车损明细
 * 
 * @author Administrator
 *
 */
@Data
public class CheckTypeReportDetail {
	private Integer type_id;
	private Integer item_id;
	private String item_name;
	private Integer special_id;
	private String special_name;
	private Integer special_status;
}
