package com.jdroid.javaweb.push;

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
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
// TODO We should add a batch process to remove all the devices disabled and the ones with the
// same registration id but different installation id
@Service
public class PushServiceImpl implements PushService {
	
	private static final Log LOG = LogFactory.getLog(PushServiceImpl.class);
	
	@Autowired
	private DeviceRepository deviceRepository;
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#enableDevice(java.lang.String, com.jdroid.javaweb.push.DeviceType,
	 *      java.lang.String)
	 */
	@Transactional
	@Override
	public void enableDevice(String installationId, DeviceType deviceType, String registrationId) {
		Device device = deviceRepository.find(installationId, deviceType);
		if (device != null) {
			device.updateRegistrationId(registrationId);
			LOG.info("Updated " + device);
		} else {
			device = new Device(installationId, registrationId, deviceType);
			deviceRepository.add(device);
			LOG.info("Enabled " + device);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#disableDevice(java.lang.String, com.jdroid.javaweb.push.DeviceType)
	 */
	@Transactional
	@Override
	public void disableDevice(String installationId, DeviceType deviceType) {
		Device device = deviceRepository.find(installationId, deviceType);
		if (device != null) {
			device.setDisabled(true);
			LOG.info("Disabled " + device);
		}
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage,
	 *      com.jdroid.javaweb.push.Device[])
	 */
	@Override
	public void send(PushMessage pushMessage, Device... devices) {
		send(pushMessage, Lists.newArrayList(devices));
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushService#send(com.jdroid.javaweb.push.PushMessage, java.util.List)
	 */
	@Override
	public void send(final PushMessage pushMessage, List<Device> devices) {
		Map<DeviceType, List<Device>> devicesMap = Maps.newHashMap();
		for (Device device : devices) {
			if (!device.isDisabled() && (device.getRegistrationId() != null)) {
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
				pushService.disableDevice(device.getInstallationId(), device.getDeviceType());
			}
			for (Device device : pushResponse.getDevicesToUpdate()) {
				pushService.enableDevice(device.getInstallationId(), device.getDeviceType(), device.getRegistrationId());
			}
		}
	}
}
