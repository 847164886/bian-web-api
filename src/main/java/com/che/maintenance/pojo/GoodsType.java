package com.che.maintenance.pojo;

/**
 * 商品类型
 * 1加油卡2罚款单代缴3查维保4充值
 * @author 
 *
 */
public enum GoodsType {
	
	GASFILLING(1),
	
	PAYBEFTIC(2),
	
	QUERYMAINT(3),
	
	RECHARGE(4);

	public int value;

	private GoodsType(int value) {
		this.value = value;
	}
	
	
}
