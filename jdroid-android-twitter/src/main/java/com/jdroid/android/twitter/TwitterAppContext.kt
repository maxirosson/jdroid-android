package com.jdroid.android.twitter

import com.jdroid.android.context.BuildConfigUtils.getBuildConfigValue

object TwitterAppContext {

    fun getTwitterOauthConsumerKey(): String? {
        return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_KEY")
    }

    fun getTwitterOauthConsumerSecret(): String? {
        return getBuildConfigValue("TWITTER_OAUTH_CONSUMER_SECRET")
    }
}