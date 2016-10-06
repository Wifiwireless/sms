package com.wifiwireless.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "Messages")
public class Messages extends PL4Base implements Serializable{
	
	
	
	public Messages() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Messages(String source, String destination, String text) {
		super();
		this.source = source;
		this.destination = destination;
		this.text = text;
	}
	public Messages(String status, String message_id, String remaining_balance, String message_price, String network) {
		super();
		this.status = status;
		this.message_id = message_id;
		this.remaining_balance = remaining_balance;
		this.message_price = message_price;
		this.network = network;
	}
	@Column(name = "source")
	private String source;
	@Column(name = "destination")
	private String destination;
	@Column(name = "text")
	private String text;
	@Column(name = "status")
	private String status;
	@Column(name = "message_id")
	private String  message_id;
	@Column(name = "remaining_balance")
	private String remaining_balance;
	@Column(name = "message_price")
	private String message_price;
	@Column(name = "network")
	private String network;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "messagetime")
	private Date messagetime;
	
	public Date getMessagetime() {
		return messagetime;
	}
	public void setMessagetime(Date messagetime) {
		this.messagetime = messagetime;
	}
	public String getSource() {
		return source;
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
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage_id() {
		return message_id;
	}
	public void setMessage_id(String message_id) {
		this.message_id = message_id;
	}
	public String getRemaining_balance() {
		return remaining_balance;
	}
	public void setRemaining_balance(String remaining_balance) {
		this.remaining_balance = remaining_balance;
	}
	public String getMessage_price() {
		return message_price;
	}
	public void setMessage_price(String message_price) {
		this.message_price = message_price;
	}
	public String getNetwork() {
		return network;
	}
	public void setNetwork(String network) {
		this.network = network;
	}
	
	

}
