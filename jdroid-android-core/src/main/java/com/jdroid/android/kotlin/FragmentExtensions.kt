package com.jdroid.android.kotlin

import androidx.fragment.app.Fragment
import java.io.Serializable

fun Fragment.getSerializableArgument(key: String?): Serializable? {
    return arguments?.getSerializable(key)
}

fun Fragment.getRequiredSerializableArgument(key: String?): Serializable {
    return arguments?.getSerializable(key)!!
}

fun Fragment.getIntArgument(key: String?): Int? {
    return arguments?.getInt(key)
}

fun Fragment.getRequiredIntArgument(key: String?): Int {
    return arguments?.getInt(key)!!
}
