package com.jdroid.android.twitter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.loading.FragmentLoading
import com.jdroid.android.loading.SwipeRefreshLoading

// TODO Add no results (or error) support
abstract class TwitterListFragment : AbstractFragment(), SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var twitterHelper: TwitterHelper
        private set

    override fun getContentFragmentLayout(): Int? {
        return R.layout.jdroid_twitter_list_fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        twitterHelper = createTwitterHelper()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        twitterHelper.tweetContainer = findView<View>(R.id.tweetContainer) as ViewGroup
        if (loadTweetsOnViewCreated()) {
            twitterHelper.loadTweets()
        }
    }

    override fun onResume() {
        super.onResume()
        twitterHelper.onResume()
    }

    override fun onRefresh() {
        twitterHelper.loadTweets()
    }

    override fun getDefaultLoading(): FragmentLoading? {
        return SwipeRefreshLoading()
    }

    protected fun loadTweetsOnViewCreated(): Boolean {
        return true
    }

    protected abstract fun createTwitterHelper(): TwitterHelper
}
