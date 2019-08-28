package com.example.demoquartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobImpl implements Job {

    private static int count;

    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Inicio: " + jobContext.getFireTime());
        JobDetail jobDetail = jobContext.getJobDetail();
        System.out.println("Info: " + jobDetail.getJobDataMap().getString("info"));
        System.out.println("Fin: " + jobContext.getJobRunTime() + ", key: " + jobDetail.getKey());
        System.out.println("Proxima ejecucion: " + jobContext.getNextFireTime());
        System.out.println("--------------------------------------------------------------------");

        ILatch latch = (ILatch) jobDetail.getJobDataMap().get("latch");
        latch.countDown();
        count++;
        System.out.println("Job count " + count);
        if (count == 2) {
            throw new RuntimeException("Some RuntimeException!");
        }
        if (count == 4) {
            throw new JobExecutionException("Some JobExecutionException!");
        }
    }

}
