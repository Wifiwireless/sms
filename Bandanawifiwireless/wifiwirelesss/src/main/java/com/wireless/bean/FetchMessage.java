package com.wireless.bean;

import java.io.Serializable;

public class FetchMessage implements Serializable{

 
 
// https://rest.nexmo.com/sms/json?api_key=abcdefg&api_secret=12345678&to=14153193699&from=16192596844&text=hello+from+Nexmo

  String from;

  
  String password;
  String last_id;
  String last_ts;
  String device;

  
 public FetchMessage(String to, String from) {
  super();

  this.from = from;

 }
 
 public FetchMessage() {
  super();
  // TODO Auto-generated constructor stub
 }


public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

 public void setFrom(String from) {
  this.from = from;
 }

public String getFrom() {
	return from;
}

public String getLast_id() {
	return last_id;
}

public void setLast_id(String last_id) {
	this.last_id = last_id;
}

public String getLast_ts() {
	return last_ts;
}

public void setLast_ts(String last_ts) {
	this.last_ts = last_ts;
}

public String getDevice() {
	return device;
}

public void setDevice(String device) {
	this.device = device;
}

  
  
  
}