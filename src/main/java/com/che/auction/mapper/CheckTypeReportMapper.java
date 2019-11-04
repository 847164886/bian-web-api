package com.che.auction.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.che.auction.entity.CheckRelatePic;
import com.che.auction.entity.CheckTypeReport;
import com.che.auction.entity.CheckTypeReportDetail;
import com.che.auction.pojo.CheckCarInfoReply.CheckTypeReportOverviewPojo;

public interface CheckTypeReportMapper {

	@Select(" SELECT  a.type_id,a.`check_unusual`,c.`pic_path`  FROM `a_check_type_report` a  JOIN `a_check_item_info` b ON  a.type_id =b.type_id  JOIN `a_check_relate_pic` c ON b.id = c.`category_id` WHERE  a.order_id =#{order_id} AND  c.order_id=#{order_id}  AND c.stauts=1 AND c.`type`=3")
	public List<CheckTypeReport> selectUnusualNum(Long order_id);

	@Select(" SELECT  a.type_id,a.`check_unusual`   FROM `a_check_type_report` a  WHERE  a.order_id =#{order_id}  ")
	public List<CheckTypeReport> selectUnusual(Long order_id);

	@Select("SELECT x.type_id, x.num, sum(x.img) AS imgNum FROM (SELECT b.type_id AS type_id, a.check_unusual AS num, CASE WHEN (c.pic_path IS NULL OR c.pic_path = '') THEN 0 ELSE 1 END AS img FROM `a_check_type_report` a INNER JOIN `a_check_item_info_report` b ON a.type_id = b.type_id AND a.order_id = b.order_id LEFT JOIN `a_check_relate_pic` c ON b.item_id = c.`category_id` AND c.order_id = #{order_id} AND c.stauts = 1 AND c.type = 3 WHERE a.order_id = #{order_id} AND b.param_value = 0) AS x GROUP BY x.type_id,x.num")
	public List<CheckTypeReportOverviewPojo> selectReportByOrderId(Long order_id);

	@Select("SELECT a.type_id AS type_id, a.item_id AS item_id, e.NAME AS item_name,d.id AS special_id,d.NAME AS special_name,c.param_value AS special_status "
			+ " FROM a_check_item_info_report AS a INNER JOIN a_check_item_special_report AS c ON c.order_id = a.order_id AND a.type_id = c.type_id AND a.item_id = c.item_id INNER JOIN a_check_item_special AS d ON c.special_id = d.id INNER JOIN a_check_item_info AS e ON e.id = a.item_id "
			+ " WHERE a.order_id = #{order_id} AND a.param_value = 0 " + " ORDER BY type_id, item_id, special_id")
	public List<CheckTypeReportDetail> selectCheckTypeReportDetails(Long order_id);

	@Select("select order_id,category_id,pic_path from a_check_relate_pic where order_id = #{order_id} and stauts = 1 and type=3")
	public List<CheckRelatePic> selectPicsByOrderId(Long order_id);

}
