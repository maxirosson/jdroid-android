package com.jdroid.android.kotlin

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable

fun Intent.requireExtras(): Bundle {
    return getExtras()!!
}

fun Intent.requireByteArrayExtra(name: String): ByteArray {
    return getByteArrayExtra(name)!!
}

fun Intent.requireStringExtra(name: String): String {
    return getStringExtra(name)!!
}

fun Intent.requireLongArrayExtra(name: String): LongArray {
    return getLongArrayExtra(name)!!
}

fun <T : Parcelable> Intent.requireParcelableExtra(name: String): T {
    return getParcelableExtra(name)!!
}

fun <T : Parcelable> Intent.requiredParcelableArrayListExtra(name: String): ArrayList<T> {
    return getParcelableArrayListExtra(name)!!
}
