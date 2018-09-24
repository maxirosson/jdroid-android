package com.jdroid.android.sample.ui.home;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;

import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.FragmentContainerActivity;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.uri.UriHandler;

public class HomeActivity extends FragmentContainerActivity {

	@Override
	protected Class<? extends Fragment> getFragmentClass() {
		return HomeFragment.class;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		NavDrawer navDrawer = super.createNavDrawer(activity, appBar);
		navDrawer.setIsNavDrawerTopLevelView(true);
		return navDrawer;
	}

	@Override
	public UriHandler createUriHandler() {
		return new HomeUriHandler();
	}
}
