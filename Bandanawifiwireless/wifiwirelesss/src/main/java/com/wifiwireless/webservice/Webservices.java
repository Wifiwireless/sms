package com.wifiwireless.webservice;

import java.util.ArrayList;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.annotations.ResponseObject;

import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.MessageRecieptsInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.MessageReciepts;
import com.wifiwireless.model.NumberDetails;
import com.wireless.bean.AcquireNumber;
import com.wireless.bean.BuyNumber;
import com.wireless.bean.BuyNumberResponse;
import com.wireless.bean.NumberResponse;
import com.wireless.bean.SendMessage;
import com.wireless.bean.SendMessageResponse;
import com.wireless.utility.NexmoServices;

@Path("/")
public class Webservices {

	/*@POST
	@Path("sendEmailTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendEmailTest() {
		System.out.println("sending email------------------------");
		ArrayList<String> arrPassAndUsernme = new ArrayList<String>();
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("kirti2091@gmail.com");
		NexmoServices.generateVerificationEmail(arrPassAndUsernme);

	}*/

	@POST
	@Path("get/number")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseObject NumberResponse getNumber(AcquireNumber acquireNumber) {
		System.out.println("in get number");
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
				System.out.println(acquireNumber.getUsername());

				System.out.println("country is " + acquireNumber.getCountry());
				// Save to database
				// number.setCountry(acquireNumber.getCountry());
				numberResponse = NexmoServices.acquireNumber(acquireNumber.getCountry(), acquireNumber.getPattern());
				System.out.println("number response	" + numberResponse.getMsisdn());
				// save to db
				number.setUsername(numberss.getUsername());
				number.setPassword(numberss.getPassword());
				number.setId(numberss.getId());
				number.setCost(numberResponse.getCost());
				number.setMsisdn(numberResponse.getMsisdn());
				number.setPaidflag(false);
				numberInterface.mergeNumber(number);
				numberResponse.setError("false");
				return numberResponse;
			} else {
				return numberResponse = new NumberResponse("Please provide correct username and Password");
			}

		} else {
			return numberResponse = new NumberResponse("Please provide all required parameters");

		}

	}

	@POST
	@Path("buy/number")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseObject BuyNumberResponse buyNumber(BuyNumber buyNumber) {

		System.out.println("country is " + buyNumber.getCountry());
		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();

		NumberDetails numberss = numberInterface.getNumberDetails(buyNumber.getUsername(),
				buyNumber.getPassword());
		
		return NexmoServices.buyNumber(buyNumber.getCountry(), buyNumber.getMsisdn(), buyNumber.getUsername(),
				buyNumber.getPassword(),numberss.getPhnno());
		

	}

	@POST
	@Path("send_message")
	@Produces(MediaType.APPLICATION_JSON)
	public SendMessageResponse sendMessage(SendMessage sendMessage) {

		System.out.println("Sending Message");
		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
		String msidn = numberInterface.checkNumber(sendMessage.getFrom(), sendMessage.getPassword());

		if (sendMessage.getFrom() != null && sendMessage.getPassword() != null && sendMessage.getTo() != null
				&& sendMessage.getBody() != null) {
			 System.out.println("country is " );
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
		System.out.println("Receipt received------------------------");
		System.out.println("msisdn" + msisdn + "netwr" + networkcode + errorCode + messageId + messagetimestamp + msisdn
				+ networkcode + price + scts + status + to);

		System.out.println("status " + status);
		System.out.println("Receipt received------------------------");
		MessageReciepts msgReciepts = new MessageReciepts(msisdn, to, networkcode, messageId, status, scts, errorCode,
				new Date());
		MessageRecieptsInterface messageREcieptsInteface = JndiLookup.getMessageRecieptsDao();
		messageREcieptsInteface.addMesages(msgReciepts);

		// System.out.println("country is " + buyNumber.getCountry());
		System.out.println("Receipt received------------------------");
		System.out.println("msisdn" + msisdn);

		System.out.println("status " + status);
		System.out.println("Receipt received------------------------");

	}

	@POST
	@Path("getnew")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getnew() {
		System.out.println("Receipt received------------------------");
//		NexmoServices.test();

	}
	
	
//	msisdn=441632960960&to=441632960961&messageId=000000FFFB0356D1&text=This+is+an+inbound+message&type=text&message-timestamp=2012-08-19+20%3A38%3A23
	@GET
	@Path("getReply")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getnew(@QueryParam("msisdn") String msisdn, @QueryParam("to") String to, @QueryParam("messageId") String messageId, @QueryParam("text") String text, @QueryParam("type") String type, @QueryParam("timestamp") String timestamp) {
		System.out.println("Reply received------------------------" + text);
		
		
		
		NumberDetailsInterface detailsInterface = JndiLookup.getNumberDetailsDao();
		NumberDetails numberDetails=detailsInterface.getNumberDetailsByMsisdn(to);
		if(numberDetails.getPhnno()!=null){
		System.out.println("Reply Sending to------------------------"+numberDetails.getPhnno());
		SendMessage message = new SendMessage();
		message.setFrom(msisdn);
		message.setTo(numberDetails.getPhnno());
		message.setBody(text);
		System.out.println(message.toString());
		NexmoServices.sendMessage(message);
		}
	    return Response.status(200).build();		
//		NexmoServices.test();

	}
	
	/*@POST
	@Path("sendEmailTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendEmailTest() {
		System.out.println("sending email------------------------");
		ArrayList<String> arrPassAndUsernme =  new ArrayList<String>();
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("123");
		arrPassAndUsernme.add("kirti2091@gmail.com");
		NexmoServices.generateVerificationEmail(arrPassAndUsernme);

	}*/

}
