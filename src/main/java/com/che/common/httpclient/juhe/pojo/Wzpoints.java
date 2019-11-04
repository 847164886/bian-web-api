/**
 * 
 */
package com.che.common.httpclient.juhe.pojo;

import java.util.List;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class Wzpoints {
	
	private Integer num;
	private String detail;
	private String province;
	private String level;
	private String district;
	private String city;
	private List<String> location;
	private String address;
	private String title;

}
