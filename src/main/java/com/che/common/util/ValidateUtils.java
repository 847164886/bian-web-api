/**
 * 
 */
package com.che.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Entity AdvOwner
 * @copyright {@link winwho.com}
 * @author simon.sang<Auto generate>
 * @version  2014-04-13 10:02:46
 */
public class ValidateUtils {
	
	//检校手机号码
	public static boolean validateMobile(String mobile){
		if(StringUtils.isBlank(mobile) || 11 != mobile.length()){
			return false;
		}
		
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(mobile);
		if(matcher.matches()){
			return true;
		}
		
		return false; 
		
	}
	
	//检校邮编
	public static boolean validateZipcode(String zipcode){
 
		if(zipcode!=null&&!zipcode.equals("")&&zipcode.length()==6 && validateNumber(zipcode)){
			
			return true;
			
		}
		
		return false; 
		
	}

	//检校是否是整数
	private static boolean validateNumber(String number){

		java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9]*");
		java.util.regex.Matcher match=pattern.matcher(number);
		
		if(number!=null&&!number.equals("") && match.matches()){
			
			return true;
			
		}
	 
	    return false; 
	}
	
}
