/**
 * 
 */
package com.che.user.service;

import com.che.user.pojo.AppVersionReply;
import com.che.user.pojo.AppVersionReq;

/**
 * @author karlhell
 *
 */
public interface AppVersionService {

	AppVersionReply version(AppVersionReq req);
	
}
