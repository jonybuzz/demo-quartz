package com.example.demoquartz;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class QuartzSchedulerCronTriggerExample implements ILatch {

    private CountDownLatch contadorSincronico = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        QuartzSchedulerCronTriggerExample quartzSchedulerExample = new QuartzSchedulerCronTriggerExample();
        quartzSchedulerExample.comenzar();
    }

    public void comenzar() throws SchedulerException, InterruptedException {

        // Creacion del scheduler
        SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedFactory.getScheduler();
        // registro de un listener propio
        scheduler.getListenerManager().addSchedulerListener(new LogSchedulerListenerImpl(scheduler));
        scheduler.start();

        // Construccion de JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(JobImpl.class);
        JobDataMap data = new JobDataMap();
        data.put("contadorSincronico", this);
        JobDetail jobDetail = jobBuilder
                .withIdentity("unJob")
                .usingJobData(data)
                .usingJobData("ejemplo", "algun valor")
                .build();

        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int min = rightNow.get(Calendar.MINUTE);

        System.out.println("Hora actual: " + new Date());

        // Construccion de Trigger
        // Corre todos los dias a la hora actual mas un minuto
        String cron = "0 " + (min + 1) + " " + hour + " * * ? *";
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("unTrigger")
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        // Asignacion del job y el trigger a la inst de scheduler
        scheduler.scheduleJob(jobDetail, trigger);
        
        contadorSincronico.await(); // esperando fin de las ejecuciones
        scheduler.shutdown();
    }

    public void countDown() {
        contadorSincronico.countDown();
    }
}
