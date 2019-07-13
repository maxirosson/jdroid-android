package com.jdroid.android.twitter

import android.view.ViewGroup
import androidx.annotation.MainThread
import com.jdroid.android.application.AbstractApplication
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.java.collections.Lists
import com.jdroid.java.http.exception.ConnectionException
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterApiException
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.SearchTimeline
import com.twitter.sdk.android.tweetui.TimelineResult
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

// TODO Add support to rotation. The helper is called on each screen rotation
abstract class TwitterHelper {

    var tweetContainer: ViewGroup? = null

    private var searchTimelineBuilder: SearchTimeline.Builder? = null
    var tweets = Lists.newArrayList<Tweet>()
        private set

    var twitterQuery: TwitterQuery? = null

    private var pendingSuccessNotification: Boolean = false
    private var pendingFailureNotification: Boolean = false

    protected abstract val abstractFragment: AbstractFragment?

    protected open fun createSearchTimeline(): SearchTimeline {
        if (twitterQuery != null) {
            searchTimelineBuilder = SearchTimeline.Builder()
            searchTimelineBuilder!!.languageCode(twitterQuery!!.languageCode)
            searchTimelineBuilder!!.maxItemsPerRequest(twitterQuery!!.maxItemsPerRequest)
            searchTimelineBuilder!!.query(twitterQuery!!.query)
        }
        return searchTimelineBuilder!!.build()
    }

    fun loadTweets() {
        onStartLoadingTweets()

        try {
            val searchTimeline = createSearchTimeline()
            searchTimeline.next(null, object : Callback<TimelineResult<Tweet>>() {
                override fun success(result: Result<TimelineResult<Tweet>>) {
                    try {
                        tweets.clear()
                        for (each in result.data.items) {
                            if (isValidTweet(each)) {
                                tweets.add(each)
                            }
                        }
                        notifySuccess()
                    } catch (e: Exception) {
                        AbstractApplication.get().exceptionHandler.logHandledException(e)
                    }
                }

                override fun failure(e: TwitterException) {
                    var connectionError: Boolean = false
                    if (e is TwitterApiException) {
                        if ("Unable to resolve host \"api.twitter.com\": No address associated with hostname" == e.message) {
                            connectionError = true
                        } else if (e.message != null && e.message!!.startsWith("failed to connect to ")) {
                            connectionError = true
                        } else {
                            val errorMessage = e.errorCode.toString() + " " + e.errorMessage
                            AbstractApplication.get().coreAnalyticsSender.trackErrorLog(errorMessage)
                        }
                    } else if ("Request Failure" == e.message) {
                        if (e.cause != null) {
                            if (e.cause is ConnectException) {
                                connectionError = true
                            } else if (e.cause is SocketTimeoutException) {
                                connectionError = true
                            } else if (e.cause is SocketException) {
                                connectionError = true
                            } else if (e.cause is SSLHandshakeException) {
                                connectionError = true
                            } else if (e.cause is SSLException) {
                                connectionError = true
                            } else if (e.cause is IOException) {
                                connectionError = true
                            } else if (e.cause?.message != null) {
                                if (e.cause?.message!!.startsWith("Failed to connect to api.twitter.com")) {
                                    connectionError = true
                                } else if (e.cause?.message == "Unable to resolve host \"api.twitter.com\": No address associated with hostname") {
                                    connectionError = true
                                }
                            }
                        }
                    }
                    if (connectionError) {
                        AbstractApplication.get().exceptionHandler.logHandledException(ConnectionException(e))
                    } else {
                        AbstractApplication.get().exceptionHandler.logHandledException(e)
                    }
                    notifyFailure()
                }
            })
        } catch (e: Exception) {
            notifyFailure()
            AbstractApplication.get().exceptionHandler.logHandledException(e)
        }
    }

    private fun notifySuccess() {
        if (abstractFragment != null && abstractFragment!!.isResumed) {
            pendingSuccessNotification = false
            this@TwitterHelper.onSuccess(tweets)
        } else {
            pendingSuccessNotification = true
        }
    }

    private fun notifyFailure() {
        if (abstractFragment != null && abstractFragment!!.isResumed) {
            pendingFailureNotification = false
            this@TwitterHelper.onFailure()
        } else {
            pendingFailureNotification = true
        }
    }

    @MainThread
    protected open fun onStartLoadingTweets() {
        // Do Nothing
    }

    @MainThread
    protected abstract fun onSuccess(tweets: List<Tweet>)

    @MainThread
    protected open fun onFailure() {
        // Do Nothing
    }

    protected fun isValidTweet(tweet: Tweet): Boolean {
        return true
    }

    fun onResume() {

        if (pendingSuccessNotification) {
            notifySuccess()
        }

        if (pendingFailureNotification) {
            notifyFailure()
        }
    }
}
