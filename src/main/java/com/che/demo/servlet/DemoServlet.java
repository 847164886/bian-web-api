package com.che.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

public class DemoServlet extends HttpServlet {
	private static final long serialVersionUID = 709486883307035128L;
//	private static final Logger logger = LogManager.getLogger(DemoServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
//		logger.info("DemoServlet init start");
//		logger.info("DemoServlet init end");
//		logger.error("qzh:errorrororororo");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		logger.info("DemoServlet service start");
//		logger.info("DemoServlet service end");
//		logger.error("QZh:2309lflkwjelfjwelk");
	}
}
