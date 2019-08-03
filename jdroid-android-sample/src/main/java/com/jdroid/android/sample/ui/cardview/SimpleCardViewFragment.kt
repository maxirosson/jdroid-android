package com.jdroid.android.sample.ui.cardview

import android.os.Bundle
import android.view.View

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class SimpleCardViewFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.simple_cardview_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.clickableCardView).setOnClickListener {
            // Do nothing
        }
    }
}
