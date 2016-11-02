package com.wireless.bean;

import java.io.Serializable;

public class AcquireNumber implements Serializable{

	
	public String username;
	public String password;
	public String mobileNumber;
	public String country;
	public String pattern;
	
	
	
	public AcquireNumber() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AcquireNumber(String username, String password, String mobileNumber, String country, String pattern) {
		super();
		this.username = username;
		this.password = password;
		this.mobileNumber = mobileNumber;
		this.country = country;
		this.pattern = pattern;
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
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	@Override
	public String toString() {
		return "AcquireNumber [username=" + username + ", password=" + password + ", mobileNumber=" + mobileNumber
				+ ", country=" + country + ", pattern=" + pattern + "]";
	}
	
	
	
}
