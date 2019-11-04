package com.che.user.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import com.che.user.model.dto.ShopUserOutputDTO;

@Service
public class UserCommonServiceImpl implements UserCommonService{

    public ShopUserOutputDTO getUser() {
    	ShopUserOutputDTO user = null;
        if(SecurityUtils.getSubject().isAuthenticated()){
            user = (ShopUserOutputDTO)SecurityUtils.getSubject().getPrincipal();
        }
        return user;
    }
    
    @Override
    public Long getUserId() {
    	ShopUserOutputDTO userEntity = getUser();
        if(null != userEntity){
           return userEntity.getUserId(); 
        }
        return null;
    }

    @Override
    public String getSessionId() {
        String sessionId = null;
        Session session = SecurityUtils.getSubject().getSession();
        if( null != session){
            sessionId = session.getId().toString();
        }
        return sessionId;
    }
    
}
