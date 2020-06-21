package com.stylefeng.guns.rest.config.pay;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置
 *
 * @author lx
 */
@Component
@PropertySource("classpath:config/conf.properties")
@Data
public class AliPayConfig {

    @Value(value = "${ali.appId}")
    public String appId;

    @Value(value = "${ali.privateKey}")
    public String privateKey;

    @Value(value = "${ali.publicKey}")
    public String publicKey;

    @Value(value = "${ali.notifyUrl}")
    public String notifyUrl;

    @Value(value = "${ali.signType}")
    public String signType;

    @Value(value = "${ali.charset}")
    public String charset;

    @Value(value = "${ali.requestUrl}")
    public String requestUrl;

    @Value(value = "${ali.queryUrl}")
    public String queryUrl;

}