package com.che.auction.mapper;

import com.che.auction.entity.WeiXinData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface CommonMapper {



  @Select("select * from t_weixin_share order by addtime desc LIMIT 1")
  public WeiXinData getWeiXinAccessToken();

  @Insert("INSERT INTO t_weixin_share (addtime, timestamps, access_token, jsapi_ticket) VALUE (#{addtime}, #{timestamps}, #{access_token}, #{jsapi_ticket})")
  public void addWeiXinAccessToken(WeiXinData data);

}
