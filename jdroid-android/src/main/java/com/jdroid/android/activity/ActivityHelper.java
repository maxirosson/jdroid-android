package com.jdroid.android.activity;

import org.slf4j.Logger;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.ads.AdSize;
import com.jdroid.android.AbstractApplication;
import com.jdroid.android.ActivityLauncher;
import com.jdroid.android.R;
import com.jdroid.android.ad.AdLoader;
import com.jdroid.android.context.DefaultApplicationContext;
import com.jdroid.android.context.SecurityContext;
import com.jdroid.android.debug.DebugSettingsActivity;
import com.jdroid.android.domain.User;
import com.jdroid.android.exception.DefaultExceptionHandler;
import com.jdroid.android.gps.LocalizationManager;
import com.jdroid.android.intent.ClearTaskIntent;
import com.jdroid.android.loading.DefaultLoadingDialogBuilder;
import com.jdroid.android.loading.LoadingDialogBuilder;
import com.jdroid.android.usecase.DefaultAbstractUseCase;
import com.jdroid.android.usecase.UseCase;
import com.jdroid.android.usecase.listener.DefaultUseCaseListener;
import com.jdroid.android.utils.AndroidUtils;
import com.jdroid.java.utils.ExecutorUtils;
import com.jdroid.java.utils.IdGenerator;
import com.jdroid.java.utils.LoggerUtils;

/**
 * 
 * @author Maxi Rosson
 */
public class ActivityHelper implements ActivityIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(ActivityHelper.class);
	
	private static final int LOCATION_UPDATE_TIMER_CODE = IdGenerator.getIntId();
	
	private Activity activity;
	protected Dialog loadingDialog;
	private Handler locationHandler;
	private BroadcastReceiver clearTaskBroadcastReceiver;
	
	/**
	 * @param activity
	 */
	public ActivityHelper(Activity activity) {
		this.activity = activity;
	}
	
	public ActivityIf getActivityIf() {
		return (ActivityIf)activity;
	}
	
	protected Activity getActivity() {
		return activity;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAndroidApplicationContext()
	 */
	@Override
	public DefaultApplicationContext getAndroidApplicationContext() {
		return AbstractApplication.get().getAndroidApplicationContext();
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#shouldRetainInstance()
	 */
	@Override
	public Boolean shouldRetainInstance() {
		throw new IllegalArgumentException();
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
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void onCreate(Bundle savedInstanceState) {
		LOGGER.trace("Executing onCreate on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		
		AbstractApplication.get().initExceptionHandlers();
		
		// Action bar
		ActionBar actionBar = getActivityIf().getActionBar();
		if (actionBar != null) {
			
			if (AndroidUtils.getApiLevel() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				actionBar.setHomeButtonEnabled(true);
			}
			if (!getActivityIf().isLauncherActivity()) {
				actionBar.setDisplayHomeAsUpEnabled(true);
			}
		}
		
		if (getActivityIf().onBeforeSetContentView()) {
			if (getContentView() != 0) {
				activity.setContentView(getContentView());
				getActivityIf().onAfterSetContentView(savedInstanceState);
			}
			clearTaskBroadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					Boolean requiresAuthentication = intent.getBooleanExtra(
						ClearTaskIntent.REQUIRES_AUTHENTICATION_EXTRA, true);
					if (requiresAuthentication.equals(getActivityIf().requiresAuthentication())) {
						LOGGER.debug("Finishing activity [" + activity + "] with requiresAuthentication = "
								+ requiresAuthentication);
						activity.finish();
					}
				}
			};
			activity.registerReceiver(clearTaskBroadcastReceiver, ClearTaskIntent.newIntentFilter());
		}
		
		// Ads
		AdLoader adLoader = createAdLoader();
		if (adLoader != null) {
			adLoader.loadAd(activity, (ViewGroup)(activity.findViewById(R.id.adViewContainer)),
				getActivityIf().getAdSize());
		}
		
		// Analytics
		ExecutorUtils.execute(new Runnable() {
			
			@Override
			public void run() {
				AbstractApplication.get().saveInstallationSource();
				if (AbstractApplication.get().hasAnalyticsSender()) {
					AbstractApplication.get().getAnalyticsSender().trackAppInstallation();
				}
			}
		});
	}
	
	protected AdLoader createAdLoader() {
		return new AdLoader();
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#isLauncherActivity()
	 */
	@Override
	public Boolean isLauncherActivity() {
		return false;
	}
	
	public void onContentChanged() {
	}
	
	public void onSaveInstanceState(Bundle outState) {
		LOGGER.trace("Executing onSaveInstanceState on " + activity);
		dismissLoading();
	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		LOGGER.trace("Executing onRestoreInstanceState on " + activity);
	}
	
	@SuppressLint("HandlerLeak")
	public void onStart() {
		LOGGER.trace("Executing onStart on " + activity);
		AbstractApplication.get().setCurrentActivity(activity);
		if (AbstractApplication.get().hasAnalyticsSender()) {
			AbstractApplication.get().getAnalyticsSender().onActivityStart(activity);
		}
		
		final Long locationFrequency = getLocationFrequency();
		if (locationFrequency != null) {
			locationHandler = new Handler() {
				
				@Override
				public void handleMessage(Message m) {
					LocalizationManager.get().startLocalization();
					locationHandler.sendMessageDelayed(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE),
						locationFrequency);
				}
			};
			locationHandler.sendMessage(Message.obtain(locationHandler, LOCATION_UPDATE_TIMER_CODE));
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getLocationFrequency()
	 */
	@Override
	public Long getLocationFrequency() {
		return getActivityIf().getLocationFrequency();
	}
	
	public void onResume() {
		LOGGER.trace("Executing onResume on " + activity);
		AbstractApplication.get().setInBackground(false);
		AbstractApplication.get().setCurrentActivity(activity);
		
		ActionBar actionBar = getActivityIf().getActionBar();
		if (actionBar != null) {
			DefaultApplicationContext context = AbstractApplication.get().getAndroidApplicationContext();
			if (!context.isProductionEnvironment() && context.displayDebugSettings()) {
				if (context.isHttpMockEnabled()) {
					actionBar.setBackgroundDrawable(getActivity().getResources().getDrawable(
						R.color.actionbarMockBackground));
				}
			}
		}
	}
	
	public void onPause() {
		LOGGER.trace("Executing onPause on " + activity);
		AbstractApplication.get().setInBackground(true);
	}
	
	public void onStop() {
		LOGGER.trace("Executing onStop on " + activity);
		if (AbstractApplication.get().hasAnalyticsSender()) {
			AbstractApplication.get().getAnalyticsSender().onActivityStop(activity);
		}
		
		if (locationHandler != null) {
			locationHandler.removeCallbacksAndMessages(null);
		}
	}
	
	public void onDestroy() {
		LOGGER.trace("Executing onDestroy on " + activity);
		if (clearTaskBroadcastReceiver != null) {
			activity.unregisterReceiver(clearTaskBroadcastReceiver);
		}
		dismissLoading();
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
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getMenuResourceId()
	 */
	@Override
	public Integer getMenuResourceId() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#doOnCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void doOnCreateOptionsMenu(Menu menu) {
		if (!getAndroidApplicationContext().displayDebugSettings()) {
			MenuItem menuItem = menu.findItem(R.id.debugSettingsItem);
			if (menuItem != null) {
				menuItem.setVisible(false);
			}
		}
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return onOptionsItemSelected(item.getItemId());
	}
	
	private boolean onOptionsItemSelected(int itemId) {
		if (itemId == android.R.id.home) {
			Intent upIntent = NavUtils.getParentActivityIntent(activity);
			if (NavUtils.shouldUpRecreateTask(activity, upIntent)) {
				// This activity is NOT part of this app's task, so create a new task
				// when navigating up, with a synthesized back stack.
				TaskStackBuilder.create(activity)
				// Add all of this activity's parents to the back stack
				.addNextIntentWithParentStack(upIntent)
				// Navigate up to the closest parent
				.startActivities();
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
		} else if (itemId == R.id.debugSettingsItem) {
			ActivityLauncher.launchActivity(DebugSettingsActivity.class);
			return true;
		}
		return false;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread()
	 */
	@Override
	public void showLoadingOnUIThread() {
		showLoadingOnUIThread(new DefaultLoadingDialogBuilder());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading()
	 */
	@Override
	public void showLoading() {
		showLoading(new DefaultLoadingDialogBuilder());
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoading(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoading(LoadingDialogBuilder builder) {
		if ((loadingDialog == null) || (!loadingDialog.isShowing())) {
			loadingDialog = builder.build(activity);
			loadingDialog.show();
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#showLoadingOnUIThread(com.jdroid.android.loading.LoadingDialogBuilder)
	 */
	@Override
	public void showLoadingOnUIThread(final LoadingDialogBuilder builder) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				showLoading(builder);
			}
		});
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
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onResumeUseCase(DefaultAbstractUseCase useCase, DefaultUseCaseListener listener) {
		onResumeUseCase(useCase, listener, UseCaseTrigger.MANUAL);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onResumeUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener,
	 *      com.jdroid.android.activity.ActivityHelper.UseCaseTrigger)
	 */
	@Override
	public void onResumeUseCase(final DefaultAbstractUseCase useCase, final DefaultUseCaseListener listener,
			final UseCaseTrigger useCaseTrigger) {
		if (useCase != null) {
			ExecutorUtils.execute(new Runnable() {
				
				@Override
				public void run() {
					useCase.addListener(listener);
					if (useCase.isNotified()) {
						if (useCaseTrigger.equals(UseCaseTrigger.ALWAYS)) {
							useCase.run();
						}
					} else {
						if (useCase.isInProgress()) {
							listener.onStartUseCase();
						} else if (useCase.isFinishSuccessful()) {
							listener.onFinishUseCase();
							useCase.markAsNotified();
						} else if (useCase.isFinishFailed()) {
							try {
								listener.onFinishFailedUseCase(useCase.getRuntimeException());
							} finally {
								useCase.markAsNotified();
							}
						} else if (useCase.isNotInvoked()
								&& (useCaseTrigger.equals(UseCaseTrigger.ONCE) || useCaseTrigger.equals(UseCaseTrigger.ALWAYS))) {
							useCase.run();
						}
					}
				}
			});
		}
	}
	
	public enum UseCaseTrigger {
		MANUAL,
		ONCE,
		ALWAYS;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#onPauseUseCase(com.jdroid.android.usecase.DefaultAbstractUseCase,
	 *      com.jdroid.android.usecase.listener.DefaultUseCaseListener)
	 */
	@Override
	public void onPauseUseCase(final DefaultAbstractUseCase userCase, final DefaultUseCaseListener listener) {
		if (userCase != null) {
			userCase.removeListener(listener);
		}
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase) {
		ExecutorUtils.execute(useCase);
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#executeUseCase(com.jdroid.android.usecase.UseCase, java.lang.Long)
	 */
	@Override
	public void executeUseCase(UseCase<?> useCase, Long delaySeconds) {
		ExecutorUtils.schedule(useCase, delaySeconds);
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
		if (getActivityIf().goBackOnError(runtimeException)) {
			DefaultExceptionHandler.markAsGoBackOnError(runtimeException);
		} else {
			DefaultExceptionHandler.markAsNotGoBackOnError(runtimeException);
		}
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
	 * @see com.jdroid.android.fragment.FragmentIf#goBackOnError(java.lang.RuntimeException)
	 */
	@Override
	public Boolean goBackOnError(RuntimeException runtimeException) {
		return true;
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
	 * @see com.jdroid.android.fragment.FragmentIf#findViewOnActivity(int)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findViewOnActivity(int id) {
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
	 * @see com.jdroid.android.activity.ActivityIf#getMenuInflater()
	 */
	@Override
	public MenuInflater getMenuInflater() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.activity.ActivityIf#getActionBar()
	 */
	@Override
	public ActionBar getActionBar() {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String)
	 */
	@Override
	public <E> E getArgument(String key) {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getArgument(java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return null;
	}
	
	/**
	 * @see com.jdroid.android.fragment.FragmentIf#getAdSize()
	 */
	@Override
	public AdSize getAdSize() {
		return AdSize.SMART_BANNER;
	}
}
