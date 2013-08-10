package com.jdroid.java.http;

/**
 * 
 * @author Maxi Rosson
 */
public interface Server {
	
	public String getName();
	
	public String getBaseUrl();
	
	public Boolean supportsSsl();
	
	public Boolean isProduction();
	
}
