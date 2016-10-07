package com.wireless.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.Messages;
import com.wireless.bean.AcquireResponse;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.bean.SendMessage;
import com.wireless.bean.SendMessageResponse;

public class NexmoServices {

	
	private static String apikey="13e20103";
	private static String api_secret="6ace27c0f1cf63e0";
	
	public static NumberResponse acquireNumber(String country,String pattern){
		
		
		
		//https://rest.nexmo.com/number/search?api_key=abcdefghi&api_secret=12345678&country=US&pattern=1619&search_pattern=0&features=sms,voice&size=1
		
		
		

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet get = new HttpGet("https://rest.nexmo.com/number/search?api_key="+apikey+"&api_secret="+api_secret+"&country="+country+"&pattern="+pattern+"&search_pattern=0&features=sms,voice&size=1");
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
	
	
public static BuyNumberResponse buyNumber(String country,String msisdn,String username,String password){
		
		
		

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

		
					NumberDetailsInterface numberInterface=JndiLookup.getNumberDetailsDao();
					numberInterface.checkandUpdate(msisdn,username,password);
					
					 BuyNumberResponse byNumResp = new BuyNumberResponse();
				     byNumResp.setSuccess("your purchase is successful");
				     
				     return byNumResp;
					
					
					//update db
				
				
		}else if(response.getStatusLine().getStatusCode() == 401) {

			   BuyNumberResponse byNumResp = new BuyNumberResponse();
			  byNumResp.setError("you supplied incorrect security and authentication information");
			   
			  return byNumResp;
			      }
			  else{
			   BuyNumberResponse byNumResp = new BuyNumberResponse();
			   byNumResp.setError("you supplied incorrect parameters");
			   return byNumResp;
			   
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
	
	
	




public static SendMessageResponse sendMessage(SendMessage message){
 
 HttpClient httpClient = new DefaultHttpClient();
 Gson gson = new Gson();

 MessagesInterface messageinterface=JndiLookup.getMessageDao();
 HttpGet get = new HttpGet("https://rest.nexmo.com/sms/search?api_key="+apikey+"&api_secret="+api_secret+"&to="+message.getTo()+"&from="+message.getFrom()+"&text="+message.getText());
 try {
  HttpResponse response;

 response = httpClient.execute(get);

 if (response.getStatusLine().getStatusCode() == 200) {

  String responseString;
 
   responseString = IOUtils.toString(response.getEntity()
     .getContent(), "UTF-8");
   System.out.println(responseString); 
   
   SendMessageResponse msgResponse= gson.fromJson(responseString, SendMessageResponse.class);
   if(msgResponse.getMessages().size()>0 && msgResponse.getMessages()!=null)
   {
	   Messages messagesdatabase=new Messages(msgResponse.getMessages().get(0).getStatus(), msgResponse.getMessages().get(0).getMessageId(), msgResponse.getMessages().get(0).getRemainingBalance(), msgResponse.getMessages().get(0).getMessagePrice(), msgResponse.getMessages().get(0).getNetwork());
	   messagesdatabase.setUsername(message.getUsername());
	   messagesdatabase.setPassword(message.getPassword());
	   messagesdatabase.setSource(message.getFrom());
	   messagesdatabase.setDestination(message.getTo());
	   messagesdatabase.setText(message.getText());
	   messagesdatabase.setMessagetime(new Date());
	   messageinterface.addMesages(messagesdatabase);
   }
   
   return msgResponse;
   
 } else {
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

 

	
	public static void main(String[] args) {
		
		buyNumber("US", "16192596886","abc","123");
	}
	
}
