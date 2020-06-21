package com.stylefeng.guns.rest.config.system;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
public class AsyncConfig {

    private static final int MAX_POOL_SIZE = 50;

    private static final int CORE_POOL_SIZE = 20;

    @Bean("taskAsyncExecutor")
    public AsyncTaskExecutor taskExecutor() {
        log.info("加载异步方法线程池....");
        ThreadPoolTaskExecutor taskAsyncExecutor = new ThreadPoolTaskExecutor();
        taskAsyncExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        taskAsyncExecutor.setCorePoolSize(CORE_POOL_SIZE);
        taskAsyncExecutor.setThreadNamePrefix("async-task-thread-pool");
        taskAsyncExecutor.initialize();
        return taskAsyncExecutor;
    }
}
