package com.jdroid.javaweb.push.gcm;

import java.util.Map;
import com.google.common.collect.Maps;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultGcmMessage implements GcmMessage {
	
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	
	private Map<String, String> parameters = Maps.<String, String>newHashMap();
	
	public DefaultGcmMessage() {
		addParameter(MESSAGE_KEY_EXTRA, getMessageKey());
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushMessage#addParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushMessage#addParameter(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public void addParameter(String key, Boolean value) {
		parameters.put(key, value.toString());
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushMessage#addParameter(java.lang.String, java.lang.Integer)
	 */
	@Override
	public void addParameter(String key, Integer value) {
		parameters.put(key, value.toString());
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushMessage#addParameter(java.lang.String, java.lang.Long)
	 */
	@Override
	public void addParameter(String key, Long value) {
		parameters.put(key, value.toString());
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.GcmMessage#getParameters()
	 */
	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.GcmMessage#getCollapseKey()
	 */
	@Override
	public String getCollapseKey() {
		return null;
	}
	
	public abstract String getMessageKey();
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.GcmMessage#isDelayWhileIdle()
	 */
	@Override
	public Boolean isDelayWhileIdle() {
		return null;
	}
	
	/**
	 * @see com.jdroid.javaweb.push.gcm.GcmMessage#getTimeToLive()
	 */
	@Override
	public Integer getTimeToLive() {
		return null;
	}
	
}
