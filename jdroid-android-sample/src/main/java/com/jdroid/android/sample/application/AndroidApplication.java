package com.jdroid.android.sample.application;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;

import com.firebase.client.Firebase;
import com.jdroid.android.about.AboutAppModule;
import com.jdroid.android.activity.AbstractFragmentActivity;
import com.jdroid.android.activity.ActivityHelper;
import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.application.AppModule;
import com.jdroid.android.context.AppContext;
import com.jdroid.android.debug.DebugContext;
import com.jdroid.android.firebase.admob.AdMobAppModule;
import com.jdroid.android.firebase.fcm.AbstractFcmAppModule;
import com.jdroid.android.firebase.remoteconfig.FirebaseRemoteConfigHelper;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.analytics.GoogleAnalyticsAppContext;
import com.jdroid.android.google.analytics.GoogleAnalyticsFactory;
import com.jdroid.android.google.analytics.GoogleCoreAnalyticsTracker;
import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.debug.AndroidDebugContext;
import com.jdroid.android.sample.firebase.fcm.AndroidFcmAppModule;
import com.jdroid.android.sample.firebase.remoteconfig.AndroidRemoteConfigParameter;
import com.jdroid.android.sample.google.inappbilling.SampleInAppBillingBroadcastListener;
import com.jdroid.android.sample.repository.UserRepositoryImpl;
import com.jdroid.android.sample.ui.AndroidActivityHelper;
import com.jdroid.android.sample.ui.AndroidFragmentHelper;
import com.jdroid.android.sample.ui.about.AndroidAboutAppModule;
import com.jdroid.android.sample.ui.google.admob.SampleAdMobAppContext;
import com.jdroid.android.sample.ui.google.inappbilling.AndroidInAppBillingAppModule;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.sample.ui.home.HomeItem;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteEntity;
import com.jdroid.android.sample.ui.sqlite.SampleSQLiteRepository;
import com.jdroid.android.sample.ui.uri.SampleUriWatcher;
import com.jdroid.android.shortcuts.AppShortcutsAppModule;
import com.jdroid.android.shortcuts.AppShortcutsHelper;
import com.jdroid.android.sqlite.SQLiteHelper;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.domain.Identifiable;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;
import com.jdroid.java.repository.Repository;

import java.util.List;
import java.util.Map;

public class AndroidApplication extends AbstractApplication {
	
	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}

	public AndroidApplication() {
		HttpConfiguration.setHttpServiceFactory(new OkHttpServiceFactory());
	}
	
	@Override
	public void onProviderInit() {
		super.onProviderInit();
		
		FirebaseRemoteConfigHelper.get().addRemoteConfigParameters(AndroidRemoteConfigParameter.values());
		AdMobAppModule.setAdMobAppContext(new SampleAdMobAppContext());
	}
	
	@Override
	protected void onMainProcessCreate() {
		if (GoogleAnalyticsAppContext.isGoogleAnalyticsEnabled()) {
			GoogleAnalyticsFactory.getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.INSTALLATION_SOURCE.name(), 1);
			GoogleAnalyticsFactory.getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.DEVICE_TYPE.name(), 2);
			GoogleAnalyticsFactory.getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.REFERRER.name(), 3);
			GoogleAnalyticsFactory.getGoogleAnalyticsHelper().addCustomDimensionDefinition(GoogleCoreAnalyticsTracker.CustomDimension.DEVICE_YEAR_CLASS.name(), 4);
		}

		getUriMapper().addUriWatcher(new SampleUriWatcher());

		Firebase.setAndroidContext(this);
		
		initAppShortcuts();
	}
	
	private void initAppShortcuts() {
		if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
			List<ShortcutInfo> shortcutInfos = Lists.newArrayList();
			int rank = 0;
			for (HomeItem item : HomeItem.values()) {
				
				Intent intent = item.getIntent();
				intent.setAction(Intent.ACTION_VIEW);
				
				ShortcutInfo.Builder shortcutInfoBuilder = new ShortcutInfo.Builder(AbstractApplication.get(), item.name());
				shortcutInfoBuilder.setShortLabel(LocalizationUtils.getString(item.getNameResource()));
				shortcutInfoBuilder.setLongLabel(LocalizationUtils.getString(item.getNameResource()));
				shortcutInfoBuilder.setIcon(Icon.createWithResource(AbstractApplication.get(), item.getIconResource()));
				shortcutInfoBuilder.setIntent(intent);
				shortcutInfoBuilder.setRank(rank);
				shortcutInfos.add(shortcutInfoBuilder.build());
				rank++;
			}
			AppShortcutsHelper.setInitialShortcutInfos(shortcutInfos);
		}
	}
	
	@Override
	protected void onInitMultiDex() {
		MultiDex.install(this);
	}

	@Override
	public Class<? extends Activity> getHomeActivityClass() {
		return HomeActivity.class;
	}

	@NonNull
	@Override
	protected AppContext createAppContext() {
		return new AndroidAppContext();
	}

	@NonNull
	public AndroidAppContext getAppContext() {
		return (AndroidAppContext)super.getAppContext();
	}

	@Override
	public ActivityHelper createActivityHelper(AbstractFragmentActivity activity) {
		return new AndroidActivityHelper(activity);
	}
	
	@Override
	public FragmentHelper createFragmentHelper(Fragment fragment) {
		return new AndroidFragmentHelper(fragment);
	}

	@Override
	protected DebugContext createDebugContext() {
		return new AndroidDebugContext();
	}

	@Override
	public UserRepository getUserRepository() {
		return new UserRepositoryImpl();
	}

	@Override
	public Boolean isDatabaseEnabled() {
		return true;
	}

	@Override
	protected void initDatabaseRepositories(Map<Class<? extends Identifiable>, Repository<? extends Identifiable>> repositories, SQLiteHelper dbHelper) {
		repositories.put(SampleSQLiteEntity.class, new SampleSQLiteRepository(dbHelper));
	}

	@Override
	protected void initAppModule(Map<String, AppModule> appModulesMap) {
		appModulesMap.put(AdMobAppModule.MODULE_NAME, new AdMobAppModule());
		appModulesMap.put(AbstractFcmAppModule.MODULE_NAME, new AndroidFcmAppModule());
		appModulesMap.put(AboutAppModule.MODULE_NAME, new AndroidAboutAppModule());
		appModulesMap.put(AppShortcutsAppModule.MODULE_NAME, new AppShortcutsAppModule());
		appModulesMap.put(InAppBillingAppModule.MODULE_NAME, new AndroidInAppBillingAppModule());
		
		AndroidInAppBillingAppModule.get().setInAppBillingBroadcastListener(new SampleInAppBillingBroadcastListener());
	}

	@Override
	public int getLauncherIconResId() {
		return R.mipmap.ic_launcher;
	}

	@Override
	public int getNotificationIconResId() {
		return R.drawable.ic_notification;
	}

	@Override
	public String getManifestPackageName() {
		return "com.jdroid.android.sample";
	}
	
	@Override
	public List<NotificationChannelType> getNotificationChannelTypes() {
		return Lists.<NotificationChannelType>newArrayList(AndroidNotificationChannelType.values());
	}
}
