package com.wireless.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class CommonUtility {

	private static final Logger log = LoggerFactory.getLogger(CommonUtility.class);

	public static String checkMsisdn(String msisdn) { 
		  PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		  
		  try {
		   PhoneNumber phoneNumberProto = phoneUtil.parse(msisdn, "US");
		   
		   if (!phoneUtil.isValidNumber(phoneNumberProto)){
		    
		    System.out.println("invalid for us");
		 return msisdn;
		   }else{
		    
		    if(!msisdn.startsWith("+1")&&msisdn.length()<=10){
		     msisdn = "+1"+msisdn;
		     System.out.println("valid for us");

		    }else{
		     System.out.println("invalid for us "+msisdn);
		    }
		     return msisdn;

		   }
		   
		  } catch (NumberParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }

		  
		  return null;
	}

	public static void main(String[] args) {
		checkMsisdn("+919172801171");
	}
}
