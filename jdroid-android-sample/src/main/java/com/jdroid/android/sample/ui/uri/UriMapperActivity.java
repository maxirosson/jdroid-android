package com.jdroid.android.sample.ui.uri;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.uri.UriHandler;

public class UriMapperActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return UriMapperFragment.class;
	}

	@Override
	public UriHandler createUriHandler() {
		return new UriMapperUriHandler();
	}
}