package com.jdroid.android.twitter

import android.content.Context
import androidx.annotation.MainThread
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.BaseTweetView
import com.twitter.sdk.android.tweetui.CompactTweetView

abstract class ListTwitterHelper : TwitterHelper() {

    override fun onStartLoadingTweets() {
        abstractFragment?.showLoading()
    }

    @MainThread
    override fun onSuccess(tweets: List<Tweet>) {
        val fragment = abstractFragment
        if (fragment != null) {
            tweetContainer!!.removeAllViews()
            for (each in tweets) {
                tweetContainer!!.addView(createTweetView(fragment.context, each))
            }
            fragment.dismissLoading()
        }
    }

    protected fun createTweetView(context: Context?, tweet: Tweet): BaseTweetView {
        return CompactTweetView(context, tweet)
    }

    @MainThread
    override fun onFailure() {
        abstractFragment?.dismissLoading()
    }
}
