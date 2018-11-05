package com.jdroid.android.shortcuts;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.os.Build;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.firebase.jobdispatcher.ServiceCommand;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;

public class AppShortcutsHelper {

	private static final Logger LOGGER = LoggerUtils.getLogger(AppShortcutsHelper.class);

	public static final int MAX_SHORTCUT_COUNT_PER_ACTIVITY = 4;
	private static final String REQUEST_PIN_SHORTCUT_ACTION = "requestPinShortcut";

	private static DynamicShortcutsLoader dynamicShortcutsLoader;

	// Dynamic Shortcuts

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void setDynamicShortcutsLoader(DynamicShortcutsLoader dynamicShortcutsLoader) {
		AppShortcutsHelper.dynamicShortcutsLoader = dynamicShortcutsLoader;
	}

	public static DynamicShortcutsLoader getDynamicShortcutsLoader() {
		return dynamicShortcutsLoader;
	}

	public static void reportShortcutUsed(String shortcutId) {
		if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			shortcutManager.reportShortcutUsed(shortcutId);
		}
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void setDynamicShortcuts(List<ShortcutInfo> shortcutInfos) {
		if (shortcutInfos != null) {
			ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
			shortcutInfos = processShortcutInfos(shortcutManager, shortcutInfos);
			shortcutManager.setDynamicShortcuts(shortcutInfos);
		}
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	public static void updateDynamicShortcuts(List<ShortcutInfo> shortcutInfos) {
		ShortcutManager shortcutManager = AbstractApplication.get().getSystemService(ShortcutManager.class);
		shortcutInfos = processShortcutInfos(shortcutManager, shortcutInfos);
		shortcutManager.updateShortcuts(shortcutInfos);
	}

	@TargetApi(Build.VERSION_CODES.N_MR1)
	private static List<ShortcutInfo> processShortcutInfos(ShortcutManager shortcutManager, List<ShortcutInfo> shortcutInfos) {
		Collections.sort(shortcutInfos, new Comparator<ShortcutInfo>() {
			@Override
			public int compare(ShortcutInfo o1, ShortcutInfo o2) {
				return o1.getRank() > o2.getRank() ? 1 : -1;
			}
		});
		int max = Math.min(MAX_SHORTCUT_COUNT_PER_ACTIVITY, shortcutManager.getMaxShortcutCountPerActivity());
		if (shortcutInfos.size() > max) {
			shortcutInfos = shortcutInfos.subList(0, max - 1);
			LOGGER.warn("App Shortcuts limit [" + max + "] reached. Ignoring some of them");
		}
		return shortcutInfos;
	}

	public static void registerDynamicShortcuts() {
		ServiceCommand serviceCommand = new AppShortcutsCommand();
		serviceCommand.start();
	}

	public static Boolean isDynamicAppShortcutsSupported() {
		return AndroidUtils.getApiLevel() >= Build.VERSION_CODES.N_MR1;
	}

	// Pinned Shortcuts

	public static Boolean isRequestPinShortcutSupported() {
		return ShortcutManagerCompat.isRequestPinShortcutSupported(AbstractApplication.get());
	}

	public static Boolean requestPinShortcut(@NonNull ShortcutInfoCompat shortcut) {

		Intent pinnedShortcutCallbackIntent = new Intent();
		pinnedShortcutCallbackIntent.setAction(REQUEST_PIN_SHORTCUT_ACTION);
		PendingIntent successCallback = PendingIntent.getBroadcast(AbstractApplication.get(),0, pinnedShortcutCallbackIntent,0);

		BroadcastReceiver requestPinShortcutBroadcastReceiver = new BroadcastReceiver() {

			@Override
			@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION })
			public void onReceive(Context context, Intent intent) {
				AppShortcutsAppModule.get().getModuleAnalyticsSender().trackPinShortcut(shortcut.getId());
			}
		};
		AbstractApplication.get().registerReceiver(requestPinShortcutBroadcastReceiver, new IntentFilter(REQUEST_PIN_SHORTCUT_ACTION));

		return requestPinShortcut(shortcut, successCallback.getIntentSender());
	}

	public static Boolean requestPinShortcut(@NonNull ShortcutInfoCompat shortcut, @Nullable IntentSender callback) {
		return ShortcutManagerCompat.requestPinShortcut(AbstractApplication.get(), shortcut, callback);
	}
}
