package com.stylefeng.guns.rest.modular.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.stylefeng.guns.core.constant.SystemConstants;
import com.stylefeng.guns.core.support.DateTimeKit;
import com.stylefeng.guns.core.util.MakeOrderNum;
import com.stylefeng.guns.core.util.MapUtil;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.dao.*;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.config.pay.AliPayConfig;
import com.stylefeng.guns.rest.config.pay.WxPayConfig;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.core.util.pay.AliPay;
import com.stylefeng.guns.rest.core.util.pay.WxPay;
import com.stylefeng.guns.rest.modular.pay.service.IPayCenterService;
import com.stylefeng.guns.rest.modular.pay.service.IUserWalletService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.enums.ConsumeType;
import com.stylefent.guns.entity.enums.WalletExpendTypeEnum;
import com.stylefent.guns.entity.query.PayQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author lx
 */
@Slf4j
@Service
public class PayCenterServiceImpl implements IPayCenterService {

    /**
     * 此处注意回调地址的写法，
     * 按照测试环境和生产环境不同配置不同的回调地址
     */

    @Value(value = "${rest.env-pay}")
    private String env;

    @Autowired
    private WxPayConfig wxPay;

    @Autowired
    private AliPayConfig aliPay;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PayRecordMapper payRecordMapper;

    @Autowired
    private IUserWalletService userWalletService;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private ChargeListMapper chargeListMapper;
    @Autowired
    private UserWalletRecordMapper userWalletRecordMapper;

    @Override
    public BaseJson aliPay(PayQuery query) {
        try {
            Integer chargeId = query.getChargeId();
            if(chargeId == null){
                return BaseJson.ofFail("参数错误");
            }
            ChargeList chargeList = chargeListMapper.selectById(chargeId);
            if(chargeList == null){
                return BaseJson.ofFail("参数错误");
            }
            int payType = query.getPayType();
            User dbUser = this.userService.selectById(query.getUserId());

            if (dbUser == null) {
                return BaseJson.ofFail("用户不存在");
            }

            BigDecimal money = chargeList.getRmb();
            BigDecimal gbmoney = BigDecimal.valueOf(chargeList.getXishiNum());

            //赠送比例
            BigDecimal rate = BigDecimal.valueOf(chargeList.getRate());
            //赠送金额
            BigDecimal leaf = BigDecimal.ZERO;
            if(rate.compareTo(BigDecimal.ZERO) != 0){
                leaf = gbmoney.multiply(rate).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN);
            }


            Map<String, String> publicParams = new HashMap<>(16);
            String orderNum = MakeOrderNum.makeOrderNum("CZ");
            String body = "";
            if (payType == 0) {
                if (money.compareTo(BigDecimal.valueOf(1)) < 0) {
                    return BaseJson.ofFail("最低充值金额不能低于1元");
                }
                body = "西施币充值";

                log.info("【支付宝充值】用户ID:【{}】,充值金额:【{}】", dbUser.getId(), money);
            } else if (payType == 1) {
                if (dbUser.getApplyStatus() == 4) {
                    return BaseJson.ofFail("您已通过实名,请勿重复申请");
                }

                body = "西施实名认证";
                money = BigDecimal.ONE;

                log.info("【支付宝-实名认证】用户ID:【{}】", dbUser.getId());
            }

            publicParams.put("appId", aliPay.appId);
            publicParams.put("orderNum", orderNum);
            publicParams.put("uid", query.getUserId().toString());
            publicParams.put("payType", payType + "");
            publicParams.put("chargeId", chargeId + "");
            publicParams.put("payMoney", money.toString());

            if (!Boolean.valueOf(env)) money = BigDecimal.valueOf(Double.valueOf("0.01"));
            String sign = AliPay.aliPay(new AliPay.AliPayParam(money, body, orderNum, publicParams, AliPay.PayType.QUICK_MSECURITY_PAY));
            //创建支付记录
            createPayRecord(query.getUserId(), body, money, AliPay.NOTIFY_URL, orderNum, 0,gbmoney,leaf);
            return BaseJson.ofSuccess(MapUtil.build().put("sign", sign).over());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("支付宝充值】生成支付宝签名错误", e);
            return BaseJson.ofFail("系统错误,请稍后再试");
        }
    }

    /**
     * 创建支付记录
     *
     * @param userId
     * @param body
     * @param money
     * @param notifyUrl
     * @param orderNum
     * @param type
     */
    public void createPayRecord(Long userId, String body, BigDecimal money, String notifyUrl, String orderNum, int type, BigDecimal gbmoney,BigDecimal leaf) {
        //支付记录
        PayRecord payRecord = new PayRecord();
        payRecord.setUserId(userId);
        payRecord.setTitle(body);
        payRecord.setMoney(money);
        payRecord.setGbMoney(gbmoney);
        payRecord.setGiftMoney(leaf);
        payRecord.setNotifyUrl(notifyUrl);
        payRecord.setOrderNo(orderNum);
        //未回调
        payRecord.setState(0);
        //0 支付宝  1 微信
        payRecord.setType(type);
        payRecord.setCreateTime(new Date());
        payRecord.setUpdateTime(new Date());
        payRecordMapper.insert(payRecord);

    }

    /**
     * 更新支付记录
     *
     * @param userId
     */
    public void updatePayRecord(Long userId, String orderNum, String tradeNo, String reason, int state) {

        PayRecord query = new PayRecord();
        query.setUserId(userId);
        query.setOrderNo(orderNum);
        PayRecord payRecord = payRecordMapper.selectOne(query);
        if (payRecord != null) {
            payRecord.setUpdateTime(new Date());
            payRecord.setTradeNo(tradeNo);
            payRecord.setReason(reason);
            payRecord.setState(state);
            payRecordMapper.updateById(payRecord);
        }
    }

    /**
     * 支付宝回调
     *
     * @param request
     * @return
     */
    @Override
    @Transactional()
    public void aliNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = AliPay.parseAliPayNotifyParam(request);
        log.info("【支付宝回调】数据=【{}】", JSON.toJSONString(params));
        try {
//            //验签
//            boolean validate = AlipaySignature.rsaCheckV1(params, aliPay.publicKey, aliPay.charset,
//                    aliPay.signType);
//            if (validate) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus)) {
                    Map<String, String> passbackParams = JSONObject.parseObject(params.get("passback_params"), Map.class);
                    Long uid = Long.valueOf(passbackParams.get("uid"));
                    String appId = passbackParams.get("appId");
                    Integer chargeId = Integer.valueOf(passbackParams.get("chargeId"));
                    String orderNo = passbackParams.get("orderNum");
                    BigDecimal payMoney = new BigDecimal(passbackParams.get("payMoney"));
                    Integer payType = Integer.valueOf(passbackParams.get("payType"));
                    //支付宝单号;
                    String tradeNo = params.get("trade_no");
                    // 验证appId
                    if (!StringUtils.isNotBlank(appId) && !aliPay.appId.equals(appId)) {
                        response.getWriter().println("failure");
                        log.info("【支付宝回调】支付宝回调APPID不匹配");
                        return;
                    }
                    response.getWriter().println("success");

                    //充值
                    if (payType == 0) {
                        //加钱+记记录
                        recordCharge(uid, payMoney, orderNo, tradeNo, 0,chargeId);
                        //支付记录
                        updatePayRecord(uid, orderNo, tradeNo, "", 1);

                    } else if (payType == 1) {
                        //实名  获取用户真实支付金额
                        String buyerPayAmount = params.get("buyer_pay_amount");
                        redisService.toCache(SystemConstants.USER_AUTH_PAY_KEY + uid, buyerPayAmount);
                    }
                }
//            } else {
//                response.getWriter().println("failure");
//                log.info("【支付宝回调】支付宝回调验签失败");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【支付宝回调】支付宝回调数据解析错误", e);
        }
    }

    /**
     * 微信支付
     *
     * @param query
     * @param request
     * @return
     */
    @Override
    public BaseJson wxPay(PayQuery query, HttpServletRequest request) {
        try {
            //0 充值 1 实名
            int payType = query.getPayType();
            User dbUser = this.userService.selectById(query.getUserId());
            if (dbUser == null) {
                return BaseJson.ofFail("用户不存在");
            }
            Integer chargeId = query.getChargeId();
            if(chargeId == null){
                return BaseJson.ofFail("参数错误");
            }
            ChargeList chargeList = chargeListMapper.selectById(chargeId);
            if(chargeList == null){
                return BaseJson.ofFail("参数错误");
            }
            Map<String, String> publicParams = new HashMap<>();
            String body = "";
            String orderNum = MakeOrderNum.makeOrderNum("CZ");
            BigDecimal money = chargeList.getRmb();
            BigDecimal gbmoney = BigDecimal.valueOf(chargeList.getXishiNum());
            BigDecimal rate = BigDecimal.valueOf(chargeList.getRate());
            BigDecimal leaf = BigDecimal.ZERO;
            if(rate.compareTo(BigDecimal.ZERO) != 0){
                leaf = gbmoney.multiply(rate).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN);
            }


            if (payType == 0) {
                if (money.compareTo(BigDecimal.valueOf(1)) < 0) {
                    return BaseJson.ofFail("最低充值金额不能低于1元");
                }
                body = "西施余额充值";

                log.info("【微信充值】用户ID:【{}】,充值金额:【{}】", dbUser.getId(), money);

            } else if (payType == 1) {
                if (dbUser.getApplyStatus() == 4) {
                    return BaseJson.ofFail("您已通过实名,请勿重复申请");
                }

                body = "西施实名认证";
                money = BigDecimal.ONE;

                log.info("【微信-实名认证】用户ID:【{}】", dbUser.getId());
            }
            publicParams.put("chargeId", chargeId + "");
            publicParams.put("appId", wxPay.mchId);
            publicParams.put("orderNum", orderNum);
            publicParams.put("uid", query.getUserId().toString());
            publicParams.put("payType", payType + "");
            publicParams.put("payMoney", money.toString());

            if (!Boolean.valueOf(env)) money = BigDecimal.valueOf(Double.valueOf("0.01"));
            SortedMap<Object, Object> resultParameter = WxPay.wxPay(new WxPay.WxPayParam(money, body, orderNum, publicParams, request, WxPay.PayType.APP));
            if (resultParameter.get("sign").toString().equals(WxPay.FAIL)) {
                return BaseJson.ofFail(resultParameter.get("msg").toString());
            }
            //创建支付记录
            createPayRecord(query.getUserId(), body, money, WxPay.NOTIFY_URL, orderNum, 1,gbmoney,leaf);
            return BaseJson.ofSuccess(MapUtil.build().put("sign", resultParameter).over());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("微信充值】生成微信签名错误", e);
            return BaseJson.ofFail("系统错误,请稍后再试");
        }
    }

    /**
     * 微信支付回调通知
     *
     * @param request
     * @param response
     */
    @Override
    @Transactional()
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) {
        SortedMap<Object, Object> packageParams = WxPay.parseWxNotify(request);
        log.info("【微信回调】数据=【{}】", JSON.toJSONString(packageParams));
        try {
            boolean validate = WxPay.isWxPaySign(wxPay.charset, packageParams);
            if (validate) {
                if ("SUCCESS".equals(String.valueOf(packageParams.get("result_code")))) {
                    //验证商户号
                    String mchId = String.valueOf(packageParams.get("mch_id"));
                    String wxTradeNo = String.valueOf(packageParams.get("transaction_id"));
                    if (!StringUtils.isNotBlank(mchId) && !wxPay.mchId.equals(mchId)) {
                        Map<String, String> result = new HashMap<>();
                        result.put("return_code", "FAIL");
                        result.put("return_msg", "OK");
                        String xmlResult = WxPay.mapToXml(result);
                        response.getWriter().println(xmlResult);
                        log.info("【微信回调】微信回调商户号不匹配,回传商户号{}", mchId);
                        return;
                    }

                    // 回调返回;
                    Map<String, String> result = new HashMap<>();
                    result.put("return_code", "SUCCESS");
                    result.put("return_msg", "OK");
                    String xmlResult = WxPay.mapToXml(result);
                    response.getWriter().println(xmlResult);

                    //透传参数;
                    String attachStr = String.valueOf(packageParams.get("attach"));
                    Map<String, String> attachMap = (Map<String, String>) JSONObject.parse(attachStr);
                    String orderNo = attachMap.get("orderNum");
                    Integer chargeId = Integer.valueOf(attachMap.get("chargeId"));
                    Long uid = Long.valueOf(attachMap.get("uid"));
                    BigDecimal payMoney = BigDecimal.valueOf(Double.valueOf(attachMap.get("payMoney")));
                    Integer payType = Integer.valueOf(attachMap.get("payType"));

                    //充值
                    if (payType == 0) {
                        //加钱+记记录
                        recordCharge(uid, payMoney, orderNo, wxTradeNo, 1,chargeId);
                        //支付记录
                        updatePayRecord(uid, orderNo, wxTradeNo, "", 1);


                    } else if (payType == 1) {
                        //实名
                        redisService.toCache(SystemConstants.USER_AUTH_PAY_KEY + uid, payMoney.toString());
                    }
                }
            } else {
                log.info("【微信回调】微信支付回调验签失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【微信回调】微信支付回调数据解析错误", e);
        }
    }


    /**
     * 充值记录及加钱
     *
     * @param uid
     * @param payMoney
     * @param orderNo
     * @param tradeNo
     * @param type
     */
    public void recordCharge(Long uid, BigDecimal payMoney, String orderNo, String tradeNo, Integer type,Integer chargeId) {
        ChargeList chargeList = chargeListMapper.selectById(chargeId);
        BigDecimal goldNum =BigDecimal.valueOf(chargeList.getXishiNum());
        BigDecimal rate = BigDecimal.valueOf(chargeList.getRate());
        BigDecimal leaf = BigDecimal.ZERO;
        if(rate.compareTo(BigDecimal.ZERO) != 0){
          leaf = goldNum.multiply(rate).divide(BigDecimal.valueOf(100),2,BigDecimal.ROUND_DOWN);
          goldNum = goldNum.add(leaf);
        }
       //TODO 充值加入用户钱包
        this.addMoney(uid,BigDecimal.ZERO,goldNum);
    }

    /**
     * 加钱
     *
     * @param userId
     * @param money
     */
    public void addMoney(Long userId, BigDecimal money,BigDecimal xishiNum) {
        UserWallet userWallet = userWalletMapper.selectByUserId(userId);
        UserWallet update = new UserWallet();
        update.setId(userWallet.getId());
        update.setUserId(userId);
        update.setGbMoeny(userWallet.getGbMoeny().add(xishiNum));
        update.setUpdateTime(new Date());
        update.setVersion(userWallet.getVersion());
        Integer count = userWalletMapper.updateById(update);
        if (count < 1) {
            addMoney(userId, money,xishiNum);
            return;
        }
//        钱包变动记录
        userWalletService.recordWalletDetail(ConsumeType.Recharge, userId, money, WalletExpendTypeEnum.INCOME, "");
    }

}
