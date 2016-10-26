package com.wifiwireless.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.wifiwireless.model.CustomerDetails;

@Remote
public interface CustomerDaoInterface {

	public void addCustomer(ArrayList<CustomerDetails> customer);
	public void updateCustomer(ArrayList<CustomerDetails> customer);
	public String checkNumber(String email,String secret);
	public List<CustomerDetails> getAllCustomers();
	public CustomerDetails getCustomerDetailsByUsername(String username);
	public void updateCustomer(CustomerDetails customer);
}
