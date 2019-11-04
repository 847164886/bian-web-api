package com.che.auction.pojo;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.che.common.web.Reply;

@Data
@EqualsAndHashCode(callSuper=false)
public class CheckCarProcedureReply extends Reply {
	
	private static final long serialVersionUID = 2224079943570924418L;
	
	private CheckCarProcedurePojo checkCarProcedurePojo;
	
	@Data
	public static class  CheckCarProcedurePojo{
		
		private Long check_car_id				;         //bigint(20) NULL检测车
		
		private Date first_register				;         //datetime NULL初次登记
		private Date expires_date				;         //datetime NULL年检有效期
		private String plate_city				;         //varchar(50) NULL车牌所在地(手填)
		private String use_property				;         //varchar(20) NULL使用性质：营转非，运营，非运营
		private String transfer					;         //varchar(20) NULL过户转籍限制：可过户，异常
		
		private Date insure_date				;         //datetime NULL交强险
		private String property_change			;         //varchar(50) NULL产证变更信息
		private String property_lease			;         //varchar(20) NULL产权曾属租赁：有，否
		private String buy_tax					;         //varchar(20) NULL购置税完税证明：有，无
		private String drive_license			;         //varchar(20) NULL机动车行驶证：需变更，有，无
		private String master_register			;         //varchar(20) NULL车主户籍：沪籍，非沪籍
		private String discharge_level			;         //varchar(50) NULL排放标准：国二，国三，国四，国五
		
		private String quantity_sell			;         //varchar(10) NULL带额度出售：是，否
		private String plate_status				;         //varchar(10) NULL车牌状态：有，无
		private Date invalid_date				;         //datetime NULL强制报废年限
		private String master_info				;         //varchar(20) NULL车主信息：公车，私车
		private String use_instruction			;         //varchar(20) NULL车辆使用说明书：有，无
		private String protect_instruction		;         //varchar(20) NULL车辆保养手册：有，无
		private Integer transfer_times			;         //int(11) NULL过户次数

	}
	
}
