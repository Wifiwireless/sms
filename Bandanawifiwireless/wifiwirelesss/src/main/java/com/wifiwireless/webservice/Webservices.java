
package com.wifiwireless.webservice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.jboss.resteasy.annotations.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.mail.imap.protocol.Status;
import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.MessageRecieptsInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.CustomerDetails;
import com.wifiwireless.model.MessageReciepts;
import com.wifiwireless.model.Messages;
import com.wifiwireless.model.NumberDetails;
import com.wireless.bean.AcquireNumber;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.FetchMessage;
import com.wireless.bean.FetchMessageResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.bean.PushToken;
import com.wireless.bean.SendMessage;
import com.wireless.bean.SendMessageResponse;
import com.wireless.bean.UnreadMessage;
import com.wireless.bean.UnreadSms;
import com.wireless.email.Mail;
import com.wireless.utility.NexmoServices;
import com.wireless.utility.PushNotificationService;
import com.wireless.utility.Schedulars;
import com.wireless.utility.WifiWirlessConstants;

import freemarker.template.TemplateException;

@Path("/")
public class Webservices {

	private static final Logger log = LoggerFactory.getLogger(Webservices.class);

	public static NumberResponse getNumber(AcquireNumber acquireNumber) {
		log.info("in get number"+acquireNumber);
		log.info("Acquire Number"+acquireNumber);
		NumberResponse numberResponse = null;
		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
		if (acquireNumber.getUsername() != null && acquireNumber.getPassword() != null
				&& acquireNumber.getCountry() != null && acquireNumber.getPattern() != null
				&& acquireNumber.getMobileNumber() != null) {

			NumberDetails numberss = numberInterface.getNumberDetails(acquireNumber.getUsername(),
					acquireNumber.getPassword());
			if (numberss.getUsername() != null) {

				NumberDetails number = new NumberDetails(acquireNumber.getCountry(), acquireNumber.getPattern(), false,
						acquireNumber.getMobileNumber());
				
				log.info("Username :"+acquireNumber.getUsername());

				log.info("Hardcoded country is " + acquireNumber.getCountry());
				// Save to database
				// number.setCountry(acquireNumber.getCountry());
				numberResponse = NexmoServices.acquireNumber(acquireNumber.getCountry(), acquireNumber.getPattern());

				log.info("number response	" + numberResponse.getMsisdn());
				if (numberResponse.getMsisdn() != null) {// save to db
					number.setUsername(numberss.getUsername());
					number.setPassword(numberss.getPassword());
					number.setId(numberss.getId());
					number.setCost(numberResponse.getCost());
					number.setMsisdn(numberResponse.getMsisdn());
					number.setPaidflag(false);
					numberInterface.mergeNumber(number);
					numberResponse.setError("");
					return numberResponse;
				} else {
					return numberResponse = new NumberResponse(
							"Invalid pattern. That pattern does not exist for selected country. Please try other pattern");
				}
			} else {
				return numberResponse = new NumberResponse("Please provide correct username and Password");
			}

		} else {
			return numberResponse = new NumberResponse("Please provide all required parameters");

		}

	}


	public static BuyNumberResponse buyNumber(BuyNumber buyNumber,String extId) {

		log.info("country is " + buyNumber.getCountry());
		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();

		NumberDetails numberss = numberInterface.getNumberDetails(buyNumber.getUsername(), buyNumber.getPassword());

		return NexmoServices.buyNumber(buyNumber.getCountry(), buyNumber.getMsisdn(), buyNumber.getUsername(),
				buyNumber.getPassword(), numberss.getPhnno(),numberss.getUsername(),extId);

	}

	@POST
	@Path("send_message")
	@Produces(MediaType.APPLICATION_JSON)
	public SendMessageResponse sendMessage(SendMessage sendMessage) {

		log.info("Sending Message");
		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
		String msidn = numberInterface.checkNumber(sendMessage.getFrom(), sendMessage.getPassword());

		if (sendMessage.getFrom() != null && sendMessage.getPassword() != null && sendMessage.getTo() != null
				&& sendMessage.getBody() != null) {
			log.info("country is ");
			if (msidn != "") {
				sendMessage.setFrom(msidn);
				return NexmoServices.sendMessage(sendMessage);
			} else {
				SendMessageResponse messageResponse = new SendMessageResponse(
						"Please Buy A Number Before Sending A Message");
				return messageResponse;
			}

		} else {
			SendMessageResponse messageResponse = new SendMessageResponse("Please provide all required parameters");
			return messageResponse;
		}
	}

	@GET
	@Path("getReceipts")
	@Produces(MediaType.APPLICATION_JSON)
	public void getReceipts(@QueryParam("msisdn") String msisdn, @QueryParam("to") String to,
			@QueryParam("network-code") String networkcode, @QueryParam("messageId") String messageId,
			@QueryParam("price") String price, @QueryParam("status") String status, @QueryParam("scts") String scts,
			@QueryParam("err-code") String errorCode, @QueryParam("message-timestamp") String messagetimestamp) {
		log.info("Receipt received------------------------");
		log.info("msisdn" + msisdn + "netwr" + networkcode + errorCode + messageId + messagetimestamp + msisdn
				+ networkcode + price + scts + status + to);

		log.info("status " + status);
		log.info("Receipt received------------------------");
		MessageReciepts msgReciepts = new MessageReciepts(msisdn, to, networkcode, messageId, status, scts, errorCode,
				new Date());
		MessageRecieptsInterface messageREcieptsInteface = JndiLookup.getMessageRecieptsDao();
		messageREcieptsInteface.addMesages(msgReciepts);

		// log.info("country is " + buyNumber.getCountry());
		log.info("Receipt received------------------------");
		log.info("msisdn" + msisdn);

		log.info("status " + status);
		log.info("Receipt received------------------------");

	}

	@POST
	@Path("getnew")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getnew() {
		log.info("Receipt received------------------------");
		// NexmoServices.test();

	}

	// msisdn=441632960960&to=441632960961&messageId=000000FFFB0356D1&text=This+is+an+inbound+message&type=text&message-timestamp=2012-08-19+20%3A38%3A23
	@GET
	@Path("getReply")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getnew(@QueryParam("msisdn") String msisdn, @QueryParam("to") String to,
			@QueryParam("messageId") String messageId, @QueryParam("text") String text, @QueryParam("type") String type,
			@QueryParam("timestamp") String timestamp) {
		log.info("Reply received------------------------" + text);

		log.info("Reply came from------------------------" + msisdn);

		NumberDetailsInterface detailsInterface = JndiLookup.getNumberDetailsDao();
		NumberDetails numberDetails = detailsInterface.getNumberDetailsByMsisdn(to);
		Messages messages = new Messages(msisdn, to, text);
		messages.setMessagetime(new Date());
		messages.setMessage_id(messageId);

		messages.setUsername(numberDetails.getUsername());
		messages.setPassword(numberDetails.getPassword());
		messages.setReadOut(false);
		JndiLookup.getMessageDao().addMesages(messages);

		PushNotificationService.pushNotification(numberDetails.getUsername());

		/*
		 * NumberDetailsInterface detailsInterface =
		 * JndiLookup.getNumberDetailsDao(); NumberDetails
		 * numberDetails=detailsInterface.getNumberDetailsByMsisdn(to); text =
		 * "You have received a reply from "+msisdn +"\n "+text;
		 * if(numberDetails.getPhnno()!=null){ log.info(
		 * "Reply Sending to------------------------"
		 * +numberDetails.getPhnno()); SendMessage message = new SendMessage();
		 * message.setFrom(WifiWirlessConstants.fromAddress);
		 * message.setTo(numberDetails.getPhnno()); message.setBody(text);
		 * log.info(message.toString());
		 * NexmoServices.sendMessage(message); }
		 */
		return Response.status(200).build();
		// NexmoServices.test();

	}

	@POST
	@Path("sendEmailTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendEmailTest() {
		log.info("sending email------------------------");
	
		CustomerDaoInterface customerDao = JndiLookup.getCustomerDetails();
		List<CustomerDetails> customerDetails = customerDao.getAllCustomers();
		String subject = "UtalkWifi Application Credentials";
		if(customerDetails != null){
			for (CustomerDetails customerDetail : customerDetails) {
				Map<String, String> rootMap = new HashMap<String, String>();

				rootMap.put("username", customerDetail.getExtension());
				rootMap.put("password", customerDetail.getSecret());
				rootMap.put("date", "" + new Date());
				try {
					Mail.email(customerDetail.getEmail(), subject, rootMap, "email.ftl");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		

		
	}

	@POST
	@Path("fetch_message")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FetchMessageResponse fetchmessage(FetchMessage fetchMessage) {
		FetchMessageResponse fetchMessageResponse = new FetchMessageResponse();
		ArrayList<UnreadMessage> arrayList = new ArrayList<UnreadMessage>();
		UnreadSms sms = new UnreadSms();

		NumberDetails details = JndiLookup.getNumberDetailsDao().getNumberDetails(fetchMessage.getFrom(),
				fetchMessage.getPassword());
		ArrayList<Messages> arrayReply = JndiLookup.getMessageDao().getMessageByMsisdn(details.getMsisdn());

		log.info("---------------fetching sms-------------");
		log.info("---------------fetching sms-------------");

		log.info("---------------fetching sms-------------");

		if (arrayReply.size() > 0) {
			for (Messages reply : arrayReply) {
				UnreadMessage message = new UnreadMessage();
				message.setSender(reply.getSource());
				log.info("checking reply time");
				log.info("checking reply time" + reply.getMessagetime());

				if (reply.getMessagetime() != null) {
					message.setSending_date(
							"" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(reply.getMessagetime()));
				}

				message.setSms_id(reply.getMessage_id());
				message.setSms_text(reply.getText());
				arrayList.add(message);
				reply.setReadOut(true);
				JndiLookup.getMessageDao().updateMesages(reply);
			}
		}

		sms.setItem(arrayList);
		fetchMessageResponse.setDate("" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
		fetchMessageResponse.setUnread_smss(sms);
		log.info(fetchMessageResponse.toString());
		return fetchMessageResponse;

	}

	@POST
	@Path("push_token")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response pushTokenReorter(PushToken pushtoken) {

		log.info("Push Token :" + pushtoken.getUsername());
		log.info("Push Token :" + pushtoken.getAppId());

		CustomerDaoInterface customerDao = JndiLookup.getCustomerDetails();
		CustomerDetails customerDetails = customerDao.getCustomerDetailsByUsername(pushtoken.getUsername());

		if (customerDetails != null) {
			customerDetails.setAppid(pushtoken.getAppId());
			customerDetails.setToken(pushtoken.getToken());
			customerDetails.setSelector(pushtoken.getSelector());

			customerDao.updateCustomer(customerDetails);
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		return Response.status(Response.Status.OK).build();
	}

}
