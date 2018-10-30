package com.jdroid.android.firebase.fcm;

import android.app.Activity;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;

import com.google.firebase.messaging.RemoteMessage;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FcmDebugPrefsAppender extends PreferencesAppender {

	private static Map<FcmMessage, Map<String, String>> fcmMessagesMap = Maps.newHashMap();

	@Override
	public int getNameResId() {
		return R.string.jdroid_fcmSettings;
	}

	@Override
	public void initPreferences(Activity activity, PreferenceGroup preferenceGroup) {
		ListPreference preference = new ListPreference(activity);
		preference.setTitle(R.string.jdroid_emulateFcmMessageTitle);
		preference.setDialogTitle(R.string.jdroid_emulateFcmMessageTitle);
		preference.setSummary(R.string.jdroid_emulateFcmMessageDescription);
		List<CharSequence> entries = Lists.newArrayList();
		for (FcmMessage entry : fcmMessagesMap.keySet()) {
			entries.add(entry.getClass().getSimpleName());
		}
		preference.setEntries(entries.toArray(new CharSequence[0]));
		preference.setEntryValues(entries.toArray(new CharSequence[0]));
		preference.setPersistent(false);
		preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {

				for (Entry<FcmMessage, Map<String, String>> entry : fcmMessagesMap.entrySet()) {
					if (entry.getKey().getClass().getSimpleName().equals(newValue.toString())) {
						RemoteMessage.Builder builder = new RemoteMessage.Builder("to");
						if (entry.getValue() != null) {
							for (Map.Entry<String, String> each : entry.getValue().entrySet()) {
								builder.addData(each.getKey(), each.getValue());
							}
						}
						entry.getKey().handle(builder.build());
						break;
					}
				}
				return false;
			}
		});
		preferenceGroup.addPreference(preference);

		Preference registerDevicePreference = new Preference(activity);
		registerDevicePreference.setTitle(R.string.jdroid_registerDeviceTitle);
		registerDevicePreference.setSummary(R.string.jdroid_registerDeviceTitle);
		registerDevicePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AbstractFcmAppModule.get().startFcmRegistration(true);
				return true;
			}
		});
		preferenceGroup.addPreference(registerDevicePreference);
	}

	@Override
	public Boolean isEnabled() {
		return (fcmMessagesMap != null) && !fcmMessagesMap.isEmpty();
	}

	public static void addFcmMessage(FcmMessage fcmMessage, Map<String, String> data) {
		fcmMessagesMap.put(fcmMessage, data);
	}
}
