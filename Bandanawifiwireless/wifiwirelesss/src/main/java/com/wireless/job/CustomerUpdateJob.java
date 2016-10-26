package com.wireless.job;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wireless.utility.NexmoServices;

public class CustomerUpdateJob implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
		try {
			NexmoServices.customerSaveOrUpdate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
