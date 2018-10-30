package com.jdroid.android.firebase.remoteconfig;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.firebase.fcm.AbstractFcmMessage;
import com.jdroid.android.utils.SharedPreferencesHelper;

public class FirebaseRemoteConfigFetchFcmMessage extends AbstractFcmMessage {

	@Override
	public String getMessageKey() {
		return "firebaseRemoteConfigFetchFcmMessage";
	}

	@Override
	public void handle(RemoteMessage remoteMessage) {
		SharedPreferencesHelper.get().savePreferenceAsync(FirebaseRemoteConfigLoader.CONFIG_STALE, true);
		new FirebaseRemoteConfigFetchCommand().start();
	}
}
