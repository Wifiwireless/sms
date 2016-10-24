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
		
	}

}
