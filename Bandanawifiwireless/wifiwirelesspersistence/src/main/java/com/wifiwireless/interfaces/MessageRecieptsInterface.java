package com.wifiwireless.interfaces;

import javax.ejb.Remote;

import com.wifiwireless.model.MessageReciepts;

@Remote
public interface MessageRecieptsInterface {
	public void addMesages(MessageReciepts reciepts);
	public void mergeNumber(MessageReciepts reciepts);
}
