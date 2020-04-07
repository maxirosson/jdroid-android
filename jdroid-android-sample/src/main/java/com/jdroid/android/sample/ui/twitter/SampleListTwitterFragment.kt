package com.jdroid.android.sample.ui.twitter

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.twitter.ListTwitterHelper
import com.jdroid.android.twitter.TwitterListFragment
import com.twitter.sdk.android.tweetui.SearchTimeline

class SampleListTwitterFragment : TwitterListFragment() {

    override fun createTwitterHelper(): ListTwitterHelper {
        return object : ListTwitterHelper() {

            public override val abstractFragment: AbstractFragment?
                get() = this@SampleListTwitterFragment

            override fun createSearchTimeline(): SearchTimeline {
                val searchTimelineBuilder = SearchTimeline.Builder()
                val queryBuilder = StringBuilder()
                queryBuilder.append("android")
                searchTimelineBuilder.maxItemsPerRequest(15)
                searchTimelineBuilder.languageCode("en")
                searchTimelineBuilder.query(queryBuilder.toString())
                return searchTimelineBuilder.build()
            }
        }
    }
}
