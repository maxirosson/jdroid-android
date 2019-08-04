package com.jdroid.android.kotlin

import android.content.Intent
import android.os.Bundle

fun Intent.requireExtras(): Bundle {
    return getExtras()!!
}
