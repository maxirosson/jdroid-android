package com.jdroid.android.kotlin

import android.net.Uri
import android.os.BaseBundle

fun Uri.getRequiredQueryParameter(key: String): String {
    return this.getQueryParameter(key)!!
}

fun BaseBundle.getRequiredString(key: String?): String {
    return this.getString(key)!!
}
