package com.jdroid.javaweb.push;

import java.util.List;
import com.jdroid.javaweb.push.gcm.GcmSender;

/**
 * 
 * @author Maxi Rosson
 */
public enum DeviceType {
	ANDROID("android", GcmSender.get()),
	IPHONE("iphone", null),
	BLACKBERRY("blackberry", null),
	WINDOWS_PHONE("windowsPhone", null);
	
	private String userAgent;
	private PushMessageSender pushMessageSender;
	
	private DeviceType(String userAgent, PushMessageSender pushMessageSender) {
		this.pushMessageSender = pushMessageSender;
		this.userAgent = userAgent;
	}
	
	public static DeviceType find(String userAgent) {
		for (DeviceType each : values()) {
			if (each.userAgent.equalsIgnoreCase(userAgent)) {
				return each;
			}
		}
		return null;
	}
	
	public PushResponse send(List<Device> devices, PushMessage pushMessage) {
		return pushMessageSender.send(devices, pushMessage);
	}
}
