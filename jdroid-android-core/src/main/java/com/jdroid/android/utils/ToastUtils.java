package com.jdroid.android.utils;

import android.os.Handler;
import android.widget.Toast;

import com.jdroid.android.application.AbstractApplication;

import java.lang.ref.WeakReference;

public final class ToastUtils {

	private static final int DEFAULT_DURATION = Toast.LENGTH_LONG;

	private static WeakReference<Toast> currentToast;

	private static final Handler HANDLER = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			String message = (String)msg.obj;
			switch (msg.arg1) {
				case 1:
					showToast(message);
					break;
				default:
					break;
			}
		}
	};

	public static void init() {
		// nothing...
	}

	private static void showToastOnUIThread(String message, int type) {
		HANDLER.removeMessages(1);
		HANDLER.sendMessage(HANDLER.obtainMessage(1, type, 0, message));
	}

	/**
	 * Show the {@link Toast} on the UI Thread.
	 *
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToastOnUIThread(String message) {
		showToastOnUIThread(message, 1);
	}

	/**
	 * Show the {@link Toast} on the UI Thread.
	 *
	 * @param messageId The id of the text to show.
	 */
	public static void showToastOnUIThread(int messageId) {
		showToastOnUIThread(LocalizationUtils.getString(messageId));
	}

	/**
	 * Show the {@link Toast} on the current Thread.
	 *
	 * @param messageId The id of the text to show.
	 */
	public static void showToast(int messageId) {
		showToast(LocalizationUtils.getString(messageId), DEFAULT_DURATION);
	}

	/**
	 * Show the {@link Toast} on the current Thread.
	 *
	 * @param message The text to show. Can be formatted text.
	 */
	public static void showToast(String message) {
		showToast(message, DEFAULT_DURATION);
	}

	/**
	 * Show the {@link Toast} on the current Thread.
	 *
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 */
	public static void showToast(String message, int duration) {
		showToast(message, duration, null, null, null);
	}

	/**
	 * Show the {@link Toast} on the current Thread.
	 *
	 * @param message The text to show. Can be formatted text.
	 * @param duration How long to display the message. Either {@link Toast#LENGTH_SHORT} {@link Toast#LENGTH_LONG}
	 * @param gravity The location at which the notification should appear on the screen.
	 * @param xOffset The X offset in pixels to apply to the gravity's location.
	 * @param yOffset The Y offset in pixels to apply to the gravity's location.
	 */
	public static void showToast(String message, int duration, Integer gravity, Integer xOffset, Integer yOffset) {
		AbstractApplication androidApplication = AbstractApplication.get();
		if (!androidApplication.isInBackground()) {

			cancelCurrentToast();

			Toast toast = Toast.makeText(androidApplication, message, duration);
			if ((gravity != null) && (xOffset != null) && (yOffset != null)) {
				toast.setGravity(gravity, xOffset, yOffset);
			}
			toast.setDuration(duration);
			toast.show();

			currentToast = new WeakReference<>(toast);
		}
	}

	public static void cancelCurrentToast() {
		Toast toast = currentToast != null ? currentToast.get() : null;
		if (toast != null) {
			toast.cancel();
		}
	}
}
