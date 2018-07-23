package com.jdroid.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.concurrent.SafeExecuteWrapperRunnable;
import com.jdroid.android.exception.AbstractErrorDisplayer;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.java.collections.Maps;
import com.jdroid.java.exception.AbstractException;
import com.jdroid.java.utils.LoggerUtils;
import com.squareup.leakcanary.LeakCanary;

import org.slf4j.Logger;

import java.util.Map;

public class FragmentHelper implements FragmentIf {
	
	private final static Logger LOGGER = LoggerUtils.getLogger(FragmentHelper.class);

	private Fragment fragment;

	private Map<AppModule, FragmentDelegate> fragmentDelegatesMap;

	private FragmentLoading loading;

	private Toolbar appBar;
	
	public FragmentHelper(Fragment fragment) {
		this.fragment = fragment;
	}
	
	public FragmentIf getFragmentIf() {
		return (FragmentIf)fragment;
	}
	
	protected Fragment getFragment() {
		return fragment;
	}
	
	@Override
	public ActivityIf getActivityIf() {
		return (ActivityIf)fragment.getActivity();
	}
	
	// //////////////////////// Layout //////////////////////// //

	@Override
	public Integer getBaseFragmentLayout() {
		return R.layout.jdroid_base_fragment;
	}

	@Override
	public Integer getContentFragmentLayout() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findView(int id) {
		return (V)fragment.getView().findViewById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return (V)fragment.getActivity().findViewById(id);
	}

	@Override
	public Integer getMenuResourceId() {
		return null;
	}

	@Override
	public View inflate(int resource) {
		return getActivityIf().inflate(resource);
	}

	public Boolean isSecondaryFragment() {
		return false;
	}

	// //////////////////////// Life cycle //////////////////////// //

	public void onCreate(Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreate on " + fragment);
		fragment.setRetainInstance(getFragmentIf().shouldRetainInstance());

		fragmentDelegatesMap = Maps.newHashMap();
		for (AppModule appModule : AbstractApplication.get().getAppModules()) {
			FragmentDelegate fragmentDelegate = getFragmentIf().createFragmentDelegate(appModule);
			if (fragmentDelegate != null) {
				fragmentDelegatesMap.put(appModule, fragmentDelegate);
				fragmentDelegate.onCreate(savedInstanceState);
			}
		}
	}

	public void onNewIntent(Intent intent) {
		LOGGER.debug("Executing onNewIntent on " + fragment);
	}

	@Override
	public Boolean shouldRetainInstance() {
		return true;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LOGGER.debug("Executing onCreateView on " + fragment);
		Integer contentFragmentLayout = getFragmentIf().getContentFragmentLayout();
		if (contentFragmentLayout != null) {
			View view = inflater.inflate(getFragmentIf().getBaseFragmentLayout(), container, false);
			ViewGroup viewGroup = (ViewGroup)view.findViewById(R.id.content);
			viewGroup.addView(inflater.inflate(contentFragmentLayout, null, false));
			return view;
		}
		return null;
	}


	public void onViewCreated(View view, Bundle savedInstanceState) {
		LOGGER.debug("Executing onViewCreated on " + fragment);

		appBar = findView(R.id.appBar);
		if (appBar != null && getActivityIf() instanceof AbstractFragmentActivity) {
			getFragmentIf().beforeInitAppBar(appBar);

			if (getFragmentIf().isSecondaryFragment()) {
				if (getFragmentIf().getMenuResourceId() != null) {
					appBar.inflateMenu(getFragmentIf().getMenuResourceId());
					appBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							return getFragmentIf().onOptionsItemSelected(item);
						}
					});
				}
			} else {
				((AbstractFragmentActivity)getActivityIf()).setSupportActionBar(appBar);
				appBar.setNavigationIcon(R.drawable.jdroid_ic_arrow_back_white_24dp);
				getActivityIf().initNavDrawer(appBar);
			}

			getFragmentIf().afterInitAppBar(appBar);
		}

		for (FragmentDelegate each : fragmentDelegatesMap.values()) {
			each.onViewCreated(view, savedInstanceState);
		}

		if (loading == null) {
			loading = getFragmentIf().getDefaultLoading();
		}
		if (loading != null) {
			loading.onViewCreated(getFragmentIf());
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		LOGGER.debug("Executing onActivityCreated on " + fragment);
	}

	public void onStart() {
		LOGGER.debug("Executing onStart on " + fragment);
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf.shouldTrackOnFragmentStart()) {
			AbstractApplication.get().getCoreAnalyticsSender().onFragmentStart(fragmentIf.getScreenViewName());
		}
	}

	public void onResume() {
		LOGGER.debug("Executing onResume on " + fragment);

		for (FragmentDelegate each : fragmentDelegatesMap.values()) {
			each.onResume();
		}
	}

	public void onBeforePause() {
		for (FragmentDelegate each : fragmentDelegatesMap.values()) {
			each.onBeforePause();
		}
	}

	public void onPause() {
		LOGGER.debug("Executing onPause on " + fragment);
	}

	public void onStop() {
		LOGGER.debug("Executing onStop on " + fragment);
	}

	public void onDestroyView() {
		LOGGER.debug("Executing onDestroyView on " + fragment);
	}

	public void onBeforeDestroy() {
		for (FragmentDelegate each : fragmentDelegatesMap.values()) {
			each.onBeforeDestroy();
		}
	}

	public void onDestroy() {
		LOGGER.debug("Executing onDestroy on " + fragment);
		
		LeakCanary.installedRefWatcher().watch(fragment);
	}

	@Override
	public <E> E getExtra(String key) {
		return getActivityIf().getExtra(key);
	}

	@Override
	public <E> E getArgument(String key) {
		return getArgument(key, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> E getArgument(String key, E defaultValue) {
		Bundle arguments = fragment.getArguments();
		E value = (arguments != null) && arguments.containsKey(key) ? (E)arguments.get(key) : null;
		return value != null ? value : defaultValue;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return false;
	}

	@Override
	public Boolean onBackPressedHandled() {
		return false;
	}

	// //////////////////////// App bar //////////////////////// //

	@Override
	public void beforeInitAppBar(Toolbar appBar) {
		// Do nothing
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
		// Do nothing
	}

	@Override
	public Toolbar getAppBar() {
		return appBar;
	}

	// //////////////////////// Error Handling //////////////////////// //

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return AbstractErrorDisplayer.getErrorDisplayer(abstractException);
	}

	// //////////////////////// Use cases //////////////////////// //

	@MainThread
	@Override
	public void onStartUseCase() {
		getFragmentIf().showLoading();
	}

	@MainThread
	@Override
	public void onUpdateUseCase() {
		// Do nothing by default
	}

	@MainThread
	@Override
	public void onFinishUseCase() {
		getFragmentIf().dismissLoading();
	}

	@MainThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		getFragmentIf().dismissLoading();
		// TODO This line shouldn't be executed outside the onStart/onStop cycle, to avoid IllegalStateException: Can not perform this action after onSaveInstanceState
		getFragmentIf().createErrorDisplayer(abstractException).displayError(getFragment().getActivity(), abstractException);
	}

	// //////////////////////// Loading //////////////////////// //

	@Override
	public void showLoading() {
		final FragmentIf fragmentIf = getFragmentIf();
		fragmentIf.executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.show(fragmentIf);
				}
			}
		});
	}

	@Override
	public void dismissLoading() {
		final FragmentIf fragmentIf = getFragmentIf();
		fragmentIf.executeOnUIThread(new Runnable() {

			@Override
			public void run() {
				if (loading != null) {
					loading.dismiss(fragmentIf);
				}
			}
		});
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return null;
	}

	@Override
	public void setLoading(FragmentLoading loading) {
		this.loading = loading;
	}

	// //////////////////////// Delegates //////////////////////// //

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		return appModule.createFragmentDelegate(fragment);
	}

	@Override
	public FragmentDelegate getFragmentDelegate(AppModule appModule) {
		return fragmentDelegatesMap.get(appModule);
	}

	// //////////////////////// Analytics //////////////////////// //

	@Override
	public Boolean shouldTrackOnFragmentStart() {
		return false;
	}

	@NonNull
	@Override
	public String getScreenViewName() {
		return fragment.getClass().getSimpleName();
	}

	// //////////////////////// Others //////////////////////// //

	@Override
	public void executeOnUIThread(Runnable runnable) {
		Activity activity = fragment.getActivity();
		if (activity != null && !activity.isDestroyed()) {
			activity.runOnUiThread(new SafeExecuteWrapperRunnable(fragment, runnable));
		}
	}
}
