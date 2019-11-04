package com.che.common.web;

/**
 * web常量
 */
public abstract class Constants {
	
	public static final Integer REPLY_SUCCESS = 0;//成功
    public static final Integer RESULT_SUCCESS = 0;//成功
    public static final Integer RESULT_ERROR_LOGIN = -1;//登录异常 或 session超时
    public static final Integer RESULT_ERROR_SYSERROR = -2;//系统错误（如error500）
    public static final Integer RESULT_UNAUTH = -3;//没有权限(用于区分会员和非会员)
    public static final Integer RESULT_ERROR_PARAM = -4;//参数错误（输入参数异常）
    public static final Integer RESULT_ERROR_REQUEST = -6;//请求参数异常（区分终端错误和服务器处理错误）
    public static final Integer RESULT_ERROR_KICKOUT = -7;//用户被踢出了
    public static final Integer RESULT_ERROR_USER_NOT_EXIST = -8;//用户不存在
    
    /** 帐户类型 **/
    //账户类型0:anonymous,1:mobile, 2:openqq_id,3:openwb_id, 4:openwx_id
    public static final Integer ACCOUNT_TYPE_ANONY =  0;
    public static final Integer ACCOUNT_TYPE_MOBILE =  1;
    public static final Integer ACCOUNT_TYPE_OPEN_QQ =  2;
    public static final Integer ACCOUNT_TYPE_OPEN_WEIBO =  3;
    public static final Integer ACCOUNT_TYPE_OPEN_WEIXIN =  4;
    
    public static final Integer ACCOUNT_NO_ENOUGH = -10;

    
    /** 认证失败类型 **/
	public static final Integer AUTHEXCEPTION_ACCOUNTNOTEXISTS = -11;     // 用户帐户不存在
	public static final Integer AUTHEXCEPTION_ACCOUNTDISABLED  = -12;     // 用户帐户已被禁用
	public static final Integer AUTHEXCEPTION_UNKOWNSOURCE     = -13;     // 未知来源
	public static final Integer AUTHEXCEPTION_ERRORTOKEN       = -14;     // TOKEN错误
	public static final Integer AUTHEXCEPTION_LOGINCOUNT       = -15;     // 登录失败超过限制
	public static final Integer AUTHEXCEPTION_OTHER            = -16;     // 其它错误
	
	
	public static final String CSJP_SHIRO_USER_LOGIN_FAILED_LIMIT_COUNT = "csjpshiroUserLoginFailedLimitCount";  
	public static final String CSJP_SHIRO_USER_REGIST = "csjpregist";
	public static final String CSJP_SHIRO_USER_REGIST_LIMIT_COUNT = "csjpshiroUserLoginRegistLimitCount";  
	public static final String CSJP_SHIRO_USER_FORGET_PWD = "csjforgetpwd";
	public static final String CSJP_SHIRO_USER_FORGET_PWD_LIMIT_COUNT = "csjpshiroUserForgetPwdLimitCount";  
	public static final String CSJP_CERTITY = "csjcertify";  
	public static final int SEND_MESSAGE_LIMIT = 5;
	public static final Long SEND_MESSAGE_LIMIT_IDLE = 30*60*1000L;
    /** 部署环境 **/
    public static final String  DEPLOY_ALPHA                              = "alpha";
    public static final String  DEPLOY_BETA                               = "beta";
    public static final String  DEPLOY_PRODUCE                            = "produce";
    
    /** 优惠码 **/
    public static final String  COUPON_KEY                              = "cet_coupon";
   
    /** 页面展示字段**/
    public static final String YOUKACHONGZHI = "油卡充值" ;
	
    public static final String FANKUANDAIJIAO = "罚款代缴" ;
	
    public static final String ZHONGGUOSHIYOU = "中国石油" ;
	
    public static final String ZHONGGUOSHIHUA = "中国石化" ;
    
    public static final String MAINTENANCEQUERY = "维保查询";
    
    public static final String MAINTENANCEREFUND = "维保退款";
    
    public static final String ACCOUNTRECHARGE = "余额充值";
    
    /** 付款类型 **/
    public static final Integer ALIPAY = 1;
    
    public static final Integer TENPAY = 2;
    
    /** 商品类型 **/
    public static final Integer YOUKACHONGZHI_TYPE = 1 ; 
    
    public static final String PAGE= "page";									// 参数key
    public static final String PAGE_INDEX = "index";							// 参数value 首页
    public static final String PAGE_VERSION = "version";						// 更新版本
    public static final String PAGE_TRAFFIC = "traffic";						// 参数value 违章列表页
    public static final String PAGE_ORDER = "order";							// 参数value 订单页表页
    
    public static final Integer PAGESIZE = 10;  //每页数量
    
    public static final String SYSNAME = "csjp"; // 业务系统名称
    
    public static final Integer EFFECTIVEDAYS = 365; // 维保有效期
    
}
