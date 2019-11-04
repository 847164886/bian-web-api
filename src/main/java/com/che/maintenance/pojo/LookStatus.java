package com.che.maintenance.pojo;

/**
 * 查看状态，0-已查看，1-未查看
 * @author 
 *
 */
public enum LookStatus {

	LOOKED(0),
	
	UNLOOK(1);
	
	public int value;

	private LookStatus(int value) {
		this.value = value;
	}
	
	
}
