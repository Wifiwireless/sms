package com.wireless.bean;

import java.io.Serializable;

public class UnreadMessage implements Serializable {

	String sms_id;
	String sending_date;
	String sender;
	String sms_text;
	public String getSms_id() {
		return sms_id;
	}
	public void setSms_id(String sms_id) {
		this.sms_id = sms_id;
	}
	public String getSending_date() {
		return sending_date;
	}
	public void setSending_date(String sending_date) {
		this.sending_date = sending_date;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSms_text() {
		return sms_text;
	}
	public void setSms_text(String sms_text) {
		this.sms_text = sms_text;
	}
	@Override
	public String toString() {
		return "UnreadMessage [sms_id=" + sms_id + ", sending_date=" + sending_date + ", sender=" + sender
				+ ", sms_text=" + sms_text + "]";
	}
	
}
