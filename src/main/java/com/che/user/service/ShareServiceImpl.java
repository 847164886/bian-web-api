/**
 * 
 */
package com.che.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.che.user.pojo.ShareReply;

/**
 * @author karlhell
 *
 */
@Service
public class ShareServiceImpl implements ShareService {
	
	@Value("${share.icon}")
	private String icon;
	
	@Value("${share.title}")
	private String title;
	
	@Value("${share.detailUrl}")
	private String detailUrl;

	@Override
	public ShareReply share() {
		ShareReply reply = new ShareReply();
		reply.setIcon(icon);
		reply.setTitle(title);
		reply.setDetailUrl(detailUrl);
		return reply;
	}

}
