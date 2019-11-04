/*
 * COPYRIGHT Beijing NetQin-Tech Co.,Ltd.                                   *
 ****************************************************************************
 * 源文件名:  web.core.CP_SpringContext.java 													       
 * 功能: cpframework框架													   
 * 版本:	@version 1.0	                                                                   
 * 编制日期: 2014年8月22日 上午10:35:16 						    						                                        
 * 修改历史: (主要历史变动原因及说明)		
 * YYYY-MM-DD |    Author      |	 Change Description		      
 * 2014年8月22日    |    Administrator     |     Created 
 */
package com.che.core;

import org.springframework.context.ApplicationContext;

/** 
 *Description: <类功能描述>. <br>
 *<p>
	<使用说明>
 </p>
 *Makedate:2014年8月22日 上午10:35:16 
 * @author Administrator  
 * @version V1.0                             
 */
public class CP_SpringContext {
	/** 
	* @Fields applicationContext : TODO(applicationContext) 
	*/ 
	private static ApplicationContext applicationContext;

	private static CP_SpringContext springContext;

	private CP_SpringContext() {
	}

	public static CP_SpringContext getInstance() {
		if (springContext == null) {
			springContext = new CP_SpringContext();
		}
		return springContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	/**                                                          
	* 描述 : <获得applicationContext中的对象>. <br> 
	*<p> 
		<使用方法说明>  
	 </p>                                                                                                                                                                                                                                                
	* @param beanName
	* @return                                                                                                      
	*/  
	public static Object getBean(String beanName) {
		return CP_SpringContext.getInstance().getApplicationContext().getBean(beanName);
	}
}


