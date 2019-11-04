package com.che.auction.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/9/5.
 */
public class WeiXinData implements Serializable {

  private long id;
  private Date addtime;			//添加时间
  private String timestamps;		//时间戳
  private String access_token;	//微信token值
  private String jsapi_ticket;	//微信ticket值

  public String getJsapi_ticket() {
    return jsapi_ticket;
  }
  public void setJsapi_ticket(String jsapi_ticket) {
    this.jsapi_ticket = jsapi_ticket;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public Date getAddtime() {
    return addtime;
  }
  public void setAddtime(Date addtime) {
    this.addtime = addtime;
  }
  public String getTimestamps() {
    return timestamps;
  }
  public void setTimestamps(String timestamps) {
    this.timestamps = timestamps;
  }
  public String getAccess_token() {
    return access_token;
  }
  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

}
