package com.jdroid.android.sample.ui.home;

import android.content.Intent;

import com.jdroid.android.ActionItem;
import com.jdroid.android.activity.ActivityLauncher;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.androidx.ArchitectureActivity;
import com.jdroid.android.sample.database.room.RoomActivity;
import com.jdroid.android.sample.ui.analytics.AnalyticsActivity;
import com.jdroid.android.sample.ui.cardview.CardViewActivity;
import com.jdroid.android.sample.ui.datetime.DateTimeActivity;
import com.jdroid.android.sample.ui.exceptions.ErrorDisplayerActivity;
import com.jdroid.android.sample.ui.fab.FabActivity;
import com.jdroid.android.sample.ui.facebook.FacebookSignInActivity;
import com.jdroid.android.sample.ui.firebase.database.FirebaseDatabaseActivity;
import com.jdroid.android.sample.ui.firebase.dynamiclinks.DynamicLinksActivity;
import com.jdroid.android.sample.ui.firebase.fcm.FcmActivity;
import com.jdroid.android.sample.ui.glide.GlideActivity;
import com.jdroid.android.sample.ui.google.admob.AdsActivity;
import com.jdroid.android.sample.ui.google.inappbilling.GoogleInAppBillingActivity;
import com.jdroid.android.sample.ui.google.maps.GoogleMapsActivity;
import com.jdroid.android.sample.ui.google.playgames.GooglePlayGamesActivity;
import com.jdroid.android.sample.ui.google.playservices.GooglePlayServicesActivity;
import com.jdroid.android.sample.ui.google.signin.GoogleSignInActivity;
import com.jdroid.android.sample.ui.hero.HeroActivity;
import com.jdroid.android.sample.ui.http.HttpActivity;
import com.jdroid.android.sample.ui.leakcanary.LeakCanaryActivity;
import com.jdroid.android.sample.ui.loading.LoadingActivity;
import com.jdroid.android.sample.ui.navdrawer.NavDrawerActivity;
import com.jdroid.android.sample.ui.notifications.NotificationsActivity;
import com.jdroid.android.sample.ui.rateme.RateAppActivity;
import com.jdroid.android.sample.ui.recyclerview.RecyclerViewActivity;
import com.jdroid.android.sample.ui.service.ServiceActivity;
import com.jdroid.android.sample.ui.shortcuts.AppShortcutsActivity;
import com.jdroid.android.sample.ui.sqlite.SQLiteActivity;
import com.jdroid.android.sample.ui.strictmode.StrictModeActivity;
import com.jdroid.android.sample.ui.tablets.LeftTabletActivity;
import com.jdroid.android.sample.ui.tablets.TabletActivity;
import com.jdroid.android.sample.ui.timer.TimerActivity;
import com.jdroid.android.sample.ui.toasts.ToastsActivity;
import com.jdroid.android.sample.ui.twitter.TwitterActivity;
import com.jdroid.android.sample.ui.uri.UriMapperActivity;
import com.jdroid.android.sample.ui.webview.WebViewActivity;
import com.jdroid.android.shortcuts.AppShortcutsHelper;
import com.jdroid.android.utils.ScreenUtils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public enum HomeItem implements ActionItem {

	ANALYTCS(R.string.analytics, R.drawable.ic_analytics, AnalyticsActivity.class),
	APP_SHORTCUTS(R.string.appShortcuts, R.drawable.ic_analytics, AppShortcutsActivity.class),
	ARCHITECTURE(R.string.architecture, R.drawable.ic_analytics, ArchitectureActivity.class),
	CARD_VIEW(R.string.cardView, R.drawable.ic_cardview, CardViewActivity.class),
	DATE_TIME(R.string.dateTime, R.drawable.ic_date_time, DateTimeActivity.class),
	ERROR_DISPLAYER(R.string.errorDisplayer, R.drawable.ic_exception_handling, ErrorDisplayerActivity.class),
	FACEBOOK(R.string.jdroid_facebook, R.drawable.ic_facebook_black_24dp, FacebookSignInActivity.class),
	FIREBASE_ADMOB(R.string.adMob, R.drawable.ic_admob, AdsActivity.class),
	FIREBASE_FCM(R.string.fcm, R.drawable.ic_fcm, FcmActivity.class),
	FIREBASE_DATABASE(R.string.firebaseDatabase, R.drawable.ic_firebase, FirebaseDatabaseActivity.class),
	FIREBASE_DYNAMIC_LINKS(R.string.firebaseDynamicLinks, R.drawable.ic_fcm, DynamicLinksActivity.class),
	FAB(R.string.floatingActionButton, R.drawable.ic_firebase, FabActivity.class),
	GLIDE(R.string.glide, R.drawable.ic_photo, GlideActivity.class),
	GOOGLE_IN_APP_BILLING(R.string.inAppBilling, R.drawable.ic_inapp_billing, GoogleInAppBillingActivity.class),
	GOOGLE_MAPS(R.string.googleMaps, R.drawable.ic_maps, GoogleMapsActivity.class),
	GOOGLE_PLAY_GAMES(R.string.googlePlayGames, R.drawable.ic_games_black_24dp, GooglePlayGamesActivity.class),
	GOOGLE_PLAY_SERVICES(R.string.googlePlayServices, R.drawable.ic_fcm, GooglePlayServicesActivity.class),
	GOOGLE_SIGN_IN(R.string.jdroid_googleSignIn, R.drawable.ic_sign_in_24dp, GoogleSignInActivity.class),
	HERO(R.string.hero, R.drawable.ic_photo, HeroActivity.class),
	HTTP(R.string.http, R.drawable.ic_http, HttpActivity.class),
	LEAK_CANARY(R.string.jdroid_leakCanary, R.drawable.ic_exception_handling, LeakCanaryActivity.class),
	LOADING(R.string.loading, R.drawable.ic_loading, LoadingActivity.class),
	NAVDRAWER(R.string.navDrawer, R.drawable.ic_nav_drawer, NavDrawerActivity.class),
	NOTIFICATIONS(R.string.notifications, R.drawable.ic_notifications, NotificationsActivity.class),
	RATE_APP(R.string.rateApp, R.drawable.ic_rate, RateAppActivity.class),
	RECYCLER_VIEW(R.string.recyclerView, R.drawable.ic_recycler_view, RecyclerViewActivity.class),
	ROOM(R.string.room, R.drawable.ic_sqlite, RoomActivity.class),
	SERVICE(R.string.service, R.drawable.ic_service, ServiceActivity.class),
	SQLITE(R.string.sqlite, R.drawable.ic_sqlite, SQLiteActivity.class),
	STRICT_MODE(R.string.strictMode, R.drawable.ic_exception_handling, StrictModeActivity.class),
	TABLETS(R.string.tablets, R.drawable.ic_tablets, ScreenUtils.is10Inches() ? TabletActivity.class : LeftTabletActivity.class),
	TIMER(R.string.timer, R.drawable.ic_toasts, TimerActivity.class),
	TOASTS(R.string.toasts, R.drawable.ic_toasts, ToastsActivity.class),
	TWITTER(R.string.jdroid_twitter, R.drawable.ic_twitter_black_24dp, TwitterActivity.class),
	URI_MAPPER(R.string.uriMapper, R.drawable.ic_photo, UriMapperActivity.class),
	WEBVIEW(R.string.webView, R.drawable.ic_http, WebViewActivity.class);

	private Integer resourceId;
	private Integer iconId;
	private Class<? extends FragmentActivity> activityClass;

	HomeItem(Integer resourceId, Integer iconId, Class<? extends FragmentActivity> activityClass) {
		this.resourceId = resourceId;
		this.iconId = iconId;
		this.activityClass = activityClass;
	}

	@Override
	public Integer getNameResource() {
		return resourceId;
	}

	@Override
	public Integer getIconResource() {
		return iconId;
	}

	@Override
	public void startActivity(FragmentActivity fragmentActivity) {
		ActivityLauncher.startActivity(fragmentActivity, getIntent());
	}

	@Override
	public Intent getIntent() {
		AppShortcutsHelper.reportShortcutUsed(name());
		return new Intent(AbstractApplication.get(), activityClass);
	}

	@Override
	public Boolean matchesActivity(FragmentActivity fragmentActivity) {
		return activityClass.equals(fragmentActivity.getClass());
	}

	@Override
	public Fragment createFragment(Object args) {
		return null;
	}

	@Override
	public String getName() {
		return name();
	}

	@Override
	public Integer getDescriptionResource() {
		return null;
	}
}