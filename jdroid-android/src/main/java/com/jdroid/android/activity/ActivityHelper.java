package com.jdroid.android.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.jdroid.android.analytics.AppLoadingSource;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.loading.DefaultBlockingLoading;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.concurrent.ExecutorUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Map;

public class ActivityHelper implements ActivityIf {
	
	private static final Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();
	private static final String TITLE_KEY = "title";
	
	private AbstractFragmentActivity activity;
	private Handler locationHandler;
	private boolean isDestroyed = false;
	
	private ActivityLoading loading;
	
	private String title;
	
	private NavDrawer navDrawer;

	private Dialog googlePlayServicesErrorDialog;

	private Map<AppModule, ActivityDelegate> activityDelegatesMap;

	private static Boolean firstAppLoad;
	private static Boolean isGooglePlayServicesAvailable;

	public ActivityHelper(AbstractFragmentActivity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return activity;
	}

	protected Activity getActivity() {
		return activity;
	}
	
	// //////////////////////// Layout //////////////////////// //

	@Override
	public int getContentView() {
		return getActivityIf().getContentView();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)activity.findViewById(id);
	}

	@Override
	public View inflate(int resource) {
		return LayoutInflater.from(activity).inflate(resource, null);
	}

	
	// //////////////////////// Life cycle //////////////////////// //

	@Override
	public Boolean onBeforeSetContentView() {
		return true;
	}

	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		// Do Nothing
	}

	public void beforeOnCreate() {
		// Do nothing
	}

	public void onCreate(Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		activityDelegatesMap = Maps.newHashMap();
		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			ActivityDelegate activityDelegate = getActivityIf().createActivityDelegate(appModule);
			if (activityDelegate != null) {
				activityDelegatesMap.put(appModule, activityDelegate);
			}
		}

		if (firstAppLoad == null) {
			firstAppLoad = true;
			UsageStats.incrementAppLoad();
		} else {
			firstAppLoad = false;
		}

		overrideStatusBarColor();

		AbstractApplication.get().initExceptionHandlers();

		UriHandler uriHandler = getActivityIf().getUriHandler();
		if (uriHandler != null) {
			AbstractApplication.get().getUriMapper().handleUri(activity, savedInstanceState, uriHandler);
		}

		if (getActivityIf().onBeforeSetContentView() && getContentView() != 0) {
			activity.setContentView(getContentView());
			getActivityIf().onAfterSetContentView(savedInstanceState);
		}

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onCreate(savedInstanceState);
		}

		if (savedInstanceState == null) {
			trackNotificationOpened(activity.getIntent());
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	protected void overrideStatusBarColor() {
		if (!AndroidUtils.isPreLollipop()) {
			activity.getWindow().setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
		}
	}

	public void onPostCreate(Bundle savedInstanceState) {
		if (navDrawer != null) {
			navDrawer.onPostCreate(savedInstanceState);
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		if (navDrawer != null) {
			navDrawer.onConfigurationChanged(newConfig);
		}
	}

	@Override
	public Boolean isLauncherActivity() {
		return false;
	}

	public void onSaveInstanceState(Bundle outState) {
		LOGGER.debug("Executing onSaveInstanceState on " + activity);
		dismissLoading();
		outState.putString(TITLE_KEY, title);
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		LOGGER.debug("Executing onRestoreInstanceState on " + activity);
		title = savedInstanceState.getString(TITLE_KEY);
	}

	@SuppressLint("HandlerLeak")
	public void onStart() {
		LOGGER.debug("Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		ExecutorUtils.execute(new Runnable() {

			@Override
			public void run() {
				AbstractApplication.get().saveInstallationSource();
				AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(activity.getIntent());
				AbstractApplication.get().getAnalyticsSender().onActivityStart(activity.getClass(), appLoadingSource,
						getOnActivityStartData());
			}
		});

		final Long locationFrequency = getActivityIf().getLocationFrequency();
		if (locationFrequency != null) {
			locationHandler = new Handler() {

				@Override
				@RequiresPermission(anyOf = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
				public void handleMessage(Message m) {
					LocationHelper.get().startLocalization();
					locationHandler.sendMessageDelayed(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE),
							locationFrequency);
				}
			};
			locationHandler.sendMessage(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE));
		}

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onStart();
		}
	}

	@Nullable
	protected Object getOnActivityStartData() {
		return null;
	}

	public void onResume() {

		LOGGER.debug("Executing onResume on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		if (getActivityIf().isGooglePlayServicesVerificationEnabled()) {
			if (googlePlayServicesErrorDialog != null) {
				googlePlayServicesErrorDialog.dismiss();
			}
			Boolean oldIsGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
			GooglePlayServicesUtils.GooglePlayServicesResponse googlePlayServicesResponse = GooglePlayServicesUtils.verifyGooglePlayServices(activity);
			googlePlayServicesErrorDialog = googlePlayServicesResponse.getDialog();
			isGooglePlayServicesAvailable = googlePlayServicesResponse.isAvailable();
			if (oldIsGooglePlayServicesAvailable != null && !oldIsGooglePlayServicesAvailable && isGooglePlayServicesAvailable) {
				for (AppModule appModule : AbstractApplication.get().getAppModules()) {
					appModule.onGooglePlayServicesUpdated();
				}
			}
		}

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onResume();
		}

		if (navDrawer != null) {
			navDrawer.onResume();
		}
	}

	public void onBeforePause() {
		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onBeforePause();
		}
	}


	public void onPause() {
		LOGGER.debug("Executing onPause on " + activity);

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onPause();
		}
	}

	public void onStop() {
		LOGGER.debug("Executing onStop on " + activity);

		UsageStats.setLastStopTime();
		ToastUtils.cancelCurrentToast();
		AbstractApplication.get().getAnalyticsSender().onActivityStop(activity);

		if (locationHandler != null) {
			locationHandler.removeCallbacksAndMessages(null);
		}

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onStop();
		}
	}

	public void onBeforeDestroy() {
		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onBeforeDestroy();
		}
	}

	public void onDestroy() {
		isDestroyed = true;
		LOGGER.debug("Executing onDestroy on " + activity);
		dismissLoading();

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onDestroy();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AbstractApplication.get().setCurrentActivity(activity);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		if (getActivityIf().getMenuResourceId() != null) {
			MenuInflater inflater = getActivityIf().getMenuInflater();
			inflater.inflate(getActivityIf().getMenuResourceId(), menu);
			getActivityIf().doOnCreateOptionsMenu(menu);
		}
		return true;
	}

	@Override
	public Integer getMenuResourceId() {
		return null;
	}

	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		// Do nothing
	}

	public void onPrepareOptionsMenu(Menu menu) {
		// Do nothing
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (navDrawer != null && navDrawer.onOptionsItemSelected(item)) {
			return true;
		} else if (item.getItemId() == android.R.id.home) {
			return onHomeOptionItemSelected();
		} else {
			return false;
		}
	}

	protected boolean onHomeOptionItemSelected() {
		Intent upIntent = getActivityIf().getUpIntent();
		if (upIntent == null) {
			upIntent = NavUtils.getParentActivityIntent(activity);
		}
		if (upIntent != null && NavUtils.shouldUpRecreateTask(activity, upIntent)) {
			// This activity is NOT part of this app's task, so create a new task
			// when navigating up, with a synthesized back stack.
			TaskStackBuilder builder = TaskStackBuilder.create(activity);
			// Add all of this activity's parents to the back stack
			builder.addNextIntentWithParentStack(upIntent);
			// Navigate up to the closest parent
			builder.startActivities();
		} else {
			// This activity is part of this app's task, so simply
			// navigate up to the logical parent activity.
			// NavUtils.navigateUpTo(activity, upIntent);
			if (upIntent != null) {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(upIntent);
				activity.finish();
			} else {
				ActivityLauncher.launchHomeActivity();
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getExtra(String key) {
		Bundle extras = activity.getIntent().getExtras();
		return extras != null ? (E)extras.get(key) : null;
	}

	public void onNewIntent(Intent intent) {
		LOGGER.debug("Executing onNewIntent on " + activity);

		trackNotificationOpened(intent);
	}

	private void trackNotificationOpened(Intent intent) {
		try {
			AppLoadingSource appLoadingSource = AppLoadingSource.getAppLoadingSource(intent);
			if (AppLoadingSource.NOTIFICATION.equals(appLoadingSource)) {
				String notificationName = intent.getStringExtra(NotificationBuilder.NOTIFICATION_NAME);
				if (notificationName != null) {
					AbstractApplication.get().getAnalyticsSender().trackNotificationOpened(notificationName);
				}
			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
	}

	@Override
	public Intent getUpIntent() {
		return null;
	}

	@Override
	public MenuInflater getMenuInflater() {
		return null;
	}

	@Override
	public Boolean isActivityDestroyed() {
		return isDestroyed;
	}

	@Override
	public Boolean onBackPressedHandled() {
		if (navDrawer != null) {
			return navDrawer.onBackPressed();
		}
		return false;
	}

	// //////////////////////// Delegates //////////////////////// //

	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		return appModule.createActivityDelegate(activity);
	}

	public ActivityDelegate getActivityDelegate(AppModule appModule) {
		return activityDelegatesMap.get(appModule);
	}

	// //////////////////////// Loading //////////////////////// //

	@Override
	public void showLoading() {
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading == null) {
					loading = getActivityIf().getDefaultLoading();
				}
				loading.show(getActivityIf());
			}
		});
	}

	@Override
	public void dismissLoading() {
		executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.dismiss(getActivityIf());
				}
			}
		});
	}

	@NonNull
	@Override
	public ActivityLoading getDefaultLoading() {
		return new DefaultBlockingLoading();
	}

	@Override
	public void setLoading(ActivityLoading loading) {
		this.loading = loading;
	}


	// //////////////////////// Navigation Drawer //////////////////////// //

	public void initNavDrawer(Toolbar appBar) {
		if (getActivityIf().isNavDrawerEnabled()) {
			navDrawer = getActivityIf().createNavDrawer(activity, appBar);
			navDrawer.init();
		}
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return false;
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return null;
	}

	// //////////////////////// Location //////////////////////// //

	@Override
	public Long getLocationFrequency() {
		return null;
	}

	// //////////////////////// Others //////////////////////// //

	@Override
	public void executeOnUIThread(Runnable runnable) {
		if (activity.equals(AbstractApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(runnable);
		}
	}

	@Override
	public UriHandler getUriHandler() {
		return null;
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return false;
	}
}
