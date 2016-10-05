package com.wifiwireless.webservice;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wifiwireless.constant.JndiLookup;
import com.wifiwireless.interfaces.NumberDetailsInterface;
import com.wifiwireless.model.NumberDetails;
import com.wireless.bean.AcquireNumber;
import com.wireless.bean.BuyNumber;
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
        NumberDetails number=new NumberDetails(acquireNumber.getUsername(), acquireNumber.getPassword(), acquireNumber.getCountry(), acquireNumber.getPattern(), false, acquireNumber.getMobileNumber());
        
        NumberDetailsInterface numberInterface=JndiLookup.getNumberDetailsDao();
		System.out.println("country is " + acquireNumber.getCountry());
//Save to database
	//number.setCountry(acquireNumber.getCountry());
		NumberResponse numberResponse = NexmoServices.acquireNumber(acquireNumber.getCountry(), acquireNumber.getPattern());
		System.out.println("number response	" + numberResponse.getMsisdn());
		//save to db
	      number.setCost(numberResponse.getCost());
	      number.setMsisdn(numberResponse.getMsisdn());
		numberInterface.addNumberDetails(number);
		return numberResponse;
	
	}
	
	
	@POST
	@Path("buy/number")
	@Produces(MediaType.APPLICATION_JSON)
	public String buyNumber(BuyNumber buyNumber) {
            
		System.out.println("country is " + buyNumber.getCountry());
	
return	NexmoServices.buyNumber(buyNumber.getCountry(), buyNumber.getMsisdn(),buyNumber.getUsername(),buyNumber.getPassword());

	}
	
	
	
	@POST
	 @Path("sendMessage")
	 @Produces(MediaType.APPLICATION_JSON)
	 public SendMessageResponse sendMessage(SendMessage sendMessage) {

	//  System.out.println("country is " + buyNumber.getCountry());
	 
	return NexmoServices.sendMessage(sendMessage);

	 }
	
	
}
