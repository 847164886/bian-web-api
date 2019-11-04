package com.che.common.web;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for Friend
 * @copyright {@link winwho.com}
 * @author Quenton.Zhou<Auto generate>
 * @version  2014-04-15 16:56:57
 */
@ControllerAdvice
public class ExceptionHandlerController {
	private static final Logger logger = LogManager.getLogger(ExceptionHandlerController.class);
	
	/**
	 * 错误统一返回
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class,IOException.class,RuntimeException.class})  
	@ResponseBody
    public Reply exception(Exception e) { 
	    logger.error(e.getMessage(), e);
		e.printStackTrace();
		Reply reply = new Reply();
		reply.setResultCode(Constants.RESULT_ERROR_SYSERROR);
		reply.setReplyCode(Constants.RESULT_ERROR_SYSERROR);
		reply.setServerTime(System.currentTimeMillis());
		reply.setMessage("系统异常");
        return reply;  
    }  
}
