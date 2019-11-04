package com.che.message.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.che.message.entity.AuctionMessage;
import com.che.message.entity.AuctionMessageText;

public interface AuctionMessageMapper {

	@Select(" SELECT COUNT(1) FROM  `a_auction_message_text` a  WHERE (a.`type`<>2 and  a.rec_id in (#{user_id},0) OR ( NOW() BETWEEN a.start_time AND a.end_time AND a.`type`=2) ) AND NOT EXISTS ( SELECT * FROM  `a_auction_message` b  WHERE a.id =b.message_id AND b.rec_id=#{user_id}) ")
	public Integer selectUnread(Long user_id);
	
	@Select("SELECT * FROM ( SELECT a.*, IF(b.id IS NULL,0,1) AS `read` FROM  `a_auction_message_text` a LEFT JOIN `a_auction_message` b  ON a.id =b.message_id AND b.rec_id=#{user_id} WHERE  (a.`type`<>2 AND  a.rec_id in (#{user_id},0) ) OR ( NOW() BETWEEN a.start_time AND a.end_time AND a.`type`=2)      )bb ORDER BY bb.read,bb.id DESC")
	public List<AuctionMessageText> selectList(Long user_id);
	
	@Select (" select * from a_auction_message_text where id =#{id}")
	public AuctionMessageText selectTextById(Long id);
	
	@Select (" select count(1) from a_auction_message where message_id =#{message_id} and rec_id=#{rec_id}")
	public Integer ifRead(@Param("message_id") Long message_id,@Param("rec_id") Long user_id);
	
	@Insert (" insert into  a_auction_message (rec_id,message_id) values(#{rec_id},#{message_id})   ")
	public int insertRead(AuctionMessage auctionMessage);
	
	@Insert({ "<script>INSERT INTO a_auction_message (rec_id, message_id) VALUES"+
			"<foreach collection='list' item='item' index='index' separator=','>(#{item.rec_id}, #{item.message_id})"+
			"</foreach></script>" })
	public int allRead(List<AuctionMessage> auctionMessageList);
	
	@Select(" SELECT a.id AS message_id,rec_id  FROM  `a_auction_message_text` a  WHERE ( a.`type`<>2 and  a.rec_id in (#{user_id},0) OR ( NOW() BETWEEN a.start_time AND a.end_time AND a.`type`=2)) AND NOT EXISTS ( SELECT * FROM  `a_auction_message` b  WHERE a.id =b.message_id AND b.rec_id=#{user_id}) ")
	public List<AuctionMessage> selectUnreadList(Long user_id);
	
}
