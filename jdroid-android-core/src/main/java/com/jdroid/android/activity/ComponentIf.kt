package com.jdroid.android.activity

import android.app.Activity
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

interface ComponentIf {

    // //////////////////////// Layout //////////////////////// //

    /**
     * Finds a view that was identified by the id attribute from the XML that was processed in
     * [Fragment.onCreateView]
     *
     * @param id The id to search for.
     * @param <V> The [View] class
     * @return The view if found or null otherwise.
    </V> */
    fun <V : View> findView(@IdRes id: Int): V?

    /**
     * Inflate a new view hierarchy from the specified xml resource.
     *
     * @param resource ID for an XML layout resource to load
     * @return The root View of the inflated XML file.
     */
    fun <V : View> inflate(@LayoutRes resource: Int): V?

    // //////////////////////// Life cycle //////////////////////// //

    @MenuRes
    fun getMenuResourceId(): Int?

    /**
     * @param key The key of the intent extra
     * @param <E> The instance type
     * @return the entry with the given key as an object.
    </E> */
    fun <E> getExtra(key: String): E

    fun onOptionsItemSelected(item: MenuItem): Boolean

    fun onBackPressedHandled(): Boolean

    // //////////////////////// Loading //////////////////////// //

    fun showLoading()

    fun dismissLoading()

    // //////////////////////// Others //////////////////////// //

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI thread, then the action is executed
     * immediately. If the current thread is not the UI thread, the action is posted to the event queue of the UI
     * thread.
     *
     *
     * If the current [Activity] is not equals to this, then no action is executed
     *
     * @param runnable the action to run on the UI thread
     */
    fun executeOnUIThread(runnable: Runnable)
}
