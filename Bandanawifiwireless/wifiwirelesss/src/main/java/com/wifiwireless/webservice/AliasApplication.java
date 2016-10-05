package com.wifiwireless.webservice;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

 public class AliasApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
 
	public AliasApplication() {
		singletons.add(new Webservices());
	}
 
	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}