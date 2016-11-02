package com.wireless.utility;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

import com.wireless.job.CheckBillJob;
import com.wireless.job.CheckOrderJob;
import com.wireless.job.CustomerUpdateJob;

public class Schedulars {
	static Scheduler scheduler;
	static Scheduler checkBillScheduler;

	public static void schedular() {

		try {

			JobDetail job = new JobDetail();
			job.setName("schedular");
			job.setJobClass(CustomerUpdateJob.class);

			SimpleTrigger trigger = new SimpleTrigger();
			trigger.setName("trigger");
			trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
			trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
			trigger.setRepeatInterval(Long.parseLong(WifiWirlessConstants.SchedularInterval));
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			System.out.println("Update customer Schedular started");
			
			JobDetail orderjob = new JobDetail();
			orderjob.setName("orderjob");
			orderjob.setJobClass(CheckOrderJob.class);

			SimpleTrigger ordertrigger = new SimpleTrigger();
			ordertrigger.setName("ordertrigger");
			ordertrigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
			ordertrigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
			ordertrigger.setRepeatInterval(Long.parseLong(WifiWirlessConstants.SchedularInterval));
			scheduler = new StdSchedulerFactory().getScheduler();
			
			scheduler.scheduleJob(job, trigger);
			scheduler.scheduleJob(orderjob, ordertrigger);
			System.out.println("Update order schedular started");


		} catch (SchedulerException e) {
			e.printStackTrace();
			// LOG.error(e);

		} finally {
		}
	}


    public static void 	checkBillScheduler(){
    	
    	try {
    		
    		JobDetail job = new JobDetail();
			job.setName("checkBillingScheduler");
			job.setJobClass(CheckBillJob.class);
			
    		CronTrigger trigger = new CronTrigger();
        	trigger.setName("checkBillingTriggerName");
			trigger.setCronExpression("0/15 * * * * ?");//0 0 12 * * ?    Fire at 12pm (noon) every day
			
			
			checkBillScheduler = new StdSchedulerFactory().getScheduler();
			checkBillScheduler.start();
			checkBillScheduler.scheduleJob(job, trigger);
	    	
	    	
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
	 
    
	public static Scheduler getCheckBillScheduler() {
		return checkBillScheduler;
	}

	public static void setCheckBillScheduler(Scheduler checkBillScheduler) {
		Schedulars.checkBillScheduler = checkBillScheduler;
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}

	public static void setScheduler(Scheduler scheduler) {
		Schedulars.scheduler = scheduler;
	}
	
	/*
	0 1 * * *
	| | | | |
	| | | | |
	| | | | +---- Run every day of the week
	| | | +------ Run every month of the year
	| | +-------- Run every day of the month
	| +---------- Run at 1 Hour (1AM)
	+------------ Run at 0 Minute*/
}
