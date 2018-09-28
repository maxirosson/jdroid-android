package com.jdroid.android.activity;

import android.app.Activity;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;
import android.view.MenuItem;
import android.view.View;

public interface ComponentIf {

	// //////////////////////// Layout //////////////////////// //

	/**
	 * Finds a view that was identified by the id attribute from the XML that was processed in
	 * {@link Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)}
	 *
	 * @param id The id to search for.
	 * @param <V> The {@link View} class
	 * @return The view if found or null otherwise.
	 */
	public <V extends View> V findView(@IdRes int id);

	/**
	 * Inflate a new view hierarchy from the specified xml resource.
	 *
	 * @param resource ID for an XML layout resource to load
	 * @return The root View of the inflated XML file.
	 */
	public View inflate(@LayoutRes int resource);

	// //////////////////////// Life cycle //////////////////////// //

	/**
	 * @param key The key of the intent extra
	 * @param <E> The instance type
	 * @return the entry with the given key as an object.
	 */
	public <E> E getExtra(String key);

	@MenuRes
	public Integer getMenuResourceId();

	public boolean onOptionsItemSelected(MenuItem item);

	public Boolean onBackPressedHandled();

	// //////////////////////// Loading //////////////////////// //

	public void showLoading();

	public void dismissLoading();

	// //////////////////////// Others //////////////////////// //

	/**
	 * Runs the specified action on the UI thread. If the current thread is the UI thread, then the action is executed
	 * immediately. If the current thread is not the UI thread, the action is posted to the event queue of the UI
	 * thread.
	 * <p>
	 * If the current {@link Activity} is not equals to this, then no action is executed
	 *
	 * @param runnable the action to run on the UI thread
	 */
	public void executeOnUIThread(Runnable runnable);
}
