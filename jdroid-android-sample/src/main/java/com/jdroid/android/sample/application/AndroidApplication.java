package com.jdroid.android.sample.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.drawable.Icon;
import android.os.Build;

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
import com.jdroid.android.firebase.fcm.FcmContext;
import com.jdroid.android.fragment.FragmentHelper;
import com.jdroid.android.google.inappbilling.InAppBillingAppModule;
import com.jdroid.android.http.HttpConfiguration;
import com.jdroid.android.notification.NotificationChannelType;
import com.jdroid.android.repository.UserRepository;
import com.jdroid.android.sample.R;
import com.jdroid.android.sample.debug.AndroidDebugContext;
import com.jdroid.android.sample.firebase.fcm.AndroidFcmAppModule;
import com.jdroid.android.sample.firebase.fcm.SampleFcmMessage;
import com.jdroid.android.sample.repository.UserRepositoryImpl;
import com.jdroid.android.sample.ui.AndroidActivityHelper;
import com.jdroid.android.sample.ui.AndroidFragmentHelper;
import com.jdroid.android.sample.ui.about.AndroidAboutAppModule;
import com.jdroid.android.sample.ui.google.admob.SampleAdMobAppContext;
import com.jdroid.android.sample.ui.google.inappbilling.AndroidInAppBillingAppModule;
import com.jdroid.android.sample.ui.home.HomeActivity;
import com.jdroid.android.sample.ui.home.HomeItem;
import com.jdroid.android.sample.ui.uri.SampleUriWatcher;
import com.jdroid.android.shortcuts.AppShortcutsAppModule;
import com.jdroid.android.shortcuts.AppShortcutsHelper;
import com.jdroid.android.shortcuts.DynamicShortcutsLoader;
import com.jdroid.android.utils.LocalizationUtils;
import com.jdroid.java.collections.Lists;
import com.jdroid.java.http.okhttp.OkHttpServiceFactory;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.multidex.MultiDex;

public class AndroidApplication extends AbstractApplication {

	public static AndroidApplication get() {
		return (AndroidApplication)AbstractApplication.INSTANCE;
	}

	public AndroidApplication() {
		HttpConfiguration.INSTANCE.setHttpServiceFactory(new OkHttpServiceFactory());
	}

	@Override
	public void onProviderInit() {
		super.onProviderInit();

		AdMobAppModule.setAdMobAppContext(new SampleAdMobAppContext());
		FcmContext.INSTANCE.addFcmMessage(new SampleFcmMessage());
	}

	@Override
	protected void onMainProcessCreate() {
		getUriMapper().addUriWatcher(new SampleUriWatcher());

		Firebase.setAndroidContext(this);

		initAppShortcuts();
	}

	private void initAppShortcuts() {
		if (AppShortcutsHelper.isDynamicAppShortcutsSupported()) {
			AppShortcutsHelper.setDynamicShortcutsLoader(new DynamicShortcutsLoader() {

				@TargetApi(Build.VERSION_CODES.N_MR1)
				@Override
				public List<ShortcutInfo> loadDynamicShortcuts() {
					List<ShortcutInfo> shortcutInfos = Lists.newArrayList();
					int rank = 0;
					for (HomeItem item : HomeItem.values()) {

						Intent intent = item.getIntent();
						intent.setAction(Intent.ACTION_VIEW);

						ShortcutInfo.Builder shortcutInfoBuilder = new ShortcutInfo.Builder(AbstractApplication.get(), item.name());
						shortcutInfoBuilder.setShortLabel(LocalizationUtils.INSTANCE.getString(item.getNameResource()));
						shortcutInfoBuilder.setLongLabel(LocalizationUtils.INSTANCE.getString(item.getNameResource()));
						shortcutInfoBuilder.setIcon(Icon.createWithResource(AbstractApplication.get(), item.getIconResource()));
						shortcutInfoBuilder.setIntent(intent);
						shortcutInfoBuilder.setRank(rank);
						shortcutInfos.add(shortcutInfoBuilder.build());
						rank++;
					}
					return shortcutInfos;
				}
			});
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
	protected void initAppModule(@NonNull Map<String, AppModule> appModulesMap) {
		appModulesMap.put(AdMobAppModule.MODULE_NAME, new AdMobAppModule());
		appModulesMap.put(AbstractFcmAppModule.MODULE_NAME, new AndroidFcmAppModule());
		appModulesMap.put(AboutAppModule.Companion.getMODULE_NAME(), new AndroidAboutAppModule());
		appModulesMap.put(AppShortcutsAppModule.Companion.getMODULE_NAME(), new AppShortcutsAppModule());
		appModulesMap.put(InAppBillingAppModule.MODULE_NAME, new AndroidInAppBillingAppModule());
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
		return Lists.newArrayList(AndroidNotificationChannelType.values());
	}

	@Override
	protected boolean isSplitCompatEnabled() {
		return true;
	}
}
