package com.jdroid.android.sample.ui.google.inappbilling;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class GoogleInAppBillingActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return GoogleInAppBillingFragment.class;
	}
}