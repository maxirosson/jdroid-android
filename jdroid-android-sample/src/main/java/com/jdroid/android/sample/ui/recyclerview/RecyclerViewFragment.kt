package com.jdroid.android.sample.ui.recyclerview

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.jdroid.android.activity.ActivityLauncher
import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class RecyclerViewFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.recycler_view_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.simpleRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, SimpleRecyclerActivity::class.java)
            )
        }
        findView<View>(R.id.complexRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, ComplexRecyclerActivity::class.java)
            )
        }
        findView<View>(R.id.paginatedRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, PaginatedRecyclerActivity::class.java)
            )
        }
        findView<View>(R.id.paginatedGridRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, PaginatedGridRecyclerActivity::class.java)
            )
        }
        findView<View>(R.id.searchPaginatedRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, SearchPaginatedRecyclerActivity::class.java)
            )
        }
        findView<View>(R.id.autofitGridLayoutRecycler).setOnClickListener {
            ActivityLauncher.startActivity(
                activity,
                Intent(activity, AutofitGridLayoutRecyclerActivity::class.java)
            )
        }
    }
}

