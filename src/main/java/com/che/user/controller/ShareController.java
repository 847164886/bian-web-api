/**
 * 
 */
package com.che.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.che.user.pojo.ShareReply;
import com.che.user.service.ShareService;

/**
 * @author karlhell
 *
 */
@RestController
public class ShareController {

	@Autowired
	private ShareService shareService;
	
	@RequestMapping("/share")
	public ShareReply version(){
		return shareService.share();
	}
	
}
