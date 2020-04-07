package com.jdroid.android.sample.ui.analytics

import android.os.Bundle
import android.view.View
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.sample.analytics.AppAnalyticsSender

class AnalyticsFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.analytics_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.sendExampleEvent).setOnClickListener {
            AppAnalyticsSender.trackExampleEvent()
        }

        findView<View>(R.id.sendExampleTransaction).setOnClickListener {
            AppAnalyticsSender.trackExampleTransaction()
        }

        findView<View>(R.id.sendExampleTiming).setOnClickListener {
            AppAnalyticsSender.trackExampleTiming()
        }
    }
}
