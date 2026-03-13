package com.example.springtask.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SpringTaskConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        // 创建一个线程池调度器
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        // 设置线程池大小
        threadPoolTaskScheduler.setPoolSize(10);
        // 设置线程名前缀
        threadPoolTaskScheduler.setThreadNamePrefix("mschedule-task-pool-");
        // 设置等待任务完成再关闭线程池
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间（单位：秒）
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}