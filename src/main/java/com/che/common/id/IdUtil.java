/**
 * 
 */
package com.che.common.id;

import java.util.Random;

/**
 * 随机验证码
 * @author liujinghao
 *
 */
public class IdUtil {
    
    private static String start = "9999"; 

    private static String RandomNum(int count){
	    StringBuffer sb = new StringBuffer();
	    String str = "0123456789";
		Random r = new Random();
		for(int i=0;i<count;i++){
		    int num = r.nextInt(str.length());
		    sb.append(str.charAt(num));
		    str = str.replace((str.charAt(num)+""), "");
	    }
	    return sb.toString();
	}
	 
    private static String defaultUserId(){
		Long systm = System.currentTimeMillis();
		String tm = String.valueOf(systm);
		tm = tm.substring(8, tm.length());
		tm = tm + RandomNum(9);
		return start+tm;
	}
	
	public static long createId(){
	   return Long.parseLong(defaultUserId());
	}

	public static void main(String[] args) {
        System.out.println(IdUtil.createId());
    }
}
