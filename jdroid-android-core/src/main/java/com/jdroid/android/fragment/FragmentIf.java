package com.jdroid.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jdroid.android.activity.ActivityIf;
import com.jdroid.android.activity.ComponentIf;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.exception.ErrorDisplayer;
import com.jdroid.android.loading.FragmentLoading;
import com.jdroid.android.usecase.listener.UseCaseListener;
import com.jdroid.java.exception.AbstractException;

public interface FragmentIf extends ComponentIf, UseCaseListener {

	public ActivityIf getActivityIf();

	// //////////////////////// Layout //////////////////////// //

	public Boolean isSecondaryFragment();

	@LayoutRes
	public Integer getBaseFragmentLayout();

	@LayoutRes
	public Integer getContentFragmentLayout();

	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in {@link Activity#onCreate}
	 *
	 * @param id The id to search for.
	 * @param <V> The {@link View} class
	 *
	 * @return The view if found or null otherwise.
	 */
	public <V extends View> V findViewOnActivity(@IdRes int id);

	// //////////////////////// Life cycle //////////////////////// //

	public void onNewIntent(Intent intent);
	
	/**
	 * @param key The key of the argument extra
	 * @param <E> The instance type
	 * @return the entry with the given key as an object.
	 */
	public <E> E getArgument(String key);
	
	public <E> E getArgument(String key, E defaultValue);
	
	public Boolean shouldRetainInstance();

	// //////////////////////// App bar //////////////////////// //

	public Toolbar getAppBar();

	public void beforeInitAppBar(Toolbar appBar);

	public void afterInitAppBar(Toolbar appBar);

	// //////////////////////// Error Handling //////////////////////// //

	public ErrorDisplayer createErrorDisplayer(AbstractException abstractException);

	// //////////////////////// Loading //////////////////////// //
	
	public FragmentLoading getDefaultLoading();
	
	public void setLoading(FragmentLoading loading);
	
	// //////////////////////// Delegates //////////////////////// //

	public FragmentDelegate createFragmentDelegate(AppModule appModule);

	public FragmentDelegate getFragmentDelegate(AppModule appModule);

	// //////////////////////// Analytics //////////////////////// //

	@NonNull
	public String getScreenViewName();
}
