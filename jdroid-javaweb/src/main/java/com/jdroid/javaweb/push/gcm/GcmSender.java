package com.jdroid.javaweb.push.gcm;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.context.AbstractApplicationContext;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushMessageSender;
import com.jdroid.javaweb.push.PushResponse;

public class GcmSender implements PushMessageSender {
	
	private static final Log LOG = LogFactory.getLog(GcmSender.class);
	
	private final static PushMessageSender INSTANCE = new GcmSender();
	
	private GcmSender() {
	}
	
	public static PushMessageSender get() {
		return INSTANCE;
	}
	
	/**
	 * @see com.jdroid.javaweb.push.PushMessageSender#send(java.util.List, com.jdroid.javaweb.push.PushMessage)
	 */
	@Override
	public PushResponse send(List<Device> devices, PushMessage pushMessage) {
		
		Set<String> registrationIds = Sets.newHashSet();
		for (Device device : devices) {
			if (device.getRegistrationId() != null) {
				registrationIds.add(device.getRegistrationId());
			}
		}
		GcmMessage gcmMessage = (GcmMessage)pushMessage;
		
		Sender sender = new Sender(AbstractApplicationContext.get().getGoogleServerApiKey());
		Message.Builder messageBuilder = new Message.Builder();
		if (StringUtils.isNotEmpty(gcmMessage.getCollapseKey())) {
			messageBuilder.collapseKey(gcmMessage.getCollapseKey());
		}
		if (gcmMessage.isDelayWhileIdle() != null) {
			messageBuilder.delayWhileIdle(gcmMessage.isDelayWhileIdle());
		}
		if (gcmMessage.getTimeToLive() != null) {
			messageBuilder.timeToLive(gcmMessage.getTimeToLive());
		}
		for (Entry<String, String> entry : gcmMessage.getParameters().entrySet()) {
			if (entry.getValue() != null) {
				messageBuilder.addData(entry.getKey(), entry.getValue());
			}
		}
		Message message = messageBuilder.build();
		PushResponse pushResponse = new PushResponse();
		try {
			MulticastResult multicastResult = sender.send(message, Lists.newArrayList(registrationIds), 10);
			
			// analyze the results
			for (int i = 0; i < registrationIds.size(); i++) {
				Result result = multicastResult.getResults().get(i);
				Device device = devices.get(i);
				if (result.getMessageId() != null) {
					LOG.info("Succesfully sent GCM message to device " + device);
					String canonicalRegId = result.getCanonicalRegistrationId();
					if (canonicalRegId != null) {
						// same device has more than on registration id: update it
						device.updateRegistrationId(canonicalRegId);
						pushResponse.addDeviceToUpdate(device);
						LOG.info("Updated registration id of device " + device);
					}
				} else {
					String error = result.getErrorCodeName();
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						// Application has been removed from device - unregister it
						pushResponse.addDeviceToRemove(device);
						LOG.info("Unregistered device " + device);
					} else {
						LOG.info("Error [" + error + "] when sending message to device " + device);
					}
				}
			}
			
		} catch (IOException e) {
			// The message could not be sent.
			LOG.warn(e);
		}
		return pushResponse;
	}
}
