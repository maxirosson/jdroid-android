package com.jdroid.android.debug;

import android.os.Bundle;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.fragment.AbstractPreferenceFragment;
import com.jdroid.android.permission.PermissionHelper;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class PreferenceAppenderFragment extends AbstractPreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.jdroid_debug_preferences);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		PreferencesAppender preferencesAppender = getArgument(PreferenceAppenderActivity.APPENDER_EXTRA);
		getActivity().setTitle(preferencesAppender.getNameResId());

		String sharedPreferencesName = preferencesAppender.getSharedPreferencesName();
		if (sharedPreferencesName != null) {
			getPreferenceManager().setSharedPreferencesName(sharedPreferencesName);
		}

		List<PermissionHelper> permissionHelpers = Lists.newArrayList();
		for (String each : preferencesAppender.getRequiredPermissions()) {
			permissionHelpers.add(new PermissionHelper((FragmentActivity)getActivity(), each, 1));
		}
		preferencesAppender.initPreferences((AppCompatActivity)getActivity(), getPreferenceScreen());

		for (PermissionHelper each : permissionHelpers) {
			each.checkPermission(true);
		}
	}
}