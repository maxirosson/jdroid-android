package com.jdroid.android.sample.ui.home

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jdroid.android.ActionItem
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.sample.R
import com.jdroid.android.sample.androidx.ArchitectureActivity
import com.jdroid.android.sample.database.room.RoomActivity
import com.jdroid.android.sample.google.dynamicfeature.SplitInstallActivity
import com.jdroid.android.sample.ui.analytics.AnalyticsActivity
import com.jdroid.android.sample.ui.cardview.CardViewActivity
import com.jdroid.android.sample.ui.datetime.DateTimeActivity
import com.jdroid.android.sample.ui.exceptions.ErrorDisplayerActivity
import com.jdroid.android.sample.ui.fab.FabActivity
import com.jdroid.android.sample.ui.facebook.FacebookSignInActivity
import com.jdroid.android.sample.ui.firebase.database.FirebaseDatabaseActivity
import com.jdroid.android.sample.ui.firebase.dynamiclinks.DynamicLinksActivity
import com.jdroid.android.sample.ui.firebase.fcm.FcmActivity
import com.jdroid.android.sample.ui.glide.GlideActivity
import com.jdroid.android.sample.ui.google.admob.AdsActivity
import com.jdroid.android.sample.ui.google.inappbilling.GoogleInAppBillingActivity
import com.jdroid.android.sample.ui.google.maps.GoogleMapsActivity
import com.jdroid.android.sample.ui.google.playgames.GooglePlayGamesActivity
import com.jdroid.android.sample.ui.google.playservices.GooglePlayServicesActivity
import com.jdroid.android.sample.ui.google.signin.GoogleSignInActivity
import com.jdroid.android.sample.ui.hero.HeroActivity
import com.jdroid.android.sample.ui.http.HttpActivity
import com.jdroid.android.sample.ui.leakcanary.LeakCanaryActivity
import com.jdroid.android.sample.ui.loading.LoadingActivity
import com.jdroid.android.sample.ui.navdrawer.NavDrawerActivity
import com.jdroid.android.sample.ui.notifications.NotificationsActivity
import com.jdroid.android.sample.ui.rateme.RateAppActivity
import com.jdroid.android.sample.ui.recyclerview.RecyclerViewActivity
import com.jdroid.android.sample.ui.service.ServiceActivity
import com.jdroid.android.sample.ui.shortcuts.AppShortcutsActivity
import com.jdroid.android.sample.ui.strictmode.StrictModeActivity
import com.jdroid.android.sample.ui.tablets.LeftTabletActivity
import com.jdroid.android.sample.ui.tablets.TabletActivity
import com.jdroid.android.sample.ui.timer.TimerActivity
import com.jdroid.android.sample.ui.toasts.ToastsActivity
import com.jdroid.android.sample.ui.twitter.TwitterActivity
import com.jdroid.android.sample.ui.uri.UriMapperActivity
import com.jdroid.android.sample.ui.webview.WebViewActivity
import com.jdroid.android.shortcuts.AppShortcutsHelper
import com.jdroid.android.utils.ScreenUtils

enum class HomeItem private constructor(
    private val resourceId: Int?,
    private val iconId: Int?,
    private val activityClass: Class<out FragmentActivity>
) : ActionItem {

    ANALYTCS(R.string.analytics, R.drawable.ic_analytics, AnalyticsActivity::class.java),
    APP_SHORTCUTS(R.string.appShortcuts, R.drawable.ic_analytics, AppShortcutsActivity::class.java),
    ARCHITECTURE(R.string.architecture, R.drawable.ic_analytics, ArchitectureActivity::class.java),
    CARD_VIEW(R.string.cardView, R.drawable.ic_cardview, CardViewActivity::class.java),
    DATE_TIME(R.string.dateTime, R.drawable.ic_date_time, DateTimeActivity::class.java),
    ERROR_DISPLAYER(R.string.errorDisplayer, R.drawable.ic_exception_handling, ErrorDisplayerActivity::class.java),
    FACEBOOK(R.string.jdroid_facebook, R.drawable.ic_facebook_black_24dp, FacebookSignInActivity::class.java),
    FIREBASE_ADMOB(R.string.adMob, R.drawable.ic_admob, AdsActivity::class.java),
    FIREBASE_FCM(R.string.fcm, R.drawable.ic_fcm, FcmActivity::class.java),
    FIREBASE_DATABASE(R.string.firebaseDatabase, R.drawable.ic_firebase, FirebaseDatabaseActivity::class.java),
    FIREBASE_DYNAMIC_LINKS(R.string.firebaseDynamicLinks, R.drawable.ic_fcm, DynamicLinksActivity::class.java),
    FAB(R.string.floatingActionButton, R.drawable.ic_firebase, FabActivity::class.java),
    GLIDE(R.string.glide, R.drawable.ic_photo, GlideActivity::class.java),
    GOOGLE_IN_APP_BILLING(R.string.inAppBilling, R.drawable.ic_inapp_billing, GoogleInAppBillingActivity::class.java),
    GOOGLE_MAPS(R.string.googleMaps, R.drawable.ic_maps, GoogleMapsActivity::class.java),
    GOOGLE_PLAY_GAMES(R.string.googlePlayGames, R.drawable.ic_games_black_24dp, GooglePlayGamesActivity::class.java),
    GOOGLE_PLAY_SERVICES(R.string.googlePlayServices, R.drawable.ic_fcm, GooglePlayServicesActivity::class.java),
    GOOGLE_SIGN_IN(R.string.jdroid_googleSignIn, R.drawable.ic_sign_in_24dp, GoogleSignInActivity::class.java),
    HERO(R.string.hero, R.drawable.ic_photo, HeroActivity::class.java),
    HTTP(R.string.http, R.drawable.ic_http, HttpActivity::class.java),
    LEAK_CANARY(R.string.jdroid_leakCanary, R.drawable.ic_exception_handling, LeakCanaryActivity::class.java),
    LOADING(R.string.loading, R.drawable.ic_loading, LoadingActivity::class.java),
    NAVDRAWER(R.string.navDrawer, R.drawable.ic_nav_drawer, NavDrawerActivity::class.java),
    NOTIFICATIONS(R.string.notifications, R.drawable.ic_notifications, NotificationsActivity::class.java),
    RATE_APP(R.string.rateApp, R.drawable.ic_rate, RateAppActivity::class.java),
    RECYCLER_VIEW(R.string.recyclerView, R.drawable.ic_recycler_view, RecyclerViewActivity::class.java),
    ROOM(R.string.room, R.drawable.ic_room, RoomActivity::class.java),
    SERVICE(R.string.service, R.drawable.ic_service, ServiceActivity::class.java),
    SPLIT_INSTALL(R.string.splitInstall, R.drawable.ic_exception_handling, SplitInstallActivity::class.java),
    STRICT_MODE(R.string.strictMode, R.drawable.ic_exception_handling, StrictModeActivity::class.java),
    TABLETS(
        R.string.tablets,
        R.drawable.ic_tablets,
        if (ScreenUtils.is10Inches()) TabletActivity::class.java else LeftTabletActivity::class.java
    ),
    TIMER(R.string.timer, R.drawable.ic_toasts, TimerActivity::class.java),
    TOASTS(R.string.toasts, R.drawable.ic_toasts, ToastsActivity::class.java),
    TWITTER(R.string.jdroid_twitter, R.drawable.ic_twitter_black_24dp, TwitterActivity::class.java),
    URI_MAPPER(R.string.uriMapper, R.drawable.ic_photo, UriMapperActivity::class.java),
    WEBVIEW(R.string.webView, R.drawable.ic_http, WebViewActivity::class.java);

    override fun getNameResource(): Int {
        return resourceId!!
    }

    override fun getIconResource(): Int {
        return iconId!!
    }

    override fun startActivity(fragmentActivity: FragmentActivity) {
        ActivityLauncher.startActivity(fragmentActivity, getIntent())
    }

    override fun getIntent(): Intent {
        AppShortcutsHelper.reportShortcutUsed(name)
        return Intent(AbstractApplication.get(), activityClass)
    }

    override fun matchesActivity(fragmentActivity: FragmentActivity): Boolean {
        return activityClass == fragmentActivity.javaClass
    }

    override fun createFragment(args: Any): Fragment? {
        return null
    }

    override fun getId(): String {
        return name
    }

    override fun getDescriptionResource(): Int? {
        return null
    }
}