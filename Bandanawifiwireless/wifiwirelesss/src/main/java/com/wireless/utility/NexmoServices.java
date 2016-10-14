package com.wireless.utility;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerDetails;
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
	urlParameters.add(new BasicNameValuePair("api_key",apikey));
	urlParameters.add(new BasicNameValuePair("api_secret",api_secret));

	
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
 HttpGet get = new HttpGet("https://rest.nexmo.com/sms/json?api_key="+apikey+"&api_secret="+api_secret+"&to="+message.getTo()+"&from="+message.getFrom()+"&text="+URLEncoder.encode(message.getBody()));
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
	   messagesdatabase.setUsername(message.getFrom());
	   messagesdatabase.setPassword(message.getPassword());
	   messagesdatabase.setSource(message.getFrom());
	   messagesdatabase.setDestination(message.getTo());
	   messagesdatabase.setText(message.getBody());
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

public static NumberResponse test(){
	
	
	



	HttpClient httpClient = new DefaultHttpClient();
	Gson gson = new Gson();
	HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers");
	try {
		HttpResponse response;
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-type", "application/json");
		post.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
		post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
		post.addHeader("X-Auth-Token","cd10af7566dc4882999d1452b361d1f827629df8");

	response = httpClient.execute(post);


	
	System.out.println(response.toString());
	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
//		   System.out.println(responseString); 
		   ArrayList<CustomerDetails> customerDetails=    gson.fromJson(responseString, new TypeToken<List<CustomerDetails>>(){}.getType());
		   System.out.println(customerDetails.get(0).getFirst_name());
		   
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	return null;

	

}



public static NumberResponse testCreate(){
	
	
	



	HttpClient httpClient = new DefaultHttpClient();
	Gson gson = new Gson();
	HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers/created");
	try {
		HttpResponse response;
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-type", "application/json");
		post.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
		post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
		post.addHeader("X-Auth-Token","cd10af7566dc4882999d1452b361d1f827629df8");

	response = httpClient.execute(post);


	
	System.out.println(response.toString());
	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
		   System.out.println(responseString); 
		   ArrayList<CustomerDetails> customerDetails=    gson.fromJson(responseString, new TypeToken<List<CustomerDetails>>(){}.getType());
		   System.out.println(customerDetails.get(0).getFirst_name());
		   
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	return null;

	

}


public static NumberResponse testPbx(){
	
	
	



	HttpClient httpClient = new DefaultHttpClient();
	Gson gson = new Gson();
	HttpGet post = new HttpGet("http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.ext.add&server=&name=kirtiTest&secret=kirti123!&email=kirti.mandwade@wwindia.com&ext=10000098&location=1&ua=50&status=1&pin=4444&incominglimit=7&outgoinglimit=3&voicemail=0&prot=sip");
	try {
		HttpResponse response;
	/*	post.addHeader("Accept", "application/json");
		post.addHeader("Content-type", "application/json");
		post.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
		post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
		post.addHeader("X-Auth-Token","cd10af7566dc4882999d1452b361d1f827629df8");
*/
	response = httpClient.execute(post);


	
	System.out.println(response.toString());
	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
		   System.out.println(responseString); 
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	return null;

	

}




public static NumberResponse createHook(){
	
	
	



	HttpClient httpClient = new DefaultHttpClient();
	Gson gson = new Gson();
	HttpPost post = new HttpPost("https://api.bigcommerce.com/stores/store-wiusit9d78.mybigcommerce.com/v2/hooks");
	
	List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	urlParameters.add(new BasicNameValuePair("scope","store/customer/created"));
//	urlParameters.add(new BasicNameValuePair("destination","http://70.182.179.17:8080/wifiwireless/getCustomer"));
	urlParameters.add(new BasicNameValuePair("is_active","true"));


	try {
		HttpResponse response;
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-type", "application/json");
//		post.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
		post.addHeader("X-Auth-Client","fykbvd5ar636ilc1stlam2tor51gpgf");
		post.addHeader("X-Auth-Token","r269xmy3i8upibr12h0myfxee6f0hlx");
//		post.addHeader("X-Custom-Auth-Header","r269xmy3i8upibr12h0myfxee6f0hlx");
		post.setEntity(new UrlEncodedFormEntity(urlParameters));

	response = httpClient.execute(post);


	
	System.out.println(response.toString());
/*	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
//		   System.out.println(responseString); 
		   ArrayList<CustomerDetails> customerDetails=    gson.fromJson(responseString, new TypeToken<List<CustomerDetails>>(){}.getType());
		   System.out.println(customerDetails.get(0).getFirst_name());
*/		   
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	return null;

	

}



public static NumberResponse oAuth(){
	
	
	



	HttpClient httpClient = new DefaultHttpClient();
	Gson gson = new Gson();
	HttpPost post = new HttpPost("https://login.bigcommerce.com/oauth2/token");
	
	List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
	urlParameters.add(new BasicNameValuePair("client_id","fykbvd5ar636ilc1stlam2tor51gpgf"));
	urlParameters.add(new BasicNameValuePair("client_secret","93go3m7lfq7yoay6kjzpwyfjradkmt1"));
	urlParameters.add(new BasicNameValuePair("code","r269xmy3i8upibr12h0myfxee6f0hlx"));
	urlParameters.add(new BasicNameValuePair("scope","store_v2_products"));
	urlParameters.add(new BasicNameValuePair("grant_type","authorization_code"));
	urlParameters.add(new BasicNameValuePair("redirect_uri","http://70.182.179.17:8080/wifiwireless/getReceipts"));
	urlParameters.add(new BasicNameValuePair("context","stores/wiusit9d78"));

	
	try {
		HttpResponse response;
//		post.addHeader("Accept", "application/json");
		post.addHeader("Content-type", "application/x-www-form-urlencoded");
//		post.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
		/*post.addHeader("X-Auth-Client","fykbvd5ar636ilc1stlam2tor51gpgf");
		post.addHeader("X-Auth-Token","cd10af7566dc4882999d1452b361d1f827629df8");
		post.addHeader("code","r269xmy3i8upibr12h0myfxee6f0hlx");
	*/	post.setEntity(new UrlEncodedFormEntity(urlParameters));

	response = httpClient.execute(post);


	
	System.out.println(response.toString());
	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
		   System.out.println(responseString); 
/*	String responseString = IOUtils.toString(response.getEntity()
		     .getContent(), "UTF-8");
//		   System.out.println(responseString); 
		   ArrayList<CustomerDetails> customerDetails=    gson.fromJson(responseString, new TypeToken<List<CustomerDetails>>(){}.getType());
		   System.out.println(customerDetails.get(0).getFirst_name());
*/		   
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
//test();
		oAuth();
//		oAuth();
		
//		testPbx();
		//buyNumber("US", "16192596886","abc","123");
	}
	
}
