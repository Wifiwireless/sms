package com.wifiwireless.interfaces;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.wifiwireless.model.Messages;
@Remote
public interface MessagesInterface {
	public void addMesages(Messages messages);
	public void mergeNumber(Messages messages);
	public ArrayList<Messages> getMessageByMsisdn(String destination);
}
