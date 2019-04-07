package tpiskorski.vboxcm.quartz.monitor;

import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

@Profile("!demo")
@Controller
public class DefaultServerMonitor implements InitializingBean, ServerMonitor {

    private static final String REGULAR_SERVER_SCAN = "regularServerScan";
    private final Scheduler scheduler;

    @Autowired public DefaultServerMonitor(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void scheduleRegularScans() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ServerRefreshJob.class)
            .withIdentity(REGULAR_SERVER_SCAN)
            .storeDurably()
            .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
            .forJob(jobDetail)
            .withIdentity(jobDetail.getKey().getName())
            .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * ? * *"))
            .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override public void afterPropertiesSet() throws Exception {
        scheduleRegularScans();
    }

    @Override public void pause() {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(REGULAR_SERVER_SCAN));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override public boolean isPaused() {
        try {
            Trigger.TriggerState regularServerScan = scheduler.getTriggerState(TriggerKey.triggerKey(REGULAR_SERVER_SCAN));
            return regularServerScan == Trigger.TriggerState.PAUSED;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
    }
}