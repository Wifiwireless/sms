package com.wireless.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.model.CustomerDetails;

public class PushNotificationService {

	
	public static void main(String[] args) {
		pushNotification(null);
	}
	public static void pushNotification(String username){
		
		System.out.println("Push Notification :");
		
		
		CustomerDaoInterface customerDao = JndiLookup.getCustomerDetails();
		CustomerDetails customerDetails = customerDao.getCustomerDetailsByUsername(username);
		
	//	if(customerDetails !=null){
			List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			urlParameters.add(new BasicNameValuePair("AppId", customerDetails.getAppid()));
			urlParameters.add(new BasicNameValuePair("DeviceToken",customerDetails.getToken()));
			urlParameters.add(new BasicNameValuePair("Selector", customerDetails.getSelector()));
			urlParameters.add(new BasicNameValuePair("verb", "NotifyTextMessage"));
			urlParameters.add(new BasicNameValuePair("username", customerDetails.getExtension()));
			urlParameters.add(new BasicNameValuePair("password", customerDetails.getSecret()));
	

			HttpClient httpClient = new DefaultHttpClient();
			Gson gson = new Gson();
			HttpPost post = new HttpPost("https://providers.cloudsoftphone.com/pnm2");
			try {
				HttpResponse response;

				post.setEntity(new UrlEncodedFormEntity(urlParameters));
				post.setHeader("Content-type", "application/x-www-form-urlencoded");
				post.setHeader("Accept", "application/json");

				response = httpClient.execute(post);
	            System.out.println("response  code"+response.getStatusLine().getStatusCode());
	            String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
	            System.out.println(responseString);
	            
			}catch(Exception e){
				e.printStackTrace();
			}

		/*}else{
			
		}*/
		
	}

}

