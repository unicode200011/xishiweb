package com.stylefeng.guns.rest.config.system;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * system parameter config
 *
 * @author lx
 */
@Component
@PropertySource(value = "classpath:config/conf.properties", encoding = "UTF-8")
@Data
public class SystemConfig {

    /**
     * 腾讯api秘钥ID
     */
    @Value(value = "${tx.secret.id}")
    private String videoApiSecretId;
    /**
     * 腾讯api秘钥key
     */
    @Value(value = "${tx.secret.key}")
    private String videoApiSecretKey;
    /**
     * video token expire time
     */
    @Value(value = "${video.secret.expireTime}")
    private Long videoSecretExpireTime;
    /**
     * video TX domain
     */
    @Value(value = "${video.tx.domain}")
    private String videoTxDomain;
    /**
     * video My domain
     */
    @Value(value = "${video.my.domain}")
    private String videoMyDomain;
    /**
     * sms prefix
     */
    @Value(value = "${sm.sms.prefix}")
    private String smsPrefix;
    /**
     * sms expire time
     */
    @Value(value = "${sm.sms.expireTime}")
    private Long smsExpireTime;
    /**
     * sms app key
     */
    @Value(value = "${sm.sms.appKey}")
    private String smsAppKey;
    /**
     * sms app secret
     */
    @Value(value = "${sm.sms.appSecret}")
    private String smsAppSecret;
    /**
     * sms temp code
     */
    @Value(value = "${sm.sms.tempCode}")
    private String smsTempCode;
    /**
     * sms sign name
     */
    @Value(value = "${sm.sms.signName}")
    private String smsSignName;
    /**
     * sms region
     */
    @Value(value = "${sm.sms.region}")
    private String smsRegion;

    /**
     * default avatar
     */
    @Value(value = "${s.defaultAvatar}")
    private String defaultAvatar;

    /**
     * allow execute ip -- spring job
     */
    @Value(value = "${s.allowTaskIP}")
    private String allowTaskIP;
    /**
     * 阿里云 rocket mq key
     */
    @Value(value = "${aliyun.rocketmq.AccessKey}")
    private String rocketMqKey;

    /**
     * 阿里云 rocket mq secret
     */
    @Value(value = "${aliyun.rocketmq.SecretKey}")
    private String rocketMqSecret;
    /**
     * 阿里云 rocket mq 服务切入点
     */
    @Value(value = "${aliyun.rocketmq.ONSAddr}")
    private String rocketMqOnsAddr;


    /** 直播点播相关配置**/

    /**
     * 业务ID
     */
    @Value(value = "${live.bizid}")
    private String liveBizid;
    /**
     * 推流域名
     */
    @Value(value = "${live.pushDomain}")
    private String livePushDomain;
    /**
     * 播流域名
     */
    @Value(value = "${live.playDomain}")
    private String livePlayDomain;
    /**
     * 推流防盗链Key
     */
    @Value(value = "${live.pushGuardKey}")
    private String livePushGuardKey;
    /**
     * api鉴权key
     */
    @Value(value = "${live.apiAuthKey}")
    private String liveApiAuthKey;
    /**
     * appid
     */
    @Value(value = "${live.appId}")
    private String liveAppId;

    /**
     * 鉴黄回到鉴权id
     */
    @Value(value = "${live.screen.id}")
    private String screenId;
    /**
     * 鉴黄回到鉴权key
     */
    @Value(value = "${live.screen.key}")
    private String screenKey;
    /**
     * 云API密匙ID
     */
    @Value(value = "${tx.secret.id}")
    private String liveYunApiSecretId;
    /**
     * 云API密匙key
     */
    @Value(value = "${tx.secret.key}")
    private String liveYunApiSecretKey;
    /**
     * 云通信appsdkid
     */
    @Value(value = "${tx.im.sdkAppId}")
    private String liveYunSDKAppId;
    /**
     * 云通信appsdkname
     */
    @Value(value = "${tx.im.adminName}")
    private String liveYunAdminName;
    /**
     * 云通信公钥
     */
    @Value(value = "${tx.im.pubStr}")
    private String liveYunPubStr;
    /**
     * 云通信user_sign 前缀
     */
    @Value(value = "${tx.im.userSign.prefix}")
    private String liveYunUserSignPrefix;
    /**
     * 云通信私钥
     */
    @Value(value = "${tx.im.priStr}")
    private String liveYunPriStr;

    /**
     * 用户防沉迷保护过期时间
     */
    @Value(value = "${s.user.protect.expireTime}")
    private Long userProtectExpireTime;
    /**
     * 接收邮件账户;
     */
    @Value("${dx.mail.receive}")
    private String mailAccount;

    /**
     * 用户任务运行机器IP
     */
    @Value(value = "${s.user}")
    private String userExecuteIP;
    /**
     * 视频任务运行机器IP
     */
    @Value(value = "${s.video}")
    private String videoExecuteIP;

}
