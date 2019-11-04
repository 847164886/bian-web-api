package com.che.banner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.che.banner.entity.PageAdvert;
import com.che.banner.mapper.PageAdvertMapper;
import com.che.banner.pojo.PageAdvertListReply;
import com.che.banner.pojo.PageAdvertListReply.PageAdvertPojo;
import com.che.banner.pojo.PageAdvertListReq;
import com.che.common.web.Constants;
import com.google.common.collect.Lists;

@Service
public class PageAdvertServiceImpl implements PageAdvertService {

	@Autowired
	public PageAdvertMapper pageAdvertMapper;
	
	@Value("${adv.web.url}")
	private String advWebUrl;
	
	@Override
	public PageAdvertListReply list(PageAdvertListReq pageAdvertSelectReq) {

		PageAdvertListReply pageAdvertListReply = new PageAdvertListReply();
		List<PageAdvertPojo> pageAdvertPojoList = Lists.newArrayList();
		List<PageAdvert> avertList =pageAdvertMapper.select();
		for(PageAdvert pageAdvert: avertList){
			PageAdvertPojo pageAdvertPojo = new PageAdvertPojo();
			pageAdvertPojo.setId(pageAdvert.getId());
			pageAdvertPojo.setLink_url(pageAdvert.getLink_url());
			pageAdvertPojo.setPic(advWebUrl+pageAdvert.getPic());
			pageAdvertPojo.setTitle(pageAdvert.getTitle());
			pageAdvertPojoList.add(pageAdvertPojo);
		}
		pageAdvertListReply.setPageAdvertPojoList(pageAdvertPojoList);
		
		pageAdvertListReply.setReplyCode(Constants.REPLY_SUCCESS);
		
		return pageAdvertListReply;
	}

}
