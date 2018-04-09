package com.jdroid.android.log;

import com.jdroid.java.date.DateUtils;
import com.jdroid.java.domain.Entity;

import java.util.Date;

public class DatabaseLog extends Entity {
	
	private static final long serialVersionUID = -2732523562518387416L;
	
	private String message;
	private Date dateTime;
	
	public DatabaseLog() {
	}
	
	public DatabaseLog(String message) {
		this.message = message;
		dateTime = DateUtils.now();
	}
	

	public Date getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
