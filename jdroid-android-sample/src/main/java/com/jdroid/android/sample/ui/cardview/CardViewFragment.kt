package com.jdroid.android.sample.ui.cardview

import android.os.Bundle
import android.view.View

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class CardViewFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.cardview_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.simpleCardView).setOnClickListener { ActivityLauncher.startActivity(activity, SimpleCardViewActivity::class.java) }

        findView<View>(R.id.cardViewInsideRecyclerView).setOnClickListener {
            ActivityLauncher.startActivity(activity, CardViewRecyclerViewActivity::class.java)
        }
    }
}
