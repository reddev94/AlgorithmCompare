package com.reddev.algorithmcompare.core.business;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class DbJob implements Job {
    private Logger logger = LoggerFactory.getLogger(DbJob.class);

    @Autowired
    private DbJobService dbJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing DbJob");
        dbJobService.deleteOldDbData().subscribe();
    }
}
