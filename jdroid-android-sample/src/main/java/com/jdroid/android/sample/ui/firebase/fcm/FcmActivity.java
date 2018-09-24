package com.jdroid.android.sample.ui.firebase.fcm;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class FcmActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return FcmFragment.class;
	}
}