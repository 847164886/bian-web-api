package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

import com.che.common.httpclient.cxyCx580.pojo.CFTQueryResult.CFTRecord;
@Data
public class CFTRecords {

	private String error_code; //-1失败 0成功
	
	private List<CFTRecord> cFTRecords;
	
	private String err_message;
	
	private String city;
	
	private String province;
	
}
