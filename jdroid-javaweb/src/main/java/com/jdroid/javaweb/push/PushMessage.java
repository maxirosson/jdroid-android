package com.jdroid.javaweb.push;

import java.util.Map;

/**
 * 
 * @author Maxi Rosson
 */
public interface PushMessage {
	
	public Map<String, String> getParameters();
	
	/**
	 * @param key
	 * @param value
	 */
	public void addParameter(String key, String value);
	
	public void addParameter(String key, Boolean value);
	
	public void addParameter(String key, Integer value);
	
	public void addParameter(String key, Long value);
	
}
