package com.wifiwireless.interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.wifiwireless.model.CustomerDetails;

@Remote
public interface CustomerDaoInterface {

	public void addCustomer(ArrayList<CustomerDetails> customer);
	public void updateCustomer(ArrayList<CustomerDetails> customer);
	
	
}
