package com.che.maintenance.pojo;

import com.che.common.web.Constants;
import com.che.common.web.Req;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintOrderReq extends Req {

	private static final long serialVersionUID = 6921289951225067868L;

	private Integer pay_status; // 0-待付款，1-付款中，2-已付款，3-退款中，4-已退款,5-已关闭

	private Integer type; // 0-相应状态订单 1-全部状态订单

	private Integer page = 1;

	private Integer pageSize = Constants.PAGESIZE;

	public static final class StatusType {

		public static final Integer SELESTAUTS = 0; // 0-相应状态订单

		public static final Integer ALLSTATUS = 1; // 1-全部状态订单
	}

}
