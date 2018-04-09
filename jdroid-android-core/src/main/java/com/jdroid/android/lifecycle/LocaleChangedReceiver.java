package com.jdroid.android.lifecycle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Locale;

public class LocaleChangedReceiver extends BroadcastReceiver {

	private final static Logger LOGGER = LoggerUtils.getLogger(LocaleChangedReceiver.class);

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null && Intent.ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				LOGGER.info("Changed display locale to " + Locale.getDefault(Locale.Category.DISPLAY));
				LOGGER.info("Changed format locale to " + Locale.getDefault(Locale.Category.FORMAT));
			} else {
				LOGGER.info("Changed locale to " + Locale.getDefault());
			}
			ApplicationLifecycleHelper.onLocaleChanged(context);
		}
	}
}
