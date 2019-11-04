package com.che.maintenance.service;


import com.che.common.web.Reply;
import com.che.maintenance.pojo.MaintAccountPayReq;
import com.che.maintenance.pojo.MaintOrderReply;
import com.che.maintenance.pojo.MaintOrderReq;
import com.che.maintenance.pojo.MaintenanceInfoReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReply;
import com.che.maintenance.pojo.MaintenanceOrderCreateReq;
import com.che.maintenance.pojo.QueryReportReq;
import com.che.search.exception.CheSearchException;


public interface MaintenanceInfoService {
	
	MaintenanceOrderCreateReply createOrder(MaintenanceOrderCreateReq maintOrderCreateReq) throws Exception;
	
	Reply checkBrand(MaintenanceOrderCreateReq maintenanceOrderCreateReq);
	
	MaintOrderReply orderList(MaintOrderReq maintOrderReq);
	
	Reply accountPay(MaintAccountPayReq req) throws Exception;

	Reply buyReport(QueryReportReq queryReportReq) throws CheSearchException, Exception;

	MaintenanceInfoReply queryReport(QueryReportReq queryReportReq) throws CheSearchException, Exception;
	
//	void testRefund(QueryReportReq queryReportReq);
}
