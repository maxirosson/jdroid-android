package com.jdroid.android.shortcuts;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;
import android.support.annotation.AnyRes;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppShortcutsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(AppShortcutsHelper.class);

	public static final int MAX_SHORTCUT_COUNT_PER_ACTIVITY = 4;
	
	private static List<ShortcutInfo> initialShortcutInfos;

	public static void reportShortcutUsed(String shortcutId) {
		if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			shortcutManager.reportShortcutUsed(shortcutId);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void setInitialShortcutInfos(List<ShortcutInfo> initialShortcutInfos) {
		AppShortcutsHelper.initialShortcutInfos = initialShortcutInfos;
	}
	
	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static List<ShortcutInfo> getInitialShortcutInfos() {
		return initialShortcutInfos;
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void setDynamicShortcuts(List<ShortcutInfo> shortcutInfos) {
		if (shortcutInfos != null) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			processShortcutInfos(shortcutInfos);
			shortcutManager.setDynamicShortcuts(shortcutInfos);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void updateDynamicShortcuts(List<ShortcutInfo> shortcutInfos) {
		ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
		processShortcutInfos(shortcutInfos);
		shortcutManager.updateShortcuts(shortcutInfos);
	}
	
	@TargetApi(Build.VERSION_CODES.N_MR1)
	private static List<ShortcutInfo> processShortcutInfos(List<ShortcutInfo> shortcutInfos) {
		Collections.sort(shortcutInfos, new Comparator<ShortcutInfo>() {
			@Override
			public int compare(ShortcutInfo o1, ShortcutInfo o2) {
				return o1.getRank() > o2.getRank() ? 1 : -1;
			}
		});
		if (shortcutInfos.size() > MAX_SHORTCUT_COUNT_PER_ACTIVITY) {
			shortcutInfos = shortcutInfos.subList(0, MAX_SHORTCUT_COUNT_PER_ACTIVITY - 1);
			LOGGER.warn("App Shortcuts limit [" + MAX_SHORTCUT_COUNT_PER_ACTIVITY + "] reached. Ignoring some of them");
		}
		return shortcutInfos;
	}
	
	public static void registerDynamicShortcuts() {
		AppShortcutsWorker.start();
	}
	
	public static Boolean isAppShortcutsAvailable() {
		return AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1;
	}
	
	@TargetApi(Build.VERSION_CODES.O)
	public static void pinShortcut(ShortcutInfo shortcutInfo) {
		ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
		if (shortcutManager.isRequestPinShortcutSupported()) {
			shortcutManager.requestPinShortcut(shortcutInfo, null);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.O)
	public static void pinShortcut(ShortcutInfo shortcutInfo, Intent shortcutResultIntent) {
		ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
		if (shortcutManager.isRequestPinShortcutSupported()) {
			// Create the PendingIntent object only if your app needs to be notified that the user allowed the shortcut to be pinned.
			// Note that, if the pinning operation fails, your app isn't notified.
			// Configure the intent so that your app's broadcast receiver gets the callback successfully.
			PendingIntent successCallback = PendingIntent.getBroadcast(AbstractApplication.get(), 0, shortcutResultIntent, 0);
			shortcutManager.requestPinShortcut(shortcutInfo, successCallback.getIntentSender());
		}
	}
	
	public static void pinShortcut(Intent shortcutIntent, String shortcutName, @AnyRes int iconResId) {
		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutName);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(AbstractApplication.get(), iconResId));
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		AbstractApplication.get().sendBroadcast(intent);
		
		AppShortcutsAppModule.get().getModuleAnalyticsSender().trackPinShortcut(shortcutName);
	}
	
	public static Boolean isPinShortcutAvailable() {
		return AndroidUtils.getApiLevel() <= Build.VERSION_CODES.N_MR1;
	}
}
