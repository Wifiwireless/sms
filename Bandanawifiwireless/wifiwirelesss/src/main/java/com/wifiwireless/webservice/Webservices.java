package com.wifiwireless.webservice;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
	public NumberResponse getNumber(AcquireNumber acquireNumber) {
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
	public BuyNumberResponse buyNumber(BuyNumber buyNumber) {

		System.out.println("country is " + buyNumber.getCountry());

		return NexmoServices.buyNumber(buyNumber.getCountry(), buyNumber.getMsisdn(), buyNumber.getUsername(),
				buyNumber.getPassword());

	}

	@POST
	@Path("sendMessage")
	@Produces(MediaType.APPLICATION_JSON)
	public SendMessageResponse sendMessage(SendMessage sendMessage) {
		if(sendMessage.getUsername()!=null&&sendMessage.getPassword()!=null&&sendMessage.getTo()!=null&&sendMessage.getFrom()!=null&&sendMessage.getText()!=null) {
		// System.out.println("country is " + buyNumber.getCountry());

		return NexmoServices.sendMessage(sendMessage);
		}
		else{
		    SendMessageResponse messageResponse = new SendMessageResponse("Please provide all required parameters");
		    return messageResponse;
		  }

	}
	@GET
	 @Path("getReceipts")
	 @Produces(MediaType.APPLICATION_JSON)
	 public void getReceipts(@PathParam("msisdn") String msisdn, @PathParam("to") String to,@PathParam("network-code") String networkcode,@PathParam("messageId") String messageId,@PathParam("price") String price,@PathParam("status") String status,@PathParam("scts") String scts,@PathParam("err-code") String errorCode,@PathParam("message-timestamp") String messagetimestamp) {

		MessageReciepts msgReciepts=new MessageReciepts(msisdn, to, networkcode, messageId, status, scts, errorCode, new Date(messagetimestamp));
		MessageRecieptsInterface messageREcieptsInteface=JndiLookup.getMessageRecieptsDao();
		messageREcieptsInteface.addMesages(msgReciepts);
		
	//  System.out.println("country is " + buyNumber.getCountry());
	  System.out.println("Receipt received------------------------");
	  System.out.println("msisdn" + msisdn);

	  System.out.println("status "+status);
	  System.out.println("Receipt received------------------------");
	  
	  

	  
	 }

}
