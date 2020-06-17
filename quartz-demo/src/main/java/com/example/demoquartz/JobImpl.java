package com.example.demoquartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobImpl implements Job {

    private static int count = 0;

    @Override
    public void execute(JobExecutionContext jobContext) throws JobExecutionException {
        
        JobDetail jobDetail = jobContext.getJobDetail();
        count++;
        
        System.out.println("--------------------------------------------------------------------");
        System.out.println("EJECUTANDO JOB " + jobDetail.getKey());
        System.out.println("Ejecucion N° " + count);
        System.out.println("Inicio: " + jobContext.getFireTime());
        System.out.println("Info: " + jobDetail.getJobDataMap().getString("ejemplo"));
        System.out.println("Fin: " + jobContext.getJobRunTime());
        System.out.println("Proxima ejecucion: " + jobContext.getNextFireTime());
        System.out.println("--------------------------------------------------------------------");

        ICuentaRegresiva contadorSincronico = (ICuentaRegresiva) jobDetail.getJobDataMap().get("contadorSincronico");
        contadorSincronico.countDown();
        if (count == 2) {
            throw new RuntimeException("RuntimeException!");
        }
        if (count == 4) {
            throw new JobExecutionException("JobExecutionException!");
        }
    
    }

}
