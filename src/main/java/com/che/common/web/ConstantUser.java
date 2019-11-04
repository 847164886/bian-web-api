package com.che.common.web;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaole on 2016/4/26.
 */
public class ConstantUser {
    //    错误信息
    public interface errorMessage {
        String input_null = "inpunt cant be null";
        String sql_error = "sql_error";
    }

    //    来源 1 车城 2 车助手  3  车商 4 客户  5 第三方
    public interface orginal_type {
        int checheng = 1;
        int chezs = 2;
        int cheshang = 3;
        int checustomer = 4;
        int chethird = 5;
        int chemm = 6;
        int cheAnonymityC = 7;
        int cheAnonymityB = 8;
    }

    //   账户余额明细 业务类型
    public interface account_bussiness_type {
        int order = 1;//订单
        int aliPay = 2;//支付宝
        int WeChat = 3;//微信
        int czsOrder = 4;//车助手订单
        int weibaoService = 5;//维保服务
    }

    //  1入账  2出账
    public interface account_operator_type {
        int enter = 1;
        int charge_off = 2;
    }

    //    券状态 0:未绑定 1：已绑定  2 已消费 3 已失效 -1 已删除
    public interface coupon_status {
        int unbinded = 0;
        int binded = 1;
        int used = 2;
        int invalid = 3;
        int deleted = -1;
    }

    //券类型 0 电子券 1 实物券 2基金券
    public interface coupon_type {
        int e_coupon = 0;
        int entity = 1;
        int che = 2;
        int fund = 3;
    }

    //    券状态  1：有效  2 无效 -1 删除
    public interface coupon_theme_status {
        int valid = 1;
        int unvalid = 2;
        int deleted = -1;
    }

    //个人领券类型 0 限量 1 不限量
    public interface individual_limit_type {
        int limit = 0;
        int unlimit = 1;
    }

    //发送规则 0 订单
    public interface coupon_bussiness_type {
        int order = 0;
        int aliPay = 1;//支付宝
        int WeChat = 2;//微信
    }

    //认证类型 0 未认证  1 实名认证  2 企业认证
    public interface certification_type {
        int unCertification = 0;
        int certification = 1;
        int enterprise = 2;
    }

    //认证状态 0未校验 1校验通过 2校验失败 3已认证未审核
    public interface certification_status {
        int unchecked = 0;
        int pass = 1;
        int refuse = 2;
        int authstr = 3;
    }

    //    合作类型
    public interface cooperation_type {
    	//车商
        String auction = "cas_auction_register_one";
        //个人
        String auction_person = "cas_auction_register_two";
        
        String carProvider = "weix_car_provider";
    }

    //支付方式
    public interface pay_method {
        int aliPay = 2;//支付宝
        int WeChat = 3;//微信
    }

    //    充值消费
    public interface pay_type {
        int recharge = 1;//充值
        int consumer = 2;//消费
    }

    public interface bank_card_type {
        int debit = 1;//借记卡
    }


    //    来源 1 车城 2 车助手  3  车商 4 客户  5 第三方
    public interface coupon_platform {
        int checheng = 1;
        int chezs = 2;
        int cheshang = 3;
        int checustomer = 4;
        int chethird = 5;
    }

    public interface coupon_isAll {
        int no = 0;//借记卡
        int yes = 1;
    }

    //    账户预支付状态 0  直接支付 1 预支付 2 预支付确认支付 3 预支付取消
    public interface account_advance {
        int pay = 0;
        int advance = 1;
        int advance_confirm = 2;
        int advance_cancel = 3;
    }

    //    账户操作类型 0  充值 1 消费 2 退款
    public interface account_type {
        int pay = 0;
        int consumer = 1;
        int refund = 2;
    }

    //队列名
    public interface mq_name {
        String pay_mq_name = "payService";
    }

    //用户角色状态  0待审核 1 审核通过 2 未审核
    public interface user_role_status {
        int wait = 0;
        int pass = 1;
        int unpass = 2;
    }

    //    角色类型 0车商  1车城
    public interface role_type {
        int cheshang = 0;
        int checheng = 1;
    }

    //    用户类型 0车商  1车城
    public interface user_type {
        int cheshang = 0;
        int checheng = 1;
    }

    //    权限等级 0测试  1正式
    public interface function_level {
        int test = 0;
        int official = 1;
    }

    //    用户类型 0车商  1车城
    public interface user_group {
        List<Integer> cGroup = new ArrayList<Integer>() {
            {
                add(orginal_type.cheAnonymityC);
                add(orginal_type.checheng);
                add(orginal_type.chemm);
                add(orginal_type.checustomer);
                add(orginal_type.chethird);
                add(orginal_type.chezs);
            }
        };
        List<Integer> bGroup = new ArrayList<Integer>() {
            {
                add(orginal_type.cheshang);
                add(orginal_type.cheAnonymityB);
            }
        };
    }
}
