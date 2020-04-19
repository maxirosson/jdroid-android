package com.jdroid.android.snackbar

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.jdroid.android.R
import com.jdroid.android.utils.LocalizationUtils

class SnackbarBuilder {

    private var parentLayoutId = R.id.fragmentContainer
    private var duration = Snackbar.LENGTH_LONG
    private var actionTextResId: Int? = null
    private var onClickListener: View.OnClickListener? = null
    private lateinit var description: String

    fun build(activity: Activity): Snackbar {
        val snackbar = Snackbar.make(activity.findViewById(parentLayoutId), description, duration)
        if (actionTextResId != null && onClickListener != null) {
            snackbar.setAction(actionTextResId!!, onClickListener)
        }
        return snackbar
    }

    fun setActionTextResId(@StringRes actionTextResId: Int) {
        this.actionTextResId = actionTextResId
    }

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun setParentLayoutId(@IdRes parentLayoutId: Int) {
        this.parentLayoutId = parentLayoutId
    }

    fun setDuration(@BaseTransientBottomBar.Duration duration: Int) {
        this.duration = duration
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun setDescription(@StringRes descriptionResId: Int) {
        this.description = LocalizationUtils.getRequiredString(descriptionResId)
    }
}
