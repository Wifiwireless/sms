package com.wifiwireless.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "NumberDetails")
public class NumberDetails  extends PL4Base implements Serializable{
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "country")
	private String country;
	@Column(name = "pattern")
	private String pattern;
	@Column(name = "msisdn")
	private String msisdn;
	@Column(name = "cost")
	private String cost;
	@Column(name = "paid")
	private Boolean paidflag;
	//real no of the customer
	@Column(name = "Phnno")
	private String Phnno;
	
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
	public Boolean getPaidflag() {
		return paidflag;
	}
	public void setPaidflag(Boolean paidflag) {
		this.paidflag = paidflag;
	}
	public String getPhnno() {
		return Phnno;
	}
	public void setPhnno(String phnno) {
		Phnno = phnno;
	}
	
	
	

}
