package com.che.baseinfo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.che.baseinfo.pojo.BaseInfoReply;
import com.che.baseinfo.pojo.BaseInfoReply.CarModelDTO;
import com.che.baseinfo.pojo.BaseInfoReply.CarSeriesDTO;
import com.che.baseinfo.pojo.BaseInfoReply.CityPojo;
import com.che.baseinfo.pojo.BaseInfoReq;
import com.che.common.web.Constants;
import com.che.search.valuation.api.IBasicInfoService;
import com.che.search.valuation.entity.CarBrand;
import com.che.search.valuation.entity.CarModel;
import com.che.search.valuation.entity.CarSeries;
import com.che.search.valuation.entity.City;

@Service
public class BasicInfoServiceImpl implements BasicInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(BasicInfoServiceImpl.class);

	@Reference(version = "1.0.0")
	private IBasicInfoService iBasicInfoService;

	/**
	 * 车辆品牌查询
	 */
	@Override
	public BaseInfoReply getCarBrands(BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = new BaseInfoReply();

		// 调用车辆品牌查询接口
		List<CarBrand> carBrands = iBasicInfoService.getCarBrands();
		baseInfoReply.setCarBrands(carBrands);
		baseInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		baseInfoReply.setMessage("车辆品牌查询成功");
		return baseInfoReply;
	}

	/**
	 * 车辆系列查询
	 */
	@Override
	public BaseInfoReply getCarSeries(BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = new BaseInfoReply();
		String brand_id = baseInfoReq.getBrand_id(); // brand_id
		// check参数
		if (brand_id == null || "".equals(brand_id)) {
			baseInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			baseInfoReply.setMessage("参数不能为空");
			logger.error("参数有误，查询参数：" + baseInfoReq.toString());
			return baseInfoReply;
		}
		
		// 调用车辆系列查询接口
		List<CarSeries> carSeries = iBasicInfoService.getCarSeries(brand_id);
		
		// 根据系列的组名进行分组处理（如奥迪下面分“一汽奥迪”，“进口奥迪”）
		Map<String, String> groupMap = new HashMap<String, String>();
		List<CarSeriesDTO> carSeriesDTOList = new ArrayList<CarSeriesDTO>();
		
		if (null != carSeries && carSeries.size() > 0) {
			for (CarSeries series : carSeries) {
				if (!groupMap.containsKey(series.getSeries_group_name())) {
					groupMap.put(series.getSeries_group_name(), series.getSeries_group_name());
				}
			}
			
			BeanCopier copy = BeanCopier.create(CarSeries.class, CarSeriesDTO.class, false);
			for (String str : groupMap.keySet()) {
				
				CarSeriesDTO _carSeriesDTO = new CarSeriesDTO();
				_carSeriesDTO.setGid(1);
				_carSeriesDTO.setSeries_group_name(groupMap.get(str));
				carSeriesDTOList.add(_carSeriesDTO);
				
				for (CarSeries series : carSeries) {
					String series_group_name = series.getSeries_group_name();
					CarSeriesDTO carSeriesDTO = new CarSeriesDTO();
					if (series_group_name != null && series_group_name.equals(groupMap.get(str))) {
						copy.copy(series, carSeriesDTO, null);
						carSeriesDTO.setGid(2);
						carSeriesDTOList.add(carSeriesDTO);
					}
				}
			}
			
		}
		
		baseInfoReply.setCarSeries(carSeriesDTOList);
		baseInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		baseInfoReply.setMessage("车辆系列查询成功");
		return baseInfoReply;
	}

	/**
	 * 车辆型号查询
	 */
	@Override
	public BaseInfoReply getCarModels(BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = new BaseInfoReply();
		String series_id = baseInfoReq.getSeries_id(); // series_id
		// check参数
		if (series_id == null || "".equals(series_id)) {
			baseInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			baseInfoReply.setMessage("参数不能为空");
			logger.error("参数有误，查询参数：" + baseInfoReq.toString());
			return baseInfoReply;
		}
		
		// 调用车辆型号查询接口
		List<CarModel> carModels = iBasicInfoService.getCarModels(series_id);
		
		Map<String, String> groupMap = new HashMap<String, String>();
		List<CarModelDTO> carModelDTOList = new ArrayList<CarModelDTO>();
		
		if (null != carModels && carModels.size() > 0) {
			for (CarModel model : carModels) {
				if (!groupMap.containsKey(model.getModel_year())) {
					groupMap.put(model.getModel_year(), model.getModel_year());
				}
			}
			
			BeanCopier copy = BeanCopier.create(CarModel.class, CarModelDTO.class, false);
			for (String str : groupMap.keySet()) {
				
				CarModelDTO _carModelDTO = new CarModelDTO();
				_carModelDTO.setGid(1);
				_carModelDTO.setGear_type(groupMap.get(str) + "款");
				carModelDTOList.add(_carModelDTO);
				
				for (CarModel model : carModels) {
					String model_year = model.getModel_year();
					CarModelDTO carModelDTO = new CarModelDTO();
					if (model_year != null && model_year.equals(groupMap.get(str))) {
						copy.copy(model, carModelDTO, null);
						carModelDTO.setGid(2);
						carModelDTOList.add(carModelDTO);
					}
				}
			}
			
		}
		
		
		baseInfoReply.setCarModels(carModelDTOList);
		baseInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		baseInfoReply.setMessage("车辆型号查询成功");
		return baseInfoReply;
	}

	// 城市列表接口
	@Override
	public BaseInfoReply getCity() {
		
		BaseInfoReply baseInfoReply = new BaseInfoReply();
		
		Long reqDate = getStartTime();
		
		List<City> cities = iBasicInfoService.getCity(reqDate);
		
		List<CityPojo> cityPojos = new ArrayList<CityPojo>();
		BeanCopier beanCopier = BeanCopier.create(City.class, CityPojo.class, false);
		
		if (cities != null) {
			for (City city : cities) {
				CityPojo cityPojo = new CityPojo();
				beanCopier.copy(city, cityPojo, null);
				cityPojos.add(cityPojo);
			}
		}
		
		baseInfoReply.setCityPojos(cityPojos);
		baseInfoReply.setMessage("获取城市列表成功");
		baseInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		return baseInfoReply;
	}
	
	private Long getStartTime(){  
        Calendar todayStart = Calendar.getInstance();  
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MINUTE, 0);   
        return todayStart.getTime().getTime();  
    }  
	
}
