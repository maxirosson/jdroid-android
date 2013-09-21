package com.jdroid.sample.server.controller.push;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.jdroid.java.utils.StringUtils;
import com.jdroid.javaweb.push.Device;
import com.jdroid.javaweb.push.DeviceType;
import com.jdroid.javaweb.push.PushMessage;
import com.jdroid.javaweb.push.PushService;
import com.jdroid.javaweb.push.gcm.DefaultGcmMessage;

/**
 * @author Maxi Rosson
 */
@Path("api/gcm")
@Controller
public class GcmController {
	
	@Autowired
	private PushService pushService;
	
	@GET
	@Path("/send")
	public void send(@QueryParam("registrationId") String registrationId,
			final @QueryParam("messageKey") String messageKey, @QueryParam("params") String params) {
		
		PushMessage pushMessage = new DefaultGcmMessage() {
			
			@Override
			public String getMessageKey() {
				return messageKey;
			}
		};
		
		if (params != null) {
			for (String param : StringUtils.splitToCollection(params.replace("[", "").replace("]", ""))) {
				String[] vec = param.split(":");
				pushMessage.addParameter(vec[0], vec[1]);
			}
		}
		Device device = new Device(null, registrationId, DeviceType.ANDROID);
		
		pushService.send(pushMessage, device);
	}
}