package com.che.auction.controller;

import cn.com.che.util.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.che.auction.entity.AuctionReport;
import com.che.auction.pojo.*;
import com.che.auction.pojo.CheckCarInfoReply.CheckCarPojo;
import com.che.auction.pojo.CheckTypeReportDetailReply.ItemReportPojo;
import com.che.auction.service.AuctionService;
import com.che.common.web.Constants;
import com.che.search.maintenance.entity.MaintenanceDetail;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Controller
public class AuctionH5Controller extends  WapBaseAction{
	@Autowired
	private AuctionService auctionService;

  @RequestMapping("/share/weixin/getToken")
  @ResponseBody
  public Map<String,Object> getToken(ModelMap model, HttpServletRequest httpServletRequest){
    String url=httpServletRequest.getParameter("url");
    Map<String,Object> data = auctionService.weiXinShare(model, url,  true);
    return data;
  }

	/**
	 * 车辆详情接口
	 */
	@RequestMapping("/share/car/detail")
	public ModelAndView carDetail(CheckCarInfoReq checkCarInfoReq,ModelAndView modelAndView) {
		modelAndView.setViewName("weixin/bid/bid_detail.html");
		CheckCarInfoReply checkCarInfoReply = auctionService.carInfoForH5(checkCarInfoReq);
		CheckTypeReportReq checkTypeReportReq = new CheckTypeReportReq();
		checkTypeReportReq.setCheck_car_id(checkCarInfoReq.getCheck_car_id());
		CheckTypeReportReply checkTypeReportReply = auctionService.checkReport(checkTypeReportReq);
		CheckCarPojo checkCarPojo=	checkCarInfoReply.getCheckCarPojo();
		String baseParam=checkCarPojo==null?null:checkCarPojo.getBase_param();
		if(StringUtils.hasText(baseParam)){
			JSONObject jsonObject=JSONObject.parseObject(baseParam);
			checkCarInfoReply.getCheckCarPojo().setBody_structure(jsonObject.getString("BODY_STRUCTURE"));
			checkCarInfoReply.getCheckCarPojo().setGearbox(jsonObject.getString("GEARBOX")); 
			checkCarInfoReply.getCheckCarPojo().setEngine_supply_mode(jsonObject.getString("ENGINE_SUPPLY_MODE")); 
			checkCarInfoReply.getCheckCarPojo().setEngine_ai_form(jsonObject.getString("ENGINE_AI_FORM")); 
		}
		modelAndView.addObject("data", checkCarInfoReply);
		modelAndView.addObject("auction_id", checkCarInfoReq.getAuction_id());
		modelAndView.addObject("check_car_id", checkCarInfoReq.getCheck_car_id());
		modelAndView.addObject("photos", checkTypeReportReply);
		modelAndView.addObject("dateUtil", new DateUtil());
		MaintenanceDetail maintenanceDetail = auctionService.getMaintenanceByCarId(checkCarInfoReq.getCheck_car_id());
		List<AuctionReport> items = new ArrayList<AuctionReport>();
		if (maintenanceDetail != null) {
			String reports = maintenanceDetail.getReport();
			if (StringUtils.hasText(reports)) {
				items = JSONObject.parseArray(reports, AuctionReport.class);
			}
		}
		modelAndView.addObject("maintenanceCount", items == null ? 0 : items.size());
		return modelAndView;
	}
	
	/**
	 * 车辆手续
	 */
	@RequestMapping("/share/car/procedures")
	public ModelAndView procedures(CheckCarProcedureReq checkCarInfoReq,ModelAndView modelAndView) {
		modelAndView.setViewName("weixin/bid/car_procedures.html");
		CheckCarProcedureReply checkCarProcedureReply = auctionService.carProcedure(checkCarInfoReq);
		modelAndView.addObject("data", checkCarProcedureReply);
		modelAndView.addObject("dateUtil", new DateUtil());
		return modelAndView;
	}
	/**
	 * 车辆评级
	 */
	@RequestMapping("/share/car/declare")
	public String carinfo(ModelAndView modelAndView) {
		return "weixin/bid/rating_sel.html";
	}
	/**
	 * 车辆评级说明
	 */
	@RequestMapping("/share/car/level")
	public String level(ModelAndView modelAndView) {
		return "weixin/bid/rating_explain.html";
	}
	/**
	 * 车辆评级规则
	 */
	@RequestMapping("/share/car/principles")
	public String principles(ModelAndView modelAndView) {
		return "weixin/bid/rating_principles.html";
	}
	/**
	 * 图片预览
	 * 
	 * @return ModelAndView
	 * @param CheckCarInfoReq
	 */
	@RequestMapping("/share/car/photo")
	public ModelAndView goPhotos(Long auction_id, Long check_car_id, String num) {
		ModelAndView modelAndView = new ModelAndView("weixin/bid/bid_photos.html");
		List<String> list = auctionService.getPhotoByCarId(check_car_id);
		modelAndView.addObject("auction_id", auction_id);
		modelAndView.addObject("check_car_id", check_car_id);
		modelAndView.addObject("photos", list);
		modelAndView.addObject("mains", 1);
		modelAndView.addObject("num", num);
		return modelAndView;
	}
	/**
	 * 基本信息接口
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/share/car/info")
	public ModelAndView carBaseInfo(CheckCarBaseInfoReq checkCarBaseInfoReq, Long auction_id, Long check_car_id,
			Long isover) {
		String json = "{\"CTV\": \"车载电视\"," + "\"TPMS\": \"胎压监测\"," + "\"FONT_ELEC_WIN\": \"电动车窗\","
				+ "\"GPS\": \"GPS导航\"," + "\"FACTORY_NAME\": \"生产厂家\"," + "\"ENGINE_AI_FORM\": \"进气形式\","
				+ "\"AUTO_WIPER\": \"感应雨刷\"," + "\"AUXILIARY\": \"并线辅助\"," + "\"roof_rack\": \"车顶行李架\","
				+ "\"BSC\": \"后排液晶屏\"," + "\"FRONT_TIRE_TYPE\": \"前胎规格\"," + "\"ENGINE_SS\": \"发动机启停技术\","
				+ "\"BRAKE_ASSIST\": \"刹车辅助\"," + "\"MAIN_DRIVER_AIRBAG\": \"主驾气囊\","
				+ "\"REAR_AXLE_LIMIT\": \"后桥差速器\"," + "\"ENV_STA\": \"环保标准\"," + "\"STEEP_DROP\": \"陡坡缓降\","
				+ "\"DRI_COM_DIS\": \"行车显示屏\"," + "\"NOKEY_ENTRY\": \"无钥匙进入系统\"," + "\"RV_ELEC_ADJ\": \"后视镜电调节\","
				+ "\"MID_AXLE_LIMIT\": \"中央差速器\"," + "\"ACTIVE_STEER\": \"整体主动转向系统\"," + "\"STAB_CON\": \"车身稳定控制\","
				+ "\"ICE_BOX\": \"车载冰箱\"," + "\"SPARE_TIRE_TYPE\": \"备胎规格\"," + "\"AUTO_PARK\": \"自动驻车\","
				+ "\"ENGINE_CAPACITY\": \"排量(mL)\"," + "\"HELP_POWER_TYPE\": \"助力类型\"," + "\"BLCP\": \"车载电话\","
				+ "\"ELEC_BACKUP\": \"电动后备厢\"," + "\"TRAC_CON\": \"牵引力控制\"," + "\"HL_WASH\": \"大灯清洗\","
				+ "\"CCSC\": \"彩屏中控台\"," + "\"FRONT_HEAT\": \"座椅加热\"," + "\"LOS\": \"定位服务\","
				+ "\"BODY_STRUCTURE\": \"车身结构\"," + "\"FRONT_AXLE_LIMIT\": \"前桥差速器\"," + "\"FRONT_MASS\": \"座椅按摩\","
				+ "\"FRONT_HEAD_AIRBAG\": \"头部气囊\"," + "\"MAIN_ELEC_ADJ\": \"前座电动调节\"," + "\"ADA_CRU\": \"自适应巡航\","
				+ "\"NIGHT_VISION\": \"夜视系统\"," + "\"ENGINE_SUPPLY_MODE\": \"供油方式\"," + "\"AUDIO_NUM\": \"扬声器\","
				+ "\"FRONT_PARK_RAD\": \"前驻车雷达\"," + "\"REAR_PARK_RAD\": \"后驻车雷达\"," + "\"SPORT_APP\": \"外观套件\","
				+ "\"RV_ELEC_FOLD\": \"后视镜电折叠\"," + "\"EBD\": \"制动力分配\"," + "\"REMOTE_KEY\": \"遥控钥匙\","
				+ "\"PAN_CAMERA\": \"全景摄像头\"," + "\"ENGINE_FUEL_TYPE\": \"燃料形式\"," + "\"ACTIVE_SAFE\": \"主动刹车/安全系统\","
				+ "\"REAR_AIR_CON\": \"后排独立空调\"," + "\"CRUISE_CON\": \"定速巡航\"," + "\"ABS\": \"ABS防抱死\","
				+ "\"REAR_TIRE_TYPE\": \"后胎规格\"," + "\"NOKEY_START\": \"无钥匙启动系统\"," + "\"MULT_STEEL\": \"多功能方向盘\","
				+ "\"ENGINE_MODEL\": \"发动机型号\"," + "\"HUD\": \"HUD数字显示\"," + "\"RV_HEAT\": \"后视镜加热\","
				+ "\"IN_ANTI_GLARE\": \"后视镜防眩目\"," + "\"ELEC_SKY\": \"电动天窗\"," + "\"ELEC_MEMORY\": \"座椅记忆\","
				+ "\"MSYS\": \"多媒体系统\"," + "\"VARI_SUSPEN\": \"可变悬架\"," + "\"LEA\": \"座椅材质\","
				+ "\"TEM_PAR_CON\": \"温度分区控制\"," + "\"PAN_SKY\": \"全景天窗\"," + "\"FRONT_SIDE_AIRBAG\": \"侧气囊\","
				+ "\"AIR_SUSPEN\": \"空气悬架\"," + "\"LANE_DEP_WARN\": \"车道偏离预警系统\"," + "\"AIR_CON_MODEL\": \"空调控制方式\","
				+ "\"ENGINE_ELEC_SEC\": \"发动机电子防盗\"," + "\"TC_AUTO_PARK\": \"自动泊车入位\"," + "\"GEARBOX\": \"变速箱\","
				+ "\"REV_VIDEO\": \"倒车影像\"," + "\"KNEE_AIRBAG\": \"膝部气囊\"," + "\"REAR_ELEC_ADJ\": \"后座电动调节\","
				+ "\"DRIVING_MODE\": \"驱动方式\"," + "\"CAR_LOCK\": \"中控锁\"," + "\"SPL_LCD\": \"中控液晶分屏显示\","
				+ "\"ENGINE_GAS_GRADE\": \"燃油标号\"," + "\"HS_ASSIST_CON\": \"上坡辅助\"}";
		ObjectMapper objectMapper = new ObjectMapper();
		ModelAndView mv = new ModelAndView("weixin/bid/car_information.html");
		try {
			Map<String, String> map = objectMapper.readValue(json, Map.class);
			Map<String, String> returnMap = new HashMap<String, String>();
			CheckCarBaseInfoReply checkCarBaseInfoReply = auctionService.carBaseInfo(checkCarBaseInfoReq);
			if (checkCarBaseInfoReply != null && checkCarBaseInfoReply.getCheckCarBaseInfoPojo() != null
					&& checkCarBaseInfoReply.getCheckCarBaseInfoPojo().getFunction_param() != null) {
				String param = checkCarBaseInfoReply.getCheckCarBaseInfoPojo().getFunction_param();
				JSONObject jsonObject = JSONObject.parseObject(param);
				for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
					String key = iterator.next();
					if (jsonObject.getString(key) != null) {
						returnMap.put(map.get(key), jsonObject.getString(key));
					}
				}
			}
			checkCarBaseInfoReply.setResultCode(Constants.RESULT_SUCCESS);
			mv.addObject("data", checkCarBaseInfoReply);
			mv.addObject("dateUtil", new DateUtil());
			mv.addObject("baseInfo", returnMap);
			mv.addObject("auction_id", auction_id);
			mv.addObject("check_car_id", check_car_id);
			mv.addObject("isover", isover);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mv;
	}
	
	/**
	 * 损伤详情查看
	 * 
	 * @return ModelAndView
	 * @param CheckCarInfoReq
	 */
	@RequestMapping("/share/car/damageDetail")
	public ModelAndView damageDetail(Long auction_id, Long check_car_id, String num, Long isover) {
		ModelAndView modelAndView = new ModelAndView("weixin/bid/car_damage.html");
		CheckTypeReportReq checkTypeReportReq = new CheckTypeReportReq();
		checkTypeReportReq.setCheck_car_id(check_car_id);
		CheckTypeReportDetailReply checkTypeReportDetailReply = auctionService.checkReportDetail(checkTypeReportReq);
		modelAndView.addObject("auction_id", auction_id);
		modelAndView.addObject("check_car_id", check_car_id);
		modelAndView.addObject("photos", checkTypeReportDetailReply);
		modelAndView.addObject("num", num);
		modelAndView.addObject("isover", isover);
		return modelAndView;
	}

	/**
	 * 损伤图片预览
	 * 
	 * @return ModelAndView
	 * @param CheckCarInfoReq
	 */
	@RequestMapping("/share/car/damagePhoto")
	public ModelAndView damagePhoto(Long auction_id, Long check_car_id, String num, String type, Integer itemId) {
		ModelAndView modelAndView = new ModelAndView("weixin/bid/bid_photos.html");
		CheckTypeReportReq checkTypeReportReq = new CheckTypeReportReq();
		checkTypeReportReq.setCheck_car_id(check_car_id);
		CheckTypeReportDetailReply checkTypeReportReply = auctionService.checkReportDetail(checkTypeReportReq);
		modelAndView.addObject("auction_id", auction_id);
		modelAndView.addObject("check_car_id", check_car_id);
		switch (type) {
		case "waiguan":
			if (checkTypeReportReply != null && checkTypeReportReply.getWaiguan() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getWaiguan().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		case "jiegou":
			if (checkTypeReportReply != null && checkTypeReportReply.getJiegou() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getJiegou().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		case "jixie":
			if (checkTypeReportReply != null && checkTypeReportReply.getJixie() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getJixie().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		case "dianqi":
			if (checkTypeReportReply != null && checkTypeReportReply.getDianqi() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getDianqi().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		case "neishi":
			if (checkTypeReportReply != null && checkTypeReportReply.getNeishi() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getNeishi().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		case "dipan":
			if (checkTypeReportReply != null && checkTypeReportReply.getDipan() != null) {
				List<ItemReportPojo> photos = new ArrayList<ItemReportPojo>();
				List<ItemReportPojo> itemReports = checkTypeReportReply.getDipan().getItemReports();
				if (itemReports != null && itemReports.size() > 0) {
					for (int i = 0; i < itemReports.size(); i++) {
						ItemReportPojo itemReportPojo = itemReports.get(i);
						if (itemReportPojo != null && itemReportPojo.getItemId() != null
								&& itemReportPojo.getPicPath() != null) {
							if (itemReportPojo.getItemId().equals(itemId)) {
								photos.add(itemReportPojo);
							}
						}
					}
				}
				modelAndView.addObject("photos", photos);
			}
			break;
		default:
			break;
		}
		modelAndView.addObject("num", num);
		return modelAndView;
	}

	/**
	 * 查询维修记录
	 * 
	 * @return
	 */
	@RequestMapping("/share/car/maintenance/detail")
	public ModelAndView getMaintenanceRecord(ModelAndView mv, Long check_car_id) {
		mv.setViewName("weixin/bid/maintenance_log.html");
		MaintenanceDetail maintenanceDetail = auctionService.getMaintenanceByCarId(check_car_id);
		Map<String, Object> map = new HashMap<String, Object>();
		String vin=maintenanceDetail.getVin();
		if(vin!=null&&vin.length()>6){
			int len=vin.length();
			vin=vin.substring(0, len-6)+"****"+vin.substring(len-2);
		}
		map.put("vin", vin);
		map.put("brand", maintenanceDetail.getBrand());
		map.put("displacement", maintenanceDetail.getDisplacement());
		map.put("gearbox", maintenanceDetail.getGearbox());
		String reports = maintenanceDetail.getReport();
		List<AuctionReport> items = new ArrayList<AuctionReport>();
		if (StringUtils.hasText(reports)) {
			items = JSONObject.parseArray(reports, AuctionReport.class);
		}
		if (items != null && items.size() > 0) {
			sort(items);
			map.put("firstItem", items.get(0));
			items.remove(0);
			map.put("otherItems", items);
		}
		mv.addObject("data", map);
		return mv;
	}

	private void sort(List<AuctionReport> data) {
		Collections.sort(data, new Comparator<AuctionReport>() {
			public int compare(AuctionReport bean1, AuctionReport bean2) {
				String a = bean1.getRepairDate();
				String b = bean2.getRepairDate();
				// 降序
				return b.compareTo(a);
			}
		});
	}
	
	@RequestMapping("/share/car/publish")
	public ModelAndView publishCarView(ModelAndView modelAndView){
		modelAndView.setViewName("weixin/bid/car_source.html");
		return modelAndView;
	}
}
