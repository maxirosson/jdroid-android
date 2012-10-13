package com.jdroid.javaweb.push;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jdroid.java.repository.ObjectNotFoundException;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
@Service
public class PushServiceImpl implements PushService {
	
	private static final Log LOG = LogFactory.getLog(PushServiceImpl.class);
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#enableDevice(java.lang.Long, java.lang.String, java.lang.String,
	 *      com.jdroid.javaweb.push.DeviceType)
	 */
	@Transactional
	@Override
	public void enableDevice(Long userId, String installationId, String registrationId, DeviceType deviceType) {
		try {
			Device device = deviceRepository.find(userId, installationId);
			device.updateRegistrationId(registrationId);
			LOG.info("Updated " + device);
		} catch (ObjectNotFoundException e) {
			Device device = new Device(userId, installationId, registrationId, deviceType);
			deviceRepository.add(device);
			LOG.info("Enabled " + device);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#disableDevice(java.lang.Long, java.lang.String)
	 */
	@Transactional
	@Override
	public void disableDevice(Long userId, String installationId) {
		if (userId != null) {
			try {
				Device device = deviceRepository.find(userId, installationId);
				deviceRepository.remove(device);
				LOG.info("Disabled " + device);
			} catch (ObjectNotFoundException e) {
				// Do nothing
			}
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage, java.lang.Long[])
	 */
	@Override
	public void send(PushMessage pushMessage, Long... userIds) {
		send(pushMessage, Lists.newArrayList(userIds));
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage, java.util.List)
	 */
	@Override
	public void send(final PushMessage pushMessage, List<Long> userIds) {
		Map<DeviceType, List<Device>> devicesMap = Maps.newHashMap();
		for (Long userId : userIds) {
			Collection<Device> userDevices = deviceRepository.findByUserId(userId);
			for (Device device : userDevices) {
				List<Device> devicesByType = devicesMap.get(device.getDeviceType());
				if (devicesByType == null) {
					devicesByType = Lists.newArrayList();
					devicesMap.put(device.getDeviceType(), devicesByType);
				}
				devicesByType.add(device);
			}
		}
		for (final Entry<DeviceType, List<Device>> entry : devicesMap.entrySet()) {
			ExecutorUtils.execute(new PushProcessor(this, entry.getKey(), entry.getValue(), pushMessage));
		}
	}
	
	private class PushProcessor implements Runnable {
		
		private PushService pushService;
		private DeviceType deviceType;
		private List<Device> devices;
		private PushMessage pushMessage;
		
		public PushProcessor(PushService pushService, DeviceType deviceType, List<Device> devices,
				PushMessage pushMessage) {
			this.pushService = pushService;
			this.deviceType = deviceType;
			this.devices = devices;
			this.pushMessage = pushMessage;
		}
		
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			PushResponse pushResponse = deviceType.send(devices, pushMessage);
			for (Device device : pushResponse.getDevicesToRemove()) {
				pushService.disableDevice(device.getUserId(), device.getInstallationId());
			}
			for (Device device : pushResponse.getDevicesToUpdate()) {
				pushService.enableDevice(device.getUserId(), device.getInstallationId(), device.getRegistrationId(),
					device.getDeviceType());
			}
		}
	}
}
