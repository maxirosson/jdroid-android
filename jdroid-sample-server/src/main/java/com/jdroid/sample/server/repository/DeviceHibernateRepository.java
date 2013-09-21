package com.jdroid.sample.server.repository;

import com.jdroid.java.repository.InMemoryRepository;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceRepository;
import com.jdroid.javaweb.push.DeviceType;

public class DeviceHibernateRepository extends InMemoryRepository<Device> implements DeviceRepository {
	
	/**
	 * @see com.jdroid.javaweb.push.DeviceRepository#find(java.lang.String, com.jdroid.javaweb.push.DeviceType)
	 */
	@Override
	public Device find(String installationId, DeviceType deviceType) {
		return null;
	}
}
