package com.jdroid.android.twitter

import android.content.Context
import android.util.Log
import com.jdroid.android.BuildConfig
import com.jdroid.android.concurrent.AppExecutors
import com.jdroid.android.lifecycle.ApplicationLifecycleCallback
import com.jdroid.java.utils.LoggerUtils
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.twitter.sdk.android.core.TwitterCore

class TwitterAppLifecycleCallback : ApplicationLifecycleCallback() {

    companion object {
        private val LOGGER = LoggerUtils.getLogger(TwitterAppLifecycleCallback::class.java)
    }

    override fun onCreate(context: Context) {
        AppExecutors.networkIOExecutor.execute {
            val twitterOauthConsumerKey = TwitterAppContext.getTwitterOauthConsumerKey()
            val twitterOauthConsumerSecret = TwitterAppContext.getTwitterOauthConsumerSecret()
            if (twitterOauthConsumerKey == null || twitterOauthConsumerSecret == null) {
                LOGGER.error("Missing TWITTER_OAUTH_CONSUMER_KEY or TWITTER_OAUTH_CONSUMER_SECRET")
            } else {
                val authConfig = TwitterAuthConfig(twitterOauthConsumerKey, twitterOauthConsumerSecret)
                val builder = TwitterConfig.Builder(context)
                builder.logger(DefaultLogger(Log.DEBUG))
                builder.twitterAuthConfig(authConfig)
                builder.debug(BuildConfig.DEBUG)
                builder.build()
                Twitter.initialize(builder.build())

                // Get TwitterCore instance on the worker thread to avoid reading the shared preferences on UI thread
                TwitterCore.getInstance()
            }
        }
    }
}
