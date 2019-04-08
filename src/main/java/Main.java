import org.quartz.*;

import java.io.IOException;

public class Main {

    public static String id, URL;

    public static void main(String[] args) throws IOException {

        if(args.length != 4) {
            System.err.println("parameters are: <hourOfTheDay> <minuteOfTheHour> <HTMLTagId> <URL>");
        }

        id = args[2];
        URL = args[3];

        JobDetail job = JobBuilder.newJob(URLPollingJob.class)
                .withIdentity("URLPollingJob", "group").build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTriggerURLPolling", "group")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(Integer.parseInt(args[0]), Integer.parseInt(args[1])))
                .build();
    }
}
