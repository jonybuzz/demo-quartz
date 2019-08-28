package com.example.quartzdemo.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class MiJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(MiJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("///////////////////////////////////////////////////////////");
        logger.info("Executando Job con key {}", jobExecutionContext.getJobDetail().getKey());
        logger.info("///////////////////////////////////////////////////////////");
    }

}
