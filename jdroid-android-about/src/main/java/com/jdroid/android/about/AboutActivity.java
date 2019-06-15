package com.jdroid.android.about;

import androidx.fragment.app.Fragment;

import com.jdroid.android.activity.FragmentContainerActivity;

public class AboutActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return AboutAppModule.Companion.get().getAboutContext().getAboutFragmentClass();
	}

}
