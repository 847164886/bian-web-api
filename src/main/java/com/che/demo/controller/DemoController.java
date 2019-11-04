package com.che.demo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.che.demo.pojo.UserDemo;

@Controller
@RequestMapping("/democ")
public class DemoController {
	
	@RequestMapping(value = "/d1", method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public UserDemo d1(HttpServletRequest request, HttpServletResponse response, String param)  {
		UserDemo user = new UserDemo();
		user.setName("abcname");
		user.setAge(99);
		user.setDdd(23.32d);
		return user;
	}

	@RequestMapping(value = "/d2", method={RequestMethod.POST,RequestMethod.GET})
	public String d2(HttpServletRequest request, HttpServletResponse response, String param)  {
//		UserDemo user = new UserDemo();
		//sangyiwen
		return "d2";
		
	}
}
