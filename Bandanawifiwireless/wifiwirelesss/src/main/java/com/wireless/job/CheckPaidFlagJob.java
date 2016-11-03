package com.wireless.job;

import java.util.List;

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
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.NumberDetails;
import com.wifiwireless.webservice.Webservices;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.NexmoBalance;
import com.wireless.email.Mail;
import com.wireless.utility.WifiWirlessConstants;

public class CheckPaidFlagJob implements WifiWirlessConstants , Job{


	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		HttpClient httpClient = new DefaultHttpClient();
		NumberDetailsInterface numberDetailsDao = JndiLookup.getNumberDetailsDao();
		
		HttpGet post = new HttpGet("https://rest.nexmo.com/account/get-balance?api_key=" + apikey + "&api_secret=" + api_secret);
		Gson gson = new Gson();
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
			
			System.out.println("Nexmo Account Balance :"+responseString);
			
			NexmoBalance  nexmoBalance = gson.fromJson(responseString, new TypeToken<NexmoBalance>(){ }.getType());
			
		   if(nexmoBalance.getValue() >= 1){
			   
			  List<NumberDetails>  numberDetailsList = numberDetailsDao.getNumberDetailsByPaidFlag(false);
			   
			  if(numberDetailsList != null){
				  
				  for (NumberDetails numberDetails : numberDetailsList) {
					   CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
					   CustomerDetails customerDetails = customerdao.getCustomerDetailsByUsername(numberDetails.getUsername());
					   
					   if(customerDetails != null){
						    BuyNumber buyNumber = new BuyNumber();
							buyNumber.setCountry("US");
							buyNumber.setMsisdn(numberDetails.getMsisdn());
							buyNumber.setUsername(customerDetails.getExtension());
							buyNumber.setPassword(customerDetails.getSecret());

							BuyNumberResponse buyNumberResponse = Webservices.buyNumber(buyNumber);
							
							if(buyNumberResponse != null && "success".equals(buyNumberResponse.getSuccess())) {
									Mail.generateVerificationEmail(customerDetails, numberDetails.getMsisdn(),true);
							}else{
									System.out.println("CheckPaidFlagJob BuyNumberResponse " + buyNumberResponse);
						    }
					   }else{
						   System.out.println("CustomerDetails Not Found in Number Details : ");
					   }
					    
				  }
			
				  
			  }
			   
			   
		   }
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
