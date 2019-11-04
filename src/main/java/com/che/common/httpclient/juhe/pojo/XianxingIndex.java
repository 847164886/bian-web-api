/**
 * 
 */
package com.che.common.httpclient.juhe.pojo;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @author karlhell
 *
 */
@Data
public class XianxingIndex {
	
	private String date;
	private String week;
	private String city;
	private String cityname;
	/**
	 * time  限行时间
	 * place 限行区域
	 * info  其他说明
	 */
	private List<Map<String,String>> des;
	private String fine;
	private String remarks;
	private Integer isxianxing;
	/**
	 * 限行尾号
	 */
	private List<Integer> xxweihao;
	private String holiday;

}
