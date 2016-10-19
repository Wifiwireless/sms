package com.wireless.utility;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CustomerUpdate implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
		NexmoServices.customerSaveOrUpdate();
	}

}
