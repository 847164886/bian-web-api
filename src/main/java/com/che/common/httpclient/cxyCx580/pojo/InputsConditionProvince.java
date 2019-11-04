package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

@Data
public class InputsConditionProvince {
	private List<City> Cities ;

	private int ProvinceID;

	private String ProvinceName;

	private String ProvincePrefix;
	
	@Data
	public static class City {
		private int CityID;

		private String CityName;

		private String Name;

		private String CarNumberPrefix;

		private int CarCodeLen;

		private int CarEngineLen;

		private int CarOwnerLen;

		private int ProxyEnable;
	}
}
