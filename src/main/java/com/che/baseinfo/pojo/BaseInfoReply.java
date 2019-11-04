package com.che.baseinfo.pojo;

import java.util.List;

import com.che.common.web.Reply;
import com.che.search.valuation.entity.CarBrand;
import com.che.search.valuation.entity.CarModel;
import com.che.search.valuation.entity.CarSeries;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseInfoReply extends Reply {

	private static final long serialVersionUID = 6388319712486197402L;

	private List<CarBrand> carBrands; // 车辆品牌

	private List<CarSeriesDTO> carSeries; // 车辆系列

	private List<CarModelDTO> carModels; // 车辆型号
	
	private List<CityPojo> cityPojos;
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class CarSeriesDTO extends CarSeries {
		
		private static final long serialVersionUID = -4160930779808560476L;
		
		private Integer gid; 
		
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class CarModelDTO extends CarModel {
		
		private static final long serialVersionUID = 7685034002977712913L;
		
		private Integer gid; 
	}
	
	@Data
	public static class CityPojo {
		
		private String city_id; // 城市id
		private String city_name; // 城市名称
		private String initial; // 城市拼音首字母缩写
	}

}
