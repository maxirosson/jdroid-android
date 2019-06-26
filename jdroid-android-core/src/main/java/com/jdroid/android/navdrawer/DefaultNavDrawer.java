package com.jdroid.android.navdrawer;

import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.images.loader.ImageViewLoader;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public abstract class DefaultNavDrawer extends NavDrawer {

	private NavigationView navigationView;
	private List<NavDrawerItem> navDrawerItems;

	public DefaultNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		super(activity, appBar);

		navDrawerItems = createNavDrawerItems();
	}

	@Override
	public View createContentView() {
		navigationView = getActivity().findView(R.id.drawer);
		navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
					DefaultNavDrawer.this.onNavigationItemSelected(menuItem);
					menuItem.setChecked(true);
					getDrawerLayout().closeDrawers();
					return true;
				}
			});
		NavDrawerHeader navDrawerHeader = new NavDrawerHeader(navigationView);
		initNavDrawerHeader(navDrawerHeader);
		return navigationView;
	}

	protected void onNavigationItemSelected(MenuItem menuItem) {
		findNavDrawerByMenu(menuItem).startActivity(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();

		checkMenuItem();
	}

	protected void checkMenuItem() {
		for (int i = 0; i < navigationView.getMenu().size(); i++) {
			MenuItem menuItem = navigationView.getMenu().getItem(i);
			NavDrawerItem navDrawerItem = findNavDrawerByMenu(menuItem);
			menuItem.setChecked(navDrawerItem.matchesActivity(getActivity()));
		}
	}

	private NavDrawerItem findNavDrawerByMenu(MenuItem menuItem) {
		for (NavDrawerItem each : navDrawerItems) {
			if (each.getItemId() == menuItem.getItemId()) {
				return each;
			}
		}
		return null;
	}

	protected abstract List<NavDrawerItem> createNavDrawerItems();

	protected void initNavDrawerHeader(NavDrawerHeader navDrawerHeader) {
		User user = SecurityContext.get().getUser();
		if (user != null) {
			String coverPictureUrl = user.getCoverPictureUrl();
			String profilePictureUrl = user.getProfilePictureUrl();
			if (coverPictureUrl != null || profilePictureUrl != null) {
				ImageViewLoader imageViewLoader = createImageViewLoader();
				if (imageViewLoader != null) {
					navDrawerHeader.setBackground(coverPictureUrl, User.PROFILE_PICTURE_TTL, imageViewLoader);
					navDrawerHeader.setMainImage(profilePictureUrl, User.PROFILE_PICTURE_TTL, imageViewLoader);
				} else {
					AbstractApplication.get().getExceptionHandler().logWarningException("Not imageViewLoader defined to load nav drawer images");
				}
			}
			navDrawerHeader.setTitle(user.getFullname());
			navDrawerHeader.setSubTitle(user.getEmail());
		} else {
			navDrawerHeader.setMainImage(AbstractApplication.get().getLauncherIconResId());
			navDrawerHeader.setTitle(getActivity().getString(R.string.jdroid_appName));
			String website = AbstractApplication.get().getAppContext().getWebsite();
			if (website != null) {
				website = website.replaceAll("http://", "");
				website = website.replaceAll("https://", "");
				navDrawerHeader.setSubTitle(website);
			}
		}
	}

	protected ImageViewLoader createImageViewLoader() {
		return null;
	}
}
