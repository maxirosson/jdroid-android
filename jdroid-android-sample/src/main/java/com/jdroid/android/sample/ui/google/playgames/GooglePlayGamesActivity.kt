package com.jdroid.android.sample.ui.google.playgames

import androidx.fragment.app.Fragment

import com.jdroid.android.activity.FragmentContainerActivity

class GooglePlayGamesActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return GooglePlayGamesFragment::class.java
    }
}
