package com.jdroid.android.gcm;

import java.util.List;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.jdroid.android.AbstractApplication;
import com.jdroid.java.collections.Lists;

/**
 * 
 * @author Maxi Rosson
 */
public abstract class GcmMessageBroadcastReceiver extends BroadcastReceiver {
	
	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		GcmMessage gcmMessage = AbstractApplication.get().getGcmResolver().resolve(intent);
		onGcmMessage(gcmMessage, intent);
	}
	
	protected abstract void onGcmMessage(GcmMessage gcmMessage, Intent intent);
	
	public static void startListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver, List<GcmMessage> gcmMessages) {
		IntentFilter intentFilter = new IntentFilter();
		for (GcmMessage gcmMessage : gcmMessages) {
			intentFilter.addAction(gcmMessage.getMessageKey());
		}
		LocalBroadcastManager.getInstance(AbstractApplication.get()).registerReceiver(broadcastReceiver, intentFilter);
	}
	
	public static void startListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver, GcmMessage... gcmMessages) {
		startListeningGcmBroadcasts(broadcastReceiver, Lists.newArrayList(gcmMessages));
	}
	
	public static void stopListeningGcmBroadcasts(BroadcastReceiver broadcastReceiver) {
		if (broadcastReceiver != null) {
			LocalBroadcastManager.getInstance(AbstractApplication.get()).unregisterReceiver(broadcastReceiver);
		}
	}
	
	public static void sendBroadcast(GcmMessage gcmMessage, Intent intent) {
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(gcmMessage.getMessageKey());
		broadcastIntent.putExtras(intent.getExtras());
		LocalBroadcastManager.getInstance(AbstractApplication.get()).sendBroadcast(broadcastIntent);
	}
}
