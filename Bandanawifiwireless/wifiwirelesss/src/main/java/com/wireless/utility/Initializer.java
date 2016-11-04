package com.wireless.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wireless.job.CheckPaidFlagJob;

public class Initializer implements ServletContextListener{

	private static final Logger log = LoggerFactory.getLogger(Initializer.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			log.info("Schedular Stop");
			Schedulars.getScheduler().shutdown();
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		log.info("Context Initialized");
		Schedulars.schedular();
		Schedulars.checkPaidFlagScheduler();
		Schedulars.checkNexmoBalanceScheduler();
		//Schedulars.checkBillScheduler();
	/*	Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("username", "user");
		rootMap.put("password", "456");
		rootMap.put("date", "" + new Date());
*/
		/*try {
			NexmoServices.email("kirti.mandwade@gmail.com", "test1234", rootMap, "email.ftl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
