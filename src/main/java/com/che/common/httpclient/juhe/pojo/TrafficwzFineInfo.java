/**
 * 
 */
package com.che.common.httpclient.juhe.pojo;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class TrafficwzFineInfo {
	
	private String fineNo;//违章单号
	private String fineTime;//违章时间 例如：2015-07-04 13:40:00
	private String fineLocation;//违章地点
	private String fineCode;//交警部违章编号
	private String fineDetail;//违章地点
	private String fineDeductPoints;//扣分(由于各个地区实际情况不同，该字段仅供参考)
	private String fineFee;//罚金
	private String delayFee;//滞纳金
	private String proxyFee;//第三方平台代办费
	private String serviceFee;//服务费（传递给手机端做呈现）
	private Integer canPay;//是否可缴费,1为可以,0不可缴费,(扣分,含滞纳金的罚单无法通过平台缴费)
	private String unique;//罚单查询唯一码(下单时需传入)
	private String fineCity;//违章所在城市
	private String canPayMsg;//不可缴费说明（可缴费时为空）
	private String itemId;//违章交通队编号(下单时需传入)

}
