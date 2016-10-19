package com.wireless.utility;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;

public class Schedulars {
	static Scheduler scheduler;
	
	
	public static void schedular() {

		 
		try {
		
		JobDetail job = new JobDetail();
		job.setName("schedular");
		job.setJobClass(CustomerUpdate.class);


		
		
		SimpleTrigger trigger = new SimpleTrigger();
		trigger.setName("trigger");
		trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
		trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
		trigger.setRepeatInterval(Long.parseLong(WifiWirlessConstants.SchedularInterval));
		
		
	
		scheduler.scheduleJob(job,trigger);
		
		
		

	} catch (SchedulerException e) {
		e.printStackTrace();
		//LOG.error(e);

	}finally {}
}
}
