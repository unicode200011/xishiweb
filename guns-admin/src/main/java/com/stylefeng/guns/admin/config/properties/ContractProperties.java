package com.stylefeng.guns.admin.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.stylefeng.guns.core.util.ToolUtil.getTempPath;
import static com.stylefeng.guns.core.util.ToolUtil.isEmpty;

/**
 * guns项目配置
 *
 * @author stylefeng
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = "web3j")
public class ContractProperties {

    private String clientAddress;
    private String fromAddress;
    private String fromPrivatekey;

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromPrivatekey() {
        return fromPrivatekey;
    }

    public void setFromPrivatekey(String fromPrivatekey) {
        this.fromPrivatekey = fromPrivatekey;
    }
}
