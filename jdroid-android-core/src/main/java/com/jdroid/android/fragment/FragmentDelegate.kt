package com.jdroid.android.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class FragmentDelegate(val fragment: Fragment) {

    open fun onCreate(savedInstanceState: Bundle?) {
        // Do Nothing
    }

    open fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Do Nothing
    }

    open fun onResume() {
        // Do Nothing
    }

    open fun onBeforePause() {
        // Do Nothing
    }

    open fun onBeforeDestroy() {
        // Do Nothing
    }
}
