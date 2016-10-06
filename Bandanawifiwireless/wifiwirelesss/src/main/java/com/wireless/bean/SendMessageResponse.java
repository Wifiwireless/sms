package com.wireless.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SendMessageResponse implements Serializable {

 @SerializedName("message-count")
 String messageCount;
 
 ArrayList<MessageResponse> messages;

public String getMessageCount() {
	return messageCount;
}

public void setMessageCount(String messageCount) {
	this.messageCount = messageCount;
}

public ArrayList<MessageResponse> getMessages() {
	return messages;
}

public void setMessages(ArrayList<MessageResponse> messages) {
	this.messages = messages;
}
 

}