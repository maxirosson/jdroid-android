package com.jdroid.android.google.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Locale;

public class LocaleChangedReceiver extends BroadcastReceiver {

	private static final Logger LOGGER = LoggerUtils.getLogger(LocaleChangedReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		LOGGER.info("Changed locale to " + Locale.getDefault());
		AbstractGcmAppModule.get().startGcmRegistration(false);
	}
}
