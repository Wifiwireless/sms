package com.wifiwireless.interfaces;

import javax.ejb.Remote;

@Remote
public interface UsersInterfaces {
	public Boolean autentication(String ext,String value);
}
