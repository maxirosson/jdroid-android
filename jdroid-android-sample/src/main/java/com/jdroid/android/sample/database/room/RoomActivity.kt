package com.jdroid.android.sample.database.room

import androidx.fragment.app.Fragment
import com.jdroid.android.activity.FragmentContainerActivity

class RoomActivity : FragmentContainerActivity() {

    override fun getFragmentClass(): Class<out Fragment> {
        return RoomFragment::class.java
    }
}
