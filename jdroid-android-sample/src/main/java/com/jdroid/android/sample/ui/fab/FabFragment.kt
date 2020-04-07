package com.jdroid.android.sample.ui.fab

import android.os.Bundle
import android.view.View

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.snackbar.SnackbarBuilder

class FabFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.fab_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.fab).setOnClickListener {
            val snackbarBuilder = SnackbarBuilder()
            snackbarBuilder.setParentLayoutId(R.id.container)
            snackbarBuilder.setDescription(R.string.jdroid_ok)
            snackbarBuilder.build(activity!!).show()
        }
    }
}
