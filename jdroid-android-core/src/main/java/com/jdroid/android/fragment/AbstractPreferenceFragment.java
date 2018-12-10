package com.jdroid.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.R;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.java.exception.AbstractException;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

public abstract class AbstractPreferenceFragment extends PreferenceFragment implements FragmentIf {

	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.jdroid_base_fragment, container, false);
		ViewGroup viewGroup = (ViewGroup)view.findViewById(R.id.content);
		viewGroup.addView(super.onCreateView(inflater, null, savedInstanceState));
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Toolbar appBar = findView(R.id.appBar);
		if (appBar != null && getActivityIf() instanceof AbstractFragmentActivity) {
			((AbstractFragmentActivity)getActivityIf()).setSupportActionBar(appBar);
			appBar.setNavigationIcon(R.drawable.jdroid_ic_arrow_back_white_24dp);

		}
	}

	@Override
	public void onNewIntent(Intent intent) {
		// Do nothing
	}

	protected FragmentIf getFragmentIf() {
		return (FragmentIf)this.getActivity();
	}

	@Override
	public Boolean shouldRetainInstance() {
		return true;
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return null;
	}

	@Override
	public Integer getContentFragmentLayout() {
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findView(int id) {
		return (V)getView().findViewById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <V extends View> V findViewOnActivity(int id) {
		return (V)getActivity().findViewById(id);
	}

	@Override
	public <V extends View> V inflate(int resource) {
		return getFragmentIf().inflate(resource);
	}

	@MainThread
	@Override
	public void onStartUseCase() {
		getFragmentIf().onStartUseCase();
	}

	@MainThread
	@Override
	public void onUpdateUseCase() {
		getFragmentIf().onUpdateUseCase();
	}

	@MainThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		getFragmentIf().onFinishFailedUseCase(abstractException);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return getFragmentIf().createErrorDisplayer(abstractException);
	}

	@MainThread
	@Override
	public void onFinishUseCase() {
		getFragmentIf().onFinishUseCase();
	}

	@Override
	public void executeOnUIThread(Runnable runnable) {
		FragmentIf fragmentIf = getFragmentIf();
		if (fragmentIf != null) {
			fragmentIf.executeOnUIThread(runnable);
		}
	}

	@Override
	public <E> E getExtra(String key) {
		return getFragmentIf().getExtra(key);
	}

	@Override
	public <E> E getArgument(String key) {
		return getArgument(key, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <E> E getArgument(String key, E defaultValue) {
		Bundle arguments = getArguments();
		E value = (arguments != null) && arguments.containsKey(key) ? (E)arguments.get(key) : null;
		return value != null ? value : defaultValue;
	}

	@Override
	public ActivityIf getActivityIf() {
		return (ActivityIf)getActivity();
	}

	@Override
	public void beforeInitAppBar(Toolbar appBar) {
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
	}

	@Override
	public Toolbar getAppBar() {
		return null;
	}

	// //////////////////////// Analytics //////////////////////// //

	@NonNull
	@Override
	public String getScreenViewName() {
		return AbstractPreferenceFragment.class.getSimpleName();
	}

	// //////////////////////// Loading //////////////////////// //

	@Override
	public void showLoading() {
	}

	@Override
	public void dismissLoading() {
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return null;
	}

	@Override
	public void setLoading(FragmentLoading loading) {
	}

	@Override
	public Integer getMenuResourceId() {
		return null;
	}

	@Override
	public Boolean isSecondaryFragment() {
		return null;
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		return null;
	}

	@Override
	public FragmentDelegate getFragmentDelegate(AppModule appModule) {
		return null;
	}

	@Override
	public Boolean onBackPressedHandled() {
		return false;
	}
}
