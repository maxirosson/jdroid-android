package com.jdroid.android.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.appindexing.Action;
import com.google.firebase.appindexing.FirebaseUserActions;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.google.GooglePlayServicesUtils;
import com.jdroid.android.google.SafeResultCallback;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.loading.DefaultBlockingLoading;
import com.jdroid.android.location.LocationHelper;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.notification.NotificationBuilder;
import com.jdroid.android.uri.ReferrerUtils;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.android.uri.UriUtils;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.android.utils.AppUtils;
import com.jdroid.android.utils.ToastUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Set;

public class ActivityHelper implements ActivityIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);

	private static final String REFERRER = "referrer";
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();

	private AbstractFragmentActivity activity;
	private Handler locationHandler;
	private boolean isDestroyed = false;
	
	private ActivityLoading loading;
	
	private NavDrawer navDrawer;

	private Map<AppModule, ActivityDelegate> activityDelegatesMap;

	private GoogleApiClient googleApiClient;
	private Action appIndexingAction;

	private static Boolean firstActivityCreate;
	private static Boolean isGooglePlayServicesAvailable;
	private static Boolean isGooglePlayServicesDialogDisplayed = false;

	private String referrer;
	private UriHandler uriHandler;
	private Boolean uriHandled = false;

	public ActivityHelper(AbstractFragmentActivity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return activity;
	}

	protected AbstractFragmentActivity getActivity() {
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

	public void onCreate(final Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		AbstractApplication.get().getCoreAnalyticsSender().onActivityCreate(activity, savedInstanceState);

		verifyGooglePlayServicesAvailability();

		activityDelegatesMap = Maps.newHashMap();
		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			ActivityDelegate activityDelegate = getActivityIf().createActivityDelegate(appModule);
			if (activityDelegate != null) {
				activityDelegatesMap.put(appModule, activityDelegate);
			}
		}

		if (firstActivityCreate == null) {
			firstActivityCreate = true;
			UsageStats.incrementAppLoadAsync();
			AbstractApplication.get().getCoreAnalyticsSender().onFirstActivityCreate(activity);
		} else {
			firstActivityCreate = false;
		}

		overrideStatusBarColor();

		AbstractApplication.get().initExceptionHandlers();

		initGoogleApiClient();

		if (savedInstanceState == null) {
			uriHandler = getActivityIf().createUriHandler();
			uriHandled = AbstractApplication.get().getUriMapper().handleUri(activity, activity.getIntent(), uriHandler, true);
			referrer = ReferrerUtils.getReferrerCategory(activity);
			if (googleApiClient != null && getActivityIf().isAppInviteEnabled() && ((uriHandled && !UriUtils.isInternalReferrerCategory(referrer)) || isHomeActivity())) {
				PendingResult<AppInviteInvitationResult> pendingResult = AppInvite.AppInviteApi.getInvitation(googleApiClient, getActivity(), false);
				pendingResult.setResultCallback(new SafeResultCallback<AppInviteInvitationResult>() {

					@Override
					public void onSuccessResult(@NonNull AppInviteInvitationResult result) {
						String deepLink = AppInviteReferral.getDeepLink(result.getInvitationIntent());
						LOGGER.debug("AppInvite invitation deep link: " + deepLink);

						String invitationId = AppInviteReferral.getInvitationId(result.getInvitationIntent());
						LOGGER.debug("AppInvite invitation id: " + invitationId);

						getActivityIf().onAppInvite(deepLink, invitationId);

						if (!uriHandled && isHomeActivity()) {
							if (uriHandler != null && deepLink.equals(uriHandler.getUrl(activity))) {
								LOGGER.debug("Skipping reopening invitation deepLink");
							} else {
								Intent targetIntent = new Intent();
								targetIntent.setData(Uri.parse(deepLink));
								targetIntent.setPackage(AppUtils.getApplicationId());
								ReferrerUtils.setReferrer(targetIntent, ActivityCompat.getReferrer(activity));
								activity.finish();
								activity.startActivity(targetIntent);
							}
						}
					}

					@Override
					public void onFailedResult(@NonNull AppInviteInvitationResult result) {
						LOGGER.debug("AppInvite invitation not found. Status code: " + result.getStatus().getStatusCode());
					}
				});
			}
		} else {
			referrer = (String)savedInstanceState.getSerializable(REFERRER);
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

	@Override
	public Boolean isAppInviteEnabled() {
		return true;
	}

	private Boolean isHomeActivity() {
		return AbstractApplication.get().getHomeActivityClass().equals(getActivity().getClass());
	}

	private void initGoogleApiClient() {
		if (isGooglePlayServicesAvailable) {
			Set<Api<? extends Api.ApiOptions.NotRequiredOptions>> googleApis = Sets.newHashSet();
			if (getActivityIf().isAppInviteEnabled()) {
				googleApis.add(AppInvite.API);
			}
			if (getActivityIf().isLocationServicesEnabled()) {
				googleApis.add(LocationServices.API);
			}
			googleApis.addAll(getCustomGoogleApis());
			if (!googleApis.isEmpty()) {
				GoogleApiClient.Builder builder = new GoogleApiClient.Builder(activity);
				for(Api<? extends Api.ApiOptions.NotRequiredOptions> api : googleApis) {
					builder.addApi(api);
				}
				builder.enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
					@Override
					public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
						AbstractApplication.get().getExceptionHandler().logHandledException("Error when connecting to google api client: " + connectionResult.getErrorMessage());
					}
				});
				onInitGoogleApiClientBuilder(builder);
				googleApiClient = builder.build();
			}
		}
	}

	protected Set<Api<? extends Api.ApiOptions.NotRequiredOptions>> getCustomGoogleApis() {
		return Sets.newHashSet();
	}

	protected void onInitGoogleApiClientBuilder(GoogleApiClient.Builder builder) {
		// Do nothing
	}

	@Override
	public void onAppInvite(String deepLink, String invitationId) {
		// Do Nothing
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
		outState.putSerializable(REFERRER, referrer);
		dismissLoading();
	}

	public void onRestoreInstanceState(Bundle savedInstanceState) {
		LOGGER.debug("Executing onRestoreInstanceState on " + activity);
	}

	@SuppressLint("HandlerLeak")
	public void onStart() {
		LOGGER.debug("Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);

		AbstractApplication.get().getCoreAnalyticsSender().onActivityStart(activity, referrer, getOnActivityStartData());

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

		if (uriHandler != null && isGooglePlayServicesAvailable) {
			if (appIndexingAction == null) {
				appIndexingAction = uriHandler.getAppIndexingAction(activity);
			}
			if (appIndexingAction != null) {
				FirebaseUserActions.getInstance().start(appIndexingAction);
			}
		}
	}

	@Nullable
	protected Object getOnActivityStartData() {
		return null;
	}

	public void onResume() {

		LOGGER.debug("Executing onResume on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		AbstractApplication.get().getCoreAnalyticsSender().onActivityResume(activity);
		
		verifyGooglePlayServicesAvailability(getActivityIf().isGooglePlayServicesVerificationEnabled());

		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onResume();
		}

		if (navDrawer != null) {
			navDrawer.onResume();
		}
	}

	private void verifyGooglePlayServicesAvailability() {
		verifyGooglePlayServicesAvailability(false);
	}

	private void verifyGooglePlayServicesAvailability(Boolean displayDialog) {
		Boolean oldIsGooglePlayServicesAvailable = isGooglePlayServicesAvailable;
		isGooglePlayServicesAvailable = displayDialog && !isGooglePlayServicesDialogDisplayed ? GooglePlayServicesUtils.verifyGooglePlayServices(activity).isAvailable() : GooglePlayServicesUtils.isGooglePlayServicesAvailable(activity);
		if (!isGooglePlayServicesAvailable && displayDialog) {
			isGooglePlayServicesDialogDisplayed = true;
		}
		if (oldIsGooglePlayServicesAvailable != null && !oldIsGooglePlayServicesAvailable && isGooglePlayServicesAvailable) {
			LOGGER.info("Google Play Services updated");
			for (AppModule appModule : AbstractApplication.get().getAppModules()) {
				appModule.onGooglePlayServicesUpdated();
			}
		}
	}

	public void onBeforePause() {
		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onBeforePause();
		}
	}


	public void onPause() {
		LOGGER.debug("Executing onPause on " + activity);
		
		AbstractApplication.get().getCoreAnalyticsSender().onActivityPause(activity);
		
		for (ActivityDelegate each : activityDelegatesMap.values()) {
			each.onPause();
		}
	}

	public void onBeforeStop() {
		if (appIndexingAction != null && isGooglePlayServicesAvailable) {
			FirebaseUserActions.getInstance().end(appIndexingAction);
		}
	}

	public void onStop() {
		LOGGER.debug("Executing onStop on " + activity);

		UsageStats.setLastStopTime();
		ToastUtils.cancelCurrentToast();
		AbstractApplication.get().getCoreAnalyticsSender().onActivityStop(activity);

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
		AbstractApplication.get().getCoreAnalyticsSender().onActivityDestroy(activity);
		
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
		Intent upIntent = getParentActivityIntent();
		if (activity.shouldUpRecreateTask(upIntent)) {
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
			activity.startActivity(upIntent);
			activity.finish();
		}
		return true;
	}

	@NonNull
	private Intent getParentActivityIntent() {
		Intent intent = activity.getParentActivityIntent();
		if (intent == null) {
			intent = new Intent(getActivity(), AbstractApplication.get().getHomeActivityClass());
		}
		intent.addFlags(getParentActivityIntentFlags());
		return intent;
	}

	protected int getParentActivityIntentFlags() {
		return Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getExtra(String key) {
		Bundle extras = activity.getIntent().getExtras();
		return extras != null ? (E)extras.get(key) : null;
	}

	public void onNewIntent(Intent intent) {
		LOGGER.debug("Executing onNewIntent on " + activity);

		if (uriHandler != null) {
			uriHandled = AbstractApplication.get().getUriMapper().handleUri(activity, intent, uriHandler, false);
			if (!uriHandled) {
				activity.setIntent(intent);
			}
		} else {
			activity.setIntent(intent);
		}

		referrer = ReferrerUtils.getReferrerCategory(activity);

		trackNotificationOpened(intent);
	}

	private void trackNotificationOpened(Intent intent) {
		try {
			if (NotificationBuilder.generateNotificationsReferrer().equals(referrer)) {
				String notificationName = intent.getStringExtra(NotificationBuilder.NOTIFICATION_NAME);
				if (notificationName != null) {
					AbstractApplication.get().getCoreAnalyticsSender().trackNotificationOpened(notificationName);
				}
			}
		} catch (Exception e) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		}
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

	@Override
	public Boolean isLocationServicesEnabled() {
		return false;
	}

	// //////////////////////// Others //////////////////////// //

	@Override
	public void executeOnUIThread(Runnable runnable) {
		if (activity.equals(AbstractApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(runnable);
		}
	}

	@Override
	public UriHandler createUriHandler() {
		return null;
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return false;
	}

	@Nullable
	public GoogleApiClient getGoogleApiClient() {
		return googleApiClient;
	}
	
	public void catchRequestedOrientationIllegalStateException(IllegalStateException e) {
		// This is to catch a validation added on android 8.0 (and removed on android 8.1)
		// This is to catch a validation added on android 8.0 (and removed on android 8.1)
		if ("Only fullscreen activities can request orientation".equals(e.getMessage())) {
			AbstractApplication.get().getExceptionHandler().logHandledException(e);
		} else {
			throw e;
		}
	}
}
