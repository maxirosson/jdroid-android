package com.jdroid.android.androidx.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.jdroid.android.utils.TagUtils
import com.jdroid.java.utils.LoggerUtils

abstract class AbstractViewModel : ViewModel() {

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        LoggerUtils.getLogger(getTag()).info("Clearing view model")
    }

    protected fun getTag(): String {
        return TagUtils.getTag(javaClass)
    }
}
