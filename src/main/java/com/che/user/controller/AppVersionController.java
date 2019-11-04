/**
 * 
 */
package com.che.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.user.pojo.AppVersionReply;
import com.che.user.pojo.AppVersionReq;
import com.che.user.service.AppVersionService;

/**
 * @author karlhell
 *
 */
@RestController
public class AppVersionController {

	@Autowired
	private AppVersionService appVersionService;
	
	@RequestMapping("/version")
	public AppVersionReply version(@RequestBody(required=false) AppVersionReq req){
		return appVersionService.version(req);
	}
	
	
}
