package com.wireless.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


public class CustomerDetailsResponse implements Serializable {


	private int id;

	private String company;
	private String first_name;
	private String last_name;
	private String email;
	private String phone;
	private String formfields;
	private String date_created;
	private String date_modified;
	private String storecredit;
	private String registrationipaddress;
	private String customergroupid;
	private String notes;
	private String taxexemptcategory;
	private String resetpassonlogin;
	private String secret;
	private String extension;
	private String acceptsmarketing;
	private String did;

	private String token;

	private String appid;

	private String selector;

	private Boolean ispbxAccountCreated;

	private String status;

	public String getCompany() {
		return company;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getSecret() {
		return secret;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Boolean getIspbxAccountCreated() {
		return ispbxAccountCreated;
	}

	public void setIspbxAccountCreated(Boolean ispbxAccountCreated) {
		this.ispbxAccountCreated = ispbxAccountCreated;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFormfields() {
		return formfields;
	}

	public void setFormfields(String formfields) {
		this.formfields = formfields;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public String getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(String date_modified) {
		this.date_modified = date_modified;
	}

	public String getStorecredit() {
		return storecredit;
	}

	public void setStorecredit(String storecredit) {
		this.storecredit = storecredit;
	}

	public String getRegistrationipaddress() {
		return registrationipaddress;
	}

	public void setRegistrationipaddress(String registrationipaddress) {
		this.registrationipaddress = registrationipaddress;
	}

	public String getCustomergroupid() {
		return customergroupid;
	}

	public void setCustomergroupid(String customergroupid) {
		this.customergroupid = customergroupid;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getTaxexemptcategory() {
		return taxexemptcategory;
	}

	public void setTaxexemptcategory(String taxexemptcategory) {
		this.taxexemptcategory = taxexemptcategory;
	}

	public String getResetpassonlogin() {
		return resetpassonlogin;
	}

	public void setResetpassonlogin(String resetpassonlogin) {
		this.resetpassonlogin = resetpassonlogin;
	}

	public String getAcceptsmarketing() {
		return acceptsmarketing;
	}

	public void setAcceptsmarketing(String acceptsmarketing) {
		this.acceptsmarketing = acceptsmarketing;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



}
