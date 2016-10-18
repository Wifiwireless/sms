package com.wifiwireless.interfaces;

import javax.ejb.Remote;

import com.wifiwireless.model.CustomerCheck;

@Remote
public interface CustomerCheckDaoInterface {
	
	public void addCustomerCheck(CustomerCheck check);
	public void updateCustomerCheck(CustomerCheck check);
	public CustomerCheck getData();
}
