package com.che.common.httpclient.jianding;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 查维保相关参数
 * @author wangfuyun
 *
 */
public class JiandingConstants {

	/** 请求url */
	public static final String ACCOUNT_INFO = "/rest/publicif/accountInfo"; //查询帐号信息
	public static final String BRAND_CHECK = "/rest/publicif/checkBrand"; //查询品牌是否支持
	public static final String REPORT_BUY = "/publicif/2.0/buy";//购买报告
	public static final String ORDER_QUERY = "/rest/publicif/orderInfo"; //订单状态查询
	public static final String REPORT_QUERY = "/rest/publicif/2.0/reportData"; //报告查询
	public static final SimpleDateFormat JIANDING_DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	/** 测试环境 */
//	public static final String JIANDING_URL = "https://test-pif.chejianding.com"; //测试URL
//	public static final BigDecimal JIANDING_PAY_AMOUNT = new BigDecimal("0.01"); //支付金额（测试）
//	/** 测试帐号私钥 */
//	public static final String JIANDING_ACCOUNT_UID = "2298884224524d649a309e2420fb0fd4"; //测试账号uid
//	public static final String JIANDING_ACCOUNT_PWD = "e10adc3949ba59abbe56e057f20f883e"; //测试账号pwd
//	public static final String TGSXX_PRIVATEKEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALo/fQ8USBV331TaAFiuBcyW/0/26TOGS4X6jUu2az1c"
//			+ "Z7Twb8FQpS2+CQcZ7prKI7fbrCevDaUCZm3hkJByxyJBTC50haGUGloIRYWNDduHUXRmk/p8p9UQXbyStmtz9M1FR87kmuNFs7MvWwCIbaT1zgqgZWPJfF3TK+toHXOl"
//			+ "AgMBAAECgYEAihQFmEdWBayAQzz34sbpf7s2f0czrsJSfXq6hDYynSRDso5KnAw42Ye4063zCojwIB/ZKvaJ7Bqya6Y98glV/ZhochV8obhXCWOTTMcyMxdKlKlLnURU"
//			+ "Jn3pxg30IEfDVp5vZyIFPljpWI+hoTO0YCQPljIoana2QHqYYCx2j0UCQQDiOnIdKs0yk0AilIrJEvVQLpJrbtS24JQTquYPZqVfXQXeaQ4xMPJQe91+wWjeKqFYuxav"
//			+ "N6qpvh+qdChAYrt/AkEA0sIeu6ZY/9KTBU5tnqJdTOoS3y592mH/QdtUYLnBpQVL1jkRt+1jRsArlr08x7OK9XCvbTtK5IcIxn7q2hfy2wJBANFkr4L3vv509QQM4XjDp"
//			+ "3QT50qxRwoOTID3ygGvTJo8C9aU/qHhYCfGvnk4o8wfQUBTyudbawe6Bq1K3obZ3BkCQCWg/QW55IgAW7mF0DKTgJ7759SrI1M7Tey/MSh34egJswmTdONm4eO+6clnDh"
//			+ "QFrVi2/ss829lDdsCh5mJ8BiMCQQDhn2RrOCINMIXW6hgSE3CapRnUfQYe/0WHYEZFgksrW7KVaUjxO/iycoRB68tY1d4mBHCCoJF39sUsK3xGZpVv"; //JAVA私钥

	
	/** 正式环境 */
	public static final String JIANDING_URL = "https://pif.chejianding.com"; //正式URL	
//	public static final BigDecimal JIANDING_PAY_AMOUNT = new BigDecimal("28"); //支付金额 
	/** 帐号私钥 */
	public static final String JIANDING_ACCOUNT_UID = "fe3421e3d489433b8c31daf0f83af262"; //账号uid
	public static final String JIANDING_ACCOUNT_PWD = "1e2e990194540d0028f290da6d2e3443"; //账号pwd
	public static final String TGSXX_PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALvJyCdp8sIpQ3uKYtBFn9/+bnAJJXab6CL0U7+d66B"
			+ "oq3LPG488U0YvAb7AdlFCIgUPUw6t9Qa3HXxvh8hX6tGeDDuz3x8ctcbRmxIPSXa5VyKJJ4TSR7XSoYQfMRyxrtxGPgvxW+D7aYNJGGQwdzCYpJ5mzwGuX9L313KFWm"
			+ "mXAgMBAAECgYBiSE1VM4I2VFpZQihNUONH3GX4hRDybs6j3LQoSYKUxbNVt0d5Q91hkiCbsSc7iB0+ALM7r5faF+R6x/SMyla+0JYl8qQCsNOyKaobH358tnsiDrhIt"
			+ "i0/mwEIuEmrO9OXGvjVYS0EWrYr0R9Nz1oO9wpsToEc40i9QAYubMeFoQJBAOvf7pDMqN3sfxpn36AqaH96szomJU9Yf497VBPm3xXTVhNnXeWUETKfaMmfcCqMSi66"
			+ "Bg0WIo0/jH88Gk/Od+0CQQDLz4Z7KEaAiYyE+7Eu5JHQ9FgRdVTGgn/Z4N5DdTRXxNAogm25mpIydF+GtCmAh6W2H7Xt2SYHvOL2Fp/Ory8TAkBYvGrUgJn6uzDp7y/"
			+ "64lljs/ZfJuCcZA/BG8V6oz1Dybi1Hgr+BmEd6UiTqW6aIyL5RzRlkfE3sDOb3jGL2oHVAkBaBJTvPIC3MQmBivFdNtM4qVNkkqAY9Xamsu04ekHeKi2OVt1DxwOfiU"
			+ "rP4i8ad883O5ZNMUXc0Q6lhqSa00FFAkBn4G0th0QAktdHf6IfGku/XKYiItGM+YtJ2gJb6qTkQ/TQS1xGcRfhC0zmOktRBQIomBStP5hVr8kzmEFj9ZE4"; //JAVA私钥

	

}
