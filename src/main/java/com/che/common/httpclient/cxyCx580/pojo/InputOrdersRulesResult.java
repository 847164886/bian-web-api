package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

@Data
public class InputOrdersRulesResult {
	private List<Province> Provencs ;

	private String Title;
	
	@Data
	public static class Province {
		private String ProvencId;

		private String ProvenceName;

		private String CarNumberLen;

		private String CarCodeLen;

		private String CarDriveLen;

		private String CarOwnerLen;

		private String DrivingLicense;

		private String MajorViolation;

		private String OwnerCardLen;
	}
}
