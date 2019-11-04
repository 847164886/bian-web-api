package com.che.auction.pojo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class CheckTypeReportReply extends Reply {
 
	private static final long serialVersionUID = 304952182982378716L;
	
	private CheckTypeReportPojo  waiguan = new CheckTypeReportPojo();
	private CheckTypeReportPojo  jiegou = new CheckTypeReportPojo();
	private CheckTypeReportPojo  jixie= new CheckTypeReportPojo();
	private CheckTypeReportPojo  dianqi = new CheckTypeReportPojo();
	private CheckTypeReportPojo  neishi = new CheckTypeReportPojo();
	private CheckTypeReportPojo  dipan = new CheckTypeReportPojo();
	
	@Data
	public static class CheckTypeReportPojo{
	 
		private Integer num;
		
		private List<String> images=new ArrayList<String>();
		 
	}
	
}
