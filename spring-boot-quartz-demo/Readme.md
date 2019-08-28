# Spring Boot Quartz Scheduler Example

Este proyecto está construido con Spring Framework Boot (https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-quartz.html).
Es un API Rest con el endpoint POST /schedule que programa una tarea para ejecutarse dentro de N segundos.
La tarea se genera con la librería Quartz (http://www.quartz-scheduler.org)

**Ejemplo completo con envio de mail y persistencia:** https://www.callicoder.com/spring-boot-quartz-scheduler-email-scheduling-example/

## Steps to Setup

**1. Clonar repo**

```bash
git clone https://github.com/jonybuzz/quartz-ejemplos.git
```

**2. Correr desde la carpeta spring-boot-quartz-demo**

Finally, You can run the app by typing the following command from the root directory of the project -

```bash
mvn spring-boot:run
```

## Activar Job usando este comando o desde Postman

```bash
curl -i -H "Content-Type: application/json" -X POST \
http://localhost:8080/schedule?segundos=5

# Output
{"success":true,"jobId":"0741eafc-0627-446f-9eaf-26f5d6b29ec2","jobGroup":"email-jobs","message":"Ok!"}
```