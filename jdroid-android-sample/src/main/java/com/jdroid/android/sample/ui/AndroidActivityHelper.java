package com.jdroid.android.sample.ui;

import com.jdroid.android.about.AboutActivity;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.navdrawer.DefaultNavDrawerItem;
import com.jdroid.android.navdrawer.DefaultNavDrawer;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.navdrawer.NavDrawerHeader;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.java.collections.Lists;

import java.util.List;

import androidx.appcompat.widget.Toolbar;

public class AndroidActivityHelper extends ActivityHelper {

	public AndroidActivityHelper(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public boolean isNavDrawerEnabled() {
		return true;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return new DefaultNavDrawer(activity, appBar) {

			@Override
			protected List<NavDrawerItem> createNavDrawerItems() {
				List<NavDrawerItem> navDrawerItems = Lists.newArrayList();
				navDrawerItems.add(new DefaultNavDrawerItem(R.id.home, HomeActivity.class));
				navDrawerItems.add(new DefaultNavDrawerItem(R.id.about, AboutActivity.class));
				return navDrawerItems;
			}

			@Override
			protected void initNavDrawerHeader(NavDrawerHeader navDrawerHeader) {
				super.initNavDrawerHeader(navDrawerHeader);
				navDrawerHeader.setBackground(R.drawable.hero);
			}
		};
	}
}
