package com.wireless.bean;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MessageResponse implements Serializable{

 
 String status;
 
 String to;
 String network;
 @SerializedName("message-id")
 String messageId;
 @SerializedName("clientref")
 String clientref;
 @SerializedName("remaining-balance")
 String remainingBalance;
 @SerializedName("message-price")
 String messagePrice;
 @SerializedName("error-text")
 String errorText;
 public String getStatus() {
  return status;
 }
 public void setStatus(String status) {
  this.status = status;
 }
 public String getTo() {
  return to;
 }
 public void setTo(String to) {
  this.to = to;
 }
 public String getNetwork() {
  return network;
 }
 public void setNetwork(String network) {
  this.network = network;
 }
 public String getMessageId() {
  return messageId;
 }
 public void setMessageId(String messageId) {
  this.messageId = messageId;
 }
 public String getClientref() {
  return clientref;
 }
 public void setClientref(String clientref) {
  this.clientref = clientref;
 }
 public String getRemainingBalance() {
  return remainingBalance;
 }
 public void setRemainingBalance(String remainingBalance) {
  this.remainingBalance = remainingBalance;
 }
 public String getMessagePrice() {
  return messagePrice;
 }
 public void setMessagePrice(String messagePrice) {
  this.messagePrice = messagePrice;
 }
 public String getErrorText() {
  return errorText;
 }
 public void setErrorText(String errorText) {
  this.errorText = errorText;
 }
 
 
}