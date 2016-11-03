package com.wifiwireless.interfaces;

import java.util.List;

import javax.ejb.Remote;

import com.wifiwireless.model.NumberDetails;


@Remote
public interface NumberDetailsInterface {
	public void addNumberDetails(NumberDetails arrContact);
	public void mergeNumber(NumberDetails number);
	public Boolean checkandUpdatePaidFlag(String msisdn,String username,String password);
	public String checkNumber(String username,String password);
	public NumberDetails getNumberDetails(String username,String Passkey);
	public NumberDetails getNumberDetailsByMsisdn(String msisdn);
	public List<NumberDetails> getNumberDetailsByPaidFlag(boolean b);
}
