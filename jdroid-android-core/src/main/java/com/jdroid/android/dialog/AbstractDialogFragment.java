package com.jdroid.android.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.fragment.FragmentDelegate;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.fragment.FragmentIf;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.java.exception.AbstractException;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class AbstractDialogFragment extends DialogFragment implements FragmentIf {

	private FragmentHelper fragmentHelper;

	@Override
	public Boolean shouldRetainInstance() {
		return fragmentHelper.shouldRetainInstance();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentHelper = AbstractApplication.get().createFragmentHelper(this);
		fragmentHelper.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return null;
	}

	@Override
	public Integer getBaseFragmentLayout() {
		return fragmentHelper.getBaseFragmentLayout();
	}

	@Override
	public Integer getContentFragmentLayout() {
		return fragmentHelper.getContentFragmentLayout();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fragmentHelper.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		fragmentHelper.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onNewIntent(Intent intent) {
		fragmentHelper.onNewIntent(intent);
	}

	@Override
	public void onStart() {
		super.onStart();
		fragmentHelper.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		fragmentHelper.onResume();
	}

	@Override
	public void onPause() {
		fragmentHelper.onBeforePause();
		super.onPause();
		fragmentHelper.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		fragmentHelper.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		fragmentHelper.onDestroyView();
	}

	@Override
	public void onDestroy() {
		fragmentHelper.onBeforeDestroy();
		super.onDestroy();
		fragmentHelper.onDestroy();
	}

	@Override
	public <V extends View> V findView(int id) {
		return fragmentHelper.findView(id);
	}

	@Override
	public <V extends View> V findViewOnActivity(int id) {
		return fragmentHelper.findViewOnActivity(id);
	}

	@Override
	public <V extends View> V inflate(int resource) {
		return fragmentHelper.inflate(resource);
	}

	@MainThread
	@Override
	public void onStartUseCase() {
		fragmentHelper.onStartUseCase();
	}

	@MainThread
	@Override
	public void onUpdateUseCase() {
		fragmentHelper.onUpdateUseCase();
	}

	@MainThread
	@Override
	public void onFinishFailedUseCase(AbstractException abstractException) {
		fragmentHelper.onFinishFailedUseCase(abstractException);
	}

	@Override
	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException) {
		return fragmentHelper.createErrorDisplayer(abstractException);
	}

	@MainThread
	@Override
	public void onFinishUseCase() {
		fragmentHelper.onFinishUseCase();
	}

	@Override
	public void executeOnUIThread(Runnable runnable) {
		fragmentHelper.executeOnUIThread(runnable);
	}

	@Override
	public <E> E getExtra(String key) {
		return fragmentHelper.getExtra(key);
	}

	@Override
	public <E> E getArgument(String key) {
		return fragmentHelper.getArgument(key);
	}

	@Override
	public <E> E getArgument(String key, E defaultValue) {
		return fragmentHelper.getArgument(key, defaultValue);
	}

	@Override
	public void beforeInitAppBar(Toolbar appBar) {
		fragmentHelper.beforeInitAppBar(appBar);
	}

	@Override
	public void afterInitAppBar(Toolbar appBar) {
		fragmentHelper.afterInitAppBar(appBar);
	}

	@Override
	public Toolbar getAppBar() {
		return fragmentHelper.getAppBar();
	}

	@Override
	public ActivityIf getActivityIf() {
		return fragmentHelper.getActivityIf();
	}

	// //////////////////////// Analytics //////////////////////// //

	@NonNull
	@Override
	public String getScreenViewName() {
		return fragmentHelper.getScreenViewName();
	}

	// //////////////////////// Loading //////////////////////// //

	@Override
	public void showLoading() {
		fragmentHelper.showLoading();
	}

	@Override
	public void dismissLoading() {
		fragmentHelper.dismissLoading();
	}

	@Override
	public FragmentLoading getDefaultLoading() {
		return fragmentHelper.getDefaultLoading();
	}

	@Override
	public void setLoading(FragmentLoading loading) {
		fragmentHelper.setLoading(loading);
	}

	@Override
	public Integer getMenuResourceId() {
		return fragmentHelper.getMenuResourceId();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return fragmentHelper.onOptionsItemSelected(item);
	}

	@Override
	public Boolean isSecondaryFragment() {
		return fragmentHelper.isSecondaryFragment();
	}

	@Override
	public FragmentDelegate createFragmentDelegate(AppModule appModule) {
		return fragmentHelper.createFragmentDelegate(appModule);
	}

	@Override
	public FragmentDelegate getFragmentDelegate(AppModule appModule) {
		return fragmentHelper.getFragmentDelegate(appModule);
	}

	@Override
	public Boolean onBackPressedHandled() {
		return fragmentHelper.onBackPressedHandled();
	}
}
