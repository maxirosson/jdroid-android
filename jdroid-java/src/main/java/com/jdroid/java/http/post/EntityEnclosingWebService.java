package com.jdroid.java.http.post;

import com.jdroid.java.http.WebService;

public interface EntityEnclosingWebService extends WebService {
	
	public void setEntity(String content);
	
}
