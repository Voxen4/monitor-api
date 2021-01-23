package pw.keller.api.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;
import pw.keller.api.common.util.UrlUtil;
import pw.keller.api.config.CustomDynamicSchedule;
import pw.keller.api.entity.url.UrlService;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class CheckUrlTask{

    private final Logger logger = LoggerFactory.getLogger(CheckUrlTask.class);

    private CustomDynamicSchedule dynamicSchedule;
    private final UrlService urlService;

    @Autowired
    public CheckUrlTask(UrlService urlService, CustomDynamicSchedule dynamicSchedule) {
        this.urlService = urlService;
        this.dynamicSchedule = dynamicSchedule;
    }

    //@Scheduled(cron = "0 0 3 ? * *")


    public void updateInterval(long freshIntervalMilliseconds){
        dynamicSchedule.delay(freshIntervalMilliseconds);
    }
}
