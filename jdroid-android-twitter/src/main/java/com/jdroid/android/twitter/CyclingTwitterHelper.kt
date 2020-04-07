package com.jdroid.android.twitter

import android.view.View
import androidx.annotation.MainThread
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.concurrent.SafeRunnable
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.CompactTweetView

abstract class CyclingTwitterHelper : TwitterHelper() {

    companion object {
        private const val TWEET_CYCLE_FREQUENCY = 10000
    }

    private var currentTweetIndex = 0
    private val displayTweetRunnable = DisplayTweetRunnable()

    fun onStart() {
        tweetContainer!!.postDelayed(displayTweetRunnable, TWEET_CYCLE_FREQUENCY.toLong())
    }

    fun onStop() {
        tweetContainer!!.removeCallbacks(displayTweetRunnable)
    }

    @MainThread
    override fun onSuccess(tweets: List<Tweet>) {
        displayTweet(tweets)
    }

    private fun displayTweet(tweets: List<Tweet>) {
        if (tweets.size > currentTweetIndex) {
            val tweet = tweets[currentTweetIndex]

            currentTweetIndex++
            if (tweets.size <= currentTweetIndex) {
                currentTweetIndex = 0
            }

            val abstractFragment = abstractFragment
            if (abstractFragment != null) {
                tweetContainer!!.removeAllViews()
                try {
                    tweetContainer!!.addView(CompactTweetView(abstractFragment.context, tweet))
                    tweetContainer!!.visibility = View.VISIBLE
                    tweetContainer!!.removeCallbacks(displayTweetRunnable)
                    tweetContainer!!.postDelayed(displayTweetRunnable, TWEET_CYCLE_FREQUENCY.toLong())
                } catch (e: Exception) {
                    AbstractApplication.get().exceptionHandler.logWarningException(e)
                    tweetContainer!!.removeCallbacks(displayTweetRunnable)
                }
            }
        }
    }

    private inner class DisplayTweetRunnable : SafeRunnable() {

        override fun doRun() {
            displayTweet(tweets)
        }
    }
}
