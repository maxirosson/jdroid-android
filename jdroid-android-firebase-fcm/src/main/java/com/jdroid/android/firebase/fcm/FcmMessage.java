package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;

public interface FcmMessage {

	Boolean matches(RemoteMessage remoteMessage);

	/**
	 * Since Android O, have a guaranteed life cycle limited to 10 seconds for this method execution
	 *
	 * @param remoteMessage
	 */
	void handle(RemoteMessage remoteMessage);

}
