package com.jdroid.javaweb.push;

import com.jdroid.java.repository.Repository;

/**
 * 
 * @author Maxi Rosson
 */
public interface DeviceRepository extends Repository<Device> {
	
	public Device find(String installationId, DeviceType deviceType);
	
}