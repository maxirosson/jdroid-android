package com.jdroid.android.firebase.fcm;

import com.google.firebase.messaging.RemoteMessage;

public interface FcmMessageResolver {

	FcmMessage resolve(RemoteMessage remoteMessage);

}
