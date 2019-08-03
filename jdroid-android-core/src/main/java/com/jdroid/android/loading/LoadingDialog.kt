package com.jdroid.android.loading

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ProgressBar

import com.jdroid.android.R

/**
 * [Dialog] that displays a [ProgressBar] on indeterminate mode
 */
class LoadingDialog
    /**
     * Constructor
     *
     * @param context The Context in which the Dialog should run.
     * @param theme A style resource describing the theme to use for the window.
     */
    constructor(context: Context, theme: Int = R.style.jdroid_customDialog) : Dialog(context, theme) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.jdroid_loading_dialog)
    }
}
