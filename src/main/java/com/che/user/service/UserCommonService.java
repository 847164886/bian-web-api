package com.che.user.service;

import com.che.user.model.dto.ShopUserOutputDTO;

public interface UserCommonService {
    
    /**
     * 获取当前登录用户entity
     */
    public ShopUserOutputDTO getUser();
    
    /**
     * 获取当前登录用户id
     */
    public Long getUserId();
    
    /**
     * 获取当前SesssionId
     */
    public String getSessionId();
    
}
