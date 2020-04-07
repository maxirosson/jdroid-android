package com.jdroid.android.firebase.admob

import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.AbstractAppContext
import com.jdroid.android.context.UsageStats
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.collections.Sets
import com.jdroid.java.date.DateUtils
import com.jdroid.java.utils.StringUtils
import java.util.concurrent.TimeUnit

open class AdMobAppContext : AbstractAppContext() {

    fun areAdsEnabledByDefault(): Boolean? {
        return AbstractApplication.get().remoteConfigLoader!!.getBoolean(AdMobRemoteConfigParameter.ADS_ENABLED)
    }

    /**
     * @return Whether the application has ads enabled or not
     */
    open fun areAdsEnabled(): Boolean {
        val prefEnabled =
            SharedPreferencesHelper.get().loadPreferenceAsBoolean(AdMobRemoteConfigParameter.ADS_ENABLED.getKey(), areAdsEnabledByDefault())
        val enoughDaysSinceFirstAppLoad =
            TimeUnit.MILLISECONDS.toDays(DateUtils.nowMillis() - UsageStats.getFirstAppLoadTimestamp()!!) >= getMinDaysSinceFirstAppLoad()
        val enoughAppLoads = UsageStats.getAppLoads() >= getMinAppLoadsToDisplayAds()
        return prefEnabled!! && enoughDaysSinceFirstAppLoad && enoughAppLoads
    }

    fun isInterstitialEnabled(): Boolean {
        return areAdsEnabled() && TimeUnit.MILLISECONDS.toSeconds(DateUtils.nowMillis() - AdsStats.getLastInterstitialOpenedTimestamp()) > getMinSecondsBetweenInterstitials()
    }

    protected fun getMinAppLoadsToDisplayAds(): Long {
        return AbstractApplication.get().remoteConfigLoader!!.getLong(AdMobRemoteConfigParameter.MIN_APP_LOADS_TO_DISPLAY_ADS)!!
    }

    protected fun getMinDaysSinceFirstAppLoad(): Long {
        return AbstractApplication.get().remoteConfigLoader!!.getLong(AdMobRemoteConfigParameter.MIN_DAYS_TO_DISPLAY_ADS)!!
    }

    protected fun getMinSecondsBetweenInterstitials(): Long {
        return AbstractApplication.get().remoteConfigLoader!!.getLong(AdMobRemoteConfigParameter.MIN_SECONDS_BETWEEN_INTERSTITIALS)!!
    }

    fun isTestAdUnitIdEnabled(): Boolean {
        return SharedPreferencesHelper.get().loadPreferenceAsBoolean(TEST_AD_UNIT_ID_ENABLED, true)
    }

    /**
     * @return The MD5-hashed ID of the devices that should display mocked ads
     */
    fun getTestDevicesIds(): Set<String> {
        val testDevicesIds = getBuildConfigValue<String>("ADS_TEST_DEVICES_IDS")
        return if (testDevicesIds != null) Sets.newHashSet(StringUtils.splitWithCommaSeparator(testDevicesIds)) else Sets.newHashSet()
    }

    /**
     * @return The AdMob Publisher ID
     */
    fun getDefaultAdUnitId(): String? {
        return AbstractApplication.get().remoteConfigLoader!!.getString(AdMobRemoteConfigParameter.DEFAULT_AD_UNIT_ID)
    }

    fun getAdMobAppId(): String? {
        return getBuildConfigValue("ADMOB_APP_ID")
    }

    companion object {
        const val TEST_AD_UNIT_ID_ENABLED = "TEST_AD_UNIT_ID_ENABLED"
    }
}
