package com.stylefeng.guns.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * SpringBoot方式启动类
 *
 * @author stylefeng
 * @Date 2017/5/21 12:06
 */

@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@EnableAsync
@EnableScheduling
public class GunsAgentApplication {

    private final static Logger logger = LoggerFactory.getLogger(GunsAgentApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GunsAgentApplication.class, args);
        logger.info("GunsAgentApplication is successd！！！！");
    }
}
