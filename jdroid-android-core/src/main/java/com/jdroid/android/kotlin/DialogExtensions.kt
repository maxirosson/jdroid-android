package com.jdroid.android.kotlin

import android.app.Dialog
import android.view.Window

val Dialog.requireWindow: Window
    get() = this.window!!
