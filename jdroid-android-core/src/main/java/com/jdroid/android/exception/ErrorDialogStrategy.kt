package com.jdroid.android.exception

import android.app.Activity

import java.io.Serializable

interface ErrorDialogStrategy : Serializable {

    fun onPositiveClick(activity: Activity?)
}
