package com.che.maintenance.service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.che.common.util.SignUtils;
import com.che.common.web.Constants;
import com.che.maintenance.entity.MaintOrderEntity;
import com.che.maintenance.mapper.MaintOrderCreateMapper;
import com.che.maintenance.pojo.MaintenanceAliPayReq;
import com.che.maintenance.pojo.OrderStatus;
import com.che.pay.ali.pojo.AliPayContext;
import com.che.pay.ali.pojo.BaseAliPayReply;
import com.che.pay.ali.prepay.AbstractAliPayPrepayService;
import com.che.pay.ten.redis.RedisTenPayPrepayIdTool;
import com.che.recharge.common.RechargeConstants;

@Service
public class MainaAliPayPrepayService extends AbstractAliPayPrepayService<BaseAliPayReply, MaintenanceAliPayReq> {

	@Value("${alipay.maintNotice}")
	private String aliPayNotice; // 支付宝回调地址
	
	@Value("${alipay.partner}")
	private String partner;
	
	@Value("${alipay.sellerEmail}")
	private String sellerEmail;
	
	@Value("${alipay.privateKey}")
	private String privateKey;

	@Resource
	private RedisTenPayPrepayIdTool redisTenPayPrepayIdTool;

	@Autowired
	private MaintOrderCreateMapper maintOrderCreateMapper;
	
//	private static final String GOODS_TYPE = "维保查询";

	@Override
	public String aliPayNotice() {
		return aliPayNotice;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	protected AliPayContext createContext(MaintenanceAliPayReq req) throws Exception {
		AliPayContext context = new AliPayContext();

		if (!this.isValid(req)) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("参数错误");
			return context;
		}

		Long orderId = req.getOrder_Id();

		MaintOrderEntity maintPayment = maintOrderCreateMapper.selectPaymentById(orderId);
		
		if (maintPayment == null) {
			context.setRight(false);
			context.setReplyCode(Constants.RESULT_ERROR_PARAM);
			context.setMsg("订单不存在");
			return context;
		} else {
			
			Integer payType = RechargeConstants.PAY_TYPE_ALI;// 支付宝支付
			BigDecimal payment = maintPayment.getPayment();
			
			// 更新订单
			MaintOrderEntity maintenanceOrderEntity = new MaintOrderEntity();
			maintenanceOrderEntity.setPay_type(payType);
			maintenanceOrderEntity.setId(orderId);
			maintOrderCreateMapper.updateMaintOrder(maintenanceOrderEntity);
			
			context.setOrderId(orderId);
			context.setPayment(payment);
		}
		return context;
	}

	private boolean isValid(MaintenanceAliPayReq req) {
		if (req == null)
			return false;
		if (req.getOrder_Id() == null)
			return false;
		if (req.getAmount() == null)
			return false;
		return true;
	}
	
	public String getOrderInfo(BaseAliPayReply oReply) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + sellerEmail+ "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + oReply.getOrderId() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + Constants.MAINTENANCEQUERY + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + Constants.MAINTENANCEQUERY + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + oReply.getPayment().toString() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + oReply.getNoticeUrl() + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
	
	public String getPayInfo(String orderInfo) {
		String sign = SignUtils.sign(orderInfo, privateKey);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        final String payInfo = orderInfo + "&sign=\"" + sign
                + "\"&sign_type=\"RSA\"";
        return payInfo;
	}
}
