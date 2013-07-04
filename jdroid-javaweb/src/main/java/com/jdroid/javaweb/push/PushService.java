package com.jdroid.javaweb.push;

import java.util.List;

/**
 * 
 * @author Maxi Rosson
 */
public interface PushService {
	
	public void enableDevice(String installationId, DeviceType deviceType, String registrationId);
	
	public void disableDevice(String installationId, DeviceType deviceType);
	
	public void send(PushMessage pushMessage, Device... devices);
	
	public void send(PushMessage pushMessage, List<Device> devices);
	
}