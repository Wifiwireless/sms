package com.wireless.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class UnreadSms implements Serializable  {

	ArrayList<UnreadMessage> item;

	public ArrayList<UnreadMessage> getItem() {
		return item;
	}

	public void setItem(ArrayList<UnreadMessage> item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "UnreadSms [item=" + item + "]";
	}
	
	
}
