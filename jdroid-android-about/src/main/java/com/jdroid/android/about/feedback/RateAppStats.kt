package com.jdroid.android.about.feedback

import androidx.annotation.WorkerThread
import com.jdroid.android.about.AboutRemoteConfigParameter
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.context.UsageStats
import com.jdroid.android.utils.SharedPreferencesHelper
import com.jdroid.java.date.DateUtils
import java.util.concurrent.TimeUnit

object RateAppStats {

    private const val RATE_APP_STATS = "rateAppStats"
    private const val LAST_RESPONSE_TIMESTAMP = "lastResponseTimestamp"
    private const val ENJOYING = "enjoying"
    private const val GIVE_FEEDBACK = "giveFeedback"
    private const val RATE_ON_GOOGLE_PLAY = "rateOnGooglePlay"

    private val sharedPreferencesHelper: SharedPreferencesHelper by lazy {
        SharedPreferencesHelper.get(RATE_APP_STATS)
    }

    @WorkerThread
    fun getEnjoyingApp(): Boolean {
        return sharedPreferencesHelper.loadPreferenceAsBoolean(ENJOYING)
    }

    fun setEnjoyingApp(enjoying: Boolean) {
        sharedPreferencesHelper.savePreferenceAsync(ENJOYING, enjoying)
    }

    fun setGiveFeedback(feedback: Boolean?) {
        sharedPreferencesHelper.savePreferenceAsync(GIVE_FEEDBACK, feedback)
        sharedPreferencesHelper.savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis())
    }

    @WorkerThread
    fun getGiveFeedback(): Boolean? {
        return sharedPreferencesHelper.loadPreferenceAsBoolean(GIVE_FEEDBACK)
    }

    fun setRateOnGooglePlay(rate: Boolean) {
        sharedPreferencesHelper.savePreferenceAsync(RATE_ON_GOOGLE_PLAY, rate)
        sharedPreferencesHelper.savePreferenceAsync(LAST_RESPONSE_TIMESTAMP, DateUtils.nowMillis())
    }

    @WorkerThread
    fun getRateOnGooglePlay(): Boolean? {
        return sharedPreferencesHelper.loadPreferenceAsBoolean(RATE_ON_GOOGLE_PLAY)
    }

    @WorkerThread
    fun getLastResponseTimestamp(): Long {
        return sharedPreferencesHelper.loadPreferenceAsLong(LAST_RESPONSE_TIMESTAMP, 0L)
    }

    @WorkerThread
    fun reset() {
        sharedPreferencesHelper.removeAllPreferences()
    }

    // FIXME Find a way to call this method from UI thread
    // @WorkerThread
    fun displayRateAppView(): Boolean {
        val alreadyRated = getRateOnGooglePlay()
        val enoughDaysSinceLastResponse =
            TimeUnit.MILLISECONDS.toDays(DateUtils.nowMillis() - getLastResponseTimestamp()) >= AbstractApplication.get().remoteConfigLoader!!.getLong(
                AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_LAST_RESPONSE
            )!!
        val enoughDaysSinceFirstAppLoad =
            TimeUnit.MILLISECONDS.toDays(DateUtils.nowMillis() - UsageStats.getFirstAppLoadTimestamp()!!) >= AbstractApplication.get().remoteConfigLoader!!.getLong(
                AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_FIRST_APP_LOAD
            )!!
        val enoughAppLoads =
            UsageStats.getAppLoads() >= AbstractApplication.get().remoteConfigLoader!!.getLong(AboutRemoteConfigParameter.RATE_APP_MIN_APP_LOADS)!!
        val enoughDaysSinceLastCrash =
            TimeUnit.MILLISECONDS.toDays(DateUtils.nowMillis() - UsageStats.getLastCrashTimestamp()!!) >= AbstractApplication.get().remoteConfigLoader!!.getLong(
                AboutRemoteConfigParameter.RATE_APP_MIN_DAYS_SINCE_LAST_CRASH
            )!!
        return (alreadyRated == null || !alreadyRated) && enoughDaysSinceLastResponse && enoughDaysSinceFirstAppLoad && enoughAppLoads && enoughDaysSinceLastCrash
    }
}
