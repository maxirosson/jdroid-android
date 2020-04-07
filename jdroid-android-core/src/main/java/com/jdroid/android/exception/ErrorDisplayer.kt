package com.jdroid.android.exception

import androidx.fragment.app.FragmentActivity

interface ErrorDisplayer {

    fun displayError(activity: FragmentActivity?, throwable: Throwable)
}
