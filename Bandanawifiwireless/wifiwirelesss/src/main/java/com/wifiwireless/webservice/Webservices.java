package com.wifiwireless.webservice;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

	@POST
	@Path("get/number")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseObject NumberResponse getNumber(AcquireNumber acquireNumber) {
		System.out.println("in get number");
		NumberResponse numberResponse = null;
		if (acquireNumber.getUsername() != null && acquireNumber.getPassword() != null
				&& acquireNumber.getCountry() != null && acquireNumber.getPattern() != null
				&& acquireNumber.getMobileNumber() != null) {
			NumberDetails number = new NumberDetails(acquireNumber.getUsername(), acquireNumber.getPassword(),
					acquireNumber.getCountry(), acquireNumber.getPattern(), false, acquireNumber.getMobileNumber());

			NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
			System.out.println("country is " + acquireNumber.getCountry());
			// Save to database
			// number.setCountry(acquireNumber.getCountry());
			numberResponse = NexmoServices.acquireNumber(acquireNumber.getCountry(), acquireNumber.getPattern());
			System.out.println("number response	" + numberResponse.getMsisdn());
			// save to db
			number.setCost(numberResponse.getCost());
			number.setMsisdn(numberResponse.getMsisdn());
			numberInterface.addNumberDetails(number);
			return numberResponse;
		} else {
			return numberResponse = new NumberResponse("Please provide all required parameters");

		}

	}

	@POST
	@Path("buy/number")
	@Produces(MediaType.APPLICATION_JSON)
	public @ResponseObject BuyNumberResponse buyNumber(BuyNumber buyNumber) {

		System.out.println("country is " + buyNumber.getCountry());

		return NexmoServices.buyNumber(buyNumber.getCountry(), buyNumber.getMsisdn(), buyNumber.getUsername(),
				buyNumber.getPassword());

	}

	@POST
	@Path("send_message")
	@Produces(MediaType.APPLICATION_JSON)
	public SendMessageResponse sendMessage(SendMessage sendMessage) {

		NumberDetailsInterface numberInterface = JndiLookup.getNumberDetailsDao();
		String msidn = numberInterface.checkNumber(sendMessage.getFrom(), sendMessage.getPassword());

		if (sendMessage.getFrom() != null && sendMessage.getPassword() != null && sendMessage.getTo() != null
				&& sendMessage.getBody() != null) {
			// System.out.println("country is " + buyNumber.getCountry());
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
		NexmoServices.test();

	}

}
