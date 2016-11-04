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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerCheck;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.NumberDetails;
import com.wifiwireless.webservice.Webservices;
import com.wireless.bean.AcquireNumber;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.CustomerDetailsResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.email.Mail;
import com.wireless.utility.Schedulars;

import javassist.bytecode.stackmap.BasicBlock.Catch;


/*This job will send the credential to customer who ordered package;
*/


public class CheckOrderJob implements Job{

	private static final Logger log = LoggerFactory.getLogger(CheckOrderJob.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		
	
		CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
		NumberDetailsInterface numberDetailsDao = JndiLookup.getNumberDetailsDao();
		HttpClient httpClient = new DefaultHttpClient();
		log.info("getCustomersDetailsNotPaid called :");
		List<CustomerDetails> unPaidcustomerDetailsList = customerdao.getCustomersDetailsNotPaid();
        if(unPaidcustomerDetailsList!=null){
		for (CustomerDetails unPaidcustomerDetails : unPaidcustomerDetailsList) {
			Gson gson = new Gson();
			HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/orders?customer_id="
					+ unPaidcustomerDetails.getId());
			
			log.info("checking orders");
			try {
				HttpResponse response;
				post.addHeader("Accept", "application/json");
				post.addHeader("Content-type", "application/json");
				post.addHeader("Authorization", "Basic " + new String(
						Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
				post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
				post.addHeader("X-Auth-Token", "cd10af7566dc4882999d1452b361d1f827629df8");
				response = httpClient.execute(post);
				log.info("status code :"+response.getStatusLine().getStatusCode());
				
				if(response.getStatusLine().getStatusCode() == 200){
					
					String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
					log.info(responseString);

					
					ArrayList<CustomerDetailsResponse> customerDetailsResponse = gson.fromJson(responseString,
							new TypeToken<List<CustomerDetailsResponse>>() {
							}.getType());

					// If customers paid for the service then will sent the credential
					
					if (customerDetailsResponse.size() > 0 && customerDetailsResponse.get(0).getStatus().equalsIgnoreCase("Completed")) {
						unPaidcustomerDetails.setOrdered(true);
						log.info(" updating customer");
						customerdao.updateCustomer(unPaidcustomerDetails);

						NumberDetails NumberDetails = numberDetailsDao.getNumberDetails(unPaidcustomerDetails.getExtension(), unPaidcustomerDetails.getSecret());
					
						
						if (NumberDetails != null) {

							// get Number
							AcquireNumber acquireNumber = new AcquireNumber();
							acquireNumber.setUsername(unPaidcustomerDetails.getExtension());
							acquireNumber.setPassword(unPaidcustomerDetails.getSecret());
							
							acquireNumber.setPattern("1619");
							acquireNumber.setCountry("US");
							acquireNumber.setMobileNumber(unPaidcustomerDetails.getPhone());

							NumberResponse numberResponse = Webservices.getNumber(acquireNumber);
                            NumberDetails.setMsisdn(numberResponse.getMsisdn());
							// buy Number

							if (numberResponse != null) {

								BuyNumber buyNumber = new BuyNumber();

								buyNumber.setCountry(numberResponse.getCountry());
								buyNumber.setMsisdn(numberResponse.getMsisdn());
								buyNumber.setUsername(unPaidcustomerDetails.getExtension());
								buyNumber.setPassword(unPaidcustomerDetails.getSecret());

								BuyNumberResponse buyNumberResponse = Webservices.buyNumber(buyNumber);

								if (buyNumberResponse != null && "success".equals(buyNumberResponse.getSuccess())) {
								  
								    Mail.generateVerificationEmail(unPaidcustomerDetails,NumberDetails.getMsisdn(),true);
								    log.info("Numberd purchased by "+unPaidcustomerDetails.getEmail());
								    
								} else {
									Mail.generateVerificationEmail(unPaidcustomerDetails,NumberDetails.getMsisdn(),false);
									log.info("BuyNumberResponse " + buyNumberResponse);
									log.info("Numbered not purchased for this user :"+unPaidcustomerDetails.getEmail());
									log.info("Possible reason : Nexmo balance ");
								}

							}
							
							
							 
							 
						}else{
							 log.info("Number details not present for this customer :");
						}
					}

					// ArrayList<NumberDetails> arryNumber = new
					// ArrayList<NumberDetails>();
					log.info("order list size " + customerDetailsResponse.size());
				}
				

			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
	
	}

	

}
