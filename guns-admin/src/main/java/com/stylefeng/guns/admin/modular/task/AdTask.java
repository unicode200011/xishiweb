package com.stylefeng.guns.admin.modular.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;


@Slf4j
//@Component
public class AdTask {


    @Scheduled(cron = "0 0 0 * * ?")
    public void doCheckAdState(){

    }

}
