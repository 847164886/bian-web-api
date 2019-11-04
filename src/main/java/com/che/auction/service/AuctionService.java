package com.che.auction.service;

import com.che.auction.pojo.*;
import com.che.search.maintenance.entity.MaintenanceDetail;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

public interface AuctionService {

	public CheckCarInfoReply carinfo(CheckCarInfoReq checkCarInfoReq);
	
	public CheckCarInfoReply carInfoForH5(CheckCarInfoReq checkCarInfoReq);

	public AuctionHistoryReply auctionHistory(AuctionHistoryReq auctionHistoryReq);

	public AuctionHistoryReply auctionToned(AuctionHistoryReq auctionHistoryReq);

	public AuctionActinfoReply auctionList(AuctionActinfoReq auctionListReq);

	public CheckCarBaseInfoReply carBaseInfo(CheckCarBaseInfoReq checkCarBaseInfoReq);

	public CheckCarProcedureReply carProcedure(CheckCarProcedureReq checkCarProcedureReq);

	public CheckTypeReportReply checkReport(CheckTypeReportReq checkTypeReportReq);

	public AuctionActinfoReply actinfo(AuctionActinfoReq auctionCarnumReq);

	public AuctionBidReply bid(AuctionBidReq auctionBidReq);

	public AuctionAttentionReply attention(AuctionAttentionReq auctionAttentionReq);

	public CheckTypeReportDetailReply checkReportDetail(CheckTypeReportReq checkTypeReportReq);
	
	public List<String > getPhotoByCarId(Long  carId);
	
	public MaintenanceDetail getMaintenanceByCarId(Long check_car_id);
  /**
   * 微信分享(返回json格式)
   */
  public Map<String,Object> weiXinShare(ModelMap model, String shareUrl, boolean isJson);

}
