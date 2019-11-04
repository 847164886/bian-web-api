/**
 * 
 */
package com.che.auction.pojo;

import java.util.ArrayList;
import java.util.List;

import com.che.common.web.Reply;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CheckTypeReportDetailReply extends Reply {
	private static final long serialVersionUID = 7602349604618461797L;

	private CheckTypeReportDetailPojo waiguan = new CheckTypeReportDetailPojo();// 外观
	private CheckTypeReportDetailPojo jiegou = new CheckTypeReportDetailPojo();// 结构
	private CheckTypeReportDetailPojo jixie = new CheckTypeReportDetailPojo();// 机械
	private CheckTypeReportDetailPojo dianqi = new CheckTypeReportDetailPojo();// 电气
	private CheckTypeReportDetailPojo neishi = new CheckTypeReportDetailPojo();// 内饰
	private CheckTypeReportDetailPojo dipan = new CheckTypeReportDetailPojo();// 底盘

	@Data
	public static class CheckTypeReportDetailPojo {
		private List<ItemReportPojo> itemReports = new ArrayList<ItemReportPojo>();// 检测项集合
	}

	@Data
	public static class ItemReportPojo {
		private Integer itemId;// 检测项ID
		
		private String itemName;// 检测项名称

		private String picPath;// 图片路径

		private List<SpecialReportPojo> specialReports = new ArrayList<SpecialReportPojo>();// 检测异常项集合
	}

	@Data
	public static class SpecialReportPojo {
		private String specialName;// 检测异常项名称

		private Integer specialStatus;// 1:正常；2:轻微；3:一般；4:严重
	}

}
