package com.wireless.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BuyNumberResponse {

 
 String success;
 String error;
 public String getSuccess() {
  return success;
 }
 public void setSuccess(String success) {
  this.success = success;
 }
 public String getError() {
  return error;
 }
 public void setError(String error) {
  this.error = error;
 }
@Override
public String toString() {
	return "BuyNumberResponse [success=" + success + ", error=" + error + "]";
}

 
 
}