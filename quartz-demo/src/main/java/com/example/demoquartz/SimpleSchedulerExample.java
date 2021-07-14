package com.example.demoquartz;

import java.util.concurrent.CountDownLatch;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SimpleSchedulerExample implements ICuentaRegresiva {

    private static int REPETICIONES = 3;
    private CountDownLatch contadorSincronico = new CountDownLatch(REPETICIONES + 1);

    
    public static void main(String[] args) throws Exception {
        SimpleSchedulerExample schedulerExample = new SimpleSchedulerExample();
        schedulerExample.comenzar();
    }

    
    public void comenzar() throws SchedulerException, InterruptedException {
        
        // Creacion del scheduler
        SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedFactory.getScheduler();
        scheduler.start();

        // Construccion de JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(JobImpl.class);
        JobDataMap data = new JobDataMap();
        data.put("contadorSincronico", this);

        JobDetail jobDetail = jobBuilder
                .withIdentity("unJob", "gr")
                .usingJobData(data)
                .usingJobData("ejemplo", "algun valor")
                .build();

        // Construccion de Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("unTrigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder
                        .simpleSchedule()
                        .withRepeatCount(REPETICIONES)
                        .withIntervalInSeconds(2))
                .build();

        // Asignacion del job y el trigger a la inst de scheduler
        scheduler.scheduleJob(jobDetail, trigger);
        
        contadorSincronico.await();
        scheduler.shutdown();
    }

    public void countDown() {
        contadorSincronico.countDown();
    }
}
