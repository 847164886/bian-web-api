package com.che.common.httpclient.allinpay.pojo;

import lombok.Data;

@Data
public class ViolationsDetail {

	private Integer tempID;//序号ID
	private Integer cityAreaID;//区域ID
	private Integer cityID;//城市ID
	private Integer regulationId;//违章条例ID
	private String violationSn;//处罚单号/文书号
	private String signID;//每一笔违章标识ID
	private Integer fromOrderID;//下单来源ID(传1)
	private Boolean isOnSiteSingle;//是否现场单(是就表示有滞纳金),滞纳金由接口处理
	private String violationDateTime;//违章时间
	private ViolationPrivateVoiture voiture;//违章车辆信息
	private String reference;//违章时间标识(暂时没用)
	private String violationRoad;//违章道路
	
	@Data
	public static class ViolationPrivateVoiture{
		
		public ViolationPrivateVoiture(String shopSign,Integer voitureType,String engineNo,String voitureNo){
			this.shopSign = shopSign;
			this.voitureType = voitureType;
			this.engineNo = engineNo;
			this.voitureNo = voitureNo;
		}
		
		private String shopSign;//车牌号
		private Integer voitureType;//机动车类型
		private String engineNo;//发动机号
		private String voitureNo;//车身号
		private String errrorMsg;//错误
	}
}
