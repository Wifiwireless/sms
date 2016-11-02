package com.wireless.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.SchedulerException;

public class Initializer implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
			System.out.println("Schedular Stop");
			Schedulars.getScheduler().shutdown();
			
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Context Initialized");
		Schedulars.schedular();
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
