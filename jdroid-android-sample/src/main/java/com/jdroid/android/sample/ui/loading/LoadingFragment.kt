package com.jdroid.android.sample.ui.loading

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class LoadingFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.loading_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.blockingLoading).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, BlockingLoadingActivity::class.java)
            )
        }
        findView<View>(R.id.customBlockingLoading).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, CustomActivityLoadingActivity::class.java)
            )
        }
        findView<View>(R.id.nonBlockingLoading).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, NonBlockingLoadingActivity::class.java)
            )
        }
        findView<View>(R.id.swipeRefresLoading).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, SwipeRefreshLoadingActivity::class.java)
            )
        }
    }
}
