package com.jdroid.android.sample.ui.google.maps;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GoogleMapsActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GoogleMapsFragment.class;
	}
}
