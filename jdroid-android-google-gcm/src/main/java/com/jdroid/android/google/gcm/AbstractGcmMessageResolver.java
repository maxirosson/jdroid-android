package com.jdroid.android.google.gcm;

import android.os.Bundle;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.NumberUtils;

import org.slf4j.Logger;

import java.util.List;

public abstract class AbstractGcmMessageResolver implements GcmMessageResolver {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(AbstractGcmMessageResolver.class);
	
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	private static final String USER_ID_KEY = "userIdKey";
	
	private List<GcmMessage> gcmMessages;
	
	public AbstractGcmMessageResolver(List<GcmMessage> gcmMessages) {
		this.gcmMessages = gcmMessages;
	}
	
	public AbstractGcmMessageResolver(GcmMessage... gcmMessages) {
		this(Lists.newArrayList(gcmMessages));
	}
	
	@Override
	public GcmMessage resolve(String from, Bundle data) {
		String messageKey = data.getString(getMessageKeyExtraName());
		LOGGER.debug("GCM message received. / Message Key: " + messageKey);
		for (GcmMessage each : gcmMessages) {
			if (each.getMessageKey().equalsIgnoreCase(messageKey)) {
				
				Long userId = NumberUtils.getLong(data.getString(USER_ID_KEY));
				
				// We should ignore messages received for previously logged users
				if ((userId != null)
						&& (!SecurityContext.get().isAuthenticated() || !SecurityContext.get().getUser().getId().equals(
							userId))) {
					LOGGER.warn("The GCM message is ignored because it was sent to another user: " + userId);
					onNotAuthenticatedUser(userId);
					return null;
				}
				return each;
			}
		}
		AbstractApplication.get().getExceptionHandler().logWarningException(
			"The GCM message key [" + messageKey + "] is unknown");
		return null;
	}
	
	protected String getMessageKeyExtraName() {
		return MESSAGE_KEY_EXTRA;
	}
	
	protected abstract void onNotAuthenticatedUser(Long userId);
}
