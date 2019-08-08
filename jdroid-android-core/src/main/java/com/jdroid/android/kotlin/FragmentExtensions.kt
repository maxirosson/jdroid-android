package com.jdroid.android.kotlin

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.util.ArrayList

val Fragment.requireArguments: Bundle
    get() = arguments!!

fun Fragment.getSerializableArgument(key: String?): Serializable? {
    return arguments?.getSerializable(key)
}

@Suppress("UNCHECKED_CAST")
fun <S : Serializable> Fragment.getRequiredSerializableArgument(key: String?): S {
    return arguments?.getSerializable(key)!! as S
}

fun Fragment.getIntArgument(key: String?): Int? {
    return arguments?.getInt(key)
}

fun Fragment.getRequiredIntArgument(key: String?): Int {
    return arguments?.getInt(key)!!
}

fun Fragment.getFloatArgument(key: String?): Float? {
    return arguments?.getFloat(key)
}

fun Fragment.getRequiredFloatArgument(key: String?): Float {
    return arguments?.getFloat(key)!!
}

fun Fragment.getBooleanArgument(key: String?): Boolean? {
    return arguments?.getBoolean(key)
}

fun Fragment.getRequiredBooleanArgument(key: String?): Boolean {
    return arguments?.getBoolean(key)!!
}

fun Fragment.getRequiredBooleanArgument(key: String?, defaultValue: Boolean): Boolean {
    return arguments?.getBoolean(key, defaultValue) ?: defaultValue
}

fun Fragment.getStringArgument(key: String?): String? {
    return arguments?.getString(key)
}

fun Fragment.getRequiredStringArgument(key: String?): String {
    return arguments?.getString(key)!!
}

fun Fragment.getRequiredStringArgument(key: String?, default: String): String {
    return arguments?.getString(key, default)!!
}

fun Fragment.getLongArgument(key: String?): Long? {
    return arguments?.getLong(key)
}

fun Fragment.getRequiredLongArgument(key: String?): Long {
    return arguments?.getLong(key)!!
}

fun Fragment.getDoubleArgument(key: String?): Double? {
    return arguments?.getDouble(key)
}

fun Fragment.getRequiredDoubleArgument(key: String?): Double {
    return arguments?.getDouble(key)!!
}

fun <T : Parcelable> Fragment.getParcelableArrayListArgument(key: String?): ArrayList<T>? {
    return arguments?.getParcelableArrayList(key)
}

fun <T : Parcelable> Fragment.getRequiredParcelableArrayListArgument(key: String?): ArrayList<T> {
    return arguments?.getParcelableArrayList(key)!!
}

fun <T : Parcelable> Fragment.getParcelableArgument(key: String?): T? {
    return arguments?.getParcelable(key)
}

fun <T : Parcelable> Fragment.getRequiredParcelableArgument(key: String?): T {
    return arguments?.getParcelable(key)!!
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> Fragment.getRequiredArgument(key: String?): T {
    return arguments?.get(key)!! as T
}

fun Fragment.getRequiredStringArrayListArgument(key: String?): ArrayList<String> {
    return arguments?.getStringArrayList(key)!!
}
