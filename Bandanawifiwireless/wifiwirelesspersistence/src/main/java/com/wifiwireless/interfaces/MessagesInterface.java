package com.wifiwireless.interfaces;

import javax.ejb.Remote;

import com.wifiwireless.model.Messages;
@Remote
public interface MessagesInterface {
	public void addMesages(Messages messages);
	public void mergeNumber(Messages messages);
}
