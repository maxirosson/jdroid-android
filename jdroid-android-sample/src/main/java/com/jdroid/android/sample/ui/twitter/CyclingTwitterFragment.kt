package com.jdroid.android.sample.ui.twitter

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.twitter.CyclingTwitterHelper
import com.jdroid.android.twitter.TwitterHelper
import com.jdroid.android.twitter.TwitterListFragment
import com.twitter.sdk.android.tweetui.SearchTimeline

class CyclingTwitterFragment : TwitterListFragment() {

    override fun createTwitterHelper(): TwitterHelper {
        return object : CyclingTwitterHelper() {

            public override val abstractFragment: AbstractFragment?
                get() = this@CyclingTwitterFragment

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
