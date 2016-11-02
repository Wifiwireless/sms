package com.wireless.utility;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
import com.wifiwireless.webservice.Webservices;
import com.wireless.bean.AcquireNumber;
import com.wireless.bean.AcquireResponse;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.bean.SendMessage;
import com.wireless.bean.SendMessageResponse;
import com.wireless.email.Mail;

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
				+ "&country=" + country + "&pattern=" + pattern + "&search_pattern=0&features=sms,voice&size=3");
		try {
			HttpResponse response;

			response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {

				String responseString;

				responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				AcquireResponse numberResponse = gson.fromJson(responseString, AcquireResponse.class);
				if (numberResponse.getNumbers() != null && numberResponse.getNumbers().size() > 0) {
					if (numberResponse.getNumbers().size() > 1) {
						return numberResponse.getNumbers().get(1);

					} else {
						return numberResponse.getNumbers().get(0);

					}
				} else {
					NumberResponse response2 = new NumberResponse();

					response2.setError("Invalid pattern. Nexmo does not allow that pattern for selected country");

					return response2;
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

	public static BuyNumberResponse buyNumber(String country, String msisdn, String username, String password,
			String phoneNumber) {

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
			System.out.println("response  code" + response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);

			if (response.getStatusLine().getStatusCode() == 200) {

				NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
				numberInterface.checkandUpdate(msisdn, username, password);

				BuyNumberResponse byNumResp = new BuyNumberResponse();
				updateNumber(country, msisdn, phoneNumber);
				callDid(username, msisdn);

				byNumResp.setSuccess("success");
				byNumResp.setError("false");
				return byNumResp;

				// update db

			} else if (response.getStatusLine().getStatusCode() == 401) {

				BuyNumberResponse byNumResp = new BuyNumberResponse();
				byNumResp.setError("you supplied incorrect security and authentication information");

				return byNumResp;
			} else {

				BuyNumberResponse byNumResp = new BuyNumberResponse();
				byNumResp.setError("Something went wrong.. Cant order the number Please try again later.");

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

	public static BuyNumberResponse updateNumber(String country, String msisdn, String phoneNo) {

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("country", country));
		urlParameters.add(new BasicNameValuePair("msisdn", msisdn));
		urlParameters.add(new BasicNameValuePair("api_key", apikey));
		urlParameters.add(new BasicNameValuePair("api_secret", api_secret));
		urlParameters.add(new BasicNameValuePair("voiceCallbackType", "tel"));
		urlParameters.add(new BasicNameValuePair("voiceCallbackValue", phoneNo));

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		HttpPost post = new HttpPost("https://rest.nexmo.com/number/update");
		try {
			HttpResponse response;

			post.setEntity(new UrlEncodedFormEntity(urlParameters));
			post.setHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept", "application/json");

			response = httpClient.execute(post);

			System.out.println(response.getStatusLine().getStatusCode());
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

		System.out.println("sending message to " + message.getTo());
		/*
		 * if(!message.getTo().startsWith("1")){
		 * message.setTo("+1"+message.getTo()); }
		 */

		message.setTo(CommonUtility.checkMsisdn(message.getTo().trim()));

		System.out.println("sending message to " + message.getTo());
		System.out.println("sending message from " + message.getFrom());

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
		int cid = check.getLength() + 1;
		// int did=Integer.parseInt(check.getDid());
		System.out.println("Last Id is" + cid);
		Gson gson = new Gson();

		HttpGet post = new HttpGet("https://store-wiusit9d78.mybigcommerce.com/api/v2/customers?min_id=" + cid);
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
			if (response.getStatusLine().getStatusCode() == 200) {
				String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);
				ArrayList<CustomerDetails> customerDetails = gson.fromJson(responseString,
						new TypeToken<List<CustomerDetails>>() {
						}.getType());
				// ArrayList<NumberDetails> arryNumber = new
				// ArrayList<NumberDetails>();
				System.out.println("customer list size " + customerDetails.size());
				ArrayList<CustomerDetails> savecustomerDetails = new ArrayList<CustomerDetails>();
				if (customerDetails != null && customerDetails.size() > 0) {
					for (CustomerDetails cus : customerDetails) {

						
						
						/*
						 * Calendar calendar = Calendar.getInstance();
						 * calendar.setTime(d); calendar.add(Calendar.SECOND,
						 * 2); d = calendar.getTime();
						 */
						NumberDetails number = new NumberDetails();
						System.out.println("new dataa");

						check.setLength(cus.getId());
						checkdao.updateCustomerCheck(check);
						cus = callPbx(cus, checkdao);

						number.setUsername(cus.getExtension());
						number.setPassword(cus.getSecret());
						cus.setIspbxAccountCreated(true);
						System.out.println(cus);
						numberDetailsInterface.addNumberDetails(number);
						savecustomerDetails.add(cus);

						// get Number
						AcquireNumber acquireNumber = new AcquireNumber();
						acquireNumber.setUsername(cus.getExtension());
						acquireNumber.setPassword(cus.getSecret());
						number.setPaidflag(false);
						acquireNumber.setPattern("1619");
						acquireNumber.setCountry("US");
						acquireNumber.setMobileNumber(cus.getPhone());

						NumberResponse numberResponse = Webservices.getNumber(acquireNumber);

						// buy Number

						if (numberResponse != null) {

							BuyNumber buyNumber = new BuyNumber();

							buyNumber.setCountry(numberResponse.getCountry());
							buyNumber.setMsisdn(numberResponse.getMsisdn());
							buyNumber.setUsername(cus.getExtension());
							buyNumber.setPassword(cus.getSecret());

							BuyNumberResponse buyNumberResponse = Webservices.buyNumber(buyNumber);

							if (buyNumberResponse != null && "success".equals(buyNumberResponse.getSuccess())) {
							//	generateVerificationEmail(cus, numberResponse.getMsisdn());
							} else {
								System.out.println("BuyNumberResponse " + buyNumberResponse);
							}

						}
					}

				}
				if (savecustomerDetails.size() > 0) {
					customerdao.addCustomer(savecustomerDetails);
					System.out.println("new customers added");
				}
			} else {
				System.out.println("no new customer");
			}

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	public static void callDid(String extension, String did) {

		boolean flag = false;

		Long di = Long.parseLong(did);
		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();
		// CustomerCheck checkExt = checkDaoInterface.getData();

		HttpGet post = new HttpGet(
				"http://70.182.179.17/?app=pbxware&apikey=Z61g0epds7S1ABzzRca4KEYUew9xlBi9&action=pbxware.did.add&server=&trunk=78&did="
						+ di + "&dest_type=0&destination=" + extension + "&disabled=0");

		try {
			HttpResponse response;

			response = httpClient.execute(post);

			System.out.println(response.toString());
			String responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
			System.out.println(responseString);

		} catch (Exception e) {
			System.out.println(e);
		}

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
			System.out.println(response.getStatusLine().getStatusCode());
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
		String password = RandomStringUtils.randomAlphanumeric(8);
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

	

	/*
	 * 
	 * public static NumberResponse test() { CustomerCheck check=new
	 * CustomerCheck(); CustomerCheckDaoInterface
	 * checkdao=JndiLookup.getCustomerCheckdao(); CustomerDaoInterface
	 * customerdao=JndiLookup.getCustomerDetails(); HttpClient httpClient = new
	 * DefaultHttpClient(); Gson gson = new Gson(); HttpGet post = new HttpGet(
	 * "https://store-wiusit9d78.mybigcommerce.com/api/v2/customers?min_id=11");
	 * try { HttpResponse response; post.addHeader("Accept",
	 * "application/json"); post.addHeader("Content-type", "application/json");
	 * post.addHeader("Authorization", "Basic " + new
	 * String(Base64.encodeBase64(
	 * "kpmurals:cd10af7566dc4882999d1452b361d1f827629df8".getBytes())));
	 * post.addHeader("X-Auth-Client", "EF6GI26V2A1KEO5283A1ZC37HB");
	 * post.addHeader("X-Auth-Token",
	 * "cd10af7566dc4882999d1452b361d1f827629df8"); response =
	 * httpClient.execute(post); System.out.println(response.toString()); String
	 * responseString = IOUtils.toString(response.getEntity().getContent(),
	 * "UTF-8"); System.out.println(responseString); ArrayList<CustomerDetails>
	 * customerDetails = gson.fromJson(responseString, new
	 * TypeToken<List<CustomerDetails>>() { }.getType());
	 * System.out.println(customerDetails.size()); ArrayList<CustomerDetails>
	 * savecustomerDetails=new ArrayList<CustomerDetails>();
	 * ArrayList<CustomerDetails> updatecustomer=new
	 * ArrayList<CustomerDetails>(); for (CustomerDetails cus : customerDetails)
	 * { Date d=new Date(cus.getDate_created()); savecustomerDetails.add(cus);
	 * check.setDatemodified(d); check.setLength(customerDetails.size());
	 * 
	 * //savecustomerDetails.add(cus); }
	 * customerdao.addCustomer(savecustomerDetails);
	 * checkdao.addCustomerCheck(check);
	 * 
	 * System.out.println(customerDetails.get(0).getFirst_name());
	 * 
	 * } }catch (IllegalStateException e) { // TODO Auto-generated catch block }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } return null;
	 * 
	 * }
	 */

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


	public static SendMessageResponse testMessage() {

		HttpClient httpClient = new DefaultHttpClient();
		Gson gson = new Gson();

		MessagesInterface messageinterface = JndiLookup.getMessageDao();
		HttpGet get = new HttpGet("https://rest.nexmo.com/sms/json?api_key=" + apikey + "&api_secret=" + api_secret
				+ "&to=" + "+919860070594" + "&from=" + "16192596886" + "&text=" + URLEncoder.encode("hello test 123"));
		try {
			HttpResponse response;

			response = httpClient.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {

				String responseString;

				responseString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
				System.out.println(responseString);

				System.out.println("message sent");

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


	
}
