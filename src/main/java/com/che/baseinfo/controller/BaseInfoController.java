package com.che.baseinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.baseinfo.pojo.BaseInfoReply;
import com.che.baseinfo.pojo.BaseInfoReq;
import com.che.baseinfo.pojo.CityInfoReq;
import com.che.baseinfo.service.BasicInfoService;
import com.che.common.web.Constants;
import com.che.common.web.Req;

/**
 * 基础信息相关接口
 * 
 * @author wangzhen
 *
 */
@RestController
@RequestMapping("/baseinfo")
public class BaseInfoController {

	@Autowired
	private BasicInfoService basicInfoService;

	/**
	 * 车辆品牌接口
	 */
	@RequestMapping("/carBrands")
	public BaseInfoReply carBrands(@RequestBody(required = false) BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = basicInfoService.getCarBrands(baseInfoReq);
		baseInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return baseInfoReply;
	}

	/**
	 * 车辆系列接口
	 */
	@RequestMapping("/carSeries")
	public BaseInfoReply carSeries(@RequestBody(required = false) BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = basicInfoService.getCarSeries(baseInfoReq);
		baseInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return baseInfoReply;
	}

	/**
	 * 车辆型号接口
	 */
	@RequestMapping("/carModels")
	public BaseInfoReply carModels(@RequestBody(required = false) BaseInfoReq baseInfoReq) {
		BaseInfoReply baseInfoReply = basicInfoService.getCarModels(baseInfoReq);
		baseInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return baseInfoReply;
	}
	
	/**
	 * 城市列表接口
	 */
	@RequestMapping("/cities")
	public BaseInfoReply cities(@RequestBody(required = false) CityInfoReq cityInfoReq) {
		BaseInfoReply baseInfoReply = basicInfoService.getCity();
		baseInfoReply.setResultCode(Constants.RESULT_SUCCESS);
		return baseInfoReply;
	}
}
