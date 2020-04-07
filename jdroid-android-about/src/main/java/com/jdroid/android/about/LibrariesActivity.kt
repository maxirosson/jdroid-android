package com.jdroid.android.about

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class LibrariesActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return AboutAppModule.get().aboutContext.getLibrariesFragmentClass()
    }
}
