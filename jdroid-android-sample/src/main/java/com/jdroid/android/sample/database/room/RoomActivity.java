package com.jdroid.android.sample.database.room;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.sample.ui.analytics.AnalyticsFragment;

import androidx.fragment.app.Fragment;

public class RoomActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return RoomFragment.class;
	}
}
