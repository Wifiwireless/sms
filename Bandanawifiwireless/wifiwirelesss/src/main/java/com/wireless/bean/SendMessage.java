package com.wireless.bean;

import java.io.Serializable;

public class SendMessage implements Serializable{

 
 
// https://rest.nexmo.com/sms/json?api_key=abcdefg&api_secret=12345678&to=14153193699&from=16192596844&text=hello+from+Nexmo
  String to;
  String from;
  String body;
  
  String password;
  
 public SendMessage(String to, String from, String text) {
  super();
  this.to = to;
  this.from = from;
  this.body = text;
 }
 
 public SendMessage() {
  super();
  // TODO Auto-generated constructor stub
 }


public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getTo() {
  return to;
 }
 public void setTo(String to) {
  this.to = to;
 }
 public String getFrom() {
  return from;
 }
 public void setFrom(String from) {
  this.from = from;
 }

public String getBody() {
	return body;
}

public void setBody(String body) {
	this.body = body;
}
 
  
  
  
}