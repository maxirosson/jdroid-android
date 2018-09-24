package com.jdroid.android.sample.ui.uri;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.uri.UriHandler;

public class MatchNullIntentActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return CommonUriMapperFragment.class;
	}

	@Override
	public UriHandler createUriHandler() {
		return new MatchNullIntentActivityUriHandler();
	}
}