package com.jdroid.android.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.common.api.GoogleApiClient
import com.jdroid.android.application.AppModule
import com.jdroid.android.loading.ActivityLoading
import com.jdroid.android.navdrawer.NavDrawer
import com.jdroid.android.uri.UriHandler

interface ActivityIf : ComponentIf {

    // //////////////////////// Layout //////////////////////// //

    @LayoutRes
    fun getContentView(): Int

    // //////////////////////// Life cycle //////////////////////// //

    fun onBeforeSetContentView(): Boolean

    fun onAfterSetContentView(savedInstanceState: Bundle)

    fun doOnCreateOptionsMenu(menu: Menu)

    fun getMenuInflater(): MenuInflater

    fun isLauncherActivity(): Boolean

    fun isActivityDestroyed(): Boolean

    // //////////////////////// Delegates //////////////////////// //

    fun createActivityDelegate(appModule: AppModule): ActivityDelegate?

    fun getActivityDelegate(appModule: AppModule): ActivityDelegate?

    // //////////////////////// Loading //////////////////////// //

    fun getDefaultLoading(): ActivityLoading

    fun setLoading(loading: ActivityLoading)

    // //////////////////////// Navigation Drawer //////////////////////// //

    fun initNavDrawer(appBar: Toolbar)

    fun isNavDrawerEnabled(): Boolean

    fun createNavDrawer(activity: AbstractFragmentActivity, appBar: Toolbar): NavDrawer

    // //////////////////////// Location //////////////////////// //

    fun getLocationFrequency(): Long?

    // //////////////////////// Uri //////////////////////// //

    fun createUriHandler(): UriHandler<*>?

    // //////////////////////// Others //////////////////////// //

    fun isGooglePlayServicesVerificationEnabled(): Boolean

    fun getGoogleApiClient(): GoogleApiClient?

    fun getActivity(): AbstractFragmentActivity
}
