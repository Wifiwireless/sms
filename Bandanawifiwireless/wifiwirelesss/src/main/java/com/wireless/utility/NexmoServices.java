package com.wireless.utility;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sound.sampled.Line;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerCheck;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.Messages;
import com.wifiwireless.model.NumberDetails;
import com.wireless.bean.AcquireResponse;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.bean.SendMessage;
import com.wireless.bean.SendMessageResponse;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class NexmoServices implements WifiWirlessConstants {

	public static NumberResponse acquireNumber(String country, String pattern) {

		// https://rest.nexmo.com/number/search?api_key=abcdefghi&api_secret=12345678&country=US&pattern=1619&search_pattern=0&features=sms,voice&size=1

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet get = new HttpGet("https://rest.nexmo.com/number/search?api_key=" + apikey + "&api_secret=" + api_secret
				+ "&country=" + country + "&pattern=" + pattern + "&search_pattern=0&features=sms,voice&size=1");
		try {
			HttpResponse response;

			response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {

				String responseString;

				responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				AcquireResponse numberResponse = gson.fromJson(responseString, AcquireResponse.class);
				if (numberResponse.getNumbers() != null && numberResponse.getNumbers().size() > 0) {

					return numberResponse.getNumbers().get(0);
				}

			} else {
				System.out.println("ERROR - CODE [" + response.getStatusLine().getStatusCode() + "]");

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

	public static BuyNumberResponse buyNumber(String country, String msisdn, String username, String password) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("country", country));
		urlParameters.add(new BasicNameValuePair("msisdn", msisdn));
		urlParameters.add(new BasicNameValuePair("api_key", apikey));
		urlParameters.add(new BasicNameValuePair("api_secret", api_secret));

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

				NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
				numberInterface.checkandUpdate(msisdn, username, password);

				BuyNumberResponse byNumResp = new BuyNumberResponse();
				byNumResp.setSuccess("your purchase is successful");
				byNumResp.setError("false");
				return byNumResp;

				// update db

			} else if (response.getStatusLine().getStatusCode() == 401) {

				BuyNumberResponse byNumResp = new BuyNumberResponse();
				byNumResp.setError("you supplied incorrect security and authentication information");

				return byNumResp;
			} else {

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

	public static SendMessageResponse sendMessage(SendMessage message) {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();

		MessagesInterface messageinterface = JndiLookup.getMessageDao();
		HttpGet get = new HttpGet("https://rest.nexmo.com/sms/json?api_key=" + apikey + "&api_secret=" + api_secret
				+ "&to=" + message.getTo() + "&from=" + message.getFrom() + "&text="
				+ URLEncoder.encode(message.getBody()));
		try {
			HttpResponse response;

			response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {

				String responseString;

				responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				SendMessageResponse msgResponse = gson.fromJson(responseString, SendMessageResponse.class);
				if (msgResponse.getMessages().size() > 0 && msgResponse.getMessages() != null) {
					Messages messagesdatabase = new Messages(msgResponse.getMessages().get(0).getStatus(),
							msgResponse.getMessages().get(0).getMessageId(),
							msgResponse.getMessages().get(0).getRemainingBalance(),
							msgResponse.getMessages().get(0).getMessagePrice(),
							msgResponse.getMessages().get(0).getNetwork());
					messagesdatabase.setUsername(message.getFrom());
					messagesdatabase.setPassword(message.getPassword());
					messagesdatabase.setSource(message.getFrom());
					messagesdatabase.setDestination(message.getTo().trim());
					messagesdatabase.setText(message.getBody());
					messagesdatabase.setMessagetime(new Date());
					messageinterface.addMesages(messagesdatabase);
				}
				System.out.println("message sent");
				return msgResponse;

			} else {
				System.out.println("ERROR - CODE [" + response.getStatusLine().getStatusCode() + "]");

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

	public static void customerSaveOrUpdate() throws ParseException {

		CustomerCheck check = new CustomerCheck();
		CustomerCheckDaoInterface checkdao = JndiLookup.getCustomerCheckdao();
		CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
		NumberDetailsInterface numberDetailsInterface = JndiLookup.getNumberDetailsDao();
		HttpClient httpClient = new DefaultHttpClient();
		check = checkdao.getData(); 
		int cid=check.getLength()+1;
		int did=Integer.parseInt(check.getDid());
		System.out.println("Last Id is"+cid);
		Gson gson = new Gson();
		
		

		HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers?min_id="+cid);
		try {
			HttpResponse response;
			post.addHeader("Accept", "application/json");
			post.addHeader("Content-type", "application/json");
			post.addHeader("Authorization", "Basic "
					+ new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
			post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
			post.addHeader("X-Auth-Token", "cd10af7566dc4882999d1452b361d1f827629df8");
			response = httpClient.execute(post);
			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
			ArrayList<CustomerDetails> customerDetails = gson.fromJson(responseString,
					new TypeToken<List<CustomerDetails>>() {
					}.getType());
			// ArrayList<NumberDetails> arryNumber = new
			// ArrayList<NumberDetails>();
			System.out.println("customer list size " + customerDetails.size());
			ArrayList<CustomerDetails> savecustomerDetails = new ArrayList<CustomerDetails>();
			
			for (CustomerDetails cus : customerDetails) {
				
			/*		Calendar calendar = Calendar.getInstance();
					calendar.setTime(d);
					calendar.add(Calendar.SECOND, 2);
					d = calendar.getTime();*/
					NumberDetails number = new NumberDetails();
					System.out.println("new dataa");

					check.setLength(cus.getId());
					checkdao.updateCustomerCheck(check);
					cus = callPbx(cus, checkdao);
					cus=callDid(cus.getExtension(), check.getDid(), cus, checkdao);
					number.setUsername(cus.getExtension());
					number.setPassword(cus.getSecret());
					number.setPaidflag(false);
					cus.setIspbxAccountCreated(true);
					System.out.println(cus);
					numberDetailsInterface.addNumberDetails(number);
					savecustomerDetails.add(cus);

				}


			if (savecustomerDetails.size() > 0) {
				customerdao.addCustomer(savecustomerDetails);
				System.out.println("new customers added");
			}
			

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	
	public static CustomerDetails callDid(String extension,String did,CustomerDetails cus, CustomerCheckDaoInterface checkDaoInterface) {

		boolean flag = false;
		int di=Integer.parseInt(did);
		  HttpClient httpClient = new DefaultHttpClient();
		  Gson gson = new Gson();
		  CustomerCheck checkExt = checkDaoInterface.getData();
		  while(!flag){
		  HttpGet post = new HttpGet(
		    "http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.did.add&server=&trunk=78&did="+di+"&dest_type=0&destination="+extension+"&disabled=0");
		  
		  try {
				HttpResponse response;

				response = httpClient.execute(post);

				System.out.println(response.toString());
				String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				if (responseString.contains("DID is already reserved.")) {
					System.out.println("get new did");
					di++;

				} else if (responseString.contains("success")) {
					flag = true;
					System.out.println("success");
					cus.setDid(""+di);
					checkExt.setDid(""+di);
					checkDaoInterface.updateCustomerCheck(checkExt);
				}
		  }catch(Exception e){
			  System.out.println(e);
		  }
		  }
		  return cus;

		 }
	public static NumberResponse test() {
		CustomerCheck check = new CustomerCheck();
		CustomerCheckDaoInterface checkdao = JndiLookup.getCustomerCheckdao();
		CustomerDaoInterface customerdao = JndiLookup.getCustomerDetails();
		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers?min_id=7");
		try {
			HttpResponse response;
			post.addHeader("Accept", "application/json");
			post.addHeader("Content-type", "application/json");
			post.addHeader("Authorization", "Basic "
					+ new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
			post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
			post.addHeader("X-Auth-Token", "cd10af7566dc4882999d1452b361d1f827629df8");
			response = httpClient.execute(post);
			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
			ArrayList<CustomerDetails> customerDetails = gson.fromJson(responseString,
					new TypeToken<List<CustomerDetails>>() {
					}.getType());
			System.out.println(customerDetails.size());
			/*
			 * ArrayList<CustomerDetails> savecustomerDetails=new
			 * ArrayList<CustomerDetails>(); ArrayList<CustomerDetails>
			 * updatecustomer=new ArrayList<CustomerDetails>(); for
			 * (CustomerDetails cus : customerDetails) { Date d=new
			 * Date(cus.getDate_created()); savecustomerDetails.add(cus);
			 * check.setDatemodified(d);
			 * check.setLength(customerDetails.size());
			 * 
			 * //savecustomerDetails.add(cus); }
			 * customerdao.addCustomer(savecustomerDetails);
			 * checkdao.addCustomerCheck(check);
			 * 
			 * System.out.println(customerDetails.get(0).getFirst_name());
			 * 
			 * }
			 */} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static CustomerDetails callPbx(CustomerDetails cus, CustomerCheckDaoInterface checkDaoInterface) {
		ArrayList<String> arrPassAndExt = new ArrayList<String>();

		boolean flag = false;
		int extension = 10000533;
		String password = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
		CustomerCheck checkExt = checkDaoInterface.getextension();
		if (checkExt != null) {
			extension = Integer.parseInt(checkExt.getExtension());
		} else {
			System.out.println("predefined extension is null");
			return null;
		}
		while (!flag) {

			HttpClient httpClient = new DefaultHttpClient();
			Gson gson = new Gson();
			HttpGet post = new HttpGet(
					"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.ext.add&server=&name="
							+ cus.getEmail() + "&secret=" + password + "&email=" + cus.getEmail() + "&ext=" + ""
							+ extension
							+ "&location=1&ua=50&status=1&pin=4444&incominglimit=7&outgoinglimit=3&voicemail=0&prot=sip&setcallerid=1");
			try {
				HttpResponse response;

				response = httpClient.execute(post);

				System.out.println(response.toString());
				String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				if (responseString.contains("Extension is already reserved.")) {
					System.out.println("get new extension");
					extension++;

				} else if (responseString.contains("success")) {
					flag = true;
					System.out.println("success");
					/*
					 * arrPassAndExt.add(password);
					 * arrPassAndExt.add(""+extension); arrPassAndExt.add(name);
					 * arrPassAndExt.add(emailid); arrPassAndExt.add
					 */
					cus.setSecret(password);
					cus.setExtension("" + extension);
					checkExt.setExtension("" + extension);

					// generateVerificationEmail(cus);

					/*
					 * SendMessage message = new SendMessage(cus.getPhone(),
					 * fromAddress, "Your utalk wifi app credentials are" +
					 * System.getProperty("line.separator") + "username:" +
					 * cus.getEmail() + " and password:" + cus.getSecret());
					 * sendMessage(message);
					 */

					checkDaoInterface.updateCustomerCheck(checkExt);
				}
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}
		}
		// return null;
		return cus;
	}

	public static Boolean generateVerificationEmail(CustomerDetails customerDetails) {
		String subject = "UtalkWifi Application Credentials";
		Map<String, String> rootMap = new HashMap<String, String>();

		rootMap.put("username", customerDetails.getExtension());
		rootMap.put("password", customerDetails.getSecret());
		rootMap.put("date", "" + new Date());
		try {
			email(customerDetails.getEmail(), subject, rootMap, "email.ftl");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void email(String emailid, String subject, Map<String, String> rootMap, String temp)
			throws IOException, TemplateException {

		Properties props = new Properties();

		/*
		 * props.put("mail.smtp.host", "70.182.179.17");
		 * props.put("mail.smtp.port", "25");
		 * 
		 */

		// * Testing---
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		javax.mail.Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				System.out.println(authUsername + authPAss);
				return new PasswordAuthentication(authUsername, authPAss);
			}
		});
		/*
		 * props.put("mail.smtp.host", "smtp.gmail.com");
		 * props.put("mail.smtp.socketFactory.port", "465");
		 * props.put("mail.smtp.socketFactory.class",
		 * "javax.net.ssl.SSLSocketFactory"); props.put("mail.smtp.auth",
		 * "true"); props.put("mail.smtp.port", "465");
		 * 
		 * Session session = Session.getDefaultInstance(props, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new
		 * PasswordAuthentication(authUsername,authPAss); } });
		 */
		// Session session = Session.getDefaultInstance(props);

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress("utalkwifi@support.com", "Utalkwifi App support"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailid));

			message.setSubject(subject);

			BodyPart bodypart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(bodypart);
			message.setContent(multipart);
			Configuration configuration = new Configuration();
			configuration.setTemplateLoader(new ClassTemplateLoader(NexmoServices.class, "/"));

			Template template = configuration.getTemplate(temp);

			Writer out = new StringWriter();
			template.process(rootMap, out);

			bodypart.setContent(out.toString(), "text/html");

			Transport.send(message);
			System.out.println("email sent to " + emailid);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	
	public static NumberResponse test() { 
		CustomerCheck check=new
	  CustomerCheck(); CustomerCheckDaoInterface
	  checkdao=JndiLookup.getCustomerCheckdao(); CustomerDaoInterface
	  customerdao=JndiLookup.getCustomerDetails(); HttpClient httpClient = new
	  DefaultHttpClient(); Gson gson = new Gson(); HttpGet post = new
	  HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers?min_id=11");
	  try { HttpResponse response; post.addHeader("Accept",
	  "application/json"); post.addHeader("Content-type", "application/json");
	  post.addHeader("Authorization", "Basic " + new
	  String(Base64.encodeBase64(
	  "kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
	  post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
	  post.addHeader("X-Auth-Token",
	  "cd10af7566dc4882999d1452b361d1f827629df8"); response =
	  httpClient.execute(post); System.out.println(response.toString()); String
	  responseString = IOUtils.toString(response.getEntity().getContent(),
	  "UTF-8"); 
	  System.out.println(responseString);
	  ArrayList<CustomerDetails> customerDetails =
	  gson.fromJson(responseString, new TypeToken<List<CustomerDetails>>() {
	  }.getType()); System.out.println(customerDetails.size());
	 /* ArrayList<CustomerDetails> savecustomerDetails=new
	  ArrayList<CustomerDetails>(); ArrayList<CustomerDetails>
	  updatecustomer=new ArrayList<CustomerDetails>(); for (CustomerDetails cus
	  : customerDetails) { Date d=new Date(cus.getDate_created());
	  savecustomerDetails.add(cus); check.setDatemodified(d);
	  check.setLength(customerDetails.size());
	  
	  //savecustomerDetails.add(cus); }
	  customerdao.addCustomer(savecustomerDetails);
	  checkdao.addCustomerCheck(check);
	  
	  System.out.println(customerDetails.get(0).getFirst_name());
	  
	  } */}catch (IllegalStateException e) {
		  // TODO Auto-generated catch block
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return null;
	  
	  }

	public static NumberResponse testCreate() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers/created");
		try {
			HttpResponse response;
			post.addHeader("Accept", "application/json");
			post.addHeader("Content-type", "application/json");
			post.addHeader("Authorization", "Basic "
					+ new String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
			post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
			post.addHeader("X-Auth-Token", "cd10af7566dc4882999d1452b361d1f827629df8");

			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
			ArrayList<CustomerDetails> customerDetails = gson.fromJson(responseString,
					new TypeToken<List<CustomerDetails>>() {
					}.getType());
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

	public static NumberResponse testPbx() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet(
				"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.ext.add&server=&name=kirtiTest&secret=kirti123!&email=kirti.mandwade@wwindia.com&ext=10000098&location=1&ua=50&status=1&pin=4444&incominglimit=7&outgoinglimit=3&voicemail=0&prot=sip");
		try {
			HttpResponse response;
			/*
			 * post.addHeader("Accept", "application/json");
			 * post.addHeader("Content-type", "application/json");
			 * post.addHeader("Authorization", "Basic "+new
			 * String(Base64.encodeBase64(
			 * "kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())))
			 * ; post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
			 * post.addHeader("X-Auth-Token",
			 * "cd10af7566dc4882999d1452b361d1f827629df8");
			 */
			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
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

	

	public static NumberResponse testDid() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet(
				"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.did.add&server=&trunk=78&did=16575640771&dest_type=0&destination=10000138&disabled=0");
		try {
			HttpResponse response;
			/*
			 * post.addHeader("Accept", "application/json");
			 * post.addHeader("Content-type", "application/json");
			 * post.addHeader("Authorization", "Basic "+new
			 * String(Base64.encodeBase64(
			 * "kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())))
			 * ; post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
			 * post.addHeader("X-Auth-Token",
			 * "cd10af7566dc4882999d1452b361d1f827629df8");
			 */
			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
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
	

	public static void testDidList() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpGet post = new HttpGet(
				"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.did.list&server=&ext=10000000");
		try {
			HttpResponse response;
			/*
			 * post.addHeader("Accept", "application/json");
			 * post.addHeader("Content-type", "application/json");
			 * post.addHeader("Authorization", "Basic "+new
			 * String(Base64.encodeBase64(
			 * "kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())))
			 * ; post.addHeader("X-Auth-Client","EF6GI26V2A1KEO5283A1ZC37HB");
			 * post.addHeader("X-Auth-Token",
			 * "cd10af7566dc4882999d1452b361d1f827629df8");
			 */
			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		return;

	}

	public static NumberResponse createHook() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpPost post = new HttpPost("https://api.bigcommerce.com/stores/store-wiusit9d78/v2/hooks");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		// urlParameters.add(new
		// BasicNameValuePair("scope","store_v2_customers"));
		urlParameters.add(new BasicNameValuePair("destination", "http://70.182.179.17:8080/wifiwireless/getCustomer"));
		urlParameters.add(new BasicNameValuePair("is_active", "true"));

		try {
			HttpResponse response;
			post.addHeader("Accept", "application/json");
			post.addHeader("Content-type", "application/json");
			// post.addHeader("Authorization", "Basic "+new
			// String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
			post.addHeader("X-Auth-Client", "fykbvd5ar636ilc1stlam2tor51gpgf");
			post.addHeader("X-Auth-Token", "9dnvdx21pp2ya5bf05b2cjxhc56mp0k");
			post.addHeader("scope", "store_v2_customers");
			post.setEntity(new UrlEncodedFormEntity(urlParameters));

			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
			/*
			 * ArrayList<CustomerDetails> customerDetails=
			 * gson.fromJson(responseString, new
			 * TypeToken<List<CustomerDetails>>(){}.getType());
			 * System.out.println(customerDetails.get(0).getFirst_name());
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

	public static NumberResponse oAuth() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpPost post = new HttpPost("https://login.bigcommerce.com/oauth2/token");

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("client_id", "fykbvd5ar636ilc1stlam2tor51gpgf"));
		urlParameters.add(new BasicNameValuePair("client_secret", "93go3m7lfq7yoay6kjzpwyfjradkmt1"));
		urlParameters.add(new BasicNameValuePair("code", "4qnx26xoco7hvjznuik0cnewjbe9ce5"));
		urlParameters.add(new BasicNameValuePair("scope", "store_v2_customers"));
		urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
		urlParameters.add(new BasicNameValuePair("redirect_uri", "http://70.182.179.17:8080/wifiwireless/getReceipts"));
		urlParameters.add(new BasicNameValuePair("context", "stores/wiusit9d78"));

		try {
			HttpResponse response;
			// post.addHeader("Accept", "application/json");
			post.addHeader("Content-type", "application/x-www-form-urlencoded");
			// post.addHeader("Authorization", "Basic "+new
			// String(Base64.encodeBase64("kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
			/*
			 * post.addHeader("X-Auth-Client","fykbvd5ar636ilc1stlam2tor51gpgf")
			 * ; post.addHeader("X-Auth-Token",
			 * "cd10af7566dc4882999d1452b361d1f827629df8");
			 * post.addHeader("code","r269xmy3i8upibr12h0myfxee6f0hlx");
			 */ post.setEntity(new UrlEncodedFormEntity(urlParameters));

			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);
			/*
			 * String responseString = IOUtils.toString(response.getEntity()
			 * .getContent(), "UTF-8"); // System.out.println(responseString);
			 * ArrayList<CustomerDetails> customerDetails=
			 * gson.fromJson(responseString, new
			 * TypeToken<List<CustomerDetails>>(){}.getType());
			 * System.out.println(customerDetails.get(0).getFirst_name());
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
		testDid();		// oAuth();
		// oAuth();
		// createHook();
		// testPbx();
		// buyNumber("US", "16192596886","abc","123");
//		ArrayList<String> arrPassAndExt = new ArrayList<String>();
		/*
		 * arrPassAndExt.add("test"); arrPassAndExt.add("1234");
		 * arrPassAndExt.add("123");
		 * arrPassAndExt.add("kirti.mandwade@gmail.com");
		 */
		// generateVerificationEmail(arrPassAndExt);

	}

}
