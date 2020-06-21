package com.stylefeng.guns.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = {"com.stylefeng.guns"})
@ServletComponentScan
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableAsync
@EnableCaching
public class GunsRestApplication {

    private final static Logger logger = LoggerFactory.getLogger(GunsRestApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GunsRestApplication.class, args);
        logger.info("GunsRestApplication is success!");
    }
}
