package com.jdroid.android.navdrawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.concurrent.AppExecutors;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.concurrent.ExecutorUtils;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public abstract class NavDrawer {

	public static final String NAV_DRAWER_MANUALLY_USED = "navDrawerManuallyUsed";

	private static Boolean navDrawerManuallyUsed = false;

	private AbstractFragmentActivity activity;
	private Toolbar appBar;
	private Boolean isNavDrawerTopLevelView = false;
	private Boolean isNavDrawerOpenedOnFirstSession = true;

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private View contentView;

	public NavDrawer(final AbstractFragmentActivity activity, Toolbar appBar) {
		this.activity = activity;
		this.appBar = appBar;
		drawerLayout = activity.findView(R.id.drawer_layout);
	}

	public void init() {

		// Set the adapter for the list view
		// set a custom shadow that overlays the main content when the drawer opens
		drawerLayout.setDrawerShadow(R.drawable.jdroid_drawer_shadow, GravityCompat.START);

		final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {

			@Override
			public void onDrawerStateChanged(int newState) {
				if (newState == DrawerLayout.STATE_DRAGGING) {
					saveNavDrawerManuallyUsed();
				}
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// Do nothing
			}

			@Override
			public void onDrawerOpened(View view) {
				// Do nothing
			}

			@Override
			public void onDrawerClosed(View view) {
				// Do nothing
			}
		};

		if (isNavDrawerTopLevelView && appBar != null) {
			drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, appBar, R.string.jdroid_drawerOpen,
				R.string.jdroid_drawerClose) {

				@Override
				public void onDrawerStateChanged(int newState) {
					super.onDrawerStateChanged(newState);
					drawerListener.onDrawerStateChanged(newState);
				}

				@Override
				public void onDrawerSlide(View drawerView, float slideOffset) {
					// 0 offset to disable the hamburger animation
					super.onDrawerSlide(drawerView, 0);
					drawerListener.onDrawerSlide(drawerView, 0);
				}

				@Override
				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					drawerListener.onDrawerClosed(view);
				}

				@Override
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					drawerListener.onDrawerOpened(drawerView);
				}
			};
			// Set the drawer toggle as the DrawerListener
			drawerLayout.addDrawerListener(drawerToggle);
		} else {
			drawerLayout.removeDrawerListener(drawerListener);
		}

		contentView = createContentView();

		if (isNavDrawerOpenedOnFirstSession) {
			ExecutorUtils.INSTANCE.schedule(new Runnable() {

				@Override
				public void run() {
					navDrawerManuallyUsed = SharedPreferencesHelper.get().loadPreferenceAsBoolean(NAV_DRAWER_MANUALLY_USED, false);
					AppExecutors.getMainThreadExecutor().execute(new Runnable() {

						@Override
						public void run() {
							String action = activity.getIntent().getAction();
							if (!navDrawerManuallyUsed && Intent.ACTION_MAIN.equals(action)) {
								drawerLayout.openDrawer(contentView);
							}
						}
					});
				}
			}, 1L, TimeUnit.SECONDS);
		}
	}

	private void saveNavDrawerManuallyUsed() {
		if ((navDrawerManuallyUsed == null) || !navDrawerManuallyUsed) {
			AppExecutors.getDiskIOExecutor().execute(new Runnable() {

				@Override
				public void run() {
					SharedPreferencesHelper.get().savePreferenceAsync(NAV_DRAWER_MANUALLY_USED, true);
					navDrawerManuallyUsed = true;
				}
			});
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app icon touch event
		if ((drawerToggle != null) && drawerToggle.onOptionsItemSelected(item)) {
			saveNavDrawerManuallyUsed();
			return true;
		} else {
			return false;
		}
	}


	public void onPostCreate(Bundle savedInstanceState) {
		if (drawerToggle != null) {
			// Sync the toggle state after onRestoreInstanceState has occurred.
			drawerToggle.syncState();
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		if (drawerToggle != null) {
			drawerToggle.onConfigurationChanged(newConfig);
		}
	}

	public Boolean onBackPressed() {
		if (drawerLayout.isDrawerOpen(contentView)) {
			drawerLayout.closeDrawer(contentView);
			return true;
		}
		return false;
	}

	public DrawerLayout getDrawerLayout() {
		return drawerLayout;
	}

	public void onResume() {
		// Do Nothing
	}

	public abstract View createContentView();

	protected AbstractFragmentActivity getActivity() {
		return activity;
	}

	public void setIsNavDrawerTopLevelView(Boolean isNavDrawerTopLevelView) {
		this.isNavDrawerTopLevelView = isNavDrawerTopLevelView;
	}

	public void setIsNavDrawerOpenedOnFirstSession(Boolean isNavDrawerOpenedOnFirstSession) {
		this.isNavDrawerOpenedOnFirstSession = isNavDrawerOpenedOnFirstSession;
	}

}
