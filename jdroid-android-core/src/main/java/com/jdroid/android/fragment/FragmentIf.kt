package com.jdroid.android.fragment

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.jdroid.android.activity.ActivityIf
import com.jdroid.android.activity.ComponentIf
import com.jdroid.android.application.AppModule
import com.jdroid.android.exception.ErrorDisplayer
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.usecase.listener.UseCaseListener
import com.jdroid.java.exception.AbstractException

interface FragmentIf : ComponentIf, UseCaseListener {

    fun getActivityIf(): ActivityIf?

    // //////////////////////// Layout //////////////////////// //

    fun isSecondaryFragment(): Boolean

    @LayoutRes
    fun getBaseFragmentLayout(): Int?

    @LayoutRes
    fun getContentFragmentLayout(): Int?

    /**
     * Finds a view that was identified by the id attribute from the XML that was processed in [Activity.onCreate]
     *
     * @param id The id to search for.
     * @param <V> The [View] class
     * @return The view if found or null otherwise.
    </V> */
    fun <V : View> findViewOnActivity(@IdRes id: Int): V?

    // //////////////////////// Life cycle //////////////////////// //

    fun onNewIntent(intent: Intent)

    /**
     * @param key The key of the argument extra
     * @param <E> The instance type
     * @return the entry with the given key as an object.
    </E> */
    fun <E> getArgument(key: String): E

    fun <E> getArgument(key: String, defaultValue: E): E

    fun shouldRetainInstance(): Boolean

    // //////////////////////// App bar //////////////////////// //

    fun getAppBar(): Toolbar?

    fun beforeInitAppBar(appBar: Toolbar)

    fun afterInitAppBar(appBar: Toolbar)

    // //////////////////////// Error Handling //////////////////////// //

    fun createErrorDisplayer(abstractException: AbstractException): ErrorDisplayer

    // //////////////////////// Loading //////////////////////// //

    fun getDefaultLoading(): FragmentLoading?

    fun setLoading(loading: FragmentLoading)

    // //////////////////////// Delegates //////////////////////// //

    fun createFragmentDelegate(appModule: AppModule): FragmentDelegate?

    fun getFragmentDelegate(appModule: AppModule): FragmentDelegate?

    // //////////////////////// Analytics //////////////////////// //

    fun getScreenViewName(): String
}
