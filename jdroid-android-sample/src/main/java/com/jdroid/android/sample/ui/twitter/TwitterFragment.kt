package com.jdroid.android.sample.ui.twitter

import android.os.Bundle
import android.view.View

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class TwitterFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.twitter_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.listTweetHelper).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                SampleListTwitterActivity::class.java
            )
        }

        findView<View>(R.id.cyclingTweetHelper).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                CyclingTwitterActivity::class.java
            )
        }
    }
}
