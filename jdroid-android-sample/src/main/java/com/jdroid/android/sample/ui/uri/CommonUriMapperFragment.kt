package com.jdroid.android.sample.ui.uri

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R

class CommonUriMapperFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.common_uri_mapper_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTextView()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        initTextView()
    }

    private fun initTextView() {
        if (requireActivity().intent.data != null) {
            (findView<View>(R.id.uri) as TextView).text = requireActivity().intent.data?.toString()
        }
        (findView<View>(R.id.activity) as TextView).text = requireActivity().javaClass.simpleName
    }
}
