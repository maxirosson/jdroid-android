package com.jdroid.javaweb.push;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public interface PushService {
	
	public void enableDevice(Long userId, String installationId, String registrationId, DeviceType deviceType);
	
	public void disableDevice(Long userId, String installationId);
	
	public void send(PushMessage pushMessage, Long... userIds);
	
	public void send(PushMessage pushMessage, List<Long> userIds);
	
}