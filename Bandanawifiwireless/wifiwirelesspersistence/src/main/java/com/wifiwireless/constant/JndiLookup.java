package com.wifiwireless.constant;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.wifiwireless.interfaces.CustomerCheckDaoInterface;
import com.wifiwireless.interfaces.CustomerDaoInterface;
import com.wifiwireless.interfaces.MessageRecieptsInterface;
import com.wifiwireless.interfaces.MessagesInterface;
import com.wifiwireless.interfaces.NumberDetailsInterface;


public class JndiLookup {

	private static String EJB_NUMBER_DETAILS="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/NumberDetailsDao!com.wifiwireless.interfaces.NumberDetailsInterface";
	private static String EJB_MESSAGES="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/MessagesDao!com.wifiwireless.interfaces.MessagesInterface";
	private static String EJB_MESSAGES_RECIEPTS="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/MessageRecieptsDao!com.wifiwireless.interfaces.MessageRecieptsInterface";
	private static String EJB_CUSTOMER_DETAILS="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/CustomerDao!com.wifiwireless.interfaces.CustomerDaoInterface";
	private static String EJB_CUSTOMER_CHECK="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/CustomerCheckDao!com.wifiwireless.interfaces.CustomerCheckDaoInterface";
	
	
	
	public static String getEJB_CUSTOMER_CHECK() {
		return EJB_CUSTOMER_CHECK;
	}
	public static void setEJB_CUSTOMER_CHECK(String eJB_CUSTOMER_CHECK) {
		EJB_CUSTOMER_CHECK = eJB_CUSTOMER_CHECK;
	}
	public static String getEJB_CUSTOMER_DETAILS() {
		return EJB_CUSTOMER_DETAILS;
	}
	public static String getEJB_MESSAGES_RECIEPTS() {
		return EJB_MESSAGES_RECIEPTS;
	}
	public static String getEJB_MESSAGES() {
		return EJB_MESSAGES;
	}
	public String getEJB_NUMBER_DETAILS() {
		return EJB_NUMBER_DETAILS;
	}
	public static Context getContext() {
		Context ctx = null;
		try {
			Properties props = new Properties();
			props.setProperty("java.naming.factory.url.pkgs",
					"org.jboss.ejb.client.naming");

			ctx = new InitialContext(props);
		} catch (NamingException e) {
			// TODO Auto-generated catch block

		}
		return ctx;
	}
	public static NumberDetailsInterface getNumberDetailsDao() {

		NumberDetailsInterface numberDetailsInterface = null;
		try {
			numberDetailsInterface = (NumberDetailsInterface) getContext().lookup(
					EJB_NUMBER_DETAILS);
		} catch (NamingException e) {
		
		}
		return numberDetailsInterface;

	}	
	
	
	public static CustomerDaoInterface getCustomerDetails() {

		CustomerDaoInterface customerDaoInterface = null;
		try {
			customerDaoInterface = (CustomerDaoInterface) getContext().lookup(
					EJB_CUSTOMER_DETAILS);
		} catch (NamingException e) {
		
		}
		return customerDaoInterface;

	}	
	
	
	public static CustomerCheckDaoInterface getCustomerCheckdao() {

		CustomerCheckDaoInterface customerCheckDaoInterface = null;
		try {
			customerCheckDaoInterface = (CustomerCheckDaoInterface) getContext().lookup(
					EJB_CUSTOMER_CHECK);
		} catch (NamingException e) {
		
		}
		return customerCheckDaoInterface;

	}	
	
	public static MessagesInterface getMessageDao() {

		MessagesInterface messagesInterface = null;
		try {
			messagesInterface = (MessagesInterface) getContext().lookup(
					EJB_MESSAGES);
		} catch (NamingException e) {
		
		}
		return messagesInterface;

	}	
	
	public static MessageRecieptsInterface getMessageRecieptsDao() {

		MessageRecieptsInterface messageRecieptsInterface = null;
		try {
			messageRecieptsInterface = (MessageRecieptsInterface) getContext().lookup(
					EJB_MESSAGES_RECIEPTS);
		} catch (NamingException e) {
		
		}
		return messageRecieptsInterface;

	}	
	
	
}
