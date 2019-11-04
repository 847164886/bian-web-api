package com.che.common.httpclient.cxyCx580;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.che.common.httpclient.HttpsRequest;
import com.che.common.httpclient.ViolationHttpUtils;
import com.che.common.httpclient.cxyCx580.pojo.CFTOrderResult;
import com.che.common.httpclient.cxyCx580.pojo.CFTQueryResult;
import com.che.common.httpclient.cxyCx580.pojo.CFTQueryResult.CFTRecord;
import com.che.common.httpclient.cxyCx580.pojo.CFTRecords;
import com.che.common.httpclient.cxyCx580.pojo.InputOrdersRulesResult;
import com.che.common.httpclient.cxyCx580.pojo.InputsConditionProvince;
import com.che.common.httpclient.cxyCx580.pojo.TrafficwzOrderData;
import com.che.common.web.GlobalInfo;

public class CxyQueryHandler {

	private static Logger log = LogManager.getLogger(CxyQueryHandler.class);
	
	private static String ACCOUNT = GlobalInfo.getInstance().cxyaccount;
	
	private static String URL = GlobalInfo.getInstance().cxyurl;
	
	private static String SECRET = GlobalInfo.getInstance().cxysecret;
	
	/*private static String ACCOUNT = "huanxin2016";
	private static String  SECRET= "BuJGfeaDFD13GrkhSG10tQ==";
	private static String URL = "http://chaxun.cx580.com:9008";*/
	
	private static String CFTQUERYINDEX= "/CFTQueryindex.aspx";
	private static String INPUTSCONDITION=  "/QueryAndOrderRules.aspx";
	private static String INPUTORDERSRULES = "/InputOrdersRules.aspx";
	private static String  GATEWAY =  "/gateway.aspx";
	//车行易下单接口
	public static TrafficwzOrderData  CFTOrder(LinkedHashMap<String,String> dataMap){
		TrafficwzOrderData trafficwzOrderData= new TrafficwzOrderData();
		//下单
		dataMap.put("method_type", "save");
		//dataMap.put("carnumber", "粤AX919Y");
		//dataMap.put("carengine", "1234");
		//dataMap.put("carcode", "123456");
		//dataMap.put("order_details", "[{\"TempID\":889346},{\"TempID\":889349}]");
		dataMap.put("is_post", "false");
		//dataMap.put("order_config", "{ Name : \"张三\", Phone : \"13333333333\", Address:\"\", CardNo:\"\"}");
		//dataMap.put("drivingId", "");
		//dataMap.put("driverId", "");
		dataMap.put("is_use_activeprice", "0");
		dataMap.put("is_order_detail", "1");

		//注意：method_type = save ，代表下单，如果调用其他接口，请按文档中的说明上送不同的参数
		String result="";
		try {
			result = ViolationHttpUtils.sendAndReceive(dataMap, URL+GATEWAY);
		} catch (Exception e) {
			trafficwzOrderData.setError_code(-1);
			trafficwzOrderData.setErr_message("下单失败");
			return trafficwzOrderData;
		}
		System.out.println(result);
		log.info("result="+result);
		if(!"".equals(result)){
			CFTOrderResult  CFTOrderResult = JSON.parseObject(result,CFTOrderResult.class);
			if(null!=CFTOrderResult &&null!=CFTOrderResult.getData() && 0==CFTOrderResult.getCode()){
				trafficwzOrderData.setId(CFTOrderResult.getData().getId());
				trafficwzOrderData.setOrderTotal(CFTOrderResult.getData().getOrderTotal());
				trafficwzOrderData.setPreOrderTotal(CFTOrderResult.getData().getPreOrderTotal());
				trafficwzOrderData.setDetail(CFTOrderResult.getData().getDetails());
				trafficwzOrderData.setError_code(CFTOrderResult.getCode());
			}else{
				trafficwzOrderData.setError_code(-1);
				trafficwzOrderData.setErr_message(StringUtils.isBlank(CFTOrderResult.getErrormsg())?"下单失败":CFTOrderResult.getErrormsg());
			}
		}
		
		return trafficwzOrderData;
		
	}
	
	//车行易查询接口
	public static CFTRecords CFTQuery(Map<String,String> paramMap){
		CFTRecords cFTRecords = new CFTRecords();
		paramMap.put("userid", ACCOUNT);
		paramMap.put("userpwd", SECRET);
		String result = HttpsRequest.sentGet(URL+CFTQUERYINDEX,paramMap);
		log.info("result="+result);
		if(!"".equals(result)){
			CFTQueryResult cFTQueryResult = JSON.parseObject(result,  CFTQueryResult.class);
			log.info("cFTQueryResult="+cFTQueryResult);
			if(null!=cFTQueryResult&&cFTQueryResult.getSuccess()&&cFTQueryResult.getErrorCode()==0){
				cFTRecords.setCFTRecords(cFTQueryResult.getRecords()!=null?cFTQueryResult.getRecords():new ArrayList<CFTRecord>());
				cFTRecords.setError_code("0");
			}else{
				cFTRecords.setError_code("-1");
				if(null!=cFTQueryResult){
					String errMessage = "";
					if(!cFTQueryResult.getSuccess()){
						log.info("查询违章通讯未成功！");
						errMessage = "查询失败！";
					}
					if(cFTQueryResult.getErrorCode()!=0){
						switch(cFTQueryResult.getErrorCode()){
							case -1: errMessage= cFTQueryResult.getErrMessage(); break; 
							case -3 : errMessage= "暂不提供该城市违章查询"; break;     			
							case -6 :  errMessage= "您输入信息有误  " ;   break;   			
							case -61: errMessage= "输入车牌号有误";  break;  					
							case -62:  errMessage= "输入车架号有误";  break;  								
							case -63: errMessage= "输入发动机号有误";  break;  		
							default : errMessage= "查询失败";break;
						}
						log.info(cFTQueryResult.getErrMessage());
					}
					cFTRecords.setErr_message(errMessage);
				}
			}
		}else{
			cFTRecords.setError_code("-1");
		}
		return cFTRecords ;
	}
	
	public static List<InputsConditionProvince> InputsCondition(){
		List<InputsConditionProvince> list = new  ArrayList<InputsConditionProvince>();
	
		String result = HttpsRequest.sentGetNoParam(URL+INPUTSCONDITION);
		if(!"".equals(result)){
			list = JSON.parseArray(result, InputsConditionProvince.class);
		}
		//System.out.println(list);
		return list;
	}
	
	public static InputOrdersRulesResult InputOrdersRules(){
		InputOrdersRulesResult inputOrdersRulesResult = new  InputOrdersRulesResult();
	
		String result = HttpsRequest.sentGetNoParam(URL+INPUTORDERSRULES);
		if(!"".equals(result)){
			inputOrdersRulesResult = JSON.parseObject(result, InputOrdersRulesResult.class);
		}
		System.out.println(inputOrdersRulesResult);
		return inputOrdersRulesResult;
	}
	
	public static void main(String[] args) {
		/*Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("carnumber", "苏A7ER30");
		paramMap.put("cardrivenumber", "CB0234");
		CxyQueryHandler.CFTQuery(paramMap);*/
		//CxyQueryHandler.InputsCondition();
	//	CxyQueryHandler.InputOrdersRules();
	/*	Map<String,String> paramMap = new HashMap<String,String>();
		CxyQueryHandler.CFTOrder(paramMap);*/
		
	}
}
