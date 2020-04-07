package com.jdroid.android.sample.ui.strictmode

import android.os.Bundle
import android.view.View

import com.jdroid.android.fragment.AbstractFragment
import com.jdroid.android.sample.R
import com.jdroid.android.utils.SharedPreferencesHelper

class StrictModeFragment : AbstractFragment() {

    override fun getContentFragmentLayout(): Int? {
        return R.layout.strictmode_fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findView<View>(R.id.diskAccessOnMainThread).setOnClickListener {
            SharedPreferencesHelper.get().savePreference("a", "b")
            SharedPreferencesHelper.get("asdas").loadAllPreferences()
        }
    }
}
