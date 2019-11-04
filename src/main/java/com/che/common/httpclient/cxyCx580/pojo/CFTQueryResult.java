package com.che.common.httpclient.cxyCx580.pojo;

import java.util.List;

import lombok.Data;

@Data
public class CFTQueryResult {

	private Boolean HasData;
	private List<CFTRecord> Records;
	private Integer ErrorCode;
	private Boolean Success;
	private String  ErrMessage;
	private String ResultType;
	private String LastSearchTime;
	private String Other;
 
	@Data
	public static class CFTRecord {                                      
		private String Time;                              		 
	    private String Location;                                  
	    private String Reason;                                    
	    private String count;                                     
	    private String status;                                    
	    private String department;                                
	    private String Degree;                                    
	    private String Code;                                      
	    private String Archive;                                   
	    private String Telephone;                                 
	    private String Excutelocation;                            
	    private String Excutedepartment;                          
	    private String Category;                                  
	    private String Latefine;                                  
	    private String Punishmentaccording;                       
	    private String Illegalentry;                              
	    private Integer Locationid;                               
	    private String LocationName;                              
	    private Integer DataSourceID;                             
	    private String RecordType;                                
	    private String Poundage;                                  
	    private String CanProcess;                                
	    private String UniqueCode;                                
	    private Integer SecondaryUniqueCode;                       
	    private Integer DegreePoundage;                            
	    private String Other;                                     
	    private String CanprocessMsg;                             
	    private String CooperPoundge;                             
	    private String ActivePoundge;                             
	    
	}
}
