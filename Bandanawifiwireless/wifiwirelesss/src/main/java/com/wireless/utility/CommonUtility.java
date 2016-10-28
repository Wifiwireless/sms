package com.wireless.utility;

import org.jboss.jandex.Main;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class CommonUtility {

	
	
	public static String checkMsisdn(String msisdn){
		
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		
		try {
			PhoneNumber phoneNumberProto = phoneUtil.parse(msisdn, "US");
			
			if (!phoneUtil.isValidNumber(phoneNumberProto)){
				
				System.out.println("in valid for us");
 return msisdn;
			}else{
				
				if(!msisdn.startsWith("+1")){
					msisdn = "+1"+msisdn;
					System.out.println(msisdn);
				}
				System.out.println("valid for us");
				 return msisdn;

			}
			
		} catch (NumberParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
			}
	
	
	public static void main(String[] args) {
		checkMsisdn("494634536");
	}
}
