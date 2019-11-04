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
public class OilPay {

	private String cardid;
	private String cardnum; /*充值金额*/
	private String ordercash;/*进货价格*/
	private String cardname;/*充值名称*/
	private String sporder_id;/*商家订单号*/
	private String game_userid;/*加油卡卡号*/
	private Integer game_state;/*充值状态:0充值中 1成功 9撤销*/
	private String uorderid;/*商户自定的订单号*/
	
}
