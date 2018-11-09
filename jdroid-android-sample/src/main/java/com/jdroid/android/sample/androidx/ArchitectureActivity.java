package com.jdroid.android.sample.androidx;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.sample.database.room.RoomFragment;

import androidx.fragment.app.Fragment;

public class ArchitectureActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return ArchitectureFragment.class;
	}
}
