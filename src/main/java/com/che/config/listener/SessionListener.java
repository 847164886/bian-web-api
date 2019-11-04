package com.che.config.listener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionListener implements HttpSessionListener {
	private static final Logger logger = LogManager.getLogger(SessionListener.class);
	private static final int TIME_OUT = 7776000 ; //s
	
    @Override
    public void sessionCreated(HttpSessionEvent event) {
    	logger.debug("==== Session is created ====");
        event.getSession().setMaxInactiveInterval(TIME_OUT);
    }
    
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    	logger.debug("==== Session is destroyed ====");
    }
}