package com.che.banner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.che.banner.entity.PageAdvert;

public interface PageAdvertMapper {

	@Select("SELECT * FROM  t_page_advert where groupkey = 'app_auction_index' and status=0 order by sort desc limit 5")
	public List<PageAdvert> select();
	
}
