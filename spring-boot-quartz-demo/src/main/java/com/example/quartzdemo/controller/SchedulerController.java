package com.example.quartzdemo.controller;

import com.example.quartzdemo.job.MiJob;
import com.example.quartzdemo.payload.ScheduleResponse;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class SchedulerController {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerController.class);

    @Autowired //instanciacion de Spring
    private Scheduler scheduler;

    @PostMapping("/schedule")
    public ScheduleResponse scheduleEmail(@RequestParam Long segundos) {
        try {
            ZonedDateTime horaEjecucion = ZonedDateTime.now().plusSeconds(segundos);

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("segundos", segundos);
            jobDataMap.put("otraInfo", "INFORMACION EXTRA");

            JobDetail jobDetail = JobBuilder.newJob(MiJob.class)
                    .withIdentity(UUID.randomUUID().toString(), "mis-jobs")
                    .withDescription("Mi JobDetail")
                    .usingJobData(jobDataMap)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity(jobDetail.getKey().getName(), "email-triggers")
                    .withDescription("Mi Trigger")
                    .startAt(Date.from(horaEjecucion.toInstant()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);

            return new ScheduleResponse(true,
                    jobDetail.getKey().getName(), jobDetail.getKey().getGroup(), "OK!");

        } catch (SchedulerException ex) {
            logger.error("Error en scheduer", ex);
            return new ScheduleResponse(false, "Error!");
        }
    }

}
