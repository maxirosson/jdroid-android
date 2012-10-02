package com.jdroid.android.activity;

import java.util.Map;
import roboguice.RoboGuice;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdSize;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.inject.Key;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.dialog.LoadingDialog;
import com.jdroid.android.domain.User;
import com.jdroid.android.intent.LogoutIntent;
import com.jdroid.android.usecase.DefaultUseCase;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.utils.ExecutorUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class BaseActivity implements ActivityIf {
	
	private final static String TAG = BaseActivity.class.getSimpleName();
	
	private Activity activity;
	private LoadingDialog loadingDialog;
	private BroadcastReceiver logoutBroadcastReceiver;
	private Map<Key<?>, Object> scopedObjects = Maps.newHashMap();
	
	/**
	 * @param activity
	 */
	public BaseActivity(Activity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return (ActivityIf)activity;
	}
	
	protected Activity getActivity() {
		return activity;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getContentView()
	 */
	@Override
	public int getContentView() {
		return getActivityIf().getContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		// Do Nothing
	}
	
	public void beforeOnCreate() {
		RoboGuice.getInjector(activity).injectMembersWithoutViews(activity);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ActionBar actionBar = getActivityIf().getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeButtonEnabled(true);
		}
		
		if (getActivityIf().onBeforeSetContentView()) {
			if (getContentView() != 0) {
				activity.setContentView(getContentView());
				getActivityIf().onAfterSetContentView(savedInstanceState);
			}
			if ((AndroidUtils.getApiLevel() < 11) && getActivityIf().requiresAuthentication()) {
				logoutBroadcastReceiver = new BroadcastReceiver() {
					
					@Override
					public void onReceive(Context context, Intent intent) {
						activity.finish();
					}
				};
				activity.registerReceiver(logoutBroadcastReceiver, LogoutIntent.newIntentFilter());
			}
		}
	}
	
	public void onContentChanged() {
		RoboGuice.getInjector(activity).injectViewMembers(activity);
	}
	
	/**
	 * @see roboguice.util.ScopedObjectMapProvider#getScopedObjectMap()
	 */
	@Override
	public Map<Key<?>, Object> getScopedObjectMap() {
		return scopedObjects;
	}
	
	public void onSaveInstanceState(Bundle outState) {
		Log.v(TAG, "Executing onSaveInstanceState on " + activity);
		dismissLoading();
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.v(TAG, "Executing onRestoreInstanceState on " + activity);
	}
	
	public void onStart() {
		Log.v(TAG, "Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		if (AbstractApplication.get().getAndroidApplicationContext().isAnalyticsEnabled()) {
			EasyTracker.getInstance().activityStart(activity);
		}
	}
	
	public void onResume() {
		Log.v(TAG, "Executing onResume on " + activity);
		AbstractApplication.get().setInBackground(false);
		AbstractApplication.get().setCurrentActivity(activity);
	}
	
	public void onPause() {
		Log.v(TAG, "Executing onPause on " + activity);
		AbstractApplication.get().setInBackground(true);
	}
	
	public void onStop() {
		Log.v(TAG, "Executing onStop on " + activity);
		if (AbstractApplication.get().getAndroidApplicationContext().isAnalyticsEnabled()) {
			EasyTracker.getInstance().activityStop(activity);
		}
	}
	
	public void onDestroy() {
		Log.v(TAG, "Executing onDestroy on " + activity);
		if (logoutBroadcastReceiver != null) {
			activity.unregisterReceiver(logoutBroadcastReceiver);
		}
		dismissLoading();
		RoboGuice.destroyInjector(activity);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		AbstractApplication.get().setCurrentActivity(activity);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getActivityIf().getMenuResourceId() != 0) {
			MenuInflater inflater = getActivityIf().getSupportMenuInflater();
			inflater.inflate(getActivityIf().getMenuResourceId(), menu);
			getActivityIf().doOnCreateOptionsMenu(menu);
		}
		return true;
	}
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		if (getActivityIf().getMenuResourceId() != 0) {
			android.view.MenuInflater inflater = activity.getMenuInflater();
			inflater.inflate(getActivityIf().getMenuResourceId(), menu);
			getActivityIf().doOnCreateOptionsMenu(menu);
		}
		return true;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		return 0;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		// Do Nothing by Default
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(android.view.Menu menu) {
		// Do Nothing by Default
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				ActivityLauncher.launchHomeActivity();
				return true;
			default:
				return false;
		}
	}
	
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				ActivityLauncher.launchHomeActivity();
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		showLoadingOnUIThread(AbstractApplication.get().isLoadingCancelable());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(Integer loadingResId) {
		showLoadingOnUIThread(AbstractApplication.get().isLoadingCancelable(), loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean)
	 */
	@Override
	public void showLoadingOnUIThread(Boolean cancelable) {
		showLoadingOnUIThread(cancelable, null);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoadingOnUIThread(final Boolean cancelable, final Integer loadingResId) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showLoading(cancelable, loadingResId);
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		showLoading(AbstractApplication.get().isLoadingCancelable());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Integer)
	 */
	@Override
	public void showLoading(Integer loadingResId) {
		showLoading(AbstractApplication.get().isLoadingCancelable(), loadingResId);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean)
	 */
	@Override
	public void showLoading(Boolean cancelable) {
		showLoading(cancelable, null);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(java.lang.Boolean, java.lang.Integer)
	 */
	@Override
	public void showLoading(Boolean cancelable, Integer loadingResId) {
		if ((loadingDialog == null) || (!loadingDialog.isShowing())) {
			loadingDialog = new LoadingDialog(activity);
			loadingDialog.setCancelable(cancelable);
			if (loadingResId != null) {
				loadingDialog.setLoadingText(loadingResId);
			}
			loadingDialog.show();
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				dismissLoading();
			}
		});
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		if (activity.equals(AbstractApplication.get().getCurrentActivity())) {
			activity.runOnUiThread(runnable);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return AbstractApplication.getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <E> E getExtra(String key) {
		Bundle extras = activity.getIntent().getExtras();
		return extras != null ? (E)extras.get(key) : null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.DefaultUseCase)
	 */
	@Override
	public void executeUseCase(DefaultUseCase<?> useCase) {
		ExecutorUtils.execute(useCase);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		getActivityIf().showLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		getActivityIf().dismissLoadingOnUIThread();
		throw runtimeException;
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		// Do nothing by default
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)activity.findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return LayoutInflater.from(activity).inflate(resource, null);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return true;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return SecurityContext.get().getUser();
	}
	
	public Boolean isAuthenticated() {
		return SecurityContext.get().isAuthenticated();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getSupportMenuInflater()
	 */
	@Override
	public MenuInflater getSupportMenuInflater() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getSupportActionBar()
	 */
	@Override
	public ActionBar getSupportActionBar() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return null;
	}
}
