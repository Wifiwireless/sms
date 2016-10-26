package com.wireless.job;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.model.CustomerDetails;

public class CheckBillJob implements Job {

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

         CustomerDaoInterface customerDao =  JndiLookup.getCustomerDetails();
         List<CustomerDetails> customerDetailsList = customerDao.getAllCustomers();
         
         if(customerDetailsList !=null && customerDetailsList.size() > 0){
        	  for (CustomerDetails customerDetails : customerDetailsList) {
      			System.out.println("Date Modofied :"+customerDetails.getDate_modified());
      		}
         }else{
        	 
        	  System.out.println("No Customer Founds :"+customerDetailsList);
         }
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println("current date :"+new Date());
		
		Date currentDate = new Date();
		
		String stringDate = "Mon, 27 Sep 2016 15:13:56 +0000";
		String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";
		
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date modifiedDate = format.parse(stringDate);
		System.out.println("modified date :"+modifiedDate);
		
	
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(modifiedDate);
		cal.add(Calendar.DATE, 25);
		
		Date checkDate = cal.getTime();
		
		
		
		if(checkDate.getMonth() == currentDate.getMonth() && checkDate.getDate() == currentDate.getDate() && checkDate.getYear() == currentDate.getYear()){
			System.out.println("Matched");
			//send email of pay  billing after 25 days 
		}
		
		GregorianCalendar diactivateCal = new GregorianCalendar();
		diactivateCal.setTime(modifiedDate);
		diactivateCal.add(Calendar.DATE, 30);
		
		Date deactiveDate = diactivateCal.getTime();
		
		System.out.println("Deactivate Date :"+deactiveDate);
		
		if(deactiveDate.getMonth() == currentDate.getMonth() && deactiveDate.getDate() == currentDate.getDate() && deactiveDate.getYear() == currentDate.getYear()){
			System.out.println("Deactivate account");
			//Deactivate account after 30 days
		}
	}

}

