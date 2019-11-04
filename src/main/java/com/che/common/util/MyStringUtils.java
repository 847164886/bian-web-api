package com.che.common.util;

public class MyStringUtils {
	public static  String rulestr(String str,int rule){
		if(0==rule){
			return "";
		}else if(99==rule||str.length()<=rule){
			return str;
		}else if(str.length()>rule){
			return str.substring(str.length()-rule, str.length());
		}else{
			return "";
		}
	}
	
	public static  String hideStr(String str,int start,int longs){
		
		if(str.length()<(start+longs)){
			return str;
		}
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(0,start)) ;
		for (int i = 0; i < longs; i++) {
			sb.append("*");
		}
		sb.append(str.substring(start+longs,str.length()));
		
		return sb.toString();
	}
	
	
	/*public static void main(String[] args) {
		System.out.println(MyStringUtils.hideStr("LSVFA49J232037048",10,4));
	}*/
}
