package com.wireless.bean;

import java.io.Serializable;

public class BuyNumber implements Serializable {

	String username;
	String password;
	String country;
	String msisdn;

	public BuyNumber() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BuyNumber(String username, String password, String country, String msisdn) {
		super();
		this.username = username;
		this.password = password;
		this.country = country;
		this.msisdn = msisdn;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
