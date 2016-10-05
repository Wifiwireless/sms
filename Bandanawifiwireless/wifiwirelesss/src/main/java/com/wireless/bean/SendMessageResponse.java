package com.wireless.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SendMessageResponse implements Serializable {

 @SerializedName("message-count")
 String messageCount;
 
 ArrayList<MessageResponse> messages;
 

}