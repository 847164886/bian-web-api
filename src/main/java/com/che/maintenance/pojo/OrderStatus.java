package com.che.maintenance.pojo;

/**
 * 0-待付款，1-付款中，2-已付款，3-退款中，4-已退款,5-已关闭
 *
 */
public enum OrderStatus {

	UNPAY(0),
	
	PAYING(1),
	
	PAYED(2),
	
	REFUNDING(3),
	
	REFUNDED(4),
	
	CLOSED(5);
	
	public int value;

	private OrderStatus(int value) {

		this.value = value;

	}

	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * 0-待付款，1-查询中，2-查询成功（未查看），3-查询成功（已查看），4-查询失败（退款中），5-查询失败（已退款），6-已关闭
	 */
	public static enum OrderCombStatus {
		
		UNPAY(0),
		
		QUERYING(1),
		
		QUERYSUC_NOLK(2),
		
		QUERYSUC_LKED(3),
		
		QUERYFAL_REFUING(4),
		
		QUERYFAL_REFUED(5),
		
		CLOSED(6);
		
		public int value;

		private OrderCombStatus(int value) {

			this.value = value;

		}

		public int getValue() {
			return value;
		}


		public void setValue(int value) {
			this.value = value;
		}
	}
}
