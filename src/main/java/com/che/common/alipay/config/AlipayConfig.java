package com.che.common.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088021114472912";
	
	public static String seller_email = "guanjun@sfbm.com";
	// 商户的私钥
	public static String private_key = "MIICXAIBAAKBgQDEZ94+pMgI3OSqWGtqZZ7GXqKwa1/+Pw4AomAyYGh6uu1WBnk+VhQZ/6ClnxgwdFvm94EagMkXRctFFCoi0HoSZfCWGVrbaVMpWf0107GdDizMC0uuVcYOwfavI2M/d6BFtplEMjNFKfXOIOpGD/0Bdzpe6n5n09Elsm3YOoMhzwIDAQABAoGAP5EKeiXk5BFyJLvnUBFBuRxJ9WVbNTN9ObFUBxny1r4HMI950DYV3XBBszjxoEUYtYEnlMdXziN1rY7gMFJfH2HbySYiqMspxQ67Ayyb7/heeP5GQkxhnGzk3Bajm0/TldY8MWAYVQSaVPhuH7iBoKxDrWnqTn34MpDgpY9ktfECQQDuJ98Ix1LEQs3KsM0vWoeCRo2eegqaTPlnsd5NebrpmFQ3rGMfuKm59wd7L8I9W8m8rqfb133lQf/6P7bthROHAkEA0x8tx0hFYp24vXZcy2gntPpGPKXMG7CXj6vvb+UTsL5J/Uohbw2lHiZR4XIlaOlODttmXg8HqsUppBtFz0OheQJBALwnMfVcjI5T/QQy9JiB3GdXWFGpN7E6ORzskKYDE2G0IG0vu8x291jdysJPNBWH6/Uuw51TPbqfjx3lsp8qsQcCQApkbIJ0wkWace+RlfvFQr8b2r09hBCQ3sUJt52QbymyFD5Nmdu3ljsns7wfPEwvv9HnZuCWaNA1KFmVphgPwjECQC1VMfPXjIikXjhMDJHRkCuuz90lKxpSr/IbPjwT4kBGMHdoqGzDgBidasYA4KRcznDJzh+HDPvyIBuXxsz+JqI=";
	
	public static String key = "ekmtpck9b9z4x389bv1kwdslbgjih8iy";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
 
	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";
	
	// 签名方式 不需修改
	public static String refund_sign_type = "MD5";
	  
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
	public static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do";

}
