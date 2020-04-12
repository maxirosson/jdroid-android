package com.jdroid.android.exception

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.jdroid.android.snackbar.SnackbarBuilder

class SnackbarErrorDisplayer : AbstractErrorDisplayer() {

    private val snackbarBuilder = SnackbarBuilder()

    public override fun onDisplayError(
        activity: FragmentActivity?,
        title: String,
        description: String,
        throwable: Throwable
    ) {
        if (activity != null) {
            snackbarBuilder.setDescription(description)
            snackbarBuilder.build(activity).show()
        }
    }

    fun setActionTextResId(@StringRes actionTextResId: Int) {
        snackbarBuilder.setActionTextResId(actionTextResId)
    }

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        snackbarBuilder.setOnClickListener(onClickListener)
    }

    fun setParentLayoutId(@IdRes parentLayoutId: Int) {
        snackbarBuilder.setParentLayoutId(parentLayoutId)
    }

    fun setDuration(@BaseTransientBottomBar.Duration duration: Int) {
        snackbarBuilder.setDuration(duration)
    }
}
