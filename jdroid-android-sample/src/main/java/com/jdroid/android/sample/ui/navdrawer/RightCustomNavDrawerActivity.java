package com.jdroid.android.sample.ui.navdrawer;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.sample.R;

public class RightCustomNavDrawerActivity extends FragmentContainerActivity {

	public int getContentView() {
		return R.layout.right_nav_fragment_container_activity;
	}

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return LeftCustomNavDrawerFragment.class;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return new RightSampleNavDrawer(activity, appBar);
	}
}
