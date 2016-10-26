package com.wireless.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;
@JsonInclude(Include.NON_NULL)
public class FetchMessageResponse implements Serializable {

	
	String date;
	UnreadSms unread_smss;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public UnreadSms getUnread_smss() {
		return unread_smss;
	}
	public void setUnread_smss(UnreadSms unread_smss) {
		this.unread_smss = unread_smss;
	}
	

}