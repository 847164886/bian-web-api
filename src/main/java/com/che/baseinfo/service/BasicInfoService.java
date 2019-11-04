package com.che.baseinfo.service;

import com.che.baseinfo.pojo.BaseInfoReply;
import com.che.baseinfo.pojo.BaseInfoReq;

public interface BasicInfoService {

	BaseInfoReply getCarBrands(BaseInfoReq baseInfoReq);

	BaseInfoReply getCarSeries(BaseInfoReq baseInfoReq);

	BaseInfoReply getCarModels(BaseInfoReq baseInfoReq);
	
	BaseInfoReply getCity();

}
