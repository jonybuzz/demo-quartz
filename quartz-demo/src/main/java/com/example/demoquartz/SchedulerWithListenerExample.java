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

public class SchedulerWithListenerExample {

    private static int REPETICIONES = 3;
    private CountDownLatch contadorSincronico = new CountDownLatch(REPETICIONES + 1);

    
    public static void main(String[] args) throws Exception {
        SchedulerWithListenerExample schedulerExample = new SchedulerWithListenerExample();
        schedulerExample.comenzar();
    }

    
    public void comenzar() throws SchedulerException, InterruptedException {

        // Creacion del scheduler
        SchedulerFactory schedFactory = new org.quartz.impl.StdSchedulerFactory();
        Scheduler scheduler = schedFactory.getScheduler();

        // registro de un listener propio
        scheduler.getListenerManager()
                .addSchedulerListener(new LogSchedulerListenerImpl(scheduler));

        scheduler.start();

        // Construccion de JobDetail
        JobBuilder jobBuilder = JobBuilder.newJob(JobImpl.class);
        JobDataMap data = new JobDataMap();
        data.put("contadorSincronico", contadorSincronico);

        JobDetail jobDetail = jobBuilder
                .withIdentity("unJob")
                .usingJobData(data)
                .usingJobData("ejemplo", "algun valor")
                .build();

        // Construccion de Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("unTrigger")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withRepeatCount(REPETICIONES)
                        .withIntervalInSeconds(2))
                .build();

        // Asignacion del job y el trigger a la inst de scheduler
        scheduler.scheduleJob(jobDetail, trigger);

        // Para que el proceso principal espere a los calendarizados.
        // Porque en Java cuando el hilo principal muere, todos los sub-hilos también.
        contadorSincronico.await();
        scheduler.shutdown();
    }

}
