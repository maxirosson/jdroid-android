package com.jdroid.android.sample.ui;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

import com.jdroid.android.about.AboutActivity;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.firebase.invites.AppInviteHelper;
import com.jdroid.android.firebase.invites.AppInviteSender;
import com.jdroid.android.navdrawer.AbstractNavDrawerItem;
import com.jdroid.android.navdrawer.DefaultNavDrawer;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.navdrawer.NavDrawerHeader;
import com.jdroid.android.navdrawer.NavDrawerItem;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.java.collections.Lists;

import java.util.List;

public class AndroidActivityHelper extends ActivityHelper {

	public AndroidActivityHelper(AbstractFragmentActivity activity) {
		super(activity);
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return true;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return new DefaultNavDrawer(activity, appBar) {

			@Override
			protected List<NavDrawerItem> createNavDrawerItems() {
				List<NavDrawerItem> navDrawerItems = Lists.newArrayList();
				navDrawerItems.add(new AbstractNavDrawerItem(R.id.home, HomeActivity.class));
				navDrawerItems.add(new AbstractNavDrawerItem(R.id.about, AboutActivity.class));
				navDrawerItems.add(new AbstractNavDrawerItem(R.id.inviteFriends) {
					@Override
					public void startActivity(AbstractFragmentActivity currentActivity) {
						new AppInviteSender().sendInvitation();
					}
				});
				return navDrawerItems;
			}

			@Override
			protected void initNavDrawerHeader(NavDrawerHeader navDrawerHeader) {
				super.initNavDrawerHeader(navDrawerHeader);
				navDrawerHeader.setBackground(R.drawable.hero);
			}
		};
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		AppInviteHelper.onActivityResult(requestCode, resultCode, data);
	}
}
