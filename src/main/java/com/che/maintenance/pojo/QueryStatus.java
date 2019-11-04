package com.che.maintenance.pojo;

/**
 * 报告查询状态
 * 查询状态（1查询中，2查询成功，4查询失败,5无维修保养记录，8第三方下单失败）
 * @author 
 *
 */
public enum QueryStatus {
	
	QUERYING(1),
	
	QUERYSUCC(2),
	
	QUERYFAIL(4),
	
	NO_MAINTEN_RECORD(5),
	
	THIRD_ORDER_FAIL(8);

	public int value;

	private QueryStatus(int value) {
		this.value = value;
	}
	
	
}
