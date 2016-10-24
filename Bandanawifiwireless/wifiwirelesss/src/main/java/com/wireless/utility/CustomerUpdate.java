package com.wireless.utility;

import java.text.ParseException;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CustomerUpdate implements Job{

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	
		try {
			NexmoServices.customerSaveOrUpdate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
