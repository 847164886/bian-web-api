/**
 * 
 */
package com.che.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.che.common.web.Constants;
import com.che.user.pojo.AppVersionReply;
import com.che.user.pojo.AppVersionReq;

/**
 * @author karlhell
 *
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {

	@Value("${app.versionCode}")
	private Integer versionCode;
	@Value("${app.url}")
	private String url;
	@Value("${app.desc}")
	private String desc;
	@Value("${app.isMustInstall}")
	private Boolean isMustInstall;
	
	@Value("${app.ios.versionCode}")
	private Integer ios_versionCode;
	@Value("${app.ios.url}")
	private String ios_url;
	@Value("${app.ios.desc}")
	private String ios_desc;
	@Value("${app.ios.isMustInstall}")
	private Boolean ios_isMustInstall;
	
	@Override
	public AppVersionReply version(AppVersionReq req) {
		AppVersionReply reply = new AppVersionReply();
		if(null== req.getType()){
			reply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			reply.setMessage("参数不能为空");
			return reply;
		}
		if(0==req.getType()){
			reply.setVersionCode(versionCode);
			reply.setUrl(url);
			reply.setDesc(desc);
			reply.setMustInstall(isMustInstall);
		}else{
			reply.setVersionCode(ios_versionCode);
			reply.setUrl(ios_url);
			reply.setDesc(ios_desc);
			reply.setMustInstall(ios_isMustInstall);
			
		}
		
		return reply;
	}

}
