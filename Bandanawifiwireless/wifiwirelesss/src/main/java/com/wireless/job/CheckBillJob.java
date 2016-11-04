package com.wireless.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.model.CustomerDetails;
import com.wireless.email.Mail;
import com.wireless.utility.Schedulars;

public class CheckBillJob implements Job {

	private static final Logger log = LoggerFactory.getLogger(CheckBillJob.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		CustomerDaoInterface customerDao = JndiLookup.getCustomerDetails();
		List<CustomerDetails> customerDetailsList = customerDao.getAllCustomers();

		if (customerDetailsList != null && customerDetailsList.size() > 0) {
			for (CustomerDetails customerDetails : customerDetailsList) {

				Date currentDate = new Date();

				if (customerDetails.getDate_modified() != null) {

					// String stringDate = "Mon, 27 Sep 2016 15:13:56 +0000";
					String pattern = "EEE, dd MMM yyyy HH:mm:ss Z";

					SimpleDateFormat format = new SimpleDateFormat(pattern);
					Date modifiedDate;
					try {
						modifiedDate = format.parse(customerDetails.getDate_modified());

						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(modifiedDate);
						cal.add(Calendar.DATE, 25);

						Date checkDate = cal.getTime();

						if (checkDate.getMonth() == currentDate.getMonth()
								&& checkDate.getDate() == currentDate.getDate()
								&& checkDate.getYear() == currentDate.getYear()) {
							log.info("Matched");
							// send email of pay billing after 25 days
							log.info("modified date :" + modifiedDate);
							log.info("date after 25 days Date :" + checkDate);
							log.info("current date :" + new Date());
							sendPayBillEmail(customerDetails);

						}

						GregorianCalendar diactivateCal = new GregorianCalendar();
						diactivateCal.setTime(modifiedDate);
						diactivateCal.add(Calendar.DATE, 30);

						Date deactiveDate = diactivateCal.getTime();


						if (deactiveDate.getMonth() == currentDate.getMonth()
								&& deactiveDate.getDate() == currentDate.getDate()
								&& deactiveDate.getYear() == currentDate.getYear()) {
							log.info("Deactivate account");
							log.info("modified date :" + modifiedDate);
							log.info("date after 30 days Date:" + deactiveDate);
							log.info("current date :" + new Date());

							// Deactivate account after 30 days
							
							deactivateAccount(customerDetails);
							
						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					log.info("customer dont have modified date ");
				}

			}
		} else {

			log.info("No Customer Founds :" + customerDetailsList);
		}
	}

	public static Boolean sendPayBillEmail(CustomerDetails customerDetails) {
		String subject = "UtalkWifi Application Credentials";
		Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("date", "" + new Date());
		try {
			Mail.email(customerDetails.getEmail(), subject, rootMap, "payBillEmail.ftl");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void deactivateAccount(CustomerDetails customerDetails) {
		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet(
				"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.ext.edit&server=&name="
						+ customerDetails.getEmail() + "&secret=" + customerDetails.getSecret() + "&email="
						+ customerDetails.getEmail() + "&ext=" + "" + customerDetails.getExtension()
						+ "&location=1&ua=50&status=0&pin=4444&incominglimit=7&outgoinglimit=3&voicemail=0&prot=sip&setcallerid=1");
		try {
			HttpResponse response;

			response = httpClient.execute(post);

			log.info(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			log.info("Account Disabled succesfully :" + responseString);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
