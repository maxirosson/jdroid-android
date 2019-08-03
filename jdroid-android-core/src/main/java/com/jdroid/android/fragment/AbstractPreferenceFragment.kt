package com.jdroid.android.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.jdroid.android.activity.AbstractFragmentActivity
import com.jdroid.android.activity.ActivityIf
import com.jdroid.android.application.AppModule
import com.jdroid.android.exception.ErrorDisplayer
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.java.exception.AbstractException

abstract class AbstractPreferenceFragment : PreferenceFragment(), FragmentIf {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.jdroid.android.R.layout.jdroid_base_fragment, container, false)
        val viewGroup = view.findViewById<View>(com.jdroid.android.R.id.content) as ViewGroup
        viewGroup.addView(super.onCreateView(inflater, null, savedInstanceState))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBar = findView<Toolbar>(com.jdroid.android.R.id.appBar)
        if (appBar != null && getActivityIf() is AbstractFragmentActivity) {
            (getActivityIf() as AbstractFragmentActivity).setSupportActionBar(appBar)
            appBar.setNavigationIcon(com.jdroid.android.R.drawable.jdroid_ic_arrow_back_white_24dp)
        }
    }

    override fun onNewIntent(intent: Intent) {
        // Do nothing
    }

    protected fun getFragmentIf(): FragmentIf {
        return this.activity as FragmentIf
    }

    override fun shouldRetainInstance(): Boolean {
        return true
    }

    override fun getBaseFragmentLayout(): Int? {
        return null
    }

    override fun getContentFragmentLayout(): Int? {
        return null
    }

    override fun <V : View> findView(id: Int): V {
        return view!!.findViewById<View>(id) as V
    }

    override fun <V : View> findViewOnActivity(id: Int): V? {
        return activity.findViewById<View>(id) as V
    }

    override fun <V : View> inflate(resource: Int): V {
        return getFragmentIf().inflate(resource)
    }

    override fun createErrorDisplayer(abstractException: AbstractException): ErrorDisplayer {
        return getFragmentIf().createErrorDisplayer(abstractException)
    }

    override fun executeOnUIThread(runnable: Runnable) {
        getFragmentIf().executeOnUIThread(runnable)
    }

    override fun <E> getExtra(key: String): E {
        return getFragmentIf().getExtra(key)
    }

    override fun <E> getArgument(key: String): E? {
        return getArgument<E>(key, null)
    }

    override fun <E> getArgument(key: String, defaultValue: E?): E? {
        val arguments = arguments
        val value = if (arguments != null && arguments.containsKey(key)) arguments.get(key) as E else null
        return value ?: defaultValue
    }

    override fun getActivityIf(): ActivityIf? {
        return activity as ActivityIf
    }

    override fun beforeInitAppBar(appBar: Toolbar) {}

    override fun afterInitAppBar(appBar: Toolbar) {}

    override fun getAppBar(): Toolbar? {
        return null
    }

    // //////////////////////// Analytics //////////////////////// //

    override fun getScreenViewName(): String {
        return AbstractPreferenceFragment::class.java.simpleName
    }

    // //////////////////////// Loading //////////////////////// //

    override fun showLoading() {}

    override fun dismissLoading() {}

    override fun getDefaultLoading(): FragmentLoading? {
        return null
    }

    override fun setLoading(loading: FragmentLoading) {}

    override fun getMenuResourceId(): Int? {
        return null
    }

    override fun isSecondaryFragment(): Boolean {
        return false
    }

    override fun createFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return null
    }

    override fun getFragmentDelegate(appModule: AppModule): FragmentDelegate? {
        return null
    }

    override fun onBackPressedHandled(): Boolean {
        return false
    }
}
