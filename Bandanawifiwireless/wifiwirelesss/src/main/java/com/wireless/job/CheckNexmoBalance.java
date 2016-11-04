package com.wireless.job;

import java.util.Date;
import java.util.HashMap;
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
import com.wireless.bean.NexmoBalance;
import com.wireless.email.Mail;
import com.wireless.utility.Schedulars;
import com.wireless.utility.WifiWirlessConstants;

public class CheckNexmoBalance implements Job ,WifiWirlessConstants{

	private static final Logger log = LoggerFactory.getLogger(CheckNexmoBalance.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		HttpClient httpClient = new DefaultHttpClient();
		
		log.info("CheckNexmoBalance :"+new Date());
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
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			
			log.info("Nexmo Account Balance :"+responseString);
			
			NexmoBalance  nexmoBalance = gson.fromJson(responseString, new TypeToken<NexmoBalance>(){ }.getType());
			
		   if(nexmoBalance.getValue() < 1){
			    String subject = "Insufficient Nexmo Balance";
				Map<String, String> rootMap = new HashMap<String, String>();

				rootMap.put("balance", "" + nexmoBalance.getValue());
				
				try {
					Mail.email("balanceMail", subject, rootMap, "nexmoBalance.ftl");
					log.info("Email sent to client succesfully");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.info("******Error while sending nexmo balance email ");
					e.printStackTrace();
				}
		   }else{
			   log.info("Sufficient Next Balance :"+nexmoBalance.getValue());
		   }
		   
		}catch(Exception e){
			log.info("******Error while sending nexmo balance email ");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws JobExecutionException {
		new CheckNexmoBalance().execute(null);
	}
}
