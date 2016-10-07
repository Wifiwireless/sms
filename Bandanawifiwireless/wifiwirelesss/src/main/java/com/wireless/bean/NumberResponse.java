package com.wireless.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class NumberResponse implements Serializable {

	
	public String country;
	public String msisdn;
	public String cost;
	public String error;
	
	 public NumberResponse(String error) {
		  super();
		  this.error = error;
		 }
	  public String getError() {
		  return error;
		 }
		 public void setError(String error) {
		  this.error = error;
		 }
	public NumberResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NumberResponse(String country, String msisdn, String cost) {
		super();
		this.country = country;
		this.msisdn = msisdn;
		this.cost = cost;
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
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	
	
}
