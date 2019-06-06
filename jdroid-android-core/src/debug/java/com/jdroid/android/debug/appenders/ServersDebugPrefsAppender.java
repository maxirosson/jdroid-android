package com.jdroid.android.debug.appenders;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceGroup;

import com.jdroid.android.R;
import com.jdroid.android.debug.PreferencesAppender;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.http.Server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import androidx.appcompat.app.AppCompatActivity;

public class ServersDebugPrefsAppender extends PreferencesAppender {

	private Map<Class<? extends Server>, List<? extends Server>> serversMap = Maps.newHashMap();

	public ServersDebugPrefsAppender(Map<Class<? extends Server>, List<? extends Server>> serversMap) {
		this.serversMap = serversMap;
	}

	@Override
	public int getNameResId() {
		return R.string.jdroid_serversSettings;
	}

	@Override
	public void initPreferences(AppCompatActivity activity, PreferenceGroup preferenceGroup) {
		for (final Entry<Class<? extends Server>, List<? extends Server>> entry : serversMap.entrySet()) {
			ListPreference preference = new ListPreference(activity);
			preference.setKey(entry.getKey().getSimpleName());
			preference.setTitle(entry.getKey().getSimpleName());
			preference.setDialogTitle(entry.getKey().getSimpleName());
			preference.setSummary(entry.getKey().getSimpleName());

			List<CharSequence> entries = Lists.newArrayList();
			for (Server each : entry.getValue()) {
				entries.add(each.getServerName());
			}
			preference.setEntries(entries.toArray(new CharSequence[0]));
			preference.setEntryValues(entries.toArray(new CharSequence[0]));
			preference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					for (Server each : entry.getValue()) {
						if (each.getServerName().equals(newValue)) {
							onServerPreferenceChange(each);
							break;
						}
					}
					return true;
				}
			});
			preferenceGroup.addPreference(preference);
		}
	}

	protected void onServerPreferenceChange(Server each) {
		// Do nothing
	}

	@Override
	public Boolean isEnabled() {
		return (serversMap != null) && !serversMap.isEmpty();
	}
}
