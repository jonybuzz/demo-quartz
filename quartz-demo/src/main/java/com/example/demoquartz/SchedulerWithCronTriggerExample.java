package com.example.demoquartz;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

public class SchedulerWithCronTriggerExample {

    private CountDownLatch contadorSincronico = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        SchedulerWithCronTriggerExample schedulerExample = new SchedulerWithCronTriggerExample();
        schedulerExample.comenzar();
    }

    public void comenzar() throws SchedulerException, InterruptedException {

        // Creacion del scheduler
        SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedFactory.getScheduler();
        // registro de un listener propio
        scheduler.getListenerManager().addSchedulerListener(new LogSchedulerListenerImpl(scheduler));

        // Construccion de JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(JobImpl.class);
        JobDataMap data = new JobDataMap();
        data.put("contadorSincronico", contadorSincronico);
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

        // Formato de expresion cron
        // ┌───────────── minute (0 - 59)
        // │ ┌───────────── hour (0 - 23)
        // │ │ ┌───────────── day of the month (1 - 31)
        // │ │ │ ┌───────────── month (1 - 12)
        // │ │ │ │ ┌───────────── day of the week (0 - 6) (Sunday to Saturday)
        // │ │ │ │ │ ┌───────────── year
        // │ │ │ │ │ │
        // * * * * * *

        String cron = "0 " + (min + 1) + " " + hour + " * * ? *";
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("unTrigger")
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();

        // Asignacion del job y el trigger a la inst de scheduler
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();

        // Para que el proceso principal espere a los calendarizados.
        // Porque en Java cuando el hilo principal muere, todos los sub-hilos también.
        contadorSincronico.await(); // esperando fin de las ejecuciones
        scheduler.shutdown();
    }

}
