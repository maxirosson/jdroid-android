package com.jdroid.android.firebase.fcm;

import com.jdroid.java.collections.Lists;

import java.util.List;

public class FcmContext {

	private static List<FcmMessage> fcmMessages = Lists.newArrayList();

	private static List<FcmEventsListener> fcmEventsListeners = Lists.newArrayList();

	public static void addFcmMessage(FcmMessage fcmMessage) {
		fcmMessages.add(fcmMessage);
	}

	public static List<FcmMessage> getFcmMessages() {
		return fcmMessages;
	}

	public static List<FcmEventsListener> getFcmEventsListeners() {
		return fcmEventsListeners;
	}

	public static void addFcmEventsListener(FcmEventsListener fcmEventsListener) {
		fcmEventsListeners.add(fcmEventsListener);
	}
}
