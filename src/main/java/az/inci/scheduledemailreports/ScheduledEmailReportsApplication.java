package az.inci.scheduledemailreports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduledEmailReportsApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(ScheduledEmailReportsApplication.class, args);
    }

}
