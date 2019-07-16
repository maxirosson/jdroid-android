package com.jdroid.android.sample.ui.shortcuts

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class AppShortcutsActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AppShortcutsFragment::class.java
    }
}