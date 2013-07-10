package com.jdroid.android.gcm;

import java.util.List;
import org.slf4j.Logger;
import android.content.Intent;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;
import com.jdroid.java.utils.NumberUtils;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class DefaultGcmMessageResolver implements GcmMessageResolver {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(DefaultGcmMessageResolver.class);
	
	private static final String MESSAGE_KEY_EXTRA = "messageKey";
	private static final String REQUIRES_AUTHENTICATION_KEY_EXTRA = "requiresAuthenticationKey";
	
	private List<GcmMessage> gcmMessages;
	
	public DefaultGcmMessageResolver(List<GcmMessage> gcmMessages) {
		this.gcmMessages = gcmMessages;
	}
	
	public DefaultGcmMessageResolver(GcmMessage... gcmMessages) {
		this(Lists.newArrayList(gcmMessages));
	}
	
	/**
	 * @see com.jdroid.android.gcm.GcmMessageResolver#resolve(android.content.Intent)
	 */
	@Override
	public GcmMessage resolve(Intent intent) {
		String messageKey = intent.getStringExtra(MESSAGE_KEY_EXTRA);
		LOGGER.debug("GCM message received. / Message Key: " + messageKey);
		for (GcmMessage each : gcmMessages) {
			if (each.getMessageKey().equalsIgnoreCase(messageKey)) {
				
				Boolean requiresAuthenticationKey = NumberUtils.getBoolean(
					intent.getStringExtra(REQUIRES_AUTHENTICATION_KEY_EXTRA), false);
				
				// We should ignore messages received for previously logged users
				if (requiresAuthenticationKey && !SecurityContext.get().isAuthenticated()) {
					LOGGER.warn("The GCM message is ignored because it requires to be authenticated");
					onAuthenticationRequired();
					return null;
				}
				return each;
			}
		}
		LOGGER.warn("The GCM message key [" + messageKey + "] is unknown");
		return null;
	}
	
	protected abstract void onAuthenticationRequired();
}
