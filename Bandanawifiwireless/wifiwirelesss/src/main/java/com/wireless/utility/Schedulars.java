package com.wireless.utility;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wireless.job.CheckBillJob;
import com.wireless.job.CheckNexmoBalance;
import com.wireless.job.CheckOrderJob;
import com.wireless.job.CheckPaidFlagJob;
import com.wireless.job.CustomerUpdateJob;

public class Schedulars {
	
	private static final Logger log = LoggerFactory.getLogger(Schedulars.class);
	
	static Scheduler scheduler;
	static Scheduler checkBillScheduler;
	static Scheduler checkPaidFlagScheduler;

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
			
			log.info("Update customer Schedular started");

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
			log.info("Update order schedular started");

		} catch (SchedulerException e) {
			e.printStackTrace();
			// LOG.error(e);

		} finally {
		}
	}

	public static void checkBillScheduler() {

		try {

			JobDetail job = new JobDetail();
			job.setName("checkBillingScheduler");
			job.setJobClass(CheckBillJob.class);

			CronTrigger trigger = new CronTrigger();
			trigger.setName("checkBillingTriggerName");
			trigger.setCronExpression("0/15 * * * * ?");// 0 0 12 * * ? Fire at
														// 12pm (noon) every day

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

	public static void checkPaidFlagScheduler() {
		try {

			log.info("checkPaidFlagScheduler Schedular started :");

			JobDetail job = new JobDetail();
			job.setName("checkPaidFlagScheduler");
			job.setJobClass(CheckPaidFlagJob.class);

			CronTrigger trigger = new CronTrigger();
			trigger.setName("checkPaidFlagTriggerName");
			trigger.setCronExpression("0/15 * * * * ?");// 0 */2 * * * this should execute in 1 hour

			checkPaidFlagScheduler = new StdSchedulerFactory().getScheduler();
			checkPaidFlagScheduler.start();
			checkPaidFlagScheduler.scheduleJob(job, trigger);

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void checkNexmoBalanceScheduler() {

		try {

			log.info("checkNexmoBalanceScheduler Schedular started :");
			
			
			JobDetail job = new JobDetail();
			job.setName("checkNexmoBalanceScheduler");
			job.setJobClass(CheckNexmoBalance.class);

			CronTrigger trigger = new CronTrigger();
			trigger.setName("checkNexmoBalanceTriggerName");
			trigger.setCronExpression("0 40 7 * * ?");// 0 0 12 * * ? Fire at
														// 12pm (noon) every day
		
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

}
