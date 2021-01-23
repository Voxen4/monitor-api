package pw.keller.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import pw.keller.api.common.util.UrlUtil;
import pw.keller.api.entity.url.UrlService;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

public class CustomDynamicSchedule extends DynamicSchedule implements Trigger {

    private TaskScheduler taskScheduler;
    private ScheduledFuture<?> schedulerFuture;

    /**
     * milliseconds
     */
    public long delayInterval = 900000;

    private UrlService getUrlService() {
        return SpringContext.getBean(UrlService.class);
    }

    public CustomDynamicSchedule(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        this.delay(delayInterval);
    }


    @Override
    public void increaseDelayInterval(Long delay) {
        if (schedulerFuture != null) {
            schedulerFuture.cancel(true);
        }
        this.delayInterval += delay;
        schedulerFuture = taskScheduler.schedule(this::checkUrls, this);
    }

    @Override
    public void decreaseDelayInterval(Long delay) {
        if (schedulerFuture != null) {
            schedulerFuture.cancel(true);
        }
        this.delayInterval -= delay;
        schedulerFuture = taskScheduler.schedule(this::checkUrls, this);
    }

    @Override
    public void delay(Long delay) {
        if (schedulerFuture != null) {
            schedulerFuture.cancel(true);
        }
        this.delayInterval = delay;
        schedulerFuture = taskScheduler.schedule(this::checkUrls, this);
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date lastTime = triggerContext.lastActualExecutionTime();
        return (lastTime == null) ? new Date() : new Date(lastTime.getTime() + delayInterval);
    }

    public void checkUrls() {
        getUrlService().getAll().forEach(url -> {
            url.setStatusCode(UrlUtil.getUrlStatusCode(url.getUrl()));
            getUrlService().update(url);
        });
    }

}
