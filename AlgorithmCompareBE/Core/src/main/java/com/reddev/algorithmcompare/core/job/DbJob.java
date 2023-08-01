package com.reddev.algorithmcompare.core.job;

import com.reddev.algorithmcompare.core.service.DbJobService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class DbJob implements Job {

    @Autowired
    private DbJobService dbJobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Executing DbJob");
        dbJobService.deleteOldDbData().subscribe();
    }
}
