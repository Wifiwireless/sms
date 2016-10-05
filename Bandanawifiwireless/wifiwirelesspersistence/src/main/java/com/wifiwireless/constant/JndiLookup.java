package com.wifiwireless.constant;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.wifiwireless.interfaces.NumberDetailsInterface;

public class JndiLookup {

	private static String EJB_NUMBER_DETAILS="java:global/wifiwirelessdatabase-0.0.1-SNAPSHOT-jar-with-dependencies/NumberDetailsDao!com.wifiwireless.interfaces.NumberDetailsInterface";

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
	
}
