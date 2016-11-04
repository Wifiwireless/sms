package com.wireless.bean;

import java.io.Serializable;

public class AddExtResponse implements Serializable{

	
	String success;
	String id;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
