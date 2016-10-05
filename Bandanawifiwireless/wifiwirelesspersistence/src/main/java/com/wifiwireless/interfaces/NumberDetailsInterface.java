package com.wifiwireless.interfaces;

import javax.ejb.Remote;

import com.wifiwireless.model.NumberDetails;


@Remote
public interface NumberDetailsInterface {
	public void addNumberDetails(NumberDetails arrContact);
	public void mergeNumber(NumberDetails number);
	public Boolean checkandUpdate(String msisdn,String username,String password);
	
}
