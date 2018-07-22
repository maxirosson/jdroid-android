package com.jdroid.android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import com.google.android.gms.common.api.GoogleApiClient;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.uri.UriHandler;

public interface ActivityIf extends ComponentIf {

	// //////////////////////// Layout //////////////////////// //

	@LayoutRes
	public int getContentView();

	// //////////////////////// Life cycle //////////////////////// //
	
	public Boolean onBeforeSetContentView();
	
	public void onAfterSetContentView(Bundle savedInstanceState);

	public void doOnCreateOptionsMenu(Menu menu);
	
	public MenuInflater getMenuInflater();
	
	public Boolean isLauncherActivity();

	public Boolean isActivityDestroyed();

	// //////////////////////// Delegates //////////////////////// //

	public ActivityDelegate createActivityDelegate(AppModule appModule);

	public ActivityDelegate getActivityDelegate(AppModule appModule);

	// //////////////////////// Loading //////////////////////// //

	@NonNull
	public ActivityLoading getDefaultLoading();
	
	public void setLoading(ActivityLoading loading);
	
	// //////////////////////// Navigation Drawer //////////////////////// //

	public void initNavDrawer(Toolbar appBar);

	public Boolean isNavDrawerEnabled();

	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar);

	// //////////////////////// Location //////////////////////// //

	@Nullable
	public Long getLocationFrequency();

	// //////////////////////// Uri, Dynamic Links & App Invites //////////////////////// //

	@Nullable
	public UriHandler createUriHandler();

	public void onAppInvite(Uri deepLink, String invitationId);

	// //////////////////////// Others //////////////////////// //

	public Boolean isGooglePlayServicesVerificationEnabled();

	public GoogleApiClient getGoogleApiClient();
	
	public AbstractFragmentActivity getActivity();
}
