package com.wifiwireless.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "CustomerCheck")
public class CustomerCheck  extends WifiBase implements Serializable{
	@Column(name="datemodified")
	private Date datemodified;
	@Column(name="length")
	private int length;
	public Date getDatemodified() {
		return datemodified;
	}
	public void setDatemodified(Date datemodified) {
		this.datemodified = datemodified;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}

}
