package com.jdroid.android.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jdroid.java.collections.Lists;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.List;

public class InstallReferrerReceiver extends BroadcastReceiver {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(InstallReferrerReceiver.class);
	
	private static final String INSTALL_ACTION = "com.android.vending.INSTALL_REFERRER";
	
	private List<BroadcastReceiver> receivers = Lists.newArrayList();
	
	public InstallReferrerReceiver(List<BroadcastReceiver> receivers) {
		this.receivers = receivers;
	}
	
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if (INSTALL_ACTION.equals(intent.getAction())) {
				for (BroadcastReceiver each : receivers) {
					try {
						LOGGER.warn("Executing " + each.getClass().getSimpleName());
						each.onReceive(context, intent);
					} catch (Exception e) {
						LOGGER.error("Error when executing " + each.getClass().getSimpleName(), e);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error when executing receivers", e);
		}
	}
	
}
