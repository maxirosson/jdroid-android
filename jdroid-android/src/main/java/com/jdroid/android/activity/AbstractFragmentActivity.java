package com.jdroid.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.R;
import com.jdroid.android.activity.BaseActivity.UseCaseTrigger;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.domain.User;
import com.jdroid.android.fragment.UseCaseFragment;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.java.exception.UnexpectedException;

/**
 * Base {@link Activity}
 * 
 */
public abstract class AbstractFragmentActivity extends SherlockFragmentActivity implements ActivityIf {
	
	private BaseActivity baseActivity;
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAndroidApplicationContext()
	 */
	@Override
	public DefaultApplicationContext getAndroidApplicationContext() {
		return baseActivity.getAndroidApplicationContext();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		throw new IllegalArgumentException();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onBeforeSetContentView()
	 */
	@Override
	public Boolean onBeforeSetContentView() {
		return baseActivity.onBeforeSetContentView();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#onAfterSetContentView(android.os.Bundle)
	 */
	@Override
	public void onAfterSetContentView(Bundle savedInstanceState) {
		baseActivity.onAfterSetContentView(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		baseActivity = AbstractApplication.get().createBaseActivity(this);
		baseActivity.beforeOnCreate();
		super.onCreate(savedInstanceState);
		baseActivity.onCreate(savedInstanceState);
	}
	
	/**
	 * @see android.app.Activity#onContentChanged()
	 */
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		baseActivity.onContentChanged();
	}
	
	/**
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		baseActivity.onSaveInstanceState(outState);
	}
	
	/**
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		baseActivity.onRestoreInstanceState(savedInstanceState);
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		baseActivity.onStart();
	}
	
	/**
	 * @see roboguice.activity.GuiceActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		baseActivity.onResume();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		baseActivity.onPause();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		baseActivity.onStop();
	}
	
	/**
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		baseActivity.onDestroy();
	}
	
	/**
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		baseActivity.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return baseActivity.onCreateOptionsMenu(menu);
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public int getMenuResourceId() {
		return baseActivity.getMenuResourceId();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(android.view.Menu menu) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(com.actionbarsherlock.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		baseActivity.doOnCreateOptionsMenu(menu);
	}
	
	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// REVIEW See if this is the correct approach
		return baseActivity.onOptionsItemSelected(item) ? true : super.onOptionsItemSelected(item);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findView(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V)findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findViewOnActivity(int id) {
		return (V)findViewById(id);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		baseActivity.showLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoading(LoadingDialogBuilder builder) {
		baseActivity.showLoading(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		baseActivity.showLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoadingOnUIThread(LoadingDialogBuilder builder) {
		baseActivity.showLoadingOnUIThread(builder);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoading()
	 */
	@Override
	public void dismissLoading() {
		baseActivity.dismissLoading();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#dismissLoadingOnUIThread()
	 */
	@Override
	public void dismissLoadingOnUIThread() {
		baseActivity.dismissLoadingOnUIThread();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeOnUIThread(java.lang.Runnable)
	 */
	@Override
	public void executeOnUIThread(Runnable runnable) {
		baseActivity.executeOnUIThread(runnable);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#inflate(int)
	 */
	@Override
	public View inflate(int resource) {
		return baseActivity.inflate(resource);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getInstance(java.lang.Class)
	 */
	@Override
	public <I> I getInstance(Class<I> clazz) {
		return baseActivity.getInstance(clazz);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getExtra(java.lang.String)
	 */
	@Override
	public <E> E getExtra(String key) {
		return baseActivity.<E>getExtra(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return baseActivity.<E>getArgument(key);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return baseActivity.<E>getArgument(key, defaultValue);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		baseActivity.onResumeUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener,
	 *      com.jdroid.android.activity.BaseActivity.UseCaseTrigger)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener,
			UseCaseTrigger useCaseTrigger) {
		baseActivity.onResumeUseCase(useCase, listener, useCaseTrigger);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onPauseUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onPauseUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		baseActivity.onPauseUseCase(useCase, listener);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase) {
		baseActivity.executeUseCase(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase,
	 *      java.lang.Long)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds) {
		baseActivity.executeUseCase(useCase, delaySeconds);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onStartUseCase()
	 */
	@Override
	public void onStartUseCase() {
		baseActivity.onStartUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onUpdateUseCase()
	 */
	@Override
	public void onUpdateUseCase() {
		baseActivity.onUpdateUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishUseCase()
	 */
	@Override
	public void onFinishUseCase() {
		baseActivity.onFinishUseCase();
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishFailedUseCase(java.lang.RuntimeException)
	 */
	@Override
	public void onFinishFailedUseCase(RuntimeException runtimeException) {
		baseActivity.onFinishFailedUseCase(runtimeException);
	}
	
	/**
	 * @see com.jdroid.android.usecase.listener.DefaultUseCaseListener#onFinishCanceledUseCase()
	 */
	@Override
	public void onFinishCanceledUseCase() {
		baseActivity.onFinishCanceledUseCase();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError()
	 */
	@Override
	public Boolean goBackOnError() {
		return baseActivity.goBackOnError();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#requiresAuthentication()
	 */
	@Override
	public Boolean requiresAuthentication() {
		return baseActivity.requiresAuthentication();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getUser()
	 */
	@Override
	public User getUser() {
		return baseActivity.getUser();
	}
	
	public Boolean isAuthenticated() {
		return baseActivity.isAuthenticated();
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
	
	public <E extends Fragment> E instanceFragment(Class<E> fragmentClass, Bundle bundle) {
		E fragment = null;
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
	
	public void commitFragment(Fragment fragment) {
		commitFragment(R.id.fragmentContainer, fragment);
	}
	
	public void commitFragment(int containerViewId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.add(containerViewId, fragment);
		fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fragmentTransaction.commit();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return baseActivity.getAdSize();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return baseActivity.isLauncherActivity();
	}
}
