package com.jdroid.android.loading

import androidx.annotation.IdRes

import com.jdroid.android.application.AbstractApplication

abstract class BlockingLoading : ActivityLoading {

    var isCancelable = AbstractApplication.get().isLoadingCancelable

    @IdRes
    var loadingResId: Int? = null
}
