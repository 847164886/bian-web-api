/**
 * 
 */
package com.che.common.util;

import java.math.BigDecimal;

/**
 * @author jinghao.liu
 * 计算工具
 */
public class CalcUtil {
	 /**
     * 提供精确的加法运算。
     * @param v1 加数
     * @param v2 被加数
     * @return 两个参数的和
     */
    public static double add(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.add(b2).doubleValue();
    }
    
    /**
     * 提供精确的减法运算。
     * @param v1 减数
     * @param v2 被减数
     * @return 两个参数的差
     */
    public static double sub(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.subtract(b2).doubleValue();
    } 

    
    /**
     * 提供精确的乘法运算。
     * @param v1 乘数
     * @param v2 被乘数
     * @return 两个参数的积
     */
    public static double mul(Double v1,Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.multiply(b2).doubleValue();
    }
    
    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     * @param v1 除数
     * @param v2 被除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(Double v1,Double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1.doubleValue()));
        BigDecimal b2 = new BigDecimal(Double.toString(v2.doubleValue()));
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    /**
     * 千万转千，转double
     * @param num
     * @return
     */
    public static Double long2Double(Long num){
		if(null == num || 0 == num){
			return 0D;
		}else{
			return CalcUtil.div(num*1D, 10000D, 0);
		}
	}
    
    public static Double long2Double(Double num){
        if(null == num || 0 == num){
            return 0D;
        }else{
            return CalcUtil.div(num, 10000D, 0);
        }
    }
    
    public static int[] randomCommon(int min, int max, int n){  
        if (n > (max - min + 1) || max < min) {  
               return null;  
           }  
        int[] result = new int[n];  
        int count = 0;  
        while(count < n) {  
            int num = (int) (Math.random() * (max - min)) + min;  
            boolean flag = true;  
            for (int j = 0; j < n; j++) {  
                if(num == result[j]){  
                    flag = false;  
                    break;  
                }  
            }  
            if(flag){  
                result[count] = num;  
                count++;  
            }  
        }  
        return result;  
    }  
	
}
