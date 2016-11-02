package com.wireless.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerCheck;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.NumberDetails;
import com.wireless.email.Mail;

import javassist.bytecode.stackmap.BasicBlock.Catch;

public class CheckOrderJob implements Job{


	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	
		CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
		NumberDetailsInterface numberDetailsDao = JndiLookup.getNumberDetailsDao();
		HttpClient httpClient = new DefaultHttpClient();
		List<CustomerDetails> unPaidcustomerDetailsList = customerdao.getCustomersDetailsNotPaid();

		for (CustomerDetails unPaidcustomerDetails : unPaidcustomerDetailsList) {
			Gson gson = new Gson();
			HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/orders?customer_id="
					+ unPaidcustomerDetails.getId());
			try {
				HttpResponse response;
				post.addHeader("Accept", "application/json");
				post.addHeader("Content-type", "application/json");
				post.addHeader("Authorization", "Basic " + new String(
						Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
				post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
				post.addHeader("X-Auth-Token", "cd10af7566dc4882999d1452b361d1f827629df8");
				response = httpClient.execute(post);
				System.out.println(response.getStatusLine().getStatusCode());
				String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				ArrayList<CustomerDetails> customerDetailsFromAPI = gson.fromJson(responseString,
						new TypeToken<List<CustomerDetails>>() {
						}.getType());

				// If customers paid for the service then will sent the credential
				
				if(customerDetailsFromAPI.size() > 0){
					
						unPaidcustomerDetails.setOrdered(true);
						customerdao.updateCustomer(unPaidcustomerDetails);
						
						NumberDetails NumberDetails = numberDetailsDao.getNumberDetails(unPaidcustomerDetails.getExtension(), unPaidcustomerDetails.getSecret());
						if(NumberDetails!= null){
							//generateVerificationEmail(unPaidcustomerDetails,NumberDetails.getMsisdn());
							System.out.println("Email sent :");
						}else{
							System.out.println("Numbet details not present for this customer :");
						}
					
				}
				

				// ArrayList<NumberDetails> arryNumber = new
				// ArrayList<NumberDetails>();
				System.out.println("customer list size " + customerDetailsFromAPI.size());

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	public static Boolean generateVerificationEmail(CustomerDetails customerDetails, String msisdn) {
		String subject = "UtalkWifi Application Credentials";
		Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("username", customerDetails.getExtension());
		rootMap.put("password", customerDetails.getSecret());
		rootMap.put("number", msisdn);
		rootMap.put("date", "" + new Date());
		try {
			Mail.email(customerDetails.getEmail(), subject, rootMap, "email.ftl");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}