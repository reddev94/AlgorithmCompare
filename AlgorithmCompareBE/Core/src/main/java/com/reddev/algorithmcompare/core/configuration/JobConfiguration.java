package com.reddev.algorithmcompare.core.configuration;

import com.reddev.algorithmcompare.core.job.DbJob;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {

    @Value("${job.trigger.timeout}")
    private long jobTriggerTimeout;

    private final ApplicationContext applicationContext;
    private final QuartzProperties quartzProperties;

    @Bean
    public SchedulerFactoryBean scheduler(Trigger... triggers) {

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setJobFactory(jobFactory);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        if (!ArrayUtils.isEmpty(triggers)) {
            schedulerFactory.setTriggers(triggers);
        }
        return schedulerFactory;
    }

    @Bean(name = "deleteOldDataJob")
    public JobDetailFactoryBean deleteOldDataJob() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName("Delete old db data Job");
        factoryBean.setJobClass(DbJob.class);
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean(name = "triggerDeleteOldDataJob")
    public SimpleTriggerFactoryBean triggerDeleteOldDataJob(@Qualifier("deleteOldDataJob") JobDetail jobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(10000L);
        factoryBean.setRepeatInterval(jobTriggerTimeout);
        factoryBean.setName("Delete old db data Job trigger");
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
        return factoryBean;
    }

}
