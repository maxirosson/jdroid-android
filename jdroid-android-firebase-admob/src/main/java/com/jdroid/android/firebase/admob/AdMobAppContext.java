package com.jdroid.android.firebase.admob;

import com.jdroid.android.application.AbstractApplication;
import com.jdroid.android.context.AbstractAppContext;
import com.jdroid.android.context.UsageStats;
import com.jdroid.android.utils.SharedPreferencesHelper;
import com.jdroid.java.collections.Sets;
import com.jdroid.java.date.DateUtils;
import com.jdroid.java.utils.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class AdMobAppContext extends AbstractAppContext {

	public static final String TEST_AD_UNIT_ID_ENABLED = "TEST_AD_UNIT_ID_ENABLED";

	public Boolean areAdsEnabledByDefault() {
		return AbstractApplication.get().getRemoteConfigLoader().getBoolean(AdMobRemoteConfigParameter.ADS_ENABLED);
	}

	/**
	 * @return Whether the application has ads enabled or not
	 */
	public Boolean areAdsEnabled() {
		Boolean prefEnabled = SharedPreferencesHelper.get().loadPreferenceAsBoolean(AdMobRemoteConfigParameter.ADS_ENABLED.getKey(), areAdsEnabledByDefault());
		Boolean enoughDaysSinceFirstAppLoad = TimeUnit.MILLISECONDS.toDays(DateUtils.nowMillis() - UsageStats.getFirstAppLoadTimestamp()) >= getMinDaysSinceFirstAppLoad();
		Boolean enoughAppLoads = UsageStats.getAppLoads() >= getMinAppLoadsToDisplayAds();
		return prefEnabled && enoughDaysSinceFirstAppLoad && enoughAppLoads;
	}

	public Boolean isInterstitialEnabled() {
		return areAdsEnabled() && TimeUnit.MILLISECONDS.toSeconds(DateUtils.nowMillis() - AdsStats.getLastInterstitialOpenedTimestamp()) > getMinSecondsBetweenInterstitials();
	}

	protected Long getMinAppLoadsToDisplayAds() {
		return AbstractApplication.get().getRemoteConfigLoader().getLong(AdMobRemoteConfigParameter.MIN_APP_LOADS_TO_DISPLAY_ADS);
	}

	protected Long getMinDaysSinceFirstAppLoad() {
		return AbstractApplication.get().getRemoteConfigLoader().getLong(AdMobRemoteConfigParameter.MIN_DAYS_TO_DISPLAY_ADS);
	}

	protected Long getMinSecondsBetweenInterstitials() {
		return AbstractApplication.get().getRemoteConfigLoader().getLong(AdMobRemoteConfigParameter.MIN_SECONDS_BETWEEN_INTERSTITIALS);
	}

	public Boolean isTestAdUnitIdEnabled() {
		return SharedPreferencesHelper.get().loadPreferenceAsBoolean(TEST_AD_UNIT_ID_ENABLED, true);
	}

	/**
	 * @return The MD5-hashed ID of the devices that should display mocked ads
	 */
	public Set<String> getTestDevicesIds() {
		String testDevicesIds = getBuildConfigValue("ADS_TEST_DEVICES_IDS");
		return testDevicesIds != null ? Sets.newHashSet(StringUtils.splitWithCommaSeparator(testDevicesIds)) : Sets.<String>newHashSet();
	}

	/**
	 * @return The AdMob Publisher ID
	 */
	public String getDefaultAdUnitId() {
		return AbstractApplication.get().getRemoteConfigLoader().getString(AdMobRemoteConfigParameter.DEFAULT_AD_UNIT_ID);
	}

	public String getAdMobAppId() {
		return AbstractApplication.get().getRemoteConfigLoader().getString(AdMobRemoteConfigParameter.ADMOB_APP_ID);
	}
}
