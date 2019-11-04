package com.che.common.httpclient.cxyCx580.pojo;

public class TrafficwzCxyFineInfo {
	
	private String  fine_no                 ; // 违章单号 
    private String  fine_time               ; // 违章时间 例如：2015-07-04 13:40:00 
    private String  fine_location           ; // 违章地点 
    private String  fine_code               ; // 交警部违章编号 
    private String  fine_detail             ; // 违章地点 
    private String  fine_deduct_points      ; // 扣分(由于各个地区实际情况不同，该字段仅供参考) 
    private String  fine_fee                ; // 罚金 
    private String  delay_fee               ; // 滞纳金 
    private String  proxy_fee               ; // 第三方平台代办费 
    private String  service_fee             ; // 服务费（传递给手机端做呈现） 
    private Integer can_pay                 ; // 是否可缴费1为可以0不可缴费(扣分含滞纳金的罚单无法通过平台缴费) 
    private String  unique                  ; // 罚单查询唯一码(下单时需传入) 
    private String  fine_city               ; // 违章所在城市 
    private String  can_pay_msg             ; // 不可缴费说明（可缴费时为空） 
    private String  item_id                 ; // 违章交通队编号(下单时需传入) 
    private Integer stats   				; //违章记录状态0 末处理  1 己处理(绝大部分情况下，车行易只能返回未处理的违章)
    
    /*private String Time;//违章时间  
	private String Location;//违章地点
	private String Reason;//违章原因
	private String Degree;//扣分 
	private String count;//罚金
	private String SecondaryUniqueCode;//违章记录ID，用于下单。
	private String Poundage;//服务费（传递给手机端做呈现）
	private String CanProcess;//是否可缴费,1为可以,0不可缴费,(扣分,含滞纳金的罚单无法通过平台缴费)
	private String CanprocessMsg;//不能代办的原因
	private String Code;//违章代码
	
	private String unique;//罚单查询唯一码(下单时需传入)
	private String LocationName;//违章所在城市
	private String Locationid; //违章所在城市ID
	private String canPayMsg;//不可缴费说明（可缴费时为空）
	private String itemId;//违章交通队编号(下单时需传入)
	*/
}
