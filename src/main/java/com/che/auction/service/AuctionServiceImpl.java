package com.che.auction.service;

import cn.com.che.util.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.che.auction.entity.*;
import com.che.auction.mapper.*;
import com.che.auction.pojo.*;
import com.che.auction.pojo.AuctionActinfoReply.AuctionActinfoPojo;
import com.che.auction.pojo.AuctionActinfoReply.CheckOrderCarPojo;
import com.che.auction.pojo.AuctionHistoryReply.AuctionHistoryPojo;
import com.che.auction.pojo.CheckCarBaseInfoReply.CheckCarBaseInfoPojo;
import com.che.auction.pojo.CheckCarInfoReply.CheckCarPojo;
import com.che.auction.pojo.CheckCarInfoReply.CheckTypeReportOverviewPojo;
import com.che.auction.pojo.CheckCarInfoReply.MaintReportPojo;
import com.che.auction.pojo.CheckCarProcedureReply.CheckCarProcedurePojo;
import com.che.auction.pojo.CheckTypeReportDetailReply.ItemReportPojo;
import com.che.auction.pojo.CheckTypeReportDetailReply.SpecialReportPojo;
import com.che.common.util.MyStringUtils;
import com.che.common.web.Constants;
import com.che.maintenance.pojo.QueryStatus;
import com.che.search.exception.CheSearchException;
import com.che.search.maintenance.api.IMaintenanceService;
import com.che.search.maintenance.entity.MaintenanceDetail;
import com.che.user.interfaces.interf.user.UniteUserService;
import com.che.user.model.dto.UniteUserInputDTO;
import com.che.user.model.dto.UniteUserOutputDTO;
import com.che.user.service.UserCommonService;
import com.che.util.RandomUtils;
import com.che.util.WeiXinUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import core.DTO.OutputDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.math.BigDecimal;
import java.util.*;

@Service
public class AuctionServiceImpl implements AuctionService {
	@Autowired
	private AuctionPriceInfoMapper auctionPriceInfoMapper;

	@Autowired
	private AuctionActInfoMapper auctionActInfoMapper;

	@Autowired
	private AuctionPriceRecordMapper auctionPriceRecordMapper;

	@Autowired
	private AuctionRelateCarMapper auctionRelateCarMapper;

	@Autowired
	private AuctionAttentionMapper auctionAttentionMapper;

	@Autowired
	private CheckOrderCarMapper checkOrderCarMapper;

	@Autowired
	private CheckOrderMapper checkOrderMapper;

	@Autowired
	private CommonMapper commonMapper;

	@Autowired
	private UserCommonService userCommonService;

	@Autowired
	private CheckTypeReportMapper checkTypeReportMapper;

	@Reference(version = "2.0.1")
	private UniteUserService uniteUserService;
	
	@Reference(version = "1.0.0")
	private IMaintenanceService iMaintenanceService;

	@Value("${img.web.url}")
	public String imgWebUrl;

	@Value("${ifShowScore}")
	public String ifShowScore;
	
	private static final Log logger = LogFactory.getLog(AuctionServiceImpl.class);

	public final static Integer PREBID = 1; // 预拍

	public final static Integer BID = 2; // 竞拍

	public final static Integer BIDTIME = -30; // 竞拍时间点

	public final static Integer ATTENTION = 1; // 关注

	public final static Integer UNATTENTION = 0; // 取消关注或者未关注

	public final static Integer REPLY_CODE_UN_CERTIFI = -10; // 未认证或者认证失败

	public final static Integer REPLY_CODE_IN_CERTIFI = -11; // 认证中

	public final static Integer OFFER = 1;// 已报价

	public final static Integer UNOFFER = 0;// 未报价
	
	private static final String KEYWORDS = "事故";

	@Value("${auction.price.policy}")
	public String auctionPricePolicy;

	@Override
	public CheckCarInfoReply carinfo(CheckCarInfoReq checkCarInfoReq) {
		CheckCarInfoReply checkCarInfoReply = new CheckCarInfoReply();
		if (null == checkCarInfoReq.getAuction_id() || null == checkCarInfoReq.getCheck_car_id()) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("参数不能为空");
			return checkCarInfoReply;
		}

		// if(0<auctionActInfoMapper.ifEnd(checkCarInfoReq.getAuction_id())){
		List<AuctionPriceInfo> auctionPriceInfoList = auctionPriceInfoMapper
				.selectPrice(checkCarInfoReq.getCheck_car_id(), checkCarInfoReq.getAuction_id());
		if (auctionPriceInfoList.size() > 0) {
			for (int i = 0; i < auctionPriceInfoList.size(); i++) {
				AuctionPriceInfo auctionPriceInfo = auctionPriceInfoList.get(i);
				// 用户属于前3名
				if (auctionPriceInfo.getShop_user_id() != null && auctionPriceInfo.getShop_user_id().longValue() == userCommonService.getUserId().longValue()) {
					if (i < 3) {
						checkCarInfoReply.setBonus(i + 1);
					}
					checkCarInfoReply.setUser_price(auctionPriceInfo.getPrice());
					checkCarInfoReply.setStatus(auctionPriceInfo.getStatus());//竞拍状态
				}
			}

			// 获取当前场次当前车辆的成交价最高的作为成交价
			AuctionPriceInfo mAuctionPriceInfo = auctionPriceInfoMapper.selectSurePrice(checkCarInfoReq.getCheck_car_id(), checkCarInfoReq.getAuction_id());
			if (mAuctionPriceInfo != null && mAuctionPriceInfo.getSure_price() != null) {
				
				checkCarInfoReply.setPrice(new BigDecimal(mAuctionPriceInfo.getSure_price()));
			} else {
				checkCarInfoReply.setPrice(auctionPriceInfoList.get(0).getPrice());// 默认最高价
				if ("FIRST".equalsIgnoreCase(auctionPricePolicy)) {

				} else if ("SECOND".equalsIgnoreCase(auctionPricePolicy)) {
					// 如果有下一名
					if (1 < auctionPriceInfoList.size()) {
						checkCarInfoReply.setPrice(auctionPriceInfoList.get(1).getPrice());
					}
				}
			}
			
			// 用户不属于前3名
			if (null == checkCarInfoReply.getUser_price()) {
				AuctionPriceInfo auctionPriceInfo = auctionPriceInfoMapper.selectByUserId(
						checkCarInfoReq.getCheck_car_id(), checkCarInfoReq.getAuction_id(),
						userCommonService.getUserId());
				if (null != auctionPriceInfo) {
					checkCarInfoReply.setUser_price(auctionPriceInfo.getPrice());
					checkCarInfoReply.setStatus(auctionPriceInfo.getStatus()); //竞拍状态
				}
			}

		}
		
		// }

		// 正面图片
		List<String> pic_path = checkOrderCarMapper.selectImageById(checkCarInfoReq.getCheck_car_id());
		List<String> totolPicPath = Lists.newLinkedList();
		for (int i = 0; i < pic_path.size(); i++) {
			totolPicPath.add(imgWebUrl + pic_path.get(i));
		}
		checkCarInfoReply.setPic_path(totolPicPath);
		//vcr
		String vcr_path=checkOrderCarMapper.selectVcrById(checkCarInfoReq.getCheck_car_id());
		checkCarInfoReply.setVcr_path(vcr_path==null?"":imgWebUrl+vcr_path);
		// 结束时间
		checkCarInfoReply.setEnd_time(auctionActInfoMapper.selectEndTimeById(checkCarInfoReq.getAuction_id()));

		// 车辆基本信息+车辆手续
		CheckCarPojo checkCarPojo = new CheckCarPojo();
		BeanCopier checkOrderCarCopy = BeanCopier.create(CheckOrderCar.class, CheckCarPojo.class, false);
		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectPojoById(checkCarInfoReq.getCheck_car_id(),
				checkCarInfoReq.getAuction_id());
		if (null == checkOrderCar) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("该车辆不存在");
			return checkCarInfoReply;
		}
		checkOrderCarCopy.copy(checkOrderCar, checkCarPojo, null);
		
		// 如果当前用户为第一候选人，显示服务费；其他用户不显示
//		if (checkCarInfoReply.getBonus() == null || checkCarInfoReply.getBonus().intValue() != 1) {
//			checkCarPojo.setService_fee("");
//		} 
		
		// 拍照所在地 错误，重新赋值
		checkCarPojo.setCity_name(checkOrderCar.getPlate_city());
		// 表显里程 转换 向下兼容
		checkCarPojo.setDrive_km_single(checkOrderCar.getDrive_km());
		checkCarPojo.setDrive_km(this.formatDriveKm(checkOrderCar.getDrive_km()));
		// 是否流拍
//		checkCarPojo.setIfAuctionPass((checkOrderCar.getAuction_relate_stauts() != null
//				&& checkOrderCar.getAuction_relate_stauts().intValue() == 2) ? 1 : 0);
		// 解析json
		Map<String, String> map = (Map<String, String>) JSON.parse(checkOrderCar.getBase_param());
		if (null != map) {
			checkCarPojo.setDriving_mode(map.get("DRIVING_MODE"));
			checkCarPojo.setEngine_full_type(map.get("ENGINE_FUEL_TYPE"));
		}
		checkCarInfoReply.setCheckCarPojo(checkCarPojo);

		// 车辆评级
		CheckCarInfoReply.CheckOrderPojo checkOrderPojo = new CheckCarInfoReply.CheckOrderPojo();
		// 是否显示评分
		checkOrderPojo.setIfShowScore(new Integer(this.ifShowScore));
		CheckOrder checkOrder = checkOrderMapper.selectByCarId(checkCarInfoReq.getCheck_car_id());
		if (null == checkOrder) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("该车辆检测信息不存在");
			return checkCarInfoReply;
		}

		BeanCopier checkOrderCopy = BeanCopier.create(CheckOrder.class, CheckCarInfoReply.CheckOrderPojo.class, false);
		checkOrderCopy.copy(checkOrder, checkOrderPojo, null);

		// 补充信息
		StringBuffer extra = new StringBuffer();
//		extra.append(checkOrderCar.getOther_inc());
//		extra.append("\r\n");
		extra.append(checkOrderCar.getOther_remark());
		checkOrderPojo.setExtra(extra.toString());

		checkCarInfoReply.setCheckOrderPojo(checkOrderPojo);

		// 车损 只需缺陷数量和图片数量
		List<CheckTypeReportOverviewPojo> checkTypeReports = checkTypeReportMapper
				.selectReportByOrderId(checkOrderCar.getOrder_id());
		for (CheckTypeReportOverviewPojo checkTypeReport : checkTypeReports) {
			switch (checkTypeReport.getType_id()) {
			case 9:
				checkCarInfoReply.setWaiguan(checkTypeReport);
				break;
			case 10:
				checkCarInfoReply.setJiegou(checkTypeReport);
				break;
			case 11:
				checkCarInfoReply.setJixie(checkTypeReport);
				break;
			case 12:
				checkCarInfoReply.setDianqi(checkTypeReport);
				break;
			case 13:
				checkCarInfoReply.setNeishi(checkTypeReport);
				break;
			case 14:
				checkCarInfoReply.setDipan(checkTypeReport);
				break;
			default:
				break;
			}
		}
		
		// 查询维保报告
		if (checkOrderCar.getJd_order_id() != null && (checkOrderCar.getJd_status() != null && checkOrderCar.getJd_status().intValue() == QueryStatus.QUERYSUCC.value)) {
			
			MaintReportPojo maintReportPojo = new MaintReportPojo();
			try {
				MaintenanceDetail maintenanceDetail = iMaintenanceService.queryReport(Constants.SYSNAME, checkOrderCar.getJd_order_id());
				if (maintenanceDetail != null) {
					String report = maintenanceDetail.getReport();
					if (report != null) {
						List<Object> list = JSON.parseArray(report, Object.class);
						maintReportPojo.setCount(list.size());
						
						if (report.contains(KEYWORDS)) {
							maintReportPojo.setType(MaintReportPojo.Type.HAS_ACCIDENT);
						} else {
							maintReportPojo.setType(MaintReportPojo.Type.NO_ACCIDENT);
						}
					} else {
						maintReportPojo.setType(MaintReportPojo.Type.NO_MAINTRECORDS);
					}
				} else {
					maintReportPojo.setType(MaintReportPojo.Type.NO_MAINTRECORDS);
				}
				maintReportPojo.setJd_order_id(checkOrderCar.getJd_order_id());
				maintReportPojo.setJd_status(checkOrderCar.getJd_status());
				checkCarInfoReply.setMaintReportPojo(maintReportPojo);
			} catch (CheSearchException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		checkCarInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		return checkCarInfoReply;
	}

	private String formatDriveKm(String driveKm) {
		if (StringUtils.isBlank(driveKm))
			return null;
		BigDecimal km = new BigDecimal(driveKm).divide(new BigDecimal(10 * 1000));
		return km.setScale(1, BigDecimal.ROUND_DOWN).toString();
	}

	@Override
	public AuctionActinfoReply auctionList(AuctionActinfoReq auctionActinfoReq) {

		AuctionActinfoReply auctionCarnumReply = new AuctionActinfoReply();

		List<AuctionActinfoPojo> auctionActinfos = Lists.newArrayList();
		BeanCopier auctionActInfoCopy = BeanCopier.create(AuctionActInfo.class, AuctionActinfoPojo.class, false);
		List<AuctionActInfo> infos = auctionActInfoMapper.selectActInfo();
		for (AuctionActInfo auctionActInfo : infos) {

			Map<Long, AuctionAttention> attentionMap = new HashMap<Long, AuctionAttention>();
			List<AuctionAttention> auctionAttentionList = auctionAttentionMapper
					.selectUserAttentionList(userCommonService.getUserId(), auctionActInfo.getId());
			for (AuctionAttention auctionAttention : auctionAttentionList) {
				attentionMap.put(auctionAttention.getCar_id(), auctionAttention);
			}
			// 报价信息
			Set<Long> checkCarIds = new HashSet<Long>();
			List<AuctionPriceInfo> auctionPriceInfos = auctionPriceInfoMapper
					.selectAuctionByUserId(auctionActInfo.getId(), userCommonService.getUserId());
			for (AuctionPriceInfo auctionPriceInfo : auctionPriceInfos) {
				checkCarIds.add(auctionPriceInfo.getCheck_car_id());
			}

			AuctionActinfoPojo auctionActinfoPojo = new AuctionActinfoPojo();
			auctionActInfoCopy.copy(auctionActInfo, auctionActinfoPojo, null);

			List<CheckOrderCarPojo> checkOrderCarPojoList = auctionRelateCarMapper
					.selectPojoByActId(auctionActinfoPojo.getId());
			for (CheckOrderCarPojo checkOrderCarPojo : checkOrderCarPojoList) {
				if (null != attentionMap.get(checkOrderCarPojo.getCheck_car_id())) {
					checkOrderCarPojo.setIfAttention(ATTENTION);
				} else {
					checkOrderCarPojo.setIfAttention(UNATTENTION);
				}
				// 报价
				if (checkCarIds.contains(checkOrderCarPojo.getCheck_car_id())) {
					checkOrderCarPojo.setIfOffer(OFFER);
				} else {
					checkOrderCarPojo.setIfOffer(UNOFFER);
				}
				checkOrderCarPojo.setCover_image(imgWebUrl + checkOrderCarPojo.getCover_image());
				// 里程处理
				// 表显里程 转换 向下兼容
				String driveKm = checkOrderCarPojo.getDrive_km();
				checkOrderCarPojo.setDrive_km_single(driveKm);
				checkOrderCarPojo.setDrive_km(this.formatDriveKm(driveKm));
			}

			auctionActinfoPojo.setCheckOrderCarPojoList(checkOrderCarPojoList);
			auctionActinfos.add(auctionActinfoPojo);
		}
		auctionCarnumReply.setAuctionActinfos(auctionActinfos);
		auctionCarnumReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionCarnumReply;
	}

	@Override
	public AuctionActinfoReply actinfo(AuctionActinfoReq auctionActinfoReq) {

		AuctionActinfoReply auctionCarnumReply = new AuctionActinfoReply();

		List<AuctionActinfoPojo> auctionActinfos = Lists.newArrayList();
		BeanCopier auctionActInfoCopy = BeanCopier.create(AuctionActInfo.class, AuctionActinfoPojo.class, false);
		List<AuctionActInfo> infos = auctionActInfoMapper.selectActInfo();
		for (AuctionActInfo auctionActInfo : infos) {
			AuctionActinfoPojo auctionActinfoPojo = new AuctionActinfoPojo();
			auctionActInfoCopy.copy(auctionActInfo, auctionActinfoPojo, null);
			auctionActinfos.add(auctionActinfoPojo);
		}
		auctionCarnumReply.setAuctionActinfos(auctionActinfos);
		auctionCarnumReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionCarnumReply;
	}

	@Override
	public AuctionAttentionReply attention(AuctionAttentionReq auctionAttentionReq) {
		AuctionAttentionReply auctionAttentionReply = new AuctionAttentionReply();
		if (null == auctionAttentionReq.getAuction_id() || null == auctionAttentionReq.getCheck_car_id()
				|| null == auctionAttentionReq.getType()) {
			auctionAttentionReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionAttentionReply.setMessage("参数不能为空");
			return auctionAttentionReply;
		}

		AuctionAttention auctionAttention = new AuctionAttention();
		auctionAttention.setCar_id(auctionAttentionReq.getCheck_car_id());
		auctionAttention.setAuction_id(auctionAttentionReq.getAuction_id());
		auctionAttention.setUser_id(userCommonService.getUserId());

		if (UNATTENTION == auctionAttentionReq.getType()) {
			auctionAttentionMapper.delete(auctionAttention);
		} else {

			AuctionAttention exist = auctionAttentionMapper.selectByAll(auctionAttention);
			if (null != exist) {
				auctionAttentionReply.setReplyCode(Constants.REPLY_SUCCESS);
				return auctionAttentionReply;
			}
			auctionAttentionMapper.insert(auctionAttention);
		}

		auctionAttentionReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionAttentionReply;
	}

	@Override
	public AuctionHistoryReply auctionHistory(AuctionHistoryReq auctionHistoryReq) {

		AuctionHistoryReply auctionHistoryReply = new AuctionHistoryReply();

		PageHelper.startPage(auctionHistoryReq.getPage(), auctionHistoryReq.getPageSize());

		List<AuctionHistoryPojo> auctionHistorys = auctionPriceInfoMapper
				.selectHistoryListByUserId(userCommonService.getUserId());

		Page<AuctionHistoryPojo> page = (Page<AuctionHistoryPojo>) auctionHistorys;

		for (AuctionHistoryPojo auctionHistoryPojo : page.getResult()) {
			auctionHistoryPojo.setCover_image(imgWebUrl + auctionHistoryPojo.getCover_image());
		}

		auctionHistoryReply.setAuctionHistorys(page.getResult());

		auctionHistoryReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionHistoryReply;
	}

	@Override
	public AuctionHistoryReply auctionToned(AuctionHistoryReq auctionHistoryReq) {

		AuctionHistoryReply auctionHistoryReply = new AuctionHistoryReply();

		PageHelper.startPage(auctionHistoryReq.getPage(), auctionHistoryReq.getPageSize());

		List<AuctionHistoryPojo> auctionHistorys = auctionPriceInfoMapper
				.selectTonedListByUserId(userCommonService.getUserId());

		Page<AuctionHistoryPojo> page = (Page<AuctionHistoryPojo>) auctionHistorys;

		for (AuctionHistoryPojo auctionHistoryPojo : page.getResult()) {
			auctionHistoryPojo.setCover_image(imgWebUrl + auctionHistoryPojo.getCover_image());
		}

		auctionHistoryReply.setAuctionHistorys(page.getResult());

		auctionHistoryReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionHistoryReply;
	}

	@SuppressWarnings("unchecked")
	@Override
	public CheckCarBaseInfoReply carBaseInfo(CheckCarBaseInfoReq checkCarBaseInfoReq) {
		CheckCarBaseInfoReply checkCarBaseInfoReply = new CheckCarBaseInfoReply();

		if (null == checkCarBaseInfoReq.getCheck_car_id()) {
			checkCarBaseInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarBaseInfoReply.setMessage("参数不能为空");
			return checkCarBaseInfoReply;
		}

		CheckCarBaseInfoPojo checkCarBaseInfoPojo = new CheckCarBaseInfoPojo();
		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectById(checkCarBaseInfoReq.getCheck_car_id());

		BeanCopier checkOrderCarCopy = BeanCopier.create(CheckOrderCar.class, CheckCarBaseInfoPojo.class, false);
		checkOrderCarCopy.copy(checkOrderCar, checkCarBaseInfoPojo, null);
		
		// 去掉非1的json属性
		String function_param = checkCarBaseInfoPojo.getFunction_param();
		Map<String, Integer> function_map = (Map<String, Integer>) JSON.parse(function_param);
		for (Iterator<Map.Entry<String, Integer>> it = function_map.entrySet().iterator(); it.hasNext();) {
			Integer value = it.next().getValue();
			if (value != 1) {
				it.remove();
			}
		}
		checkCarBaseInfoPojo.setFunction_param(JSON.toJSONString(function_map));
		
		// 解析json
		Map<String, String> map = (Map<String, String>) JSON.parse(checkOrderCar.getBase_param());
		if (null != map) {

			checkCarBaseInfoPojo.setSpare_tire(map.get("SPARE_TIRE_TYPE"));
			checkCarBaseInfoPojo.setFactory_name(map.get("FACTORY_NAME"));
			checkCarBaseInfoPojo.setDriving_mode(map.get("DRIVING_MODE"));
			checkCarBaseInfoPojo.setEngine_full_type(map.get("ENGINE_FUEL_TYPE"));
		}
		// hidevin
		if (checkOrderCar.getVin_number().length() > 7) {
			checkCarBaseInfoPojo.setVin_number(MyStringUtils.hideStr(checkOrderCar.getVin_number(),
					checkOrderCar.getVin_number().length() - 7, 4));
		}
		// 里程处理
		checkCarBaseInfoPojo.setDrive_km_single(checkOrderCar.getDrive_km());
		checkCarBaseInfoPojo.setDrive_km(this.formatDriveKm(checkOrderCar.getDrive_km()));

		checkCarBaseInfoReply.setCheckCarBaseInfoPojo(checkCarBaseInfoPojo);
		checkCarBaseInfoReply.setReplyCode(Constants.REPLY_SUCCESS);

		return checkCarBaseInfoReply;
	}

	@Override
	public CheckCarProcedureReply carProcedure(CheckCarProcedureReq checkCarProcedureReq) {
		CheckCarProcedureReply checkCarProcedureReply = new CheckCarProcedureReply();

		if (null == checkCarProcedureReq.getCheck_car_id()) {
			checkCarProcedureReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarProcedureReply.setMessage("参数不能为空");
			return checkCarProcedureReply;
		}

		CheckCarProcedurePojo checkCarProcedurePojo = new CheckCarProcedurePojo();
		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectById(checkCarProcedureReq.getCheck_car_id());

		BeanCopier checkOrderCarCopy = BeanCopier.create(CheckOrderCar.class, CheckCarProcedurePojo.class, false);
		checkOrderCarCopy.copy(checkOrderCar, checkCarProcedurePojo, null);

		checkCarProcedureReply.setCheckCarProcedurePojo(checkCarProcedurePojo);
		checkCarProcedureReply.setReplyCode(Constants.REPLY_SUCCESS);

		return checkCarProcedureReply;
	}

	@Override
	public CheckTypeReportReply checkReport(CheckTypeReportReq checkTypeReportReq) {
		CheckTypeReportReply checkTypeReportReply = new CheckTypeReportReply();

		if (null == checkTypeReportReq.getCheck_car_id()) {
			checkTypeReportReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkTypeReportReply.setMessage("参数不能为空");
			return checkTypeReportReply;
		}

		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectById(checkTypeReportReq.getCheck_car_id());
		if (null == checkOrderCar) {
			checkTypeReportReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkTypeReportReply.setMessage("该车不存在");
			return checkTypeReportReply;
		}

		// 异常数目
		List<CheckTypeReport> checkTypeReportNum = checkTypeReportMapper.selectUnusual(checkOrderCar.getOrder_id());
		for (CheckTypeReport checkTypeReport : checkTypeReportNum) {
			switch (checkTypeReport.getType_id().toString()) {
			case "9":
				checkTypeReportReply.getWaiguan().setNum(checkTypeReport.getCheck_unusual());
				break;
			case "10":
				checkTypeReportReply.getJiegou().setNum(checkTypeReport.getCheck_unusual());
				break;
			case "11":
				checkTypeReportReply.getJixie().setNum(checkTypeReport.getCheck_unusual());
				break;
			case "12":
				checkTypeReportReply.getDianqi().setNum(checkTypeReport.getCheck_unusual());
				break;
			case "13":
				checkTypeReportReply.getNeishi().setNum(checkTypeReport.getCheck_unusual());
				break;
			case "14":
				checkTypeReportReply.getDipan().setNum(checkTypeReport.getCheck_unusual());
				break;
			default:
				break;
			}
		}

		// 异常图片
		List<CheckTypeReport> checkTypeReportList = checkTypeReportMapper.selectUnusualNum(checkOrderCar.getOrder_id());
		for (CheckTypeReport checkTypeReport : checkTypeReportList) {
			switch (checkTypeReport.getType_id().toString()) {
			case "9":
				checkTypeReportReply.getWaiguan().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			case "10":
				checkTypeReportReply.getJiegou().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			case "11":
				checkTypeReportReply.getJixie().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			case "12":
				checkTypeReportReply.getDianqi().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			case "13":
				checkTypeReportReply.getNeishi().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			case "14":
				checkTypeReportReply.getDipan().getImages().add(imgWebUrl + checkTypeReport.getPic_path());
				break;
			default:
				break;
			}
		}

		checkTypeReportReply.setReplyCode(Constants.REPLY_SUCCESS);
		return checkTypeReportReply;
	}
	/*
	 * public static void main(String[] args) { Calendar calendar =
	 * Calendar.getInstance(); Date now = calendar.getTime(); //
	 * calendar.setTime(auctionActInfo.getEnd_time());
	 * calendar.add(Calendar.MINUTE, BIDTIME); Date auctionTime=
	 * calendar.getTime();
	 * //System.out.println("getStar_time="+auctionActInfo.getStar_time());
	 * //System.out.println("getEnd_time="+auctionActInfo.getEnd_time());
	 * System.out.println("auctionTime="+auctionTime);
	 * System.out.println("now="+now);
	 * System.out.println("now.before(auctionTime)="+now.before(auctionTime));
	 * calendar.setTime(auctionTime); System.out.println(calendar.getTime()); }
	 */

	@Override
	public AuctionBidReply bid(AuctionBidReq auctionBidReq) {
		if (userCommonService.getUser() != null) {
			
			logger.info("出价接口=========" + "用户认证名：" + userCommonService.getUser().getName() + ", 用户手机号：" + userCommonService.getUser().getMobile() + ", 用户id：" + userCommonService.getUser().getUserId()+", 用户session：" + userCommonService.getSessionId());
		} else {
			logger.info("出价接口=========用户id：" + userCommonService.getUserId() + "，用户session：" + userCommonService.getSessionId());
		}
		
		AuctionBidReply auctionBidReply = new AuctionBidReply();
		
		// 登录异常，直接退回到登录
		Long userId = userCommonService.getUserId();
		if (userId == null) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_LOGIN);
			auctionBidReply.setMessage("登录异常");
			return auctionBidReply;
		} 

		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();

		if (null == auctionBidReq.getAuction_id() || null == auctionBidReq.getCheck_car_id()
				|| null == auctionBidReq.getPrice()) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionBidReply.setMessage("参数不能为空");
			return auctionBidReply;
		}

		UniteUserInputDTO param = new UniteUserInputDTO();
		param.setCode(userCommonService.getUser().getCode());
		OutputDTO<UniteUserOutputDTO> outputDTO = uniteUserService.queryUniterUserByCode(param);
		if (!"0".equals(outputDTO.getCode())) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionBidReply.setMessage(outputDTO.getMessage());
			return auctionBidReply;
		}
		UniteUserOutputDTO uniteUserOutputDTO = outputDTO.getData();

		if (0 == uniteUserOutputDTO.getCertificationType() || 2 == uniteUserOutputDTO.getCertificationStatus()) {
			auctionBidReply.setReplyCode(REPLY_CODE_UN_CERTIFI);
			auctionBidReply.setMessage("未认证或者认证失败");
			return auctionBidReply;
		}

		if (1 != uniteUserOutputDTO.getCertificationStatus()) {
			auctionBidReply.setReplyCode(REPLY_CODE_IN_CERTIFI);
			auctionBidReply.setMessage("认证中");
			return auctionBidReply;
		}

		AuctionActInfo auctionActInfo = auctionActInfoMapper.selectById(auctionBidReq.getAuction_id());

		if (null == auctionActInfo || now.before(auctionActInfo.getStar_time())) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionBidReply.setMessage("非常抱歉，本场竞价未开始");
			return auctionBidReply;
		}
		if (now.after(auctionActInfo.getEnd_time())) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionBidReply.setMessage("非常抱歉，本场竞价已结束");
			return auctionBidReply;
		}

		AuctionRelateCar auctionRelateCar = auctionRelateCarMapper.selectByCarIdAndActId(auctionBidReq.getAuction_id(),
				auctionBidReq.getCheck_car_id());
		if (null == auctionRelateCar) {
			auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			auctionBidReply.setMessage("场次和车辆不匹配");
			return auctionBidReply;
		}

		calendar.setTime(auctionActInfo.getEnd_time());
		calendar.add(Calendar.MINUTE, BIDTIME);
		Date auctionTime = calendar.getTime();
		System.out.println("getStar_time=" + auctionActInfo.getStar_time());
		System.out.println("getEnd_time=" + auctionActInfo.getEnd_time());
		System.out.println("auctionTime=" + auctionTime);
		System.out.println("now=" + now);
		System.out.println("now.before(auctionTime)=" + now.before(auctionTime));
		
	

		AuctionPriceInfo existPriceInfo = auctionPriceInfoMapper.ifExist(auctionRelateCar.getId(),
				userCommonService.getUserId());
		if (now.before(auctionTime)) {
			// 预出价阶段
			if (null == existPriceInfo) {
				// 初次出价
				this.insertAuctionPrice(auctionBidReq, auctionRelateCar.getId(), PREBID, now);
			} else {
				existPriceInfo.setPre_price(auctionBidReq.getPrice());
				existPriceInfo.setPrice(auctionBidReq.getPrice());
				existPriceInfo.setPrice_time(now);
				auctionPriceInfoMapper.updatePrice(existPriceInfo);
			}
//			Long userId = userCommonService.getUserId();
//			if (userId != null) {
				
				this.insertRecord(auctionBidReq.getPrice(), auctionRelateCar.getId(), PREBID, now);
//			} 
//			else {
//				auctionBidReply.setReplyCode(Constants.RESULT_ERROR_LOGIN);
//				auctionBidReply.setMessage("登录异常");
//				return auctionBidReply;
//			}
		} else {
			// 竞价阶段
			if (null == existPriceInfo) {
				// 初次出价
//				Long userId = userCommonService.getUserId();
//				if (userId != null) {
					this.insertAuctionPrice(auctionBidReq, auctionRelateCar.getId(), BID, now);

					this.insertRecord(auctionBidReq.getPrice(), auctionRelateCar.getId(), BID, now);
//				}else {
//					auctionBidReply.setReplyCode(Constants.RESULT_ERROR_LOGIN);
//					auctionBidReply.setMessage("登录异常");
//					return auctionBidReply;
//				}
			} else {

				if (PREBID == existPriceInfo.getPrice_stage()) {
					existPriceInfo.setPrice(auctionBidReq.getPrice());
					existPriceInfo.setPrice_time(now);
					existPriceInfo.setPrice_stage(BID);// 2竞价阶段
					auctionPriceInfoMapper.updatePrice(existPriceInfo);
//					Long userId = userCommonService.getUserId();
//					if (userId != null) {
						this.insertRecord(auctionBidReq.getPrice(), auctionRelateCar.getId(), BID, now);
//					}else {
//						auctionBidReply.setReplyCode(Constants.RESULT_ERROR_LOGIN);
//						auctionBidReply.setMessage("登录异常");
//						return auctionBidReply;
//					}
				} else {
					auctionBidReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
					auctionBidReply.setMessage("非常抱歉，竞价阶段只能输入一次价格");
					return auctionBidReply;
				}

			}

		}

		auctionBidReply.setReplyCode(Constants.REPLY_SUCCESS);

		return auctionBidReply;

	}

	private void insertRecord(BigDecimal price, Long relate_id, Integer type, Date now) {

		AuctionPriceRecord auctionPriceRecord = new AuctionPriceRecord();
		auctionPriceRecord.setPrice(price);
		auctionPriceRecord.setRelate_id(relate_id);
		auctionPriceRecord.setType(type);
		auctionPriceRecord.setShop_user_id(userCommonService.getUserId());
		auctionPriceRecord.setAddTime(now);

		auctionPriceRecordMapper.insert(auctionPriceRecord);
	}

	private void insertAuctionPrice(AuctionBidReq auctionBidReq, Long relate_id, Integer type, Date now) {

		AuctionPriceInfo auctionPriceInfo = new AuctionPriceInfo();
		auctionPriceInfo.setAddTime(now);
		auctionPriceInfo.setCheck_car_id(auctionBidReq.getCheck_car_id());
		if (PREBID == type) {
			auctionPriceInfo.setPre_price(auctionBidReq.getPrice());
		}
		auctionPriceInfo.setPrice(auctionBidReq.getPrice());
		auctionPriceInfo.setPrice_time(now);
		auctionPriceInfo.setRelate_id(relate_id);
		auctionPriceInfo.setAuction_id(auctionBidReq.getAuction_id());
		auctionPriceInfo.setShop_user_id(userCommonService.getUserId());
		auctionPriceInfo.setStatus(0);
		auctionPriceInfo.setPrice_stage(type);
		auctionPriceInfoMapper.insert(auctionPriceInfo);
	}

	@Override
	public CheckTypeReportDetailReply checkReportDetail(CheckTypeReportReq checkTypeReportReq) {
		CheckTypeReportDetailReply ret = new CheckTypeReportDetailReply();
		// 1.valid
		if (null == checkTypeReportReq.getCheck_car_id()) {
			ret.setReplyCode(Constants.RESULT_ERROR_PARAM);
			ret.setMessage("参数不能为空");
			return ret;
		}
		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectById(checkTypeReportReq.getCheck_car_id());
		if (null == checkOrderCar) {
			ret.setReplyCode(Constants.RESULT_ERROR_PARAM);
			ret.setMessage("该车不存在");
			return ret;
		}

		// 2.begin
		List<CheckTypeReportDetail> checkTypeReportDetails = checkTypeReportMapper
				.selectCheckTypeReportDetails(checkOrderCar.getOrder_id());
		// 图片
		List<CheckRelatePic> pics = checkTypeReportMapper.selectPicsByOrderId(checkOrderCar.getOrder_id());

		// 检测项,key=type_id
		Map<Integer, Map<Integer, ItemReportPojo>> itemMap = this.groupCheckReportData(checkTypeReportDetails, pics);
		for (Integer typeId : itemMap.keySet()) {
			switch (typeId) {
			case 9:
				ret.getWaiguan().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			case 10:
				ret.getJiegou().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			case 11:
				ret.getJixie().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			case 12:
				ret.getDianqi().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			case 13:
				ret.getNeishi().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			case 14:
				ret.getDipan().getItemReports().addAll(itemMap.get(typeId).values());
				break;
			default:
				break;
			}
		}
		ret.setReplyCode(Constants.REPLY_SUCCESS);
		return ret;
	}

	private Map<Integer, Map<Integer, ItemReportPojo>> groupCheckReportData(
			List<CheckTypeReportDetail> checkTypeReportDetails, List<CheckRelatePic> pics) {
		// 图片map
		Map<Integer, String> picMap = new HashMap<Integer, String>();
		for (CheckRelatePic pic : pics) {
			picMap.put(pic.getCategory_id(), pic.getPic_path());
		}
		// 检测异常项,key=item_id
		Map<Integer, List<SpecialReportPojo>> specialMap = new HashMap<Integer, List<SpecialReportPojo>>();
		// 检测项,key=type_id,value-key=item_id
		Map<Integer, Map<Integer, ItemReportPojo>> itemMap = new HashMap<Integer, Map<Integer, ItemReportPojo>>();
		for (CheckTypeReportDetail detail : checkTypeReportDetails) {
			Integer itemId = detail.getItem_id();
			Integer typeId = detail.getType_id();
			// 检测异常项
			SpecialReportPojo special = new SpecialReportPojo();
			special.setSpecialName(detail.getSpecial_name());
			special.setSpecialStatus(detail.getSpecial_status());
			if (!specialMap.containsKey(itemId)) {
				List<SpecialReportPojo> list = new ArrayList<SpecialReportPojo>();
				specialMap.put(itemId, list);
			}
			specialMap.get(itemId).add(special);
			// 检测项
			if (!itemMap.containsKey(typeId)) {
				Map<Integer, ItemReportPojo> map = new HashMap<Integer, ItemReportPojo>();
				itemMap.put(typeId, map);
			}
			Map<Integer, ItemReportPojo> items = itemMap.get(typeId);
			if (!items.containsKey(itemId)) {
				ItemReportPojo item = new ItemReportPojo();
				item.setItemId(detail.getItem_id());
				item.setItemName(detail.getItem_name());
				// 放入图片url
				if (picMap.containsKey(itemId)) {
					item.setPicPath(this.imgWebUrl + picMap.get(itemId));
				}
				item.setSpecialReports(specialMap.get(itemId));
				items.put(itemId, item);
			}
		}
		return itemMap;
	}
	//
	// public static void main(String[] args) {
	// List<CheckTypeReportDetail> checkTypeReportDetails = new ArrayList<>();
	// CheckTypeReportDetail d1 = new CheckTypeReportDetail();
	// CheckTypeReportDetail d2 = new CheckTypeReportDetail();
	// CheckTypeReportDetail d3 = new CheckTypeReportDetail();
	// checkTypeReportDetails.add(d1);
	// checkTypeReportDetails.add(d2);
	// checkTypeReportDetails.add(d3);
	//
	// d1.setType_id(9);
	// d2.setType_id(9);
	// d3.setType_id(9);
	//
	// d1.setItem_id(10);
	// d2.setItem_id(20);
	// d3.setItem_id(20);
	//
	// d1.setItem_name("i1");
	// d2.setItem_name("i2");
	// d3.setItem_name("i2");
	//
	// d1.setSpecial_id(100);
	// d2.setSpecial_id(200);
	// d3.setSpecial_id(300);
	//
	// d1.setSpecial_name("s1");
	// d2.setSpecial_name("s2");
	// d3.setSpecial_name("s3");
	//
	// d1.setSpecial_status(1000);
	// d2.setSpecial_status(2000);
	// d3.setSpecial_status(3000);
	//
	// AuctionServiceImpl im = new AuctionServiceImpl();
	// CheckTypeReportDetailReply ret = new CheckTypeReportDetailReply();
	// // 2.begin
	// // 检测项,key=type_id
	// Map<Integer, Map<Integer, ItemReportPojo>> itemMap =
	// im.groupCheckReportData(checkTypeReportDetails);
	// for (Integer typeId : itemMap.keySet()) {
	// switch (typeId) {
	// case 9:
	// ret.getWaiguan().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// case 10:
	// ret.getJiegou().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// case 11:
	// ret.getJixie().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// case 12:
	// ret.getDianqi().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// case 13:
	// ret.getNeishi().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// case 14:
	// ret.getDipan().getItemReports().addAll(itemMap.get(typeId).values());
	// break;
	// default:
	// break;
	// }
	// }
	//
	// System.out.println(JSON.toJSONString(ret));
	// }

	@Override
	public CheckCarInfoReply carInfoForH5(CheckCarInfoReq checkCarInfoReq) {
		checkOrderCarMapper.countVisitIncrement();
		CheckCarInfoReply checkCarInfoReply = new CheckCarInfoReply();
		if (checkCarInfoReq==null||null == checkCarInfoReq.getAuction_id() || null == checkCarInfoReq.getCheck_car_id()) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("参数不能为空");
			return checkCarInfoReply;
		}
		// 正面图片
		List<String> pic_path = checkOrderCarMapper.selectImageById(checkCarInfoReq.getCheck_car_id());
		List<String> totolPicPath = Lists.newLinkedList();
		for (int i = 0; i < pic_path.size(); i++) {
			totolPicPath.add(imgWebUrl + pic_path.get(i));
		}
		checkCarInfoReply.setPic_path(totolPicPath);
		//vcr
		String vcr_path=checkOrderCarMapper.selectVcrById(checkCarInfoReq.getCheck_car_id());
		checkCarInfoReply.setVcr_path(vcr_path==null?"":vcr_path);
		// 结束时间
		checkCarInfoReply.setEnd_time(auctionActInfoMapper.selectEndTimeById(checkCarInfoReq.getAuction_id()));

		// 车辆基本信息+车辆手续
		CheckCarPojo checkCarPojo = new CheckCarPojo();
		BeanCopier checkOrderCarCopy = BeanCopier.create(CheckOrderCar.class, CheckCarPojo.class, false);
		CheckOrderCar checkOrderCar = checkOrderCarMapper.selectPojoById(checkCarInfoReq.getCheck_car_id(),
				checkCarInfoReq.getAuction_id());
		if (null == checkOrderCar) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("该车辆不存在");
			return checkCarInfoReply;
		}
		checkOrderCarCopy.copy(checkOrderCar, checkCarPojo, null);
		checkCarPojo.setAuction_id(checkCarInfoReq.getAuction_id());
		// 拍照所在地 错误，重新赋值
		checkCarPojo.setCity_name(checkOrderCar.getPlate_city());
		// 表显里程 转换 向下兼容
		checkCarPojo.setDrive_km_single(checkOrderCar.getDrive_km());
		checkCarPojo.setDrive_km(this.formatDriveKm(checkOrderCar.getDrive_km()));
		// 解析json
		Map<String, String> map = (Map<String, String>) JSON.parse(checkOrderCar.getBase_param());
		if (null != map) {
			checkCarPojo.setDriving_mode(map.get("DRIVING_MODE"));
			checkCarPojo.setEngine_full_type(map.get("ENGINE_FUEL_TYPE"));
		}
		checkCarInfoReply.setCheckCarPojo(checkCarPojo);

		// 车辆评级
		CheckCarInfoReply.CheckOrderPojo checkOrderPojo = new CheckCarInfoReply.CheckOrderPojo();
		// 是否显示评分
		checkOrderPojo.setIfShowScore(new Integer(this.ifShowScore));
		CheckOrder checkOrder = checkOrderMapper.selectByCarId(checkCarInfoReq.getCheck_car_id());
		if (null == checkOrder) {
			checkCarInfoReply.setReplyCode(Constants.RESULT_ERROR_PARAM);
			checkCarInfoReply.setMessage("该车辆检测信息不存在");
			return checkCarInfoReply;
		}

		BeanCopier checkOrderCopy = BeanCopier.create(CheckOrder.class, CheckCarInfoReply.CheckOrderPojo.class, false);
		checkOrderCopy.copy(checkOrder, checkOrderPojo, null);

		// 补充信息
		StringBuffer extra = new StringBuffer();
		extra.append(checkOrderCar.getOther_remark());
		checkOrderPojo.setExtra(extra.toString());

		checkCarInfoReply.setCheckOrderPojo(checkOrderPojo);

		// 车损 只需缺陷数量和图片数量
		List<CheckTypeReportOverviewPojo> checkTypeReports = checkTypeReportMapper
				.selectReportByOrderId(checkOrderCar.getOrder_id());
		for (CheckTypeReportOverviewPojo checkTypeReport : checkTypeReports) {
			switch (checkTypeReport.getType_id()) {
			case 9:
				checkCarInfoReply.setWaiguan(checkTypeReport);
				break;
			case 10:
				checkCarInfoReply.setJiegou(checkTypeReport);
				break;
			case 11:
				checkCarInfoReply.setJixie(checkTypeReport);
				break;
			case 12:
				checkCarInfoReply.setDianqi(checkTypeReport);
				break;
			case 13:
				checkCarInfoReply.setNeishi(checkTypeReport);
				break;
			case 14:
				checkCarInfoReply.setDipan(checkTypeReport);
				break;
			default:
				break;
			}
		}
		
		// 查询维保报告
		if (checkOrderCar.getJd_order_id() != null && (checkOrderCar.getJd_status() != null && checkOrderCar.getJd_status().intValue() == QueryStatus.QUERYSUCC.value)) {
			
			MaintReportPojo maintReportPojo = new MaintReportPojo();
			try {
				MaintenanceDetail maintenanceDetail = iMaintenanceService.queryReport(Constants.SYSNAME, checkOrderCar.getJd_order_id());
				if (maintenanceDetail != null) {
					String report = maintenanceDetail.getReport();
					if (report != null) {
						List<Object> list = JSON.parseArray(report, Object.class);
						maintReportPojo.setCount(list.size());
						
						if (report.contains(KEYWORDS)) {
							maintReportPojo.setType(MaintReportPojo.Type.HAS_ACCIDENT);
						} else {
							maintReportPojo.setType(MaintReportPojo.Type.NO_ACCIDENT);
						}
					} else {
						maintReportPojo.setType(MaintReportPojo.Type.NO_MAINTRECORDS);
					}
				} else {
					maintReportPojo.setType(MaintReportPojo.Type.NO_MAINTRECORDS);
				}
				maintReportPojo.setJd_order_id(checkOrderCar.getJd_order_id());
				maintReportPojo.setJd_status(checkOrderCar.getJd_status());
				checkCarInfoReply.setMaintReportPojo(maintReportPojo);
			} catch (CheSearchException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		checkCarInfoReply.setReplyCode(Constants.REPLY_SUCCESS);
		return checkCarInfoReply;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.com.che.web.service.auction.AuctionService#getPhotoByCarId(java.lang.
	 * String)
	 */
	@Override
	public List<String> getPhotoByCarId(Long carId) {
		// 正面图片
		List<String> pic_path = checkOrderCarMapper.selectImageById(carId);
		List<String> totolPicPath = Lists.newLinkedList();
		for (int i = 0; i < pic_path.size(); i++) {
			totolPicPath.add(imgWebUrl + pic_path.get(i));
		}
		return totolPicPath;
	}

	@Override
	public MaintenanceDetail getMaintenanceByCarId(Long check_car_id) {
		String vin = auctionRelateCarMapper.selectVinByCarId(check_car_id);
		MaintenanceDetail maintenanceDetail = null;
		if(StringUtils.isNotBlank(vin)){
			try {
				maintenanceDetail = iMaintenanceService.queryByVin("微信竞拍", vin);
			} catch (CheSearchException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return maintenanceDetail==null?new MaintenanceDetail():maintenanceDetail;
	}

  @Override
  public Map<String,Object> weiXinShare(ModelMap model, String shareUrl, boolean isJson) {
    String randStr = RandomUtils.randomInt(16)+"";	//随机字符串
    Long times = System.currentTimeMillis()/1000;

    boolean notLoad = false;

    String ticket = "";
    WeiXinData wxData = commonMapper.getWeiXinAccessToken();
    if(wxData!=null)
      if (StringUtils.isNotBlank(wxData.getAccess_token()) && StringUtils.isNotBlank(wxData.getJsapi_ticket())) {
        logger.info("=====id:" + wxData.getId() + ", accessToken=" + wxData.getAccess_token() + ",  ticket=" + wxData.getJsapi_ticket());
        if (WeiXinUtils.isInTime(times, wxData.getTimestamps())) {
          ticket = wxData.getJsapi_ticket();
          notLoad = true;
        }
      }

    if(!notLoad){
      String token = WeiXinUtils.getAccessToken();
      ticket = WeiXinUtils.getTicket(token);
      wxData = new WeiXinData();
      wxData.setAccess_token(token);
      wxData.setAddtime(DateUtil.getCurDate());
      wxData.setTimestamps(String.valueOf(times));
      wxData.setJsapi_ticket(ticket);
      commonMapper.addWeiXinAccessToken(wxData);
      logger.info("--添加微信分享token="+token+"   ticket="+ticket);
    }
		/*try {
			shareUrl = URLEncoder.encode(shareUrl, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}*/
    String sign = WeiXinUtils.getSignature(ticket, times, randStr, shareUrl);
    logger.info("-------ticket" + ticket);
    logger.info("-------times=" + times);
    logger.info("-------randStr=" + randStr);
    logger.info("-------sign=" + sign);
    logger.info("-------shareUrl=" + shareUrl);
    if(isJson){
      Map<String, Object> data = new HashMap<String, Object>();
      data.put("times", times);
      data.put("randStr", randStr);
      data.put("sign", sign);
      data.put("shareUrl", shareUrl);
      //model.put("map", data);
      return data;
    }else{
      model.put("times", times);
      model.put("randStr", randStr);
      model.put("sign", sign);
      model.put("shareUrl", shareUrl);
    }
    return null;
  }
}
