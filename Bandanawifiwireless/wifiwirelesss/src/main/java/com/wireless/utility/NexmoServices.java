package com.wireless.utility;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.wireless.bean.AcquireResponse;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.NumberResponse;

public class NexmoServices {

	
	public static NumberResponse acquireNumber(String country,String pattern){
		
		
		
		//https://rest.nexmo.com/number/search?api_key=abcdefghi&api_secret=12345678&country=US&pattern=1619&search_pattern=0&features=sms,voice&size=1
		
		
		

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet get = new HttpGet("https://rest.nexmo.com/number/search?api_key=596e18cd&api_secret=8b4a428e&country="+country+"&pattern="+pattern+"&search_pattern=0&features=sms,voice&size=1");
		try {
			HttpResponse response;
	
		response = httpClient.execute(get);

		if (response.getStatusLine().getStatusCode() == 200) {

			String responseString;
		
				responseString = IOUtils.toString(response.getEntity()
						.getContent(), "UTF-8");
				System.out.println(responseString);	
				
				AcquireResponse numberResponse=	gson.fromJson(responseString, AcquireResponse.class);
				if(numberResponse.getNumbers()!=null&&numberResponse.getNumbers().size()>0){


					return numberResponse.getNumbers().get(0);
				}
				
				
		}	else {
					System.out.println("ERROR - CODE ["
									+ response.getStatusLine().getStatusCode() + "]");

						}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		return null;

		

	}
	
	
public static String buyNumber(String country,String msisdn){
		
		
		

	List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	urlParameters.add(new BasicNameValuePair("country",country));
	urlParameters.add(new BasicNameValuePair("msisdn",msisdn));
	urlParameters.add(new BasicNameValuePair("api_key","596e18cd"));
	urlParameters.add(new BasicNameValuePair("api_secret","8b4a428e"));

	
		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpPost post = new HttpPost("https://rest.nexmo.com/number/buy");
		try {
			HttpResponse response;

post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "application/json");

		response = httpClient.execute(post);

		if (response.getStatusLine().getStatusCode() == 200) {

			String responseString;
		
				responseString = IOUtils.toString(response.getEntity()
						.getContent(), "UTF-8");
				System.out.println(responseString);	
				
				if(responseString.equals("200")){
					return "Virtual number rented!";
					//update db
				}
				else{
					return "HTTP Response: " + response.getStatusLine();
				}
			
				
		}	else {
					System.out.println("ERROR - CODE ["
									+ response.getStatusLine().getStatusCode() + "]");
 return "HTTP Response: " + response.getStatusLine();
						}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		return null;

		

	}
	
	
	
	
	
	public static void main(String[] args) {
		
		buyNumber("US", "16192596886");
	}
	
}
