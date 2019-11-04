package com.che.common.httpclient.allinpay;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.che.common.httpclient.allinpay.pojo.OrderDetailResult;
import com.che.common.httpclient.allinpay.pojo.OrdersaveReturn;
import com.che.common.httpclient.allinpay.pojo.PayorderResult;
import com.che.common.httpclient.allinpay.pojo.Regulations;
import com.che.common.httpclient.allinpay.pojo.Regulations.Regulation;
import com.che.common.httpclient.allinpay.pojo.ViolationsDetail;
import com.che.common.httpclient.allinpay.pojo.ViolationsDetail.ViolationPrivateVoiture;
import com.che.common.web.GlobalInfo;

public class OffencesOnline {
	private static final Logger logger = LogManager.getLogger(OffencesOnline.class);
	// -------------------------------------以下为查询接口先关代码-----------------------------//
	//real
	private static String API_URL = GlobalInfo.getInstance().API_URL;
	private static String SECRET = GlobalInfo.getInstance().SECRET;;// 接口密钥
    private static String APP_KEY = GlobalInfo.getInstance().APP_KEY;;
//  	private static final String API_URL = "http://116.236.192.117:8080/aop/rest";
//   	private static final String SECRET = "test";// 接口密钥
//    private static final String APP_KEY = "test";
	
	/**
	 * 返回值的KEY
	 */
	private static final String RESULT_EXC = "exc";// 返回值是否有异常
	private static final String RESULT_CODE = "code";// 返回值代码
	private static final String RESULT_STR = "resultStr";// 服务器返回值(用来记录日志)
	private static final String RESULT_JSON = "resultJson";// 返回值JSON格式
	private static final String RESULT_ERRORMSG = "errorMsg";// 返回值JSON格式
	/**
	 * KEYS 请求服务器参数名称
	 */
	private static final String KEY_V = "v";// API协议版本号
	private static final String KEY_METHOD = "method";// API接口名称
	private static final String KEY_APP_KEY = "app_key";// 分配给应用的AppKey
	private static final String KEY_SIGN_V = "sign_v";// 签名版本号
	private static final String KEY_FORMAT = "format";// 指定响应格式。默认xml,目前支持格式为xml,json
	private static final String KEY_TIMESTAMP = "timestamp";// 时间戳，格式为yyyyMMddHHmmss
	private static final String KEY_SIGN = "sign";// API输入参数签名结果，使用 md5,dsa 加密
	private static final String KEY_ORDER_ID = "order_id";// 订单号
	private static final String KEY_DETAIL = "detail";// 违章内容和车辆信息
	private static final String KEY_TRANS_NO = "trans_no";// 通联交易号
	/**
	 * VALUES 请求服务器参数值(常量)
	 */
	private static final String V = "1.0";// API协议版本号为1.0

	// 分配给应用的AppKey // 测试号为test
	private static final String SIGN_V = "1";// 签名版本号目前1
	private static final String FORMAT = "json";//

	/**
	 * 服务器API接口名称
	 */
	private static final String VIOL_RULE = "allinpay.jtfk.agencyrule.get";// 违章代办规则查询
	private static final String VIOL_INFO = "allinpay.jtfk.violationsinfo.get";// 查询车辆违章信息
	private static final String ORDER_DETAIL = "allinpay.jtfk.orderdetail.get";// 获取违章的缴费信息
	private static final String ORDER_SAVE = "allinpay.jtfk.violationsordersave.add";// 保存违章订单
	private static final String ORDER_INFO = "allinpay.jtfk.orderlistinfo.get";// 单笔订单明细查询
	private static final String ORDER_CANCEL = "allinpay.jtfk.cancelorder.add";// 取消订单
	private static final String ORDER_PAY = "allinpay.jtfk.payorder.add";// 支付订单

	/**
	 * 查询违章信息
	 * 
	 * @param shopSign
	 *            车牌号
	 * @param engineNo
	 *            发动机号
	 * @param voitureNo
	 *            车架号
	 * @param carType
	 *            车辆类型
	 * @return
	 */
	
	public static Regulations getViolationInfo(String shopSign,String engineNo, String voitureNo, String carType) {
		logger.info("OffencesOnline getViolationInfo request -- API_URL="+API_URL+"  SECRET="+SECRET+"  APP_KEY="+APP_KEY);
		logger.info("OffencesOnline getViolationInfo request -- shopSign="+shopSign+"  engineNo="+engineNo+"  voitureNo="+voitureNo+" carType="+carType);
		Regulations regulations = new Regulations();
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, VIOL_INFO);
		signMap.put("shop_sign", shopSign);
		signMap.put("engine_no", engineNo);
		signMap.put("voiture_no", voitureNo);
		signMap.put("car_type", carType);
		String sign = sign(signMap);
		String resultStr = doPost(signMap.entrySet(), sign);
		logger.info("OffencesOnline getViolationInfo httpclient result json = "+resultStr);
		if(!"".equals(resultStr)){
			JSONObject jsonResult = JSON.parseObject(resultStr).getJSONObject("jtfk_violationsinfo_get_response");
			if(null != jsonResult){
				Integer resultCode = jsonResult.getInteger("code_id");
				if(null != resultCode && (72012 == resultCode || 72008 == resultCode)){
					List<Regulation> regulationList = JSON.parseArray(jsonResult.getJSONObject("regulations_arrays").getString("regulations"), Regulation.class);
					regulations.setError_code("0");
					regulations.setRegulationList(regulationList);
				}else{
					regulations.setError_code("-1");
				}
			}else{
				JSONObject errorJsonResult = JSON.parseObject(resultStr).getJSONObject("error_response");
				if(null != errorJsonResult){
					Integer subCode = errorJsonResult.getInteger("sub_code");
					if(null != subCode && (72012 == subCode || 72008 == subCode)){
						regulations.setError_code("0");
					}else{
						regulations.setError_code("-1");
					}
				}else{
					regulations.setError_code("-1");
				}
			}

		}else{
			regulations.setError_code("-1");
		}
		logger.info("OffencesOnline getViolationInfo reply = "+JSON.toJSONString(regulations));
		return regulations;
	}
	
	/**
	 * 查看订单详细信息
	 * trans_no	String		通联内部流水号
	 * bill_id	String	900	业务订单号
	 * order_sn	String	351111000900	
	 * cust_id	String	780794	会员ID
	 * order_time	String		下订单时间 
	 * postage	String	10.0	邮寄费
	 * amount	String	213.0	所需费用
	 * order_status_id	String	1 处理中   2  已完成3 等待支付 4 已撤消	订单状态
	 * orders_status	String	已撤销	
	 * mailing_address	String	浦东新区	邮寄地址
	 * need_mail_invoice	String	0-否 1- 是	是否邮寄发票
	 * need_penalty_receipts	String	0-否 1-是	是否邮寄回执
	 * violation_order_info_list_array	ViolationOrderInfoList[]违章明细
	 * @param transNo
	 * @return
	 */
	public static Map<String, Object> getOrderInfo(String transNo) {
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, ORDER_INFO);
		signMap.put(KEY_TRANS_NO, transNo);
		String sign = sign(signMap);
		String resultStr = doPost(signMap.entrySet(), sign);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("".equals(resultStr)) {
			resultMap.put(RESULT_EXC, "-1");
			resultMap.put(RESULT_CODE, "-1");
			resultMap.put(RESULT_STR, "网络异常");
			resultMap.put(RESULT_JSON, "");
		} else {
			JSONObject jo = JSON.parseObject(resultStr);
//			System.out.println("date= " + DateUtil.getCurrentDate() + "OffencesOnline.getOrderInfo====server_msg= "
//					+ resultStr);
			// 查询到结果
			if (jo.get("jtfk_orderlistinfo_get_response") != null) {
				resultMap.put(RESULT_EXC, "0");
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON,
						jo.get("jtfk_orderlistinfo_get_response"));
				resultMap.put(RESULT_CODE, "ok");
			}
			// 服务器返回异常信息
			if (jo.get("error_response") != null) {
				JSONObject errorInfo = JSON.parseObject(jo
						.get("error_response").toString());
				// 如果没有查到违章记录(服务器没有查到违章记录也在异常信息中)
				resultMap.put(RESULT_EXC, "-1");
				resultMap.put(RESULT_CODE, errorInfo.get("sub_code"));
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON, "");
			}
		}
		return resultMap;
	}
	
	
	/**
	 * 获取违章的缴费信息
	 * @param tempIds
	 * @param shopSign 车牌号
	 * @param engineNo 发动机号
	 * @param voitureNo 车架号
	 * @return
	 */
	public static OrderDetailResult getOrderDetail(String shopSign, String engineNo, String voitureNo,Integer carType,Regulations regulations) {
		logger.info("OffencesOnline getOrderDetail request -- API_URL="+API_URL+"  SECRET="+SECRET+"  APP_KEY="+APP_KEY);
		logger.info("OffencesOnline getOrderDetail request -- shopSign="+shopSign+"  engineNo="+engineNo+"  voitureNo="+voitureNo+" carType="+carType+" regulations="+JSON.toJSONString(regulations));
		OrderDetailResult detailResult = new OrderDetailResult();
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, ORDER_DETAIL);
		
		List<ViolationsDetail> vDetailList = Lists.newArrayList();
		
		for(Regulation regulation : regulations.getRegulationList()){
			ViolationsDetail vDetail = new ViolationsDetail();
			vDetail.setTempID(Integer.parseInt(regulation.getTemp_id()));
			if(!"".equals(regulation.getCity_area_id())){
				vDetail.setCityAreaID(Integer.parseInt(regulation.getCity_area_id()));
			}
			vDetail.setCityAreaID(0);
			vDetail.setCityID(Integer.parseInt(regulation.getCity_id()));
			if(!"".equals(regulation.getRegulation_id())){
				vDetail.setRegulationId(Integer.parseInt(regulation.getRegulation_id()));
			}
			vDetail.setRegulationId(0);
			vDetail.setViolationSn(regulation.getViolation_sn());
			vDetail.setSignID(regulation.getTemp_id());
			vDetail.setFromOrderID(1);
			if("1".equals(regulation.getIs_on_site_single())){
				vDetail.setIsOnSiteSingle(true);
			}else{
				vDetail.setIsOnSiteSingle(false);
			}
			vDetail.setViolationDateTime(regulation.getViolation_time());
			ViolationPrivateVoiture voiture = new ViolationsDetail.ViolationPrivateVoiture(shopSign,carType,engineNo,voitureNo);
			vDetail.setVoiture(voiture);
			vDetail.setReference(regulation.getReference());
			vDetail.setViolationRoad(regulation.getViolation_road());
			vDetailList.add(vDetail);
		}
		
		signMap.put("detail", "{\"violationsDetail\":"+JSON.toJSONString(vDetailList)+"}");// 违章内容和车辆信息
		signMap.put("is_need_penalty_peceipts", "1");// 是否邮寄回执
		signMap.put("is_need_mail_invoice", "1");// 是否邮寄发票
		signMap.put("mail_type_id","2");// 邮寄类型
		String sign = sign(signMap);
		logger.info("OffencesOnline getOrderDetail signMap = "+JSON.toJSONString(signMap));
		String resultStr = doPost(signMap.entrySet(), sign);
		logger.info("OffencesOnline getOrderDetail resultStr = "+resultStr);
		if (!"".equals(resultStr)) {
			OrderDetailResult detailResultJson = JSON.parseObject(JSON.parseObject(resultStr).getString("jtfk_orderdetail_get_response"), OrderDetailResult.class);
			if(null != detailResultJson){
				detailResultJson.setOrder_id(signMap.get(KEY_ORDER_ID));
				detailResult = detailResultJson;
				detailResult.setErrror_msg("0");
			}else{
				detailResult.setErrror_msg("-1");
			}
			
		}else{
			detailResult.setErrror_msg("-1");
		}
		
		logger.info("OffencesOnline getOrderDetail reply = "+JSON.toJSONString(detailResult));
		return detailResult;
		
	}

	
	
	/**
	 * 保存违章订单
	 * @param tempIds
	 * @param shopSign
	 * @param engineNo
	 * @param voitureNo
	 * @return
	 */
	public static OrdersaveReturn saveOrder(String shopSign, String engineNo, String voitureNo,Integer carType,Regulations regulations) {
		logger.info("OffencesOnline saveOrder param shopSign="+shopSign+" engineNo="+engineNo+" voitureNo="+voitureNo+" carType="+carType+" regulations="+JSON.toJSONString(regulations));
		OrdersaveReturn ordersaveReturn = new OrdersaveReturn();
		
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, ORDER_SAVE);
		
		List<ViolationsDetail> vDetailList = Lists.newArrayList();
		
		for(Regulation regulation : regulations.getRegulationList()){
			ViolationsDetail vDetail = new ViolationsDetail();
			vDetail.setTempID(regulation.getTemp_id()!=null?Integer.parseInt(regulation.getTemp_id()):null);
			if(!"".equals(regulation.getCity_area_id())){
				vDetail.setCityAreaID(Integer.parseInt(regulation.getCity_area_id()));
			}
			vDetail.setCityID(Integer.parseInt(regulation.getCity_id()));
			if(!"".equals(regulation.getRegulation_id())){
				vDetail.setRegulationId(Integer.parseInt(regulation.getRegulation_id()));
			}
			vDetail.setViolationSn(regulation.getViolation_sn());
			vDetail.setSignID(regulation.getTemp_id());
			vDetail.setFromOrderID(1);
			if("1".equals(regulation.getIs_on_site_single())){
				vDetail.setIsOnSiteSingle(true);
			}else{
				vDetail.setIsOnSiteSingle(false);
			}
			vDetail.setViolationDateTime(regulation.getViolation_time());
			ViolationPrivateVoiture voiture = new ViolationsDetail.ViolationPrivateVoiture(shopSign,carType,engineNo,voitureNo);
			vDetail.setVoiture(voiture);
			vDetail.setReference(regulation.getReference());
			vDetail.setViolationRoad(regulation.getViolation_road());
			vDetailList.add(vDetail);
		}
		
		signMap.put(KEY_DETAIL, "{\"violationsDetail\":"+JSON.toJSONString(vDetailList)+"}");
		signMap.put("is_need_penalty_receipts", "0");
		signMap.put("is_need_mail_invoice", "0");
//		signMap.put("mail_type_id", "2");
		signMap.put("cust_name", "刘景昊");
		signMap.put("gender", "1");
		signMap.put("mobile_phone", "18930076815");
		signMap.put("link_man", "刘景昊");
		signMap.put("tele_phone", "18930076815");
		
		String sign = sign(signMap);
		
		String resultStr = doPost(signMap.entrySet(), sign);
		
		logger.info("OffencesOnline saveOrder httpclient resultStr="+resultStr);
		
		if(!"".equals(resultStr)){
			ordersaveReturn = JSON.parseObject(JSON.parseObject(resultStr).getString("jtfk_violationsordersave_add_response"), OrdersaveReturn.class);
			if(null != ordersaveReturn){
				ordersaveReturn.setOrder_id(signMap.get(KEY_ORDER_ID));
				ordersaveReturn.setErrror_msg("0");
			}else{
				ordersaveReturn = new OrdersaveReturn();
				ordersaveReturn.setErrror_msg("-1");
			}
		}else{
			ordersaveReturn.setErrror_msg("-1");
		}
		logger.info("OffencesOnline saveOrder reply ="+JSON.toJSONString(ordersaveReturn));
		return ordersaveReturn;
	}
	
	/**
	 * 支付订单
	 * @param bill_id
	 * @return
	 */
	public static PayorderResult payOrder(String bill_id,String order_id) {
		logger.info("OffencesOnline payOrder param bill_id="+bill_id+" order_id="+order_id);
		PayorderResult pResult = new PayorderResult();
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, ORDER_PAY);
		signMap.put(KEY_TRANS_NO, bill_id);
		signMap.put(KEY_ORDER_ID, order_id);
		String sign = sign(signMap);
		String resultStr = doPost(signMap.entrySet(), sign);
		logger.info("OffencesOnline payOrder httpclient resultStr="+resultStr);
		if (!"".equals(resultStr)) {
			PayorderResult pResultJson = JSON.parseObject(JSON.parseObject(resultStr).getString("jtfk_payorder_add_response"), PayorderResult.class);
			if(null != pResultJson){
				if("72000".equals(pResultJson.getPay_code())){
					pResultJson.setPay_code("0");
				}
				pResult = pResultJson;
			}else{
				pResult.setPay_code("-1");
			}
			
		}else{
			pResult.setPay_code("-1");
		}
		pResult.setPay_code("0");
		logger.info("OffencesOnline payOrder reply ="+JSON.toJSONString(pResult));
		return pResult;
		
	}

	/**
	 * 违章代办规则查询
	 * 
	 * @return
	 */
	public static Map<String, Object> getRule() {
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, VIOL_RULE);
		String sign = sign(signMap);
		String resultStr = doPost(signMap.entrySet(), sign);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("".equals(resultStr)) {
			resultMap.put(RESULT_EXC, "-1");
			resultMap.put(RESULT_CODE, "-1");
			resultMap.put(RESULT_STR, "网络异常");
			resultMap.put(RESULT_JSON, "");
		} else {
			JSONObject jo = JSON.parseObject(resultStr);
			// 查询到结果
			if (jo.get("jtfk_agencyrule_get_response") != null) {
				resultMap.put(RESULT_EXC, "0");
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON,
						jo.getJSONObject("jtfk_agencyrule_get_response")
								.getJSONObject("rules_arrays").get("rule"));
				resultMap.put(RESULT_CODE, "ok");
			}
			// 服务器返回异常信息
			if (jo.get("error_response") != null) {
				JSONObject errorInfo = JSON.parseObject(jo
						.get("error_response").toString());
				resultMap.put(RESULT_EXC, "-1");
				resultMap.put(RESULT_CODE, errorInfo.get("sub_code"));
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON, "");
			}
		}
		return resultMap;
	}

	/**
	 * signMap中放常量
	 * 
	 * @param signMap
	 * @return
	 */
	private static Map<String, String> putSignConst(
			Map<String, String> signMap, String method) {
		String uuid = getUUID();
		String timeStr = getTimeStamp();
		signMap.put(KEY_V, V);
		signMap.put(KEY_APP_KEY, APP_KEY);
		signMap.put(KEY_SIGN_V, SIGN_V);
		signMap.put(KEY_TIMESTAMP, timeStr);
		signMap.put(KEY_FORMAT, FORMAT);
		signMap.put(KEY_ORDER_ID, uuid);
		signMap.put(KEY_METHOD, method);

		return signMap;
	}

	/**
	 * 向供应商发送http请求
	 * 
	 * @param mapEntry
	 * @param sign
	 * @return
	 */
	private static String doPost(Set<Entry<String, String>> mapEntry,String sign) {
		logger.info("OffencesOnline doPost request mapEntry="+JSON.toJSONString(mapEntry)+" sign="+sign);
		String result = "";
		HttpClient hc = new HttpClient();
		hc.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8"); // 设置字符集
		hc.getHttpConnectionManager().getParams().setConnectionTimeout(10000);// 设置请求超时时间
		hc.getHttpConnectionManager().getParams().setSoTimeout(20000);// 设置读取超时时间
		PostMethod pm = new PostMethod(API_URL);// POST请求
		try {
			pm.addRequestHeader("Content-type",
					"application/x-www-form-urlencoded; charset=UTF-8");// 设置POST请求方式
			NameValuePair[] nvpArray = {};
			List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : mapEntry) {
				nvpList.add(new NameValuePair(entry.getKey(), entry.getValue()));
			}
			nvpList.add(new NameValuePair(KEY_SIGN, sign));
			nvpArray = nvpList.toArray(nvpArray);
			pm.setRequestBody(nvpArray);
			int _status = hc.executeMethod(pm);
			if (_status == HttpStatus.SC_OK) {
				result = pm.getResponseBodyAsString();
			} else {

			}
		} catch (Exception e) {

		} finally {
			pm.releaseConnection();
			pm = null;
			hc = null;
		}
		return result;
	}

	/**
	 * 判断String数组是否都为空 (用来判断Map<String, String>的key,value)
	 * 
	 * @param values
	 * @return
	 */
	private static boolean areNotEmpty(String[] values) {
		boolean result = true;
		if (values == null || values.length == 0)
			result = false;
		else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 大写 md5加密 (utf-8格式)
	 * 
	 * @param data
	 * @return
	 */
	private static String encryptMD5(String data) {
		StringBuilder sign = new StringBuilder();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(data.getBytes("UTF-8"));
			for (byte b : bytes) {
				String hex = Integer.toHexString(b & 0xFF);
				if (hex.length() == 1) {
					sign.append("0");
				}
				sign.append(hex.toUpperCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign.toString();
	}

	/**
	 * 判断一个Sting值是否为空
	 * 
	 * @param value
	 * @return
	 */
	private static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计算sign的值
	 * 
	 * @param sortedParams
	 *            格式为SECRET+排序后的key+value+SECRET
	 * @return
	 */
	private static String sign(Map<String, String> sortedParams) {
		StringBuilder query = new StringBuilder();
		query.append(SECRET);
		Set<Entry<String, String>> paramSet = sortedParams.entrySet();
		for (Entry<String, String> param : paramSet) {
			if (areNotEmpty(new String[] { param.getKey(), param.getValue() }))
				query.append(param.getKey()).append(param.getValue());
		}
		query.append(SECRET);
		return encryptMD5(query.toString());
	}

	/**
	 * 获得API要求的UUID 格式为UUID前30位
	 * 
	 * @return
	 */
	private static String getUUID() {
		return UUID.randomUUID().toString().substring(0, 30);
	}

	/**
	 * 获得API要求的时间戳 格式为yyyyMMddHHmmss 时区为GMT+8
	 * 
	 * @return
	 */
	private static String getTimeStamp() {
		Long timestamp = Long.valueOf(new Date().getTime());
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		String timeStr = df.format(new Date(timestamp.longValue()));
		return timeStr;
	}


	
	
	

	/**
	 * 取消订单
	 * @param transNo 实际上是bill_id
	 * @return
	 */
	public static Map<String, Object> cancelOrder(String transNo) {
		Map<String, String> signMap = new TreeMap<String, String>();
		putSignConst(signMap, ORDER_CANCEL);
		signMap.put(KEY_TRANS_NO, transNo);
		String sign = sign(signMap);
		String resultStr = doPost(signMap.entrySet(), sign);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if ("".equals(resultStr)) {
			resultMap.put(RESULT_EXC, "-1");
			resultMap.put(RESULT_CODE, "-1");
			resultMap.put(RESULT_STR, "网络异常");
			resultMap.put(RESULT_JSON, "");
		} else {
			JSONObject jo = JSON.parseObject(resultStr);
//			System.out.println("date= " + DateUtil.getCurrentDate() + "OffencesOnline.cancelOrder====server_msg= "
//					+ resultStr);
			// 查询到结果
			if (jo.get("jtfk_cancelorder_add_response") != null) {
				JSONObject cancel = JSON.parseObject(jo.get(
						"jtfk_cancelorder_add_response").toString());
				resultMap.put(RESULT_EXC, "0");
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON,
						jo.get("jtfk_cancelorder_add_response"));
				resultMap.put(RESULT_CODE, cancel.get("cancel_result"));
			}
			// 服务器返回异常信息
			if (jo.get("error_response") != null) {
				JSONObject errorInfo = JSON.parseObject(jo
						.get("error_response").toString());
				resultMap.put(RESULT_EXC, "-1");
				resultMap.put(RESULT_CODE, errorInfo.get("sub_code"));
				resultMap.put(RESULT_STR, resultStr);
				resultMap.put(RESULT_JSON, "");
			}
		}
		return resultMap;
	}

	
	
	public static void main(String[] args) throws Exception{
		System.out.println(getOrderInfo("TEST201510200012"));
	}
}
