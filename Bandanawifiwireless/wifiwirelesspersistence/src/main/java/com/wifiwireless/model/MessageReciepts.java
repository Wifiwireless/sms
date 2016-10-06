package com.wifiwireless.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "MessageReciepts")
public class MessageReciepts  extends PL4Base implements Serializable{
	@Column(name = "msisdn")
	private String msisdn;
	@Column(name = "to")
	private String to;
	@Column(name = "network_code")
	private String network_code;
	@Column(name = "messageId")
	private String messageId;
	@Column(name = "status")
	private String status;
	@Column(name = "scts")
	private String scts;
	@Column(name = "err_code")
	private String err_code;
	@Column(name = "message_timestamp")
	private Date message_timestamp;
	public String getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getNetwork_code() {
		return network_code;
	}
	public void setNetwork_code(String network_code) {
		this.network_code = network_code;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getScts() {
		return scts;
	}
	public void setScts(String scts) {
		this.scts = scts;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public Date getMessage_timestamp() {
		return message_timestamp;
	}
	public void setMessage_timestamp(Date message_timestamp) {
		this.message_timestamp = message_timestamp;
	}

}
