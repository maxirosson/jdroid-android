package com.jdroid.android.sample.ui.uri

import android.os.Bundle
import android.view.View

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class UriMapperFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.uri_mapper_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.singleTop).setOnClickListener { ActivityLauncher.startActivity(activity, UriMapperSingleTopActivity::class.java) }

        findView<View>(R.id.noFlags).setOnClickListener { ActivityLauncher.startActivity(activity, UriMapperNoFlagsActivity::class.java) }

        findView<View>(R.id.matchError).setOnClickListener { ActivityLauncher.startActivity(activity, MatchErrorActivity::class.java) }

        findView<View>(R.id.mainIntentError).setOnClickListener { ActivityLauncher.startActivity(activity, MainIntentErrorActivity::class.java) }

        findView<View>(R.id.defaultIntentError).setOnClickListener {
            ActivityLauncher.startActivity(activity, DefaulItntentErrorActivity::class.java)
        }

        findView<View>(R.id.matchNewActivity).setOnClickListener { ActivityLauncher.startActivity(activity, MatchNewActivity::class.java) }

        findView<View>(R.id.noMatchNewActivity).setOnClickListener { ActivityLauncher.startActivity(activity, NoMatchNewActivity::class.java) }

        findView<View>(R.id.matchSameActivity).setOnClickListener { ActivityLauncher.startActivity(activity, MatchSameActivity::class.java) }

        findView<View>(R.id.noMatchSameActivity).setOnClickListener { ActivityLauncher.startActivity(activity, NoMatchSameActivity::class.java) }

        findView<View>(R.id.matchNullIntent).setOnClickListener { ActivityLauncher.startActivity(activity, MatchNullIntentActivity::class.java) }

        findView<View>(R.id.noMatchNullIntent).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                NoMatchNullIntentActivity::class.java
            )
        }
    }
}
