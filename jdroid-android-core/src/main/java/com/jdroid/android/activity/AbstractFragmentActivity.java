package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.loading.ActivityLoading;
import com.jdroid.android.navdrawer.NavDrawer;
import com.jdroid.android.uri.UriHandler;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Base {@link Activity}
 */
public abstract class AbstractFragmentActivity extends AppCompatActivity implements ActivityIf {

	private ActivityHelper activityHelper;

	public ActivityHelper getActivityHelper() {
		return activityHelper;
	}

	@Override
	public Boolean onBeforeSetContentView() {
		return activityHelper.onBeforeSetContentView();
	}

	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		activityHelper.onAfterSetContentView(savedInstanceState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Should we create a new instance on rotation?
		activityHelper = AbstractApplication.get().createActivityHelper(this);
		activityHelper.beforeOnCreate();
		super.onCreate(savedInstanceState);
		activityHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		activityHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		activityHelper.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		activityHelper.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityHelper.onResume();
	}

	@Override
	protected void onPause() {
		activityHelper.onBeforePause();
		super.onPause();
		activityHelper.onPause();
	}

	@Override
	protected void onStop() {
		activityHelper.onBeforeStop();
		super.onStop();
		activityHelper.onStop();
	}

	@Override
	protected void onDestroy() {
		activityHelper.onBeforeDestroy();
		super.onDestroy();
		activityHelper.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		activityHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return activityHelper.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		activityHelper.onPrepareOptionsMenu(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public Integer getMenuResourceId() {
		return activityHelper.getMenuResourceId();
	}

	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		activityHelper.doOnCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return activityHelper.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	@Override
	public <V extends View> V findView(int id) {
		return activityHelper.findView(id);
	}

	@Override
	public View inflate(int resource) {
		return activityHelper.inflate(resource);
	}

	@Override
	public <E> E getExtra(String key) {
		return activityHelper.getExtra(key);
	}

	public void loadUseCaseFragment(Bundle savedInstanceState, Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {

		if (savedInstanceState == null) {
			try {
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				fragmentTransaction.add(useCaseFragmentClass.newInstance(), useCaseFragmentClass.getSimpleName());
				fragmentTransaction.commit();
			} catch (InstantiationException e) {
				throw new UnexpectedException(e);
			} catch (IllegalAccessException e) {
				throw new UnexpectedException(e);
			}
		}
	}

	public void removeUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(getUseCaseUseCaseFragment(useCaseFragmentClass));
		fragmentTransaction.commit();
	}

	public UseCaseFragment<?> getUseCaseUseCaseFragment(Class<? extends UseCaseFragment<?>> useCaseFragmentClass) {
		return (UseCaseFragment<?>)getSupportFragmentManager().findFragmentByTag(useCaseFragmentClass.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	public <E extends Fragment> E getFragment(Class<E> fragmentClass) {
		return (E)getSupportFragmentManager().findFragmentByTag(fragmentClass.getSimpleName());
	}

	public void addFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerId, newFragment, newFragment.getClass().getSimpleName());
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.commit();
	}

	public void replaceFragment(Fragment newFragment, int containerId, boolean addToBackStack) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(containerId, newFragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(newFragment.getClass().getSimpleName());
		}
		fragmentTransaction.commit();
	}

	public <E extends Fragment> E instanceFragment(Class<E> fragmentClass, Bundle bundle) {
		E fragment;
		try {
			fragment = fragmentClass.newInstance();
		} catch (InstantiationException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	public void commitFragment(int containerViewId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerViewId, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public Boolean isLauncherActivity() {
		return activityHelper.isLauncherActivity();
	}

	@Override
	public Long getLocationFrequency() {
		return activityHelper.getLocationFrequency();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		activityHelper.onPostCreate(savedInstanceState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		activityHelper.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		activityHelper.onNewIntent(intent);
	}

	@Override
	public void executeOnUIThread(Runnable runnable) {
		activityHelper.executeOnUIThread(runnable);
	}

	@Override
	public Boolean isActivityDestroyed() {
		return activityHelper.isActivityDestroyed();
	}

	@Override
	public Boolean isGooglePlayServicesVerificationEnabled() {
		return activityHelper.isGooglePlayServicesVerificationEnabled();
	}

	// //////////////////////// Loading //////////////////////// //

	@Override
	public void showLoading() {
		activityHelper.showLoading();
	}

	@Override
	public void dismissLoading() {
		activityHelper.dismissLoading();
	}

	@NonNull
	@Override
	public ActivityLoading getDefaultLoading() {
		return activityHelper.getDefaultLoading();
	}

	@Override
	public void setLoading(ActivityLoading loading) {
		activityHelper.setLoading(loading);
	}

	@Override
	public void onBackPressed() {
		if (!onBackPressedHandled()) {
			super.onBackPressed();
		}
	}

	public Boolean onBackPressedHandled() {
		return activityHelper.onBackPressedHandled();
	}

	// //////////////////////// Navigation Drawer //////////////////////// //

	@Override
	public void initNavDrawer(Toolbar appBar) {
		activityHelper.initNavDrawer(appBar);
	}

	@Override
	public NavDrawer createNavDrawer(AbstractFragmentActivity activity, Toolbar appBar) {
		return activityHelper.createNavDrawer(activity, appBar);
	}

	@Override
	public Boolean isNavDrawerEnabled() {
		return activityHelper.isNavDrawerEnabled();
	}

	@Override
	public ActivityDelegate createActivityDelegate(AppModule appModule) {
		return activityHelper.createActivityDelegate(appModule);
	}

	@Override
	public ActivityDelegate getActivityDelegate(AppModule appModule) {
		return activityHelper.getActivityDelegate(appModule);
	}

	// //////////////////////// Uri, Dynamic Links & App Invites //////////////////////// //

	@Override
	public UriHandler createUriHandler() {
		return activityHelper.createUriHandler();
	}

	@Override
	public void onAppInvite(Uri deepLink, String invitationId) {
		activityHelper.onAppInvite(deepLink, invitationId);
	}

	@Override
	public GoogleApiClient getGoogleApiClient() {
		return activityHelper.getGoogleApiClient();
	}

	@Override
	public void setRequestedOrientation(int requestedOrientation) {
		try {
			super.setRequestedOrientation(requestedOrientation);
		} catch (IllegalStateException e) {
			activityHelper.catchRequestedOrientationIllegalStateException(e);
		}
	}

	@Override
	public AbstractFragmentActivity getActivity() {
		return this;
	}
}
