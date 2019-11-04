  
/**
 * 
 */
package com.che.common.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author karlhell
 *
 */
@Component
public class GlobalInfo {

	private static GlobalInfo globalInfo;
	
	public static GlobalInfo getInstance(){
		return globalInfo;
	}
	
	public GlobalInfo(){
		globalInfo = this;
	}
	
	@Value("${app.versionCode}")
	public Integer versionCode;
	
	@Value("${redis.ip}")
	public String redisIp;
	
	@Value("${allinpay.url}")
	public String API_URL;
	
	@Value("${allinpay.secret}")
	public String SECRET;
	
	@Value("${allinpay.app_key}")
	public String APP_KEY;
	
	@Value("${swftomp4.outFolder}")
	public String swftomp4OutFolder;
	
	@Value("${swftomp4.image}")
	public String swftomp4Image;
	
	@Value("${swftomp4.ffmpeg}")
	public String swftomp4Ffmpeg;
	
	@Value("${cxy.account}")
	public String cxyaccount;
	
	@Value("${cxy.url}")
	public String cxyurl;
	
	@Value("${cxy.secret}")
	public String cxysecret;
	
	@Value("${oss.endpoint}")
	public String ossEndpoint;

	@Value("${oss.accessKeyId}")
	public String ossAccessKeyId;
	
	@Value("${oss.accessKeySecret}")
	public String ossAccessKeySecret;
	
	@Value("${oss.bucketName}")
	public String ossBucketName;
	
	@Value("${oss.web.url}")
	public String ossWebUrl;
	
	@Value("${oss.path.certify}")
	public String ossPathCertify;
	
	@Value("${img.web.url}")
	public String imgWebUrl;
	
}
 
