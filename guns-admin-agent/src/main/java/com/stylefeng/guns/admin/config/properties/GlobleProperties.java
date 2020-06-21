package com.stylefeng.guns.admin.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:config/conf.properties")
@Data
public class GlobleProperties {

    @Value("${getCodeUrl}")
    private String getCodeUrl;    //获取短信验证码接口

    @Value("${resetPwd}")
    private String resetPwd;    //获取短信验证码接口

    @Value("${awardKanvalue}")
    private String awardKanvalue;    //视频审核通过接口

    @Value("${qrCodePath}")
    private String qrCodePath;
    @Value("${unitRoleId}")
    private String unitRoleId;
    @Value("${baiduMapAK}")
    private String baiduMapAK;

    @Value("${localTargetPath}")
    private String localTargetPath;

    @Value("${wkhtmltopdfLocalLinuxSavePath}")
    private String wkhtmltopdfLocalLinuxSavePath;

    @Value("${wkhtmltopdfLocalWinSavePath}")
    private String wkhtmltopdfLocalWinSavePath;

    @Value("${wkhtmltopdfWinAppPath}")
    private String wkhtmltopdfWinAppPath;

    @Value("${wkhtmltopdfLinuxAppPath}")
    private String wkhtmltopdfLinuxAppPath;

}
